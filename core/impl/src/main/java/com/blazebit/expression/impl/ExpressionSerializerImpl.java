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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.domain.runtime.model.TemporalInterval;
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

    protected final DomainModel domainModel;
    protected final LiteralFactory literalFactory;
    protected StringBuilder sb;
    protected Context context;

    public ExpressionSerializerImpl(DomainModel domainModel, LiteralFactory literalFactory) {
        this(domainModel, literalFactory, new StringBuilder());
    }

    public ExpressionSerializerImpl(DomainModel domainModel, LiteralFactory literalFactory, StringBuilder sb) {
        this.domainModel = domainModel;
        this.literalFactory = literalFactory;
        this.sb = sb;
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
    public void serializeTo(Expression expression, StringBuilder target) {
        serializeTo(null, expression, target);
    }

    @Override
    public void serializeTo(Context newContext, Expression expression, StringBuilder target) {
        StringBuilder old = sb;
        Context oldContext = context;
        sb = target;
        context = newContext;
        try {
            expression.accept(this);
        } finally {
            sb = old;
            context = oldContext;
        }
    }

    @Override
    public void visit(FunctionInvocation e) {
        sb.append(e.getFunction().getName()).append('(');
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
            } else {
                for (Map.Entry<DomainFunctionArgument, Expression> entry : e.getArguments().entrySet()) {
                    String name = entry.getKey().getName();
                    if (name != null) {
                        sb.append(name).append(" = ");
                    }

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
            case ENUM:
                literalFactory.appendEnumValue(sb, (EnumDomainTypeValue) value);
                break;
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
            case ENTITY:
            case COLLECTION:
                LiteralRenderer literalRenderer = e.getType().getMetadata(LiteralRenderer.class);
                if (literalRenderer == null) {
                    throw new IllegalArgumentException("No literal renderer registered for the literal type: " + e.getType());
                }

                // TODO: maybe the resolved literal should allow structural access so we can render this here?
                literalRenderer.render(value, sb);
                break;
            //CHECKSTYLE:ON: FallThrough
            default:
                throw new IllegalArgumentException("Unsupported domain type kind: " + e.getType().getKind());
        }
    }

    @Override
    public void visit(Path e) {
        if (e.getBase() == null) {
            sb.append(e.getAlias());
        } else {
            e.getBase().accept(this);
        }
        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            sb.append('.');
            sb.append(attributes.get(i).getName());
        }
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

    @Override
    public void visit(IsEmptyPredicate e) {
        e.getLeft().accept(this);
        sb.append(" IS ");
        if (e.isNegated()) {
            sb.append("NOT ");
        }
        sb.append("EMPTY");
    }
}
