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
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.Expression;
import com.blazebit.expression.Literal;
import com.blazebit.expression.spi.DomainOperatorInterpreter;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class TimestampOperatorHandler implements ComparisonOperatorInterpreter, DomainOperatorInterpreter, DomainOperatorRenderer, Serializable {

    public static final TimestampOperatorHandler INSTANCE = new TimestampOperatorHandler();

    private TimestampOperatorHandler() {
    }

    @Override
    public Boolean interpret(DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
        if (leftValue instanceof Instant && rightValue instanceof Instant) {
            Instant l = (Instant) leftValue;
            Instant r = (Instant) rightValue;
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
        } else {
            throw new DomainModelException("Illegal arguments [" + leftValue + ", " + rightValue + "]!");
        }

        throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }

    @Override
    public Object interpret(DomainType targetType, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, DomainOperator operator) {
        if (leftValue instanceof TemporalInterval && rightValue instanceof TemporalInterval) {
            TemporalInterval interval1 = (TemporalInterval) leftValue;
            TemporalInterval interval2 = (TemporalInterval) rightValue;

            switch (operator) {
                case PLUS:
                    return interval1.add(interval2);
                case MINUS:
                    return interval1.subtract(interval2);
                default:
                    break;
            }
        } else if (leftValue instanceof Instant && rightValue instanceof TemporalInterval) {
            Instant instant = (Instant) leftValue;
            TemporalInterval interval = (TemporalInterval) rightValue;
            switch (operator) {
                case PLUS:
                    return interval.add(instant);
                case MINUS:
                    return interval.subtract(instant);
                default:
                    break;
            }
        } else if (leftValue instanceof TemporalInterval && rightValue instanceof Instant) {
            TemporalInterval interval = (TemporalInterval) leftValue;
            Instant instant = (Instant) rightValue;

            if (operator == DomainOperator.PLUS) {
                return interval.add(instant);
            }
        } else {
            throw new DomainModelException("Illegal arguments [" + leftValue + ", " + rightValue + "]!");
        }

        throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }

    @Override
    public void render(ChainingArithmeticExpression e, PersistenceExpressionSerializer serializer) {
        DomainOperator domainOperator = e.getOperator().getDomainOperator();
        if (domainOperator == DomainOperator.PLUS || domainOperator == DomainOperator.MINUS) {
            int factor = domainOperator == DomainOperator.PLUS ? 1 : -1;
            Expression expression = null;
            TemporalInterval interval = null;
            StringBuilder sb = serializer.getStringBuilder();
            if (e.getLeft() instanceof Literal) {
                if (domainOperator == DomainOperator.PLUS) {
                    expression = e.getRight();
                    interval = (TemporalInterval) ((Literal) e.getLeft()).getValue();
                }
            } else if (e.getRight() instanceof Literal) {
                expression = e.getLeft();
                interval = (TemporalInterval) ((Literal) e.getRight()).getValue();
            }

            if (interval != null) {
                if (interval.getYears() != 0) {
                    sb.append("ADD_YEAR(");
                }
                if (interval.getMonths() != 0) {
                    sb.append("ADD_MONTH(");
                }
                if (interval.getDays() != 0) {
                    sb.append("ADD_DAY(");
                }
                int seconds = 0;
                if (interval.getHours() != 0) {
                    seconds = interval.getHours() * 60 * 60;
                }
                if (interval.getMinutes() != 0) {
                    seconds += interval.getMinutes() * 60;
                }
                if (interval.getSeconds() != 0) {
                    seconds += interval.getSeconds();
                }
                if (seconds != 0) {
                    sb.append("ADD_SECOND(");
                }
                expression.accept(serializer);
                if (seconds != 0) {
                    sb.append(", ");
                    sb.append(seconds * factor).append(')');
                }
                if (interval.getDays() != 0) {
                    sb.append(", ");
                    sb.append(interval.getDays() * factor).append(')');
                }
                if (interval.getMonths() != 0) {
                    sb.append(", ");
                    sb.append(interval.getMonths() * factor).append(')');
                }
                if (interval.getYears() != 0) {
                    sb.append(", ");
                    sb.append(interval.getYears() * factor).append(')');
                }
            }
        }
        throw new DomainModelException("Can't handle the operator " + domainOperator + " for the arguments [" + e.getLeft() + ", " + e.getRight() + "]!");
    }
}
