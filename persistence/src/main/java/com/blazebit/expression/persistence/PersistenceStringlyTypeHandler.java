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

package com.blazebit.expression.persistence;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A contract for defining conversions from and to string representations for a basic type.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface PersistenceStringlyTypeHandler extends Serializable {

    /**
     * A pass through handler.
     */
    PersistenceStringlyTypeHandler INSTANCE = new PersistenceStringlyTypeHandler() {
    };

    /**
     * Renders the construction of the type to the string builder as JPQL.next expression.
     * This is used in the renderer of the constructor function and usually just renders the argument through,
     * because it is assumed to be a string on the persistence side.
     *
     * @param sb       The string builder into which to render
     * @param renderer The consumer that renders the argument to a string builder
     */
    default void appendPersistenceConstructTo(StringBuilder sb, Consumer<StringBuilder> renderer) {
        renderer.accept(sb);
    }

    /**
     * Renders the destruction of the type to the string builder as JPQL.next expression.
     * This is used in the renderer of the destructor function and usually just renders the argument through,
     * because it is assumed to be a string on the persistence side.
     *
     * @param sb       The string builder into which to render
     * @param renderer The consumer that renders the argument to a string builder
     * @return whether the rendered value is constant
     */
    default boolean appendPersistenceDestructTo(StringBuilder sb, Function<StringBuilder, Boolean> renderer) {
        return renderer.apply(sb);
    }

}
