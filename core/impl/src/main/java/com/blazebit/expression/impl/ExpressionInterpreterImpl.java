/*
 * Copyright 2019 - 2021 Blazebit.
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

import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.CollectionLiteral;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.EntityLiteral;
import com.blazebit.expression.EnumLiteral;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsEmptyPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.spi.AttributeAccessor;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.DomainOperatorInterpreter;
import com.blazebit.expression.spi.FunctionInvoker;
import com.blazebit.expression.spi.TypeAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionInterpreterImpl implements Expression.ResultVisitor<Object>, ExpressionInterpreter {

    protected final ExpressionService expressionService;
    protected Context context;
    protected TypeAdapter typeAdapter;

    public ExpressionInterpreterImpl(ExpressionService expressionService) {
        this.expressionService = expressionService;
    }

    @Override
    public Context createContext(Map<String, DomainType> rootDomainTypes, Map<String, Object> rootObjects) {
        return new Context() {

            private final Map<String, Object> properties = new HashMap<>();

            @Override
            public ExpressionService getExpressionService() {
                return expressionService;
            }

            @Override
            public Object getProperty(String key) {
                return properties.get(key);
            }

            @Override
            public void setProperty(String key, Object value) {
                properties.put(key, value);
            }

            @Override
            public Object getRoot(String alias) {
                return rootObjects.get(alias);
            }

            @Override
            public DomainType getRootDomainType(String alias) {
                return rootDomainTypes.get(alias);
            }
        };
    }

    protected <T> T evaluate(Expression expression, Context interpreterContext, boolean asModelType) {
        Context oldContext = context;
        context = interpreterContext;
        try {
            Object value = expression.accept(this);
            if (typeAdapter != null && asModelType) {
                value = typeAdapter.toModelType(context, value, expression.getType());
            }
            return (T) value;
        } finally {
            context = oldContext;
            typeAdapter = null;
        }
    }

    @Override
    public <T> T evaluate(Expression expression, Context interpreterContext) {
        return evaluate(expression, interpreterContext, false);
    }
    @Override
    public <T> T evaluateAsModelType(Expression expression, Context interpreterContext) {
        return evaluate(expression, interpreterContext, true);
    }

    @Override
    public Boolean evaluate(Predicate expression, Context interpreterContext) {
        return Boolean.TRUE.equals(evaluate((Expression) expression, interpreterContext));
    }

    @Override
    public Object visit(ArithmeticFactor e) {
        try {
            Object result = e.getExpression().accept(this);
            if (result == null) {
                return null;
            }
            if (e.isInvertSignum()) {
                return arithmetic(e.getType(), e.getType(), null, result, null, DomainOperator.UNARY_MINUS);
            }
            return result;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(ExpressionPredicate e) {
        try {
            Boolean result = (Boolean)e.getExpression().accept(this);
            if (result == null) {
                return null;
            }
            return (e.isNegated() != result);
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(BetweenPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            Object lower = e.getLower().accept(this);
            if (lower == null) {
                return null;
            }
            Object upper = e.getUpper().accept(this);
            if (upper == null) {
                return null;
            }

            Boolean testValue = e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
            Boolean compare = compare(e.getLeft().getType(), e.getLower().getType(), left, lower, ComparisonOperator.GREATER_OR_EQUAL);
            if (compare == null) {
                return null;
            } else if (testValue.equals(compare)) {
                return testValue;
            }
            compare = compare(e.getLeft().getType(), e.getUpper().getType(), left, upper, ComparisonOperator.LOWER_OR_EQUAL);
            if (compare == null) {
                return null;
            } else if (testValue.equals(compare)) {
                return testValue;
            }
            return Boolean.TRUE;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(InPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            List<ArithmeticExpression> inItems = e.getInItems();
            Boolean testValue = e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
            for (int i = 0; i < inItems.size(); i++) {
                ArithmeticExpression inItem = inItems.get(i);
                Object value = inItem.accept(this);
                if (value == null) {
                    return null;
                }
                Boolean b = compare(e.getLeft().getType(), inItem.getType(), left, value, ComparisonOperator.EQUAL);
                if (!testValue.equals(b)) {
                    return b;
                }
            }

            return testValue;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(ChainingArithmeticExpression e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            Object right = e.getRight().accept(this);
            if (right == null) {
                return null;
            }

            return arithmetic(e.getType(), e.getLeft().getType(), e.getRight().getType(), left, right, e.getOperator().getDomainOperator());
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(CompoundPredicate e) {
        try {
            List<Predicate> predicates = e.getPredicates();
            int size = predicates.size();
            if (e.isConjunction()) {
                if (size == 0) {
                    return e.isNegated();
                }
                for (int i = 0; i < predicates.size(); i++) {
                    Object result = predicates.get(i).accept(this);
                    if (result == null) {
                        return null;
                    } else if (!Boolean.TRUE.equals(result)) {
                        return e.isNegated();
                    }
                }
                return !e.isNegated();
            } else {
                if (size == 0) {
                    return !e.isNegated();
                }
                for (int i = 0; i < predicates.size(); i++) {
                    Object result = predicates.get(i).accept(this);
                    if (result == null) {
                        return null;
                    } else if (Boolean.TRUE.equals(result)) {
                        return !e.isNegated();
                    }
                }
                return e.isNegated();
            }
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(ComparisonPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            Object right = e.getRight().accept(this);
            if (right == null) {
                return null;
            }

            Boolean compare = compare(e.getLeft().getType(), e.getRight().getType(), left, right, e.getOperator());
            if (compare == null) {
                return null;
            } else if (e.isNegated()) {
                return !compare;
            } else {
                return compare;
            }
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(IsNullPredicate e) {
        // LEFT  NN NOT R
        // NULL  F   F  T
        // NULL  F   T  F
        // VAL   T   F  F
        // VAL   T   T  T
        try {
            return (e.getLeft().accept(this) != null) == e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(IsEmptyPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            if (e.isNegated()) {
                return ((Iterable<?>) left).iterator().hasNext();
            } else {
                return !((Iterable<?>) left).iterator().hasNext();
            }
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(Path e) {
        Object value;
        if (e.getBase() == null) {
            value = context.getRoot(e.getAlias());
        } else {
            value = e.getBase().accept(this);
        }
        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        if (attributes.isEmpty()) {
            typeAdapter = null;
        } else {
            for (int i = 0; i < attributes.size(); i++) {
                if (value == null) {
                    return null;
                }
                EntityDomainTypeAttribute attribute = attributes.get(i);
                AttributeAccessor attributeAccessor = attribute.getMetadata(AttributeAccessor.class);
                if (attributeAccessor == null) {
                    throw new IllegalArgumentException("No attribute accessor available for attribute: " + attribute);
                }
                value = attributeAccessor.getAttribute(context, value, attribute);
                TypeAdapter<Object, Object> adapter = attribute.getMetadata(TypeAdapter.class);
                if (adapter != null) {
                    value = adapter.toInternalType(context, value, attribute.getType());
                }
            }
            typeAdapter = attributes.get(attributes.size() - 1).getMetadata(TypeAdapter.class);
        }
        return value;
    }

    @Override
    public Object visit(FunctionInvocation e) {
        DomainFunction domainFunction = e.getFunction();
        FunctionInvoker functionInvoker = domainFunction.getMetadata(FunctionInvoker.class);
        if (functionInvoker == null) {
            throw new IllegalArgumentException("No function invoker available for function: " + domainFunction);
        }

        Map<DomainFunctionArgument, Expression> arguments = e.getArguments();
        DomainFunctionArguments argumentValues;

        if (arguments.isEmpty()) {
            argumentValues = DomainFunctionArguments.EMPTY;
        } else {
            int size = domainFunction.getArguments().size();
            Object[] values = new Object[size];
            DomainType[] types = new DomainType[size];
            for (Map.Entry<DomainFunctionArgument, Expression> entry : arguments.entrySet()) {
                DomainFunctionArgument domainFunctionArgument = entry.getKey();
                Expression expression = entry.getValue();
                Object argumentValue = expression.accept(this);
                if (typeAdapter != null) {
                    argumentValue = typeAdapter.toInternalType(context, argumentValue, domainFunctionArgument.getType());
                }
                TypeAdapter argumentAdapter = domainFunctionArgument.getMetadata(TypeAdapter.class);
                if (argumentAdapter != null) {
                    argumentValue = argumentAdapter.toModelType(context, argumentValue, domainFunctionArgument.getType());
                }
                types[domainFunctionArgument.getPosition()] = expression.getType();
                values[domainFunctionArgument.getPosition()] = argumentValue;
            }
            argumentValues = new DefaultDomainFunctionArguments(values, types, arguments.size());
        }

        typeAdapter = domainFunction.getMetadata(TypeAdapter.class);
        return functionInvoker.invoke(context, domainFunction, argumentValues);
    }

    @Override
    public Object visit(Literal e) {
        typeAdapter = null;
        if (e.getType().getKind() == DomainType.DomainTypeKind.COLLECTION) {
            DomainType elementType = ((CollectionDomainType) e.getType()).getElementType();
            Collection<Expression> collection = (Collection<Expression>) e.getValue();
            if (collection.isEmpty()) {
                return Collections.emptyList();
            }
            List<Object> resolved = new ArrayList<>(collection.size());
            for (Expression expression : collection) {
                Object value = expression.accept(this);
                if (typeAdapter != null) {
                    value = typeAdapter.toInternalType(context, value, elementType);
                    typeAdapter = null;
                }
                resolved.add(value);
            }
            return resolved;
        } else {
            return e.getValue();
        }
    }

    @Override
    public Object visit(EnumLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Object visit(EntityLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Object visit(CollectionLiteral e) {
        return visit((Literal) e);
    }

    protected Boolean compare(DomainType leftType, DomainType rightType, Object left, Object right, ComparisonOperator operator) {
        ComparisonOperatorInterpreter comparisonOperatorInterpreter = leftType.getMetadata(ComparisonOperatorInterpreter.class);
        if (comparisonOperatorInterpreter == null) {
            throw new IllegalArgumentException("No comparison operator interpreter available for type: " + leftType);
        }
        return comparisonOperatorInterpreter.interpret(context, leftType, rightType, left, right, operator);
    }

    protected Object arithmetic(DomainType targetType, DomainType leftType, DomainType rightType, Object left, Object right, DomainOperator operator) {
        DomainOperatorInterpreter domainOperatorInterpreter = targetType.getMetadata(DomainOperatorInterpreter.class);
        if (domainOperatorInterpreter == null) {
            throw new IllegalArgumentException("No domain operator interpreter available for type: " + targetType);
        }
        return domainOperatorInterpreter.interpret(context, targetType, leftType, rightType, left, right, operator);
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    protected static final class DefaultDomainFunctionArguments implements DomainFunctionArguments {

        private final Object[] values;
        private final DomainType[] types;
        private final int assignedArguments;

        public DefaultDomainFunctionArguments(Object[] values, DomainType[] types, int assignedArguments) {
            this.values = values;
            this.types = types;
            this.assignedArguments = assignedArguments;
        }

        @Override
        public Object getValue(int position) {
            try {
                return values[position];
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            }
        }

        @Override
        public DomainType getType(int position) {
            try {
                return types[position];
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            }
        }

        @Override
        public int assignedArguments() {
            return assignedArguments;
        }
    }
}
