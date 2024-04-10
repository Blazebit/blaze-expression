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

package com.blazebit.expression.spi;

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ExpressionInterpreter;

/**
 * An interpreter for comparison operators that is registered as metadata on a domain type.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ComparisonOperatorInterpreter {

    /**
     * Interprets the comparison operator as applied on the given values of the given domain types to a Boolean.
     *
     * @param context The expression interpreter context
     * @param leftType The domain type of the left value
     * @param rightType The domain type of the right value
     * @param leftValue The left value
     * @param rightValue The right value
     * @param operator The domain operator
     * @return the interpretation result
     */
    public Boolean interpret(ExpressionInterpreter.Context context, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator);
}
