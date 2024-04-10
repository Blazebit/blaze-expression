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

package com.blazebit.expression.base;

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class StringlyOperatorInterpreter implements ComparisonOperatorInterpreter, Serializable {

    private final StringlyTypeHandler<Object> handler;

    /**
     * Create a new operator interpreter with the given stringly type handler.
     *
     * @param handler The stringly type handler
     */
    public StringlyOperatorInterpreter(StringlyTypeHandler<?> handler) {
        this.handler = (StringlyTypeHandler<Object>) handler;
    }

    @Override
    public Boolean interpret(ExpressionInterpreter.Context context, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
        Object referenceValue;
        if (leftType == rightType) {
            referenceValue = leftValue;
        } else {
            referenceValue = handler.destruct(leftValue);
        }
        switch (operator) {
            case EQUAL:
                return referenceValue.equals(rightValue);
            case NOT_EQUAL:
                return !referenceValue.equals(rightValue);
            default:
                break;
        }

        throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }
}
