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

import com.blazebit.expression.ExpressionService;

import java.util.Map;

/**
 * A {@link java.util.ServiceLoader} loaded serializer that can serialize expression service elements.
 *
 * @param <X> The expression service element type
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionServiceSerializer<X> {

    /**
     * Returns whether this serializer can serialize the element.
     *
     * @param element The element to check
     * @return Whether this serializer can serialize the element
     */
    default boolean canSerialize(Object element) {
        return true;
    }

    /**
     * Serializes the expression service to the given target type with the given format.
     *
     * @param expressionService The expression service
     * @param element The element to serialize
     * @param targetType The target type
     * @param format The serialization format
     * @param properties Serialization properties
     * @param <T> The target type
     * @return The serialized form or <code>null</code> if the type or format is unsupported
     */
    public <T> T serialize(ExpressionService expressionService, X element, Class<T> targetType, String format, Map<String, Object> properties);

    /**
     * Serializes the expression service to the given target type with the given format.
     * It only serializes elements that do not belong to the given base model already or are overridden.
     *
     * @param expressionService The expression service
     * @param baseModel The base expression service
     * @param element The element to serialize
     * @param targetType The target type
     * @param format The serialization format
     * @param properties Serialization properties
     * @param <T> The target type
     * @return The serialized form or <code>null</code> if the type or format is unsupported
     */
    default <T> T serialize(ExpressionService expressionService, ExpressionService baseModel, X element, Class<T> targetType, String format, Map<String, Object> properties) {
        return serialize(expressionService, element, targetType, format, properties);
    }

}
