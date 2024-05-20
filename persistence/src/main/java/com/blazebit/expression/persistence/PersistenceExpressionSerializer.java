/*
 * Copyright 2019 - 2024 Blazebit.
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
import com.blazebit.domain.runtime.model.DomainFunctionVolatility;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.CollectionLiteral;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.EntityLiteral;
import com.blazebit.expression.EnumLiteral;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.FromItem;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsEmptyPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Join;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.Query;
import com.blazebit.expression.spi.DefaultResolvedLiteral;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.MultipleSubqueryInitiator;
import com.blazebit.persistence.WhereBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceExpressionSerializer implements Expression.ResultVisitor<Boolean>, ExpressionSerializer<WhereBuilder<?>> {

    public static final String CONSTANT_INLINING_INTERPRETER_CONTEXT = "persistence.constant_inlining_interpreter_context";
    public static final String PATHS_TO_INLINE = "persistence.paths_to_inline";

    private static final String SUBQUERY_PREFIX = "_expr_subquery_";
    private static final String CORRELATION_ALIAS_PREFIX = "_expr_correlation_";

    private final ExpressionService expressionService;
    private final StringBuilder tempSb;
    private final Map<String, PersistenceSubqueryProvider> subqueryProviders;
    private final Map<Object, Object> properties;
    private int subqueryCount;
    private int correlationCount;
    private StringBuilder sb;
    private WhereBuilder<?> whereBuilder;
    private Context context;
    private ExpressionInterpreter interpreterForInlining;
    private ExpressionInterpreter.Context interpreterContextForInlining;
    private Set<String> pathsToInline;

    /**
     * Creates a new serializer for serializing to a Blaze-Persistence Core WhereBuilder.
     *
     * @param expressionService The expression service
     */
    public PersistenceExpressionSerializer(ExpressionService expressionService) {
        this.expressionService = expressionService;
        this.tempSb = new StringBuilder();
        this.subqueryProviders = new HashMap<>();
        this.properties = new HashMap<>();
        this.sb = new StringBuilder();
    }

    /**
     * Returns the domain model.
     *
     * @return the domain model
     */
    public ExpressionService getExpressionService() {
        return expressionService;
    }

    /**
     * Returns the current serialization context.
     *
     * @return the current serialization context
     */
    public Context getContext() {
        return context;
    }

    /**
     * Registers the given subquery provider and returns the alias for it.
     *
     * @param persistenceSubqueryProvider The subquery provider to register
     * @return The subquery alias
     */
    public String registerSubqueryProvider(PersistenceSubqueryProvider persistenceSubqueryProvider) {
        String alias = SUBQUERY_PREFIX + (subqueryCount++);
        subqueryProviders.put(alias, persistenceSubqueryProvider);
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
     * Returns the current string builder to which the serialization is done.
     *
     * @return the current string builder
     */
    public StringBuilder getStringBuilder() {
        return sb;
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
        WhereBuilder<?> old = whereBuilder;
        Context oldContext = context;
        ExpressionInterpreter.Context oldInterpreterContextForInlining = interpreterContextForInlining;
        Set<String> oldPathsToInline = pathsToInline;
        whereBuilder = target;
        context = newContext;
        Object constantInliningInterpreterContext;
        Object pathsToInline;
        if (newContext == null) {
            constantInliningInterpreterContext = null;
            pathsToInline = null;
        } else {
            constantInliningInterpreterContext = newContext.getContextParameter(CONSTANT_INLINING_INTERPRETER_CONTEXT);
            pathsToInline = newContext.getContextParameter(PATHS_TO_INLINE);
        }
        if (constantInliningInterpreterContext == null) {
            interpreterContextForInlining = null;
        } else if (constantInliningInterpreterContext instanceof ExpressionInterpreter.Context) {
            interpreterContextForInlining = (ExpressionInterpreter.Context) constantInliningInterpreterContext;
        } else {
            throw new IllegalArgumentException("Illegal value given for '" + CONSTANT_INLINING_INTERPRETER_CONTEXT + "'. Expected ExpressionInterpreter.Context but got: " + constantInliningInterpreterContext);
        }
        if (pathsToInline == null) {
            this.pathsToInline = null;
        } else if (pathsToInline instanceof Set<?>) {
            this.pathsToInline = (Set<String>) pathsToInline;
        } else {
            throw new IllegalArgumentException("Illegal value given for '" + PATHS_TO_INLINE + "'. Expected Set<String> but got: " + pathsToInline);
        }
        try {
            sb.setLength(0);
            expression.accept(this);
            MultipleSubqueryInitiator<?> multiSubqueryInitiator = target.whereExpressionSubqueries(sb.toString());
            for (Map.Entry<String, PersistenceSubqueryProvider> entry : subqueryProviders.entrySet()) {
                entry.getValue().createSubquery(multiSubqueryInitiator.with(entry.getKey()));
            }
            multiSubqueryInitiator.end();
        } finally {
            whereBuilder = old;
            context = oldContext;
            interpreterContextForInlining = oldInterpreterContextForInlining;
            this.pathsToInline = oldPathsToInline;
        }
    }

    private Boolean inlineIfConstant(Expression expression, int startIndex, boolean constant) {
        if (!constant || interpreterContextForInlining == null) {
            return Boolean.FALSE;
        }
        ExpressionInterpreter interpreter = interpreterForInlining;
        if (interpreter == null) {
            interpreter = interpreterForInlining = expressionService.createInterpreter();
        }
        try {
            Object value = interpreter.evaluateAsModelType(expression, interpreterContextForInlining);
            sb.setLength(startIndex);
            visit(new Literal(new DefaultResolvedLiteral(expression.getType(), value)));
            return Boolean.TRUE;
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Could not inline expression '" + expression + "'", ex);
        }
    }

    @Override
    public Boolean visit(FunctionInvocation e) {
        int startIndex = sb.length();
        PersistenceFunctionRenderer renderer = e.getFunction().getMetadata(PersistenceFunctionRenderer.class);
        if (renderer == null) {
            if (interpreterContextForInlining != null) {
                return inlineIfConstant(e, startIndex, true);
            }
            throw new IllegalStateException("The domain function '" + e.getFunction().getName() + "' has no registered persistence function renderer!");
        }
        Map<DomainFunctionArgument, Expression> arguments = e.getArguments();

        if (arguments.isEmpty()) {
            renderer.render(e.getFunction(), e.getType(), PersistenceDomainFunctionArgumentRenderers.EMPTY, sb, this);
            return inlineIfConstant(e, startIndex, e.getFunction().getVolatility() != DomainFunctionVolatility.VOLATILE);
        } else {
            int size = e.getFunction().getArguments().size();
            Expression[] expressions = new Expression[size];
            for (Map.Entry<DomainFunctionArgument, Expression> entry : arguments.entrySet()) {
                DomainFunctionArgument domainFunctionArgument = entry.getKey();
                Expression expression = entry.getValue();
                expressions[domainFunctionArgument.getPosition()] = expression;
            }
            DefaultPersistenceDomainFunctionArgumentRenderers argumentRenderers = new DefaultPersistenceDomainFunctionArgumentRenderers(expressions, arguments.size());
            renderer.render(e.getFunction(), e.getType(), argumentRenderers, sb, this);
            return inlineIfConstant(e, startIndex, argumentRenderers.isAllArgumentsConstant() && e.getFunction().getVolatility() != DomainFunctionVolatility.VOLATILE);
        }
    }

    @Override
    public Boolean visit(Literal e) {
        if (e.getType().getKind() == DomainType.DomainTypeKind.COLLECTION) {
            @SuppressWarnings("unchecked")
            Collection<Expression> expressions = (Collection<Expression>) e.getValue();
            if (expressions != null && !expressions.isEmpty()) {
                for (Expression expression : expressions) {
                    expression.accept(PersistenceExpressionSerializer.this);
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
            }
        } else {
            Object value = e.getValue();
            PersistenceLiteralRenderer literalRenderer = e.getType().getMetadata(PersistenceLiteralRenderer.class);
            if (literalRenderer != null) {
                literalRenderer.render(value, e.getType(), this);
            } else {
                sb.append(value);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean visit(EnumLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Boolean visit(EntityLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Boolean visit(CollectionLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Boolean visit(Path e) {
        int startIndex = sb.length();
        boolean isConstant = true;
        tempSb.setLength(0);
        if (e.getBase() == null) {
            String persistenceAlias = getPersistenceAlias(e);
            if (persistenceAlias == null) {
                if (interpreterContextForInlining != null) {
                    return inlineIfConstant(e, startIndex, true);
                }
                throw new IllegalStateException("The domain root object alias '" + e.getAlias() + "' has no registered persistence alias!");
            }
            tempSb.append(persistenceAlias);
        } else {
            StringBuilder old = sb;
            sb = tempSb;
            isConstant = e.getBase().accept(this);
            sb = old;
        }

        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        if (pathsToInline == null || e.getAlias() == null) {
            for (int i = 0; i < attributes.size(); i++) {
                EntityDomainTypeAttribute attribute = attributes.get(i);
                if (appendPersistenceAttribute(tempSb, attribute)) {
                    if (interpreterContextForInlining != null) {
                        tempSb.setLength(0);
                        if (isConstant) {
                            return inlineIfConstant(e, startIndex, true);
                        }
                    }
                    throw new IllegalStateException("The domain attribute '" + attribute.getOwner().getName() + "." + attribute.getName() + "' has no registered ExpressionRenderer or CorrelationRenderer metadata!");
                }
            }
        } else {
            StringBuilder pathSb = new StringBuilder();
            pathSb.append(e.getAlias());
            for (int i = 0; i < attributes.size(); i++) {
                EntityDomainTypeAttribute attribute = attributes.get(i);
                pathSb.append('.').append(attribute.getName());
                if (appendPersistenceAttribute(tempSb, attribute)) {
                    if (interpreterContextForInlining != null) {
                        tempSb.setLength(0);
                        if (isConstant) {
                            return inlineIfConstant(e, startIndex, true);
                        }
                    }
                    throw new IllegalStateException("The domain attribute '" + attribute.getOwner().getName() + "." + attribute.getName() + "' has no registered ExpressionRenderer or CorrelationRenderer metadata!");
                }
            }
            if (pathsToInline.contains(pathSb.toString())) {
                tempSb.setLength(0);
                return inlineIfConstant(e, startIndex, true);
            }
        }
        sb.append(tempSb);
        tempSb.setLength(0);
        return isConstant;
    }

    /**
     * Applies a JPQL.Next expression for the entity domain attribute to the given string builder.
     *
     * @param sb The string builder to append to
     * @param attribute The entity domain attribute
     * @return <code>true</code> if no renderer was found, <code>false</code> otherwise
     */
    protected boolean appendPersistenceAttribute(StringBuilder sb, EntityDomainTypeAttribute attribute) {
        PersistenceExpressionRenderer metadata = attribute.getMetadata(PersistenceExpressionRenderer.class);
        if (metadata != null) {
            metadata.render(sb, this);
        } else {
            PersistenceCorrelationRenderer persistenceCorrelationRenderer = attribute.getMetadata(PersistenceCorrelationRenderer.class);
            if (persistenceCorrelationRenderer != null) {
                String parent = sb.toString();
                sb.setLength(0);
                sb.append(persistenceCorrelationRenderer.correlate((CriteriaBuilder<?>) whereBuilder, parent, this));
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the root alias in the query builder for the path.
     *
     * @param p The path
     * @return The alias in the query builder
     * @throws IllegalStateException when the root variable has no registered query builder alias
     */
    protected String getPersistenceAlias(Path p) {
        String alias = p.getAlias();
        Object o = context.getContextParameter(alias);
        if (o instanceof String) {
            return (String) o;
        }
        DomainType type;
        if (p.getAttributes().isEmpty()) {
            type = p.getType();
        } else {
            type = p.getAttributes().get(0).getOwner();
        }
        PersistenceCorrelationRenderer persistenceCorrelationRenderer = type.getMetadata(PersistenceCorrelationRenderer.class);
        if (persistenceCorrelationRenderer != null) {
            return persistenceCorrelationRenderer.correlate((CriteriaBuilder<?>) whereBuilder, null, this);
        }
        return null;
    }

    @Override
    public Boolean visit(ArithmeticFactor e) {
        int startIndex = sb.length();
        if (e.isInvertSignum()) {
            sb.append('-');
        }
        return inlineIfConstant(e, startIndex, e.getExpression().accept(this));
    }

    @Override
    public Boolean visit(ExpressionPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        boolean isConstant = e.getExpression().accept(this);
        if (e.getExpression() instanceof FunctionInvocation) {
            PersistenceFunctionRenderer persistenceFunctionRenderer = ((FunctionInvocation) e.getExpression()).getFunction().getMetadata(PersistenceFunctionRenderer.class);
            if (!persistenceFunctionRenderer.rendersPredicate()) {
                sb.append(" = TRUE");
            }
        }
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(ChainingArithmeticExpression e) {
        int startIndex = sb.length();
        PersistenceDomainOperatorRenderer operatorRenderer = e.getType().getMetadata(PersistenceDomainOperatorRenderer.class);
        return inlineIfConstant(e, startIndex, operatorRenderer.render(e, this));
    }

    @Override
    public Boolean visit(BetweenPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        boolean isConstant = e.getLeft().accept(this);
        sb.append(" BETWEEN ");
        isConstant = e.getLower().accept(this) && isConstant;
        sb.append(" AND ");
        isConstant = e.getUpper().accept(this) && isConstant;
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(InPredicate e) {
        int startIndex = sb.length();
        boolean isConstant = e.getLeft().accept(this);
        if (e.isNegated()) {
            sb.append(" NOT");
        }
        sb.append(" IN ");
        if (e.getInItems().size() == 1 && e.getInItems().get(0) instanceof Path) {
            isConstant = e.getInItems().get(0).accept(this) && isConstant;
        } else {
            sb.append('(');
            for (ArithmeticExpression inItem : e.getInItems()) {
                isConstant = inItem.accept(this) && isConstant;
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(CompoundPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        List<Predicate> predicates = e.getPredicates();
        int size = predicates.size();
        Predicate predicate = predicates.get(0);
        boolean isConstant;
        if (predicate instanceof CompoundPredicate && e.isConjunction() != ((CompoundPredicate) predicate).isConjunction()) {
            sb.append('(');
            isConstant = predicate.accept(this);
            sb.append(')');
        } else {
            isConstant = predicate.accept(this);
        }
        String connector = e.isConjunction() ? " AND " : " OR ";
        for (int i = 1; i < size; i++) {
            predicate = predicates.get(i);
            sb.append(connector);
            if (predicate instanceof CompoundPredicate && !predicate.isNegated() && e.isConjunction() != ((CompoundPredicate) predicate).isConjunction()) {
                sb.append('(');
                isConstant = predicate.accept(this) && isConstant;
                sb.append(')');
            } else {
                isConstant = predicate.accept(this) && isConstant;
            }
        }
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(ComparisonPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        boolean isConstant = e.getLeft().accept(this);
        sb.append(' ');
        sb.append(e.getOperator().getOperator());
        sb.append(' ');
        isConstant = e.getRight().accept(this) && isConstant;
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(IsNullPredicate e) {
        int startIndex = sb.length();
        boolean isConstant = e.getLeft().accept(this);
        sb.append(" IS ");
        if (e.isNegated()) {
            sb.append("NOT ");
        }
        sb.append("NULL");
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(IsEmptyPredicate e) {
        int startIndex = sb.length();
        boolean isConstant = e.getLeft().accept(this);
        sb.append(" IS ");
        if (e.isNegated()) {
            sb.append("NOT ");
        }
        sb.append("EMPTY");
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(Query e) {
        throw new UnsupportedOperationException("No support for queries yet");
    }

    @Override
    public Boolean visit(FromItem e) {
        throw new UnsupportedOperationException("No support for queries yet");
    }

    @Override
    public Boolean visit(Join e) {
        throw new UnsupportedOperationException("No support for queries yet");
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private final class DefaultPersistenceDomainFunctionArgumentRenderers implements PersistenceDomainFunctionArgumentRenderers {

        private final Expression[] expressions;
        private final int assignedArguments;
        private boolean allArgumentsConstant = true;

        public DefaultPersistenceDomainFunctionArgumentRenderers(Expression[] expressions, int assignedArguments) {
            this.expressions = expressions;
            this.assignedArguments = assignedArguments;
        }

        public boolean isAllArgumentsConstant() {
            return allArgumentsConstant;
        }

        @Override
        public Expression getExpression(int position) {
            try {
                return expressions[position];
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            }
        }

        @Override
        public DomainType getType(int position) {
            try {
                return expressions[position].getType();
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            } catch (NullPointerException ex) {
                return null;
            }
        }

        @Override
        public int assignedArguments() {
            return assignedArguments;
        }

        @Override
        public boolean renderArgument(StringBuilder sb, int position) {
            StringBuilder oldSb = PersistenceExpressionSerializer.this.sb;
            PersistenceExpressionSerializer.this.sb = sb;
            try {
                if (expressions[position].accept(PersistenceExpressionSerializer.this)) {
                    return true;
                } else {
                    allArgumentsConstant = false;
                    return false;
                }
            } finally {
                PersistenceExpressionSerializer.this.sb = oldSb;
            }
        }

        @Override
        public void renderArguments(StringBuilder sb) {
            if (assignedArguments != 0) {
                StringBuilder oldSb = PersistenceExpressionSerializer.this.sb;
                PersistenceExpressionSerializer.this.sb = sb;
                try {
                    for (int i = 0; i < assignedArguments; i++) {
                        if (!expressions[i].accept(PersistenceExpressionSerializer.this)) {
                            allArgumentsConstant = false;
                        }
                        sb.append(", ");
                    }
                    sb.setLength(sb.length() - 2);
                } finally {
                    PersistenceExpressionSerializer.this.sb = oldSb;
                }
            }
        }
    }
}
