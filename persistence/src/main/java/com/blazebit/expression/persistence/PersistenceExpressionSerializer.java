/*
 * Copyright 2019 - 2020 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.expression.persistence;

import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.MultipleSubqueryInitiator;
import com.blazebit.persistence.WhereBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceExpressionSerializer implements Expression.Visitor, ExpressionSerializer<WhereBuilder<?>> {

    private static final String SUBQUERY_PREFIX = "_expr_subquery_";
    private static final String CORRELATION_ALIAS_PREFIX = "_expr_correlation_";

    private final StringBuilder tempSb;
    private final Map<String, SubqueryProvider> subqueryProviders;
    private final Map<Object, Object> properties;
    private int subqueryCount;
    private int correlationCount;
    private StringBuilder sb;
    private WhereBuilder<?> whereBuilder;
    private Context context;

    /**
     * Creates a new serializer for serializing to a Blaze-Persistence Core WhereBuilder.
     *
     * @param domainModel The expression domain model
     */
    public PersistenceExpressionSerializer(DomainModel domainModel) {
        this.tempSb = new StringBuilder();
        this.subqueryProviders = new HashMap<>();
        this.properties = new HashMap<>();
        this.sb = new StringBuilder();
    }

    @Override
    public Context createContext(Map<String, Object> contextParameters) {
        return new Context() {
            @Override
            public Object getContextParameter(String contextParameterName) {
                return contextParameters.get(contextParameterName);
            }
        };
    }

    /**
     * Registers the given subquery provider and returns the alias for it.
     *
     * @param subqueryProvider The subquery provider to register
     * @return The subquery alias
     */
    public String registerSubqueryProvider(SubqueryProvider subqueryProvider) {
        String alias = SUBQUERY_PREFIX + (subqueryCount++);
        subqueryProviders.put(alias, subqueryProvider);
        return alias;
    }

    /**
     * Returns a new correlation alias that may be used in queries.
     *
     * @return The alias
     */
    public String nextCorrelationAlias() {
        return CORRELATION_ALIAS_PREFIX + (correlationCount++);
    }

    /**
     * Returns the properties map.
     *
     * @return the properties map
     */
    public Map<Object, Object> getProperties() {
        return properties;
    }

    /**
     * Returns the query builder.
     *
     * @return the query builder
     */
    public WhereBuilder<?> getWhereBuilder() {
        return whereBuilder;
    }

    @Override
    public void serializeTo(Expression expression, WhereBuilder<?> target) {
        serializeTo(null, expression, target);
    }

    @Override
    public void serializeTo(Context newContext, Expression expression, WhereBuilder<?> target) {
        WhereBuilder old = whereBuilder;
        Context oldContext = context;
        whereBuilder = target;
        context = newContext;
        try {
            sb.setLength(0);
            expression.accept(this);
            MultipleSubqueryInitiator<?> multiSubqueryInitiator = target.whereExpressionSubqueries(sb.toString());
            for (Map.Entry<String, SubqueryProvider> entry : subqueryProviders.entrySet()) {
                entry.getValue().createSubquery(multiSubqueryInitiator.with(entry.getKey()));
            }
            multiSubqueryInitiator.end();
        } finally {
            whereBuilder = old;
            context = oldContext;
        }
    }

    @Override
    public void visit(FunctionInvocation e) {
        FunctionRenderer renderer = e.getFunction().getMetadata(FunctionRenderer.class);
        if (renderer == null) {
            throw new IllegalStateException("The domain function '" + e.getFunction().getName() + "' has no registered persistence function renderer!");
        }
        Map<DomainFunctionArgument, Expression> arguments = e.getArguments();
        Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers;

        if (arguments.isEmpty()) {
            argumentRenderers = Collections.emptyMap();
        } else {
            argumentRenderers = new LinkedHashMap<>(arguments.size());
            for (Map.Entry<DomainFunctionArgument, Expression> entry : arguments.entrySet()) {
                argumentRenderers.put(entry.getKey(), sb -> {
                    StringBuilder oldSb = this.sb;
                    this.sb = sb;
                    try {
                        entry.getValue().accept(PersistenceExpressionSerializer.this);
                    } finally {
                        this.sb = oldSb;
                    }
                });
            }
        }

        renderer.render(e.getFunction(), e.getType(), argumentRenderers, sb);
    }

    @Override
    public void visit(Literal e) {
        // TODO: implement some kind of SPI contract for literal rendering which can be replaced i.e. there should be a default impl that can be replaced
        if (e.getType().getJavaType() == String.class) {
            sb.append('\'');
            String value = e.getValue().toString();
            for (int i = 0; i < value.length(); i++) {
                final char c = value.charAt(i);
                if (c == '\'') {
                    sb.append('\'')
                        .append('\'');
                } else {
                    sb.append(c);
                }
            }
            sb.append('\'');
        } else {
            sb.append(e.getValue());
        }
    }

    @Override
    public void visit(Path e) {
        tempSb.setLength(0);
        String persistenceAlias = getPersistenceAlias(e.getType(), e.getAlias());
        tempSb.append(persistenceAlias);

        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            appendPersistenceAttribute(tempSb, attributes.get(i));
        }
        sb.append(tempSb);
        tempSb.setLength(0);
    }

    /**
     * Applies a JPQL.Next expression for the entity domain attribute to the given string builder.
     *
     * @param sb The string builder to append to
     * @param attribute The entity domain attribute
     */
    protected void appendPersistenceAttribute(StringBuilder sb, EntityDomainTypeAttribute attribute) {
        ExpressionRenderer metadata = attribute.getMetadata(ExpressionRenderer.class);
        if (metadata != null) {
            metadata.render(sb, this);
        } else {
            CorrelationRenderer correlationRenderer = attribute.getMetadata(CorrelationRenderer.class);
            if (correlationRenderer != null) {
                String parent = sb.toString();
                sb.setLength(0);
                sb.append(correlationRenderer.correlate((CriteriaBuilder<?>) whereBuilder, parent, this));
            } else {
                throw new IllegalStateException("The domain attribute '" + attribute.getOwner().getName() + "." + attribute.getName() + "' has no registered ExpressionRenderer or CorrelationRenderer metadata!");
            }
        }
    }

    /**
     * Returns the alias in the query builder for the root variable alias.
     *
     * @param type The domain type of the root
     * @param alias The root variable alias
     * @return The alias in the query builder
     * @throws IllegalStateException when the root variable has no registered query builder alias
     */
    protected String getPersistenceAlias(DomainType type, String alias) {
        Object o = context.getContextParameter(alias);
        if (o instanceof String) {
            return (String) o;
        }
        CorrelationRenderer correlationRenderer = type.getMetadata(CorrelationRenderer.class);
        if (correlationRenderer != null) {
            return correlationRenderer.correlate((CriteriaBuilder<?>) whereBuilder, null, this);
        }
        throw new IllegalStateException("The domain root object alias '" + alias + "' has no registered persistence alias!");
    }

    @Override
    public void visit(ArithmeticFactor e) {
        if (e.isInvertSignum()) {
            sb.append('-');
        }
        e.getExpression().accept(this);
    }

    @Override
    public void visit(ExpressionPredicate e) {
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        e.getExpression().accept(this);
        if (e.getExpression() instanceof FunctionInvocation) {
            FunctionRenderer functionRenderer = ((FunctionInvocation) e.getExpression()).getFunction().getMetadata(FunctionRenderer.class);
            if (!functionRenderer.rendersPredicate()) {
                sb.append(" = TRUE");
            }
        }
        if (negated) {
            sb.append(')');
        }
    }

    @Override
    public void visit(ChainingArithmeticExpression e) {
        e.getLeft().accept(this);
        sb.append(' ');
        sb.append(e.getOperator().getOperator());
        sb.append(' ');
        e.getRight().accept(this);
    }

    @Override
    public void visit(BetweenPredicate e) {
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        e.getLeft().accept(this);
        sb.append(" BETWEEN ");
        e.getLower().accept(this);
        sb.append(" AND ");
        e.getUpper().accept(this);
        if (negated) {
            sb.append(')');
        }
    }

    @Override
    public void visit(InPredicate e) {
        e.getLeft().accept(this);
        if (e.isNegated()) {
            sb.append(" NOT");
        }
        sb.append(" IN ");
        if (e.getInItems().size() == 1 && e.getInItems().get(0) instanceof Path) {
            e.getInItems().get(0).accept(this);
        } else {
            sb.append('(');
            for (ArithmeticExpression inItem : e.getInItems()) {
                inItem.accept(this);
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(')');
        }
    }

    @Override
    public void visit(CompoundPredicate e) {
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        List<Predicate> predicates = e.getPredicates();
        int size = predicates.size();
        Predicate predicate = predicates.get(0);
        if (predicate instanceof CompoundPredicate && e.isConjunction() != ((CompoundPredicate) predicate).isConjunction()) {
            sb.append('(');
            predicate.accept(this);
            sb.append(')');
        } else {
            predicate.accept(this);
        }
        String connector = e.isConjunction() ? " AND " : " OR ";
        for (int i = 1; i < size; i++) {
            predicate = predicates.get(i);
            sb.append(connector);
            if (predicate instanceof CompoundPredicate && !predicate.isNegated() && e.isConjunction() != ((CompoundPredicate) predicate).isConjunction()) {
                sb.append('(');
                predicate.accept(this);
                sb.append(')');
            } else {
                predicate.accept(this);
            }
        }
        if (negated) {
            sb.append(')');
        }
    }

    @Override
    public void visit(ComparisonPredicate e) {
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        e.getLeft().accept(this);
        sb.append(' ');
        sb.append(e.getOperator().getOperator());
        sb.append(' ');
        e.getRight().accept(this);
        if (negated) {
            sb.append(')');
        }
    }

    @Override
    public void visit(IsNullPredicate e) {
        e.getLeft().accept(this);
        sb.append(" IS ");
        if (e.isNegated()) {
            sb.append("NOT ");
        }
        sb.append("NULL");
    }
}
