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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.spi.AttributeAccessor;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainOperatorInterpreter;
import com.blazebit.expression.spi.FunctionInvoker;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionInterpreterImpl implements Expression.ResultVisitor<Object>, ExpressionInterpreter {

    private final DomainModel domainModel;
    private Context context;

    public ExpressionInterpreterImpl(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    @Override
    public Context createContext(Map<String, DomainType> rootDomainTypes, Map<String, Object> rootObjects) {
        return new Context() {

            private final Map<String, Object> properties = new HashMap<>();

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

    @Override
    public <T> T evaluate(Expression expression, Context interpreterContext) {
        Context oldContext = context;
        context = interpreterContext;
        try {
            return (T) expression.accept(this);
        } finally {
            context = oldContext;
        }
    }

    @Override
    public Boolean evaluate(Predicate expression, Context interpreterContext) {
        return Boolean.TRUE.equals(evaluate((Expression) expression, interpreterContext));
    }

    @Override
    public Object visit(ArithmeticFactor e) {
        Object result = e.getExpression().accept(this);
        if (result == null) {
            return null;
        }
        if (e.isInvertSignum()) {
            return arithmetic(e.getType(), e.getType(), null, result, null, DomainOperator.UNARY_MINUS);
        }
        return result;
    }

    @Override
    public Object visit(ExpressionPredicate e) {
        Object left = e.getExpression().accept(this);
        if (left == null) {
            return null;
        }
        Boolean testValue = e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
        Boolean b = compare(e.getExpression().getType(), e.getExpression().getType(), left, left, ComparisonOperator.EQUAL);
        if (!testValue.equals(b)) {
            return b;
        }
        return testValue;
    }

    @Override
    public Object visit(BetweenPredicate e) {
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
    }

    @Override
    public Object visit(InPredicate e) {
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
    }

    @Override
    public Object visit(ChainingArithmeticExpression e) {
        Object left = e.getLeft().accept(this);
        if (left == null) {
            return null;
        }
        Object right = e.getRight().accept(this);
        if (right == null) {
            return null;
        }

        return arithmetic(e.getType(), e.getLeft().getType(), e.getRight().getType(), left, right, e.getOperator().getDomainOperator());
    }

    @Override
    public Object visit(CompoundPredicate e) {
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
    }

    @Override
    public Object visit(ComparisonPredicate e) {
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
    }

    @Override
    public Object visit(IsNullPredicate e) {
        // LEFT  NN NOT R
        // NULL  F   F  T
        // NULL  F   T  F
        // VAL   T   F  F
        // VAL   T   T  T
        return (e.getLeft().accept(this) != null) == e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Object visit(Path e) {
        Object value = context.getRoot(e.getAlias());
        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            if (value == null) {
                return null;
            }
            EntityDomainTypeAttribute attribute = attributes.get(i);
            AttributeAccessor attributeAccessor = attribute.getMetadata(AttributeAccessor.class);
            if (attributeAccessor == null) {
                throw new IllegalArgumentException("No attribute accessor available for attribute: " + attribute);
            }
            value = attributeAccessor.getAttribute(value, attribute);
        }

        return value;
    }

    @Override
    public Object visit(FunctionInvocation e) {
        FunctionInvoker functionInvoker = e.getFunction().getMetadata(FunctionInvoker.class);
        if (functionInvoker == null) {
            throw new IllegalArgumentException("No function invoker available for function: " + e.getFunction());
        }

        Map<DomainFunctionArgument, Expression> arguments = e.getArguments();
        Map<DomainFunctionArgument, Object> argumentValues;

        if (arguments.isEmpty()) {
            argumentValues = Collections.emptyMap();
        } else {
            argumentValues = new LinkedHashMap<>(arguments.size());
            for (Map.Entry<DomainFunctionArgument, Expression> entry : arguments.entrySet()) {
                argumentValues.put(entry.getKey(), entry.getValue().accept(this));
            }
        }

        return functionInvoker.invoke(context, e.getFunction(), argumentValues);
    }

    @Override
    public Object visit(Literal e) {
        return e.getValue();
    }

    private Boolean compare(DomainType leftType, DomainType rightType, Object left, Object right, ComparisonOperator operator) {
        ComparisonOperatorInterpreter comparisonOperatorInterpreter = leftType.getMetadata(ComparisonOperatorInterpreter.class);
        if (comparisonOperatorInterpreter == null) {
            throw new IllegalArgumentException("No comparison operator interpreter available for type: " + leftType);
        }
        return comparisonOperatorInterpreter.interpret(leftType, rightType, left, right, operator);
    }

    private Object arithmetic(DomainType targetType, DomainType leftType, DomainType rightType, Object left, Object right, DomainOperator operator) {
        DomainOperatorInterpreter domainOperatorInterpreter = targetType.getMetadata(DomainOperatorInterpreter.class);
        if (domainOperatorInterpreter == null) {
            throw new IllegalArgumentException("No domain operator interpreter available for type: " + targetType);
        }
        return domainOperatorInterpreter.interpret(targetType, leftType, rightType, left, right, operator);
    }
}
