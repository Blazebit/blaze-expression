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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.ArithmeticOperatorType;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.CollectionLiteral;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.EntityLiteral;
import com.blazebit.expression.EnumLiteral;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsEmptyPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.spi.LiteralRenderer;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionSerializerImpl implements Expression.Visitor, ExpressionSerializer<StringBuilder> {

    public static final String CONTEXT_PARAMETER_FUNCTION_ARGS_NAMED = "serializer.function_args_named";

    protected final ExpressionService expressionService;
    protected final LiteralFactory literalFactory;
    protected final boolean templateMode;
    protected boolean inExpression;
    protected boolean functionArgsNamed;
    protected StringBuilder sb;
    protected Context context;

    public ExpressionSerializerImpl(ExpressionService expressionService, LiteralFactory literalFactory, boolean templateMode) {
        this.expressionService = expressionService;
        this.literalFactory = literalFactory;
        this.templateMode = templateMode;
    }

    @Override
    public void serializeTo(Expression expression, StringBuilder target) {
        serializeTo(null, expression, target);
    }

    @Override
    public void serializeTo(Context newContext, Expression expression, StringBuilder target) {
        sb = target;
        context = newContext;
        functionArgsNamed = context != null && Boolean.TRUE.equals(context.getContextParameter(CONTEXT_PARAMETER_FUNCTION_ARGS_NAMED));
        try {
            if (templateMode && expression instanceof Literal) {
                renderTemplateLiteral((Literal) expression);
            } else {
                expression.accept(this);
            }
        } finally {
            sb = null;
            context = null;
            functionArgsNamed = false;
        }
    }

    @Override
    public void visit(FunctionInvocation e) {
        appendIdentifier(e.getFunction().getName());
        sb.append('(');
        if (!e.getArguments().isEmpty()) {
            int lastIdx = e.getFunction().getArguments().size() - 1;
            DomainFunctionArgument lastArgument = e.getFunction().getArguments().get(lastIdx);
            if (e.getFunction().getArgumentCount() == -1 && e.getArguments().containsKey(lastArgument)) {
                // Var-args
                for (Map.Entry<DomainFunctionArgument, Expression> entry : e.getArguments().entrySet()) {
                    if (lastArgument == entry.getKey()) {
                        Literal literal = (Literal) entry.getValue();
                        Collection<Expression> values = (Collection<Expression>) literal.getValue();
                        if (values == null || values.isEmpty()) {
                            sb.append(", ");
                        } else {
                            for (Expression value : values) {
                                value.accept(this);
                                sb.append(", ");
                            }
                        }
                    } else {
                        entry.getValue().accept(this);
                        sb.append(", ");
                    }
                }
            } else if (functionArgsNamed) {
                for (Map.Entry<DomainFunctionArgument, Expression> entry : e.getArguments().entrySet()) {
                    String name = entry.getKey().getName();
                    if (name != null) {
                        appendIdentifier(name);
                        sb.append(" = ");
                    }

                    entry.getValue().accept(this);
                    sb.append(", ");
                }
            } else {
                for (Map.Entry<DomainFunctionArgument, Expression> entry : e.getArguments().entrySet()) {
                    entry.getValue().accept(this);
                    sb.append(", ");
                }
            }

            sb.setLength(sb.length() - 2);
        }
        sb.append(')');
    }

    @Override
    public void visit(Literal e) {
        Object value = e.getValue();
        switch (e.getType().getKind()) {
            case BASIC:
                if (value instanceof Boolean) {
                    literalFactory.appendBoolean(sb, (Boolean) value);
                    break;
                } else if (value instanceof Number) {
                    literalFactory.appendNumeric(sb, (Number) value);
                    break;
                } else if (value instanceof String) {
                    literalFactory.appendString(sb, (String) value);
                    break;
                } else if (value instanceof Instant) {
                    literalFactory.appendInstant(sb, (Instant) value);
                    break;
                } else if (value instanceof TemporalInterval) {
                    literalFactory.appendInterval(sb, (TemporalInterval) value);
                    break;
                }
                //CHECKSTYLE:OFF: FallThrough
            case ENUM:
            case ENTITY:
            case COLLECTION:
                LiteralRenderer literalRenderer = e.getType().getMetadata(LiteralRenderer.class);
                if (literalRenderer == null) {
                    throw new IllegalArgumentException("No literal renderer registered for the literal type: " + e.getType());
                }

                literalRenderer.render(context, value, sb);
                break;
            //CHECKSTYLE:ON: FallThrough
            default:
                throw new IllegalArgumentException("Unsupported domain type kind: " + e.getType().getKind());
        }
    }

    @Override
    public void visit(EnumLiteral e) {
        EnumDomainTypeValue enumValue = e.getEnumValue();
        appendIdentifier(enumValue.getOwner().getName());
        sb.append('.');
        appendIdentifier(enumValue.getValue());
    }

    @Override
    public void visit(EntityLiteral e) {
        Map<EntityDomainTypeAttribute, ? extends Literal> attributeValues = e.getAttributeValues();
        appendIdentifier(e.getType().getName());
        sb.append('(');
        for (Map.Entry<EntityDomainTypeAttribute, ? extends Literal> entry : attributeValues.entrySet()) {
            appendIdentifier(entry.getKey().getName());
            sb.append(" = ");
            entry.getValue().accept(this);
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);

        sb.append(')');
    }

    @Override
    public void visit(CollectionLiteral e) {
        sb.append('[');
        Collection<? extends Literal> values = e.getValues();
        if (!values.isEmpty()) {
            for (Literal value : values) {
                value.accept(this);
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
        }
        sb.append(']');
    }

    @Override
    public void visit(Path e) {
        if (e.getBase() == null) {
            appendIdentifier(e.getAlias());
        } else {
            e.getBase().accept(this);
        }
        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            sb.append('.');
            appendIdentifier(attributes.get(i).getName());
        }
    }

    @Override
    public void visit(ArithmeticFactor e) {
        ArithmeticExpression expression = e.getExpression();
        boolean parenthesis = false;
        if (e.isInvertSignum()) {
            sb.append('-');
            if (parenthesis = expression instanceof ChainingArithmeticExpression || expression instanceof ArithmeticFactor) {
                sb.append('(');
            }
        }
        expression.accept(this);
        if (parenthesis) {
            sb.append(')');
        }
    }

    @Override
    public void visit(ExpressionPredicate e) {
        Expression expression = e.getExpression();
        boolean parenthesis = false;
        if (e.isNegated()) {
            sb.append('!');
            if (parenthesis = expression instanceof ChainingArithmeticExpression || expression instanceof ArithmeticFactor) {
                sb.append('(');
            }
        }
        expression.accept(this);
        if (parenthesis) {
            sb.append(')');
        }
    }

    @Override
    public void visit(ChainingArithmeticExpression e) {
        if (templateMode && !inExpression) {
            if (e.getOperator() != ArithmeticOperatorType.PLUS) {
                throw new IllegalStateException("Illegal template expression: " + e);
            }
            renderTemplateConcat(e);
        } else {
            boolean leftParenthesis = false;
            if (e.getLeft() instanceof ChainingArithmeticExpression) {
                ChainingArithmeticExpression expr = (ChainingArithmeticExpression) e.getLeft();
                leftParenthesis = e.getOperator().hasPrecedenceOver(expr.getOperator());
            }
            if (leftParenthesis) {
                sb.append('(');
            }
            e.getLeft().accept(this);
            if (leftParenthesis) {
                sb.append(')');
            }
            sb.append(' ');
            sb.append(e.getOperator().getOperator());
            sb.append(' ');
            boolean rightParenthesis = false;
            if (e.getRight() instanceof ChainingArithmeticExpression) {
                ChainingArithmeticExpression expr = (ChainingArithmeticExpression) e.getRight();
                rightParenthesis = e.getOperator().hasPrecedenceOver(expr.getOperator());
            }
            if (rightParenthesis) {
                sb.append('(');
            }
            e.getRight().accept(this);
            if (rightParenthesis) {
                sb.append(')');
            }
        }
    }

    protected void renderTemplateConcat(ChainingArithmeticExpression e) {
        renderTemplateConcatArgument(e.getLeft());
        renderTemplateConcatArgument(e.getRight());
    }

    protected void renderTemplateConcatArgument(ArithmeticExpression argument) {
        if (argument instanceof Literal) {
            renderTemplateLiteral((Literal) argument);
        } else if (argument instanceof ChainingArithmeticExpression && ((ChainingArithmeticExpression) argument).getOperator() == ArithmeticOperatorType.PLUS) {
            renderTemplateConcat((ChainingArithmeticExpression) argument);
        } else {
            sb.append('#').append('{');
            inExpression = true;
            argument.accept(this);
            inExpression = false;
            sb.append('}');
        }
    }

    protected void renderTemplateLiteral(Literal e) {
        Object value = e.getValue();
        if (e.getType().getKind() == DomainType.DomainTypeKind.BASIC && value instanceof String) {
            literalFactory.appendTemplateString(sb, (String) value);
        } else {
            sb.append('#').append('{');
            inExpression = true;
            e.accept(this);
            inExpression = false;
            sb.append('}');
        }
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
        if (e.isConjunction()) {
            String connector = "";
            for (int i = 0; i < size; i++) {
                Predicate predicate = predicates.get(i);
                sb.append(connector);
                if (predicate instanceof CompoundPredicate && !predicate.isNegated() && !((CompoundPredicate) predicate).isConjunction()) {
                    sb.append('(');
                    predicate.accept(this);
                    sb.append(')');
                } else {
                    predicate.accept(this);
                }
                connector = " AND ";
            }
        } else {
            String connector = "";
            for (int i = 0; i < size; i++) {
                sb.append(connector);
                predicates.get(i).accept(this);
                connector = " OR ";
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

    @Override
    public void visit(IsEmptyPredicate e) {
        e.getLeft().accept(this);
        sb.append(" IS ");
        if (e.isNegated()) {
            sb.append("NOT ");
        }
        sb.append("EMPTY");
    }

    private void appendIdentifier(String identifier) {
        int startIndex = sb.length();
        int end = identifier.length();
        sb.ensureCapacity(startIndex + end);
        char c = identifier.charAt(0);
        if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_' || c == '$' || c >= '\u0080' && c < '\uffff') {
            sb.append(c);
            for (int i = 1; i < end; i++) {
                c = identifier.charAt(i);
                if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '_' || c == '$' || c >= '\u0080' && c < '\uffff') {
                    sb.append(c);
                } else {
                    sb.insert(startIndex, '`');
                    sb.append(identifier, i, end);
                    sb.append('`');
                    break;
                }
            }
        } else {
            sb.append('`');
            sb.append(identifier);
            sb.append('`');
        }
    }
}
