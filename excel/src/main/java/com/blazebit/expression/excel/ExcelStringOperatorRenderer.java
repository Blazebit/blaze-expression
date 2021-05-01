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

package com.blazebit.expression.excel;

import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.DomainModelException;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelStringOperatorRenderer implements ExcelDomainOperatorRenderer, Serializable {

    public static final ExcelStringOperatorRenderer INSTANCE = new ExcelStringOperatorRenderer();

    private ExcelStringOperatorRenderer() {
    }

    @Override
    public boolean render(ChainingArithmeticExpression e, ExcelExpressionSerializer serializer) {
        if (e.getOperator().getDomainOperator() == DomainOperator.PLUS) {
            StringBuilder sb = serializer.getStringBuilder();
            ExcelStringlyTypeHandler stringlyTypeHandler = e.getRight().getType().getMetadata(ExcelStringlyTypeHandler.class);
            boolean isConstant = e.getLeft().accept(serializer);
            sb.append(" & ");
            if (stringlyTypeHandler == null) {
                return e.getRight().accept(serializer) && isConstant;
            } else {
                return stringlyTypeHandler.appendExcelDestructTo(sb, stringBuilder -> {
                    if (sb == stringBuilder) {
                        return e.getRight().accept(serializer);
                    } else {
                        int idx = sb.length();
                        boolean rightConstant = e.getRight().accept(serializer);
                        stringBuilder.append(sb, idx, sb.length());
                        sb.setLength(idx);
                        return rightConstant;
                    }
                }) && isConstant;
            }
        } else {
            throw new DomainModelException("Can't handle the operator " + e.getOperator().getDomainOperator() + " for the arguments [" + e.getLeft() + ", " + e.getRight() + "]!");
        }
    }
}
