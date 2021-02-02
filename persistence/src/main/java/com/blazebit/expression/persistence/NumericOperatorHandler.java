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

package com.blazebit.expression.persistence;

import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainOperatorInterpreter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class NumericOperatorHandler implements ComparisonOperatorInterpreter, DomainOperatorInterpreter, Serializable {

    public static final NumericOperatorHandler INSTANCE = new NumericOperatorHandler();

    private NumericOperatorHandler() {
    }

    @Override
    public Boolean interpret(DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
        if (leftValue == null || rightValue == null) {
            return null;
        }
        Comparable l;
        Comparable r;
        if (leftValue instanceof BigDecimal && rightValue instanceof BigInteger) {
            l = (Comparable) leftValue;
            r = new BigDecimal((BigInteger) rightValue);
        } else if (leftValue instanceof BigInteger && rightValue instanceof BigDecimal) {
            l = new BigDecimal((BigInteger) leftValue);
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
    public Object interpret(DomainType targetType, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, DomainOperator operator) {
        if (rightValue == null) {
            if (leftValue == null) {
                return null;
            }
            if (operator == DomainOperator.UNARY_MINUS) {
                if (leftValue instanceof BigDecimal) {
                    return ((BigDecimal) leftValue).negate();
                } else if (leftValue instanceof BigInteger) {
                    return ((BigInteger) leftValue).negate();
                }
            }
        } else {
            if (leftValue == null) {
                return null;
            }

            if (rightValue instanceof String) {
                return leftValue + rightValue.toString();
            } else if (leftValue instanceof BigInteger && rightValue instanceof BigInteger) {
                BigInteger l = (BigInteger) leftValue;
                BigInteger r = (BigInteger) rightValue;

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
            } else {
                BigDecimal l;
                BigDecimal r;
                if (leftValue instanceof BigDecimal && rightValue instanceof BigInteger) {
                    l = (BigDecimal) leftValue;
                    r = new BigDecimal((BigInteger) rightValue);
                } else if (leftValue instanceof BigInteger && rightValue instanceof BigDecimal) {
                    l = new BigDecimal((BigInteger) leftValue);
                    r = (BigDecimal) rightValue;
                } else if (leftValue instanceof BigDecimal && rightValue instanceof BigDecimal) {
                    l = (BigDecimal) leftValue;
                    r = (BigDecimal) rightValue;
                } else {
                    throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
                }

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
            }
        }

        throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }
}
