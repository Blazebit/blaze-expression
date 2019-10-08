/*
 * Copyright 2019 Blazebit.
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

import com.blazebit.domain.persistence.EntityAttribute;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.persistence.WhereBuilder;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceExpressionSerializer implements Expression.Visitor, ExpressionSerializer<WhereBuilder<?>> {

    private final DomainModel domainModel;
    private StringBuilder sb;
    private WhereBuilder<?> whereBuilder;
    private Context context;

    /**
     * Creates a new serializer for serializing to a Blaze-Persistence Core WhereBuilder.
     *
     * @param domainModel The expression domain model
     */
    public PersistenceExpressionSerializer(DomainModel domainModel) {
        this.domainModel = domainModel;
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
            // TODO: adapt BP to support this
            sb.append("CASE WHEN ");
            expression.accept(this);
            sb.append(" THEN 1 ELSE 0 END");
            target.whereSubqueries(sb.toString()).end().eqExpression("1");
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
        // TODO: fix this
        if (e.getType().getJavaType() == String.class) {
            sb.append('\'').append(e.getValue()).append('\'');
        } else {
            sb.append(e.getValue());
        }
    }

    @Override
    public void visit(Path e) {
        String persistenceAlias = getPersistenceAlias(e.getAlias());
        sb.append(persistenceAlias);

        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            EntityDomainTypeAttribute attribute = attributes.get(i);
            sb.append('.');
            sb.append(getPersistenceAttribute(attribute));
        }
    }

    /**
     * Returns the JPQL.Next expression to the entity domain attribute.
     *
     * @param attribute The entity domain attribute
     * @return the JPQL.Next expression
     */
    protected String getPersistenceAttribute(EntityDomainTypeAttribute attribute) {
        EntityAttribute metadata = attribute.getMetadata(EntityAttribute.class);
        return metadata.getExpression();
    }

    /**
     * Returns the alias in the query builder for the root variable alias.
     *
     * @param alias The root variable alias
     * @return The alias in the query builder
     * @throws IllegalStateException when the root variable has no registered query builder alias
     */
    protected String getPersistenceAlias(String alias) {
        Object o = context.getContextParameter(alias);
        if (o instanceof String) {
            return (String) o;
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
