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

import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainOperatorInterpreter;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class StringOperatorHandler implements ComparisonOperatorInterpreter, DomainOperatorInterpreter, DomainOperatorRenderer, Serializable {

    public static final StringOperatorHandler INSTANCE = new StringOperatorHandler();

    private StringOperatorHandler() {
    }

    @Override
    public Boolean interpret(DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
        String l;
        String r;
        if (leftValue instanceof String && rightValue instanceof String) {
            l = (String) leftValue;
            r = (String) rightValue;
        } else {
            throw new IllegalArgumentException("Illegal arguments [" + leftValue + ", " + rightValue + "]!");
        }

        switch (operator) {
            case EQUAL:
                return l.compareTo(r) == 0;
            case NOT_EQUAL:
                return l.compareTo(r) != 0;
            case GREATER_OR_EQUAL:
                return l.compareTo(r) > -1;
            case GREATER:
                return l.compareTo(r) > 0;
            case LOWER_OR_EQUAL:
                return l.compareTo(r) < 1;
            case LOWER:
                return l.compareTo(r) < 0;
            default:
                break;
        }

        throw new IllegalArgumentException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }

    @Override
    public Object interpret(DomainType targetType, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, DomainOperator operator) {
        if (operator == DomainOperator.PLUS) {
            return leftValue.toString().concat(rightValue.toString());
        }

        throw new IllegalArgumentException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }

    @Override
    public void render(ChainingArithmeticExpression e, PersistenceExpressionSerializer serializer) {
        if (e.getOperator().getDomainOperator() == DomainOperator.PLUS) {
            StringBuilder sb = serializer.getStringBuilder();
            sb.append("CONCAT(");
            e.getLeft().accept(serializer);
            sb.append(", ");
            e.getRight().accept(serializer);
            sb.append(')');
        } else {
            throw new IllegalArgumentException("Can't handle the operator " + e.getOperator().getDomainOperator() + " for the arguments [" + e.getLeft() + ", " + e.getRight() + "]!");
        }
    }
}
