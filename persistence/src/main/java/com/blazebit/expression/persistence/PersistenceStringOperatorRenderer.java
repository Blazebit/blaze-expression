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
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.DomainModelException;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceStringOperatorRenderer implements PersistenceDomainOperatorRenderer, Serializable {

    public static final PersistenceStringOperatorRenderer INSTANCE = new PersistenceStringOperatorRenderer();

    private PersistenceStringOperatorRenderer() {
    }

    @Override
    public boolean render(ChainingArithmeticExpression e, PersistenceExpressionSerializer serializer) {
        if (e.getOperator().getDomainOperator() == DomainOperator.PLUS) {
            StringBuilder sb = serializer.getStringBuilder();
            PersistenceStringlyTypeHandler stringlyTypeHandler = e.getRight().getType().getMetadata(PersistenceStringlyTypeHandler.class);
            sb.append("CONCAT(");
            boolean isConstant = e.getLeft().accept(serializer);
            sb.append(", ");
            if (stringlyTypeHandler == null) {
                isConstant = e.getRight().accept(serializer) && isConstant;
            } else {
                isConstant = stringlyTypeHandler.appendPersistenceDestructTo(sb, stringBuilder -> {
                    if (sb == stringBuilder) {
                        return e.getRight().accept(serializer);
                    } else {
                        int idx = sb.length();
                        boolean constant = e.getRight().accept(serializer);
                        stringBuilder.append(sb, idx, sb.length());
                        sb.setLength(idx);
                        return constant;
                    }
                }) && isConstant;
            }
            sb.append(')');
            return isConstant;
        } else {
            throw new DomainModelException("Can't handle the operator " + e.getOperator().getDomainOperator() + " for the arguments [" + e.getLeft() + ", " + e.getRight() + "]!");
        }
    }
}
