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

package com.blazebit.expression.declarative.impl;

import java.io.Serializable;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EnumComparisonOperatorInterpreter implements ComparisonOperatorInterpreter,
        MetadataDefinition<ComparisonOperatorInterpreter>, Serializable {

    public static final EnumComparisonOperatorInterpreter INSTANCE = new EnumComparisonOperatorInterpreter();

    private EnumComparisonOperatorInterpreter() {
    }

    @Override
    public Class<ComparisonOperatorInterpreter> getJavaType() {
        return ComparisonOperatorInterpreter.class;
    }

    @Override
    public ComparisonOperatorInterpreter build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }

    @Override
    public Boolean interpret(ExpressionInterpreter.Context context, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
        if (leftValue == null || rightValue == null) {
            return null;
        }
        String leftEnumValue;
        String rightEnumValue;
        if (leftValue instanceof EnumDomainTypeValue) {
            leftEnumValue = ((EnumDomainTypeValue) leftValue).getValue();
        } else {
            leftEnumValue = ((Enum<?>) leftValue).name();
        }
        if (rightValue instanceof EnumDomainTypeValue) {
            rightEnumValue = ((EnumDomainTypeValue) rightValue).getValue();
        } else {
            rightEnumValue = ((Enum<?>) rightValue).name();
        }

        switch (operator) {
            case EQUAL:
                return leftEnumValue.equals(rightEnumValue);
            case NOT_EQUAL:
                return !leftEnumValue.equals(rightEnumValue);
            default:
                break;
        }

        throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
    }

}
