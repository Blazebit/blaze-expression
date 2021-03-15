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

package com.blazebit.expression.spi;

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ExpressionInterpreter;

/**
 * An adapter for converting between a model and the internal expression type.
 *
 * @param <X> The model type
 * @param <Y> The internal type
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface TypeAdapter<X, Y> {

    /**
     * Converts the given value in model representation of the given domain type to the internal representation.
     *
     * @param context The interpreter context
     * @param value The value to convert
     * @param domainType The domain type of the value
     * @return the internal representation
     */
    public Y toInternalType(ExpressionInterpreter.Context context, X value, DomainType domainType);

    /**
     * Converts the given value in internal representation of the given domain type to the model representation.
     *
     * @param context The interpreter context
     * @param value The value to convert
     * @param domainType The domain type of the value
     * @return the model representation
     */
    public X toModelType(ExpressionInterpreter.Context context, Y value, DomainType domainType);
}
