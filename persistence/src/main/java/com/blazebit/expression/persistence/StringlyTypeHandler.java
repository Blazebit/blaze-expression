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

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * A contract for defining conversions from and to string representations for a basic type.
 *
 * @param <T> The stringly type
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface StringlyTypeHandler<T> extends Serializable {

    /**
     * Creates an instance of the type from the given string.
     * This is used in the interpreter for the constructor function which is usually named like the type.
     *
     * @param string The string
     * @return The instance of the stringly type
     * @throws com.blazebit.expression.DomainModelException When the string can't be converted
     */
    T construct(String string);

    /**
     * Renders the construction of the type to the string builder as JPQL.next expression.
     * This is used in the renderer of the constructor function and usually just renders the argument through,
     * because it is assumed to be a string on the persistence side.
     *
     * @param sb       The string builder into which to render
     * @param renderer The consumer that renders the argument to a string builder
     */
    default void appendConstructTo(StringBuilder sb, Consumer<StringBuilder> renderer) {
        renderer.accept(sb);
    }

    /**
     * Creates the string representation for the given instance type.
     * This is used in the interpreter for the destructor function.
     *
     * @param value The instance of the stringly type
     * @return The string representation
     */
    default String destruct(T value) {
        return value.toString();
    }

    /**
     * Renders the destruction of the type to the string builder as JPQL.next expression.
     * This is used in the renderer of the destructor function and usually just renders the argument through,
     * because it is assumed to be a string on the persistence side.
     *
     * @param sb       The string builder into which to render
     * @param renderer The consumer that renders the argument to a string builder
     */
    default void appendDestructTo(StringBuilder sb, Consumer<StringBuilder> renderer) {
        renderer.accept(sb);
    }

}
