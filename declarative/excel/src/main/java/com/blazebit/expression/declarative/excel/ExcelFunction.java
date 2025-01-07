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

package com.blazebit.expression.declarative.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the excel related rendering of a declaratively defined domain function as template string.
 * The domain function arguments are rendered into placeholders.
 *
 * A function like
 *
 * <code>
 * &#064;DomainFunction("IS_OLD")
 * &#064;ExcelFunction("A?1 &gt; 18")
 * static Boolean isOld(&#064;DomainFunctionParam("person") User user) {
 *     return user.getAge() &gt; 18;
 * }
 * </code>
 *
 * will render for the use of the function like <code>IS_OLD(me)</code>
 * to something like <code>A5 &gt; 18</code>
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ExcelFunction {

    /**
     * The function expression template.
     *
     * @return the function expression template
     */
    String value();

    /**
     * The argument separator that is used in the template.
     *
     * @return The used argument separator
     */
    char argumentSeparator() default ',';
}
