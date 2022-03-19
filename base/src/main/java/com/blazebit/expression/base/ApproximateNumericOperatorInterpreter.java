/*
 * Copyright 2019 - 2022 Blazebit.
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

package com.blazebit.expression.base;

import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainOperatorInterpreter;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ApproximateNumericOperatorInterpreter implements ComparisonOperatorInterpreter, DomainOperatorInterpreter, Serializable {

    public static final ApproximateNumericOperatorInterpreter INSTANCE = new ApproximateNumericOperatorInterpreter();

    private ApproximateNumericOperatorInterpreter() {
    }

    @Override
    public Boolean interpret(ExpressionInterpreter.Context context, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
        if (leftValue == null || rightValue == null) {
            return null;
        }
        Comparable l;
        Comparable r;
        if (leftValue instanceof Double && rightValue instanceof Long) {
            l = (Comparable) leftValue;
            r = ((Long) rightValue).doubleValue();
        } else if (leftValue instanceof Long && rightValue instanceof Double) {
            l = ((Long) leftValue).doubleValue();
            r = (Comparable) rightValue;
        } else {
            l = (Comparable) leftValue;
            r = (Comparable) rightValue;
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

        throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }

    @Override
    public Object interpret(ExpressionInterpreter.Context context, DomainType targetType, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, DomainOperator operator) {
        if (rightValue == null) {
            if (leftValue == null) {
                return null;
            }
            if (operator == DomainOperator.UNARY_MINUS) {
                if (leftValue instanceof Double) {
                    return -((Double) leftValue);
                } else if (leftValue instanceof Long) {
                    return -((Long) leftValue);
                }
            }
        } else {
            if (leftValue == null) {
                return null;
            }

            if (rightValue instanceof String) {
                return leftValue + rightValue.toString();
            } else if (leftValue instanceof Long && rightValue instanceof Long) {
                long l = (Long) leftValue;
                long r = (Long) rightValue;

                switch (operator) {
                    case PLUS:
                        return l + r;
                    case MINUS:
                        return l - r;
                    case MULTIPLICATION:
                        return l * r;
                    case DIVISION:
                        return l / r;
                    case MODULO:
                        return l % r;
                    default:
                        break;
                }
            } else {
                double l;
                double r;
                if (leftValue instanceof Double && rightValue instanceof Long) {
                    l = (Double) leftValue;
                    r = ((Long) rightValue).doubleValue();
                } else if (leftValue instanceof Long && rightValue instanceof Double) {
                    l = ((Long) leftValue).doubleValue();
                    r = (Double) rightValue;
                } else if (leftValue instanceof Double && rightValue instanceof Double) {
                    l = (Double) leftValue;
                    r = (Double) rightValue;
                } else {
                    throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
                }

                switch (operator) {
                    case PLUS:
                        return l + r;
                    case MINUS:
                        return l - r;
                    case MULTIPLICATION:
                        return l * r;
                    case DIVISION:
                        return l / r;
                    case MODULO:
                        return l % r;
                    default:
                        break;
                }
            }
        }

        throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }
}
