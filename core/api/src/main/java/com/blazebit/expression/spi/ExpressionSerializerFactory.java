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

package com.blazebit.expression.spi;

import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;

/**
 * A factory for custom expression serializers.
 * Interface implemented by the expression implementation provider or extension provider.
 *
 * Implementations are instantiated via {@link java.util.ServiceLoader}.
 *
 * @param <T> The serialization target type
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionSerializerFactory<T> {

    /**
     * Returns the serialization target type class.
     *
     * @return the serialization target type class
     */
    public Class<T> getSerializationTargetType();

    /**
     * Returns the supported serialization format.
     *
     * @return the supported serialization format
     */
    default String getSerializationFormat() {
        return null;
    }

    /**
     * Returns a new serializer for the given expression service.
     *
     * @param expressionService The expression service the serializer should be based on
     * @return a new serializer for the given domain model
     */
    public ExpressionSerializer<T> createSerializer(ExpressionService expressionService);
}
