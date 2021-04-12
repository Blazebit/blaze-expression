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

package com.blazebit.expression.examples.web.editor;

import com.blazebit.domain.declarative.DomainFunction;
import com.blazebit.domain.declarative.DomainFunctionParam;
import com.blazebit.domain.declarative.DomainFunctions;
import com.blazebit.domain.runtime.model.StaticDomainFunctionTypeResolvers;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@DomainFunctions
public class ExampleDomainFunctions {

    private ExampleDomainFunctions() {
    }

    @DomainFunction(value = "SELF", typeResolver = StaticDomainFunctionTypeResolvers.FirstArgumentDomainFunctionTypeResolver.class)
    public static Object self(@DomainFunctionParam("object") Object o) {
        return o;
    }

    @DomainFunction(value = "THE SELF", typeResolver = StaticDomainFunctionTypeResolvers.FirstArgumentDomainFunctionTypeResolver.class)
    public static Object theSelf(@DomainFunctionParam("object") Object o) {
        return o;
    }

}
