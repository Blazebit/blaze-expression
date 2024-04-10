/*
 * Copyright 2019 - 2024 Blazebit.
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
import com.blazebit.expression.ChainingArithmeticExpression;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelNumericOperatorRenderer implements ExcelDomainOperatorRenderer, Serializable {

    public static final ExcelNumericOperatorRenderer INSTANCE = new ExcelNumericOperatorRenderer();

    private ExcelNumericOperatorRenderer() {
    }

    @Override
    public boolean render(ChainingArithmeticExpression e, ExcelExpressionSerializer serializer) {
        StringBuilder sb = serializer.getStringBuilder();
        if (e.getOperator().getDomainOperator() == DomainOperator.MODULO) {
            sb.append("MOD(");
            boolean isConstant = e.getLeft().accept(serializer);
            sb.append(serializer.getArgumentSeparator()).append(' ');
            isConstant = e.getRight().accept(serializer) && isConstant;
            sb.append(')');
            return isConstant;
        } else {
            boolean isConstant = e.getLeft().accept(serializer);
            sb.append(' ');
            sb.append(e.getOperator().getOperator());
            sb.append(' ');
            return e.getRight().accept(serializer) && isConstant;
        }
    }
}
