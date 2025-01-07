/*
 * Copyright 2019 - 2025 Blazebit.
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

package com.blazebit.expression.excel;

import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.Expression;
import com.blazebit.expression.Literal;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelIntervalOperatorRenderer implements ExcelDomainOperatorRenderer, Serializable {

    public static final ExcelIntervalOperatorRenderer INSTANCE = new ExcelIntervalOperatorRenderer();

    private ExcelIntervalOperatorRenderer() {
    }

    @Override
    public boolean render(ChainingArithmeticExpression e, ExcelExpressionSerializer serializer) {
        DomainOperator domainOperator = e.getOperator().getDomainOperator();
        if (domainOperator == DomainOperator.PLUS || domainOperator == DomainOperator.MINUS) {
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
                return renderIntervalArithmetic(serializer, domainOperator, expression, interval, sb);
            }
        }
        throw new DomainModelException("Can't handle the operator " + domainOperator + " for the arguments [" + e.getLeft() + ", " + e.getRight() + "]!");
    }

    /**
     * Render interval arithmetic.
     *
     * @param serializer The serializer
     * @param domainOperator The domain operator
     * @param expression The lhs
     * @param interval The rhs
     * @param sb The string builder
     * @return Whether the expression is constant
     */
    static boolean renderIntervalArithmetic(ExcelExpressionSerializer serializer, DomainOperator domainOperator, Expression expression, TemporalInterval interval, StringBuilder sb) {
        String operator = domainOperator == DomainOperator.PLUS ? " + " : " - ";
        String argumentSeparator = serializer.getArgumentSeparator();
        sb.append("(DATE(YEAR(");
        boolean constant = expression.accept(serializer);
        sb.append(')');
        if (interval.getYears() != 0) {
            sb.append(operator).append(interval.getYears());
        }
        sb.append(argumentSeparator).append(" MONTH(");
        expression.accept(serializer);
        sb.append(')');
        if (interval.getMonths() != 0) {
            sb.append(operator).append(interval.getMonths());
        }
        sb.append(argumentSeparator).append(" DAY(");
        expression.accept(serializer);
        sb.append(')');
        if (interval.getDays() != 0) {
            sb.append(operator).append(interval.getDays());
        }

        sb.append(") + TIME(HOUR(");
        expression.accept(serializer);
        sb.append(')');
        sb.append(argumentSeparator).append(" MINUTE(");
        expression.accept(serializer);
        sb.append(')');
        sb.append(argumentSeparator).append(" SECOND(");
        expression.accept(serializer);
        sb.append(")))");
        if (interval.getHours() != 0 || interval.getMinutes() != 0 || interval.getSeconds() != 0) {
            sb.append(operator);
            sb.append("TIME(");
            sb.append(interval.getHours());
            sb.append(argumentSeparator).append(' ');
            sb.append(interval.getMinutes());
            sb.append(argumentSeparator).append(' ');
            sb.append(interval.getSeconds());
            sb.append(")");
        }
        return constant;
    }
}
