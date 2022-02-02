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
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.Expression;
import com.blazebit.expression.Literal;

import java.io.Serializable;

public class PersistenceLocalDateOperatorRenderer implements PersistenceDomainOperatorRenderer, Serializable {

    public static final PersistenceLocalDateOperatorRenderer INSTANCE = new PersistenceLocalDateOperatorRenderer();

    private PersistenceLocalDateOperatorRenderer() {
    }

    @Override
    public boolean render(ChainingArithmeticExpression e, PersistenceExpressionSerializer serializer) {
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
                boolean isConstant = expression.accept(serializer);
                if (interval.getYears() != 0) {
                    sb.append(", ");
                    sb.append(interval.getYears() * factor).append(')');
                }
                if (interval.getMonths() != 0) {
                    sb.append(", ");
                    sb.append(interval.getMonths() * factor).append(')');
                }
                if (interval.getDays() != 0) {
                    sb.append(", ");
                    sb.append(interval.getDays() * factor).append(')');
                }
                return isConstant;
            }
        }
        throw new DomainModelException(
                "Can't handle the operator " + domainOperator + " for the arguments [" + e.getLeft() + ", "
                        + e.getRight() + "]!");
    }
}
