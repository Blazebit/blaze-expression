/*
 * Copyright 2019 - 2022 Blazebit.
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

import java.io.Serializable;

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
     * Creates the string representation for the given instance type.
     * This is used in the interpreter for the destructor function.
     *
     * @param value The instance of the stringly type
     * @return The string representation
     */
    default String destruct(T value) {
        return value.toString();
    }

}
