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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.DomainModel;

/**
 * A factory for expression related functionality based on a domain model.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionServiceFactory {

    /**
     * The domain model that is used for the expression services.
     *
     * @return the domain model
     */
    public DomainModel getDomainModel();

    /**
     * Creates and returns an expression compiler to parse and type check expression strings.
     *
     * @return the expression compiler
     */
    public ExpressionCompiler createCompiler();

    /**
     * Creates and returns an expression interpreter to interpret a compiled expression.
     *
     * @return the expression interpreter
     */
    public ExpressionInterpreter createInterpreter();

    /**
     * Creates and returns an expression serializer to serialize a compiled expression.
     *
     * @param serializationTarget The serialization target type
     * @param <T> The serialization target type
     * @return the expression serializer
     */
    public <T> ExpressionSerializer<T> createSerializer(Class<T> serializationTarget);

    /**
     * Serializes the given compiled expression to a string.
     *
     * @param expression The expression to serialize
     * @return the string serialized form of the expression
     */
    public default String serialize(Expression expression) {
        ExpressionSerializer<StringBuilder> serializer = createSerializer(StringBuilder.class);
        StringBuilder sb = new StringBuilder();
        serializer.serializeTo(expression, sb);
        return sb.toString();
    }
}
