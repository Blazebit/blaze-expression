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
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.persistence.util.ConversionUtils;
import com.blazebit.expression.spi.DomainOperatorInterpreter;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class NumericOperatorHandler implements ComparisonOperatorInterpreter, DomainOperatorInterpreter {

    public static final NumericOperatorHandler INSTANCE = new NumericOperatorHandler();

    private NumericOperatorHandler() {
    }

    @Override
    public Boolean interpret(DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
        Comparable l;
        Comparable r;
        if (leftValue instanceof BigDecimal || rightValue instanceof BigDecimal || leftValue instanceof Double || rightValue instanceof Double || leftValue instanceof Float || rightValue instanceof Float) {
            l = ConversionUtils.getBigDecimal(leftValue);
            r = ConversionUtils.getBigDecimal(rightValue);
        } else if (leftValue instanceof BigInteger || rightValue instanceof BigInteger) {
            l = ConversionUtils.getBigInteger(leftValue);
            r = ConversionUtils.getBigInteger(rightValue);
        } else if (leftValue instanceof Long || rightValue instanceof Long) {
            l = leftValue instanceof Long ? (Long) leftValue : ((Number) leftValue).longValue();
            r = rightValue instanceof Long ? (Long) rightValue : ((Number) rightValue).longValue();
        } else if (leftValue instanceof Integer || rightValue instanceof Integer) {
            l = leftValue instanceof Integer ? (Integer) leftValue : ((Number) leftValue).intValue();
            r = rightValue instanceof Integer ? (Integer) rightValue : ((Number) rightValue).intValue();
        } else if (leftValue instanceof Short || rightValue instanceof Short) {
            l = leftValue instanceof Short ? (Short) leftValue : ((Number) leftValue).shortValue();
            r = rightValue instanceof Short ? (Short) rightValue : ((Number) rightValue).shortValue();
        } else if (leftValue instanceof Byte || rightValue instanceof Byte) {
            l = leftValue instanceof Byte ? (Byte) leftValue : ((Number) leftValue).byteValue();
            r = rightValue instanceof Byte ? (Byte) rightValue : ((Number) rightValue).byteValue();
        } else if (leftValue instanceof Number && rightValue instanceof Number) {
            l = ((Number) leftValue).doubleValue();
            r = ((Number) rightValue).doubleValue();
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
        if (rightValue == null) {
            if (operator == DomainOperator.UNARY_MINUS) {
                if (leftValue instanceof BigDecimal) {
                    return ((BigDecimal) leftValue).negate();
                } else if (leftValue instanceof BigInteger) {
                    return ((BigInteger) leftValue).negate();
                }
            }
        } else {
            if (leftValue instanceof BigDecimal || rightValue instanceof BigDecimal || leftValue instanceof Double || rightValue instanceof Double || leftValue instanceof Float || rightValue instanceof Float) {
                BigDecimal l = ConversionUtils.getBigDecimal(leftValue);
                BigDecimal r = ConversionUtils.getBigDecimal(rightValue);

                switch (operator) {
                    case PLUS:
                        return l.add(r);
                    case MINUS:
                        return l.subtract(r);
                    case MULTIPLICATION:
                        return l.multiply(r);
                    case DIVISION:
                        return l.divide(r);
                    case MODULO:
                        return l.remainder(r);
                    default:
                        break;
                }
            } else if (leftValue instanceof BigInteger || rightValue instanceof BigInteger) {
                BigInteger l = ConversionUtils.getBigInteger(leftValue);
                BigInteger r = ConversionUtils.getBigInteger(rightValue);

                switch (operator) {
                    case PLUS:
                        return l.add(r);
                    case MINUS:
                        return l.subtract(r);
                    case MULTIPLICATION:
                        return l.multiply(r);
                    case DIVISION:
                        return l.divide(r);
                    case MODULO:
                        return l.remainder(r);
                    default:
                        break;
                }
            } else if (leftValue instanceof Long || rightValue instanceof Long) {
                long l = leftValue instanceof Long ? (Long) leftValue : ((Number) leftValue).longValue();
                long r = rightValue instanceof Long ? (Long) rightValue : ((Number) rightValue).longValue();
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
            } else if (leftValue instanceof Integer || rightValue instanceof Integer) {
                int l = leftValue instanceof Integer ? (Integer) leftValue : ((Number) leftValue).intValue();
                int r = rightValue instanceof Integer ? (Integer) rightValue : ((Number) rightValue).intValue();
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
            } else if (leftValue instanceof Short || rightValue instanceof Short) {
                short l = leftValue instanceof Short ? (Short) leftValue : ((Number) leftValue).shortValue();
                short r = rightValue instanceof Short ? (Short) rightValue : ((Number) rightValue).shortValue();
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
            } else if (leftValue instanceof Byte || rightValue instanceof Byte) {
                byte l = leftValue instanceof Byte ? (Byte) leftValue : ((Number) leftValue).byteValue();
                byte r = rightValue instanceof Byte ? (Byte) rightValue : ((Number) rightValue).byteValue();
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
            } else if (leftValue instanceof Number && rightValue instanceof Number) {
                double l = ((Number) leftValue).doubleValue();
                double r = ((Number) rightValue).doubleValue();
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

        throw new IllegalArgumentException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }
}
