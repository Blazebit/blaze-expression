/*
 * Copyright 2019 - 2020 Blazebit.
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
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainFunctionTypeResolver;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.spi.DomainSerializer;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@DomainFunctions
public class ExampleDomainFunctions {

    private ExampleDomainFunctions() {
    }

    @DomainFunction(value = "SELF", typeResolver = FirstArgumentDomainFunctionTypeResolver.class)
    public static Object self(@DomainFunctionParam("object") Object o) {
        return o;
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    public static class FirstArgumentDomainFunctionTypeResolver implements DomainFunctionTypeResolver, DomainSerializer<DomainFunctionTypeResolver>, Serializable {

        @Override
        public DomainType resolveType(DomainModel domainModel, com.blazebit.domain.runtime.model.DomainFunction function, Map<DomainFunctionArgument, DomainType> argumentTypes) {
            for (DomainFunctionArgument argument : function.getArguments()) {
                DomainType domainType = argumentTypes.get(argument);
                if (domainType != null) {
                    return domainType;
                }
            }

            return null;
        }

        @Override
        public <T> T serialize(DomainModel domainModel, DomainFunctionTypeResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            return (T) "\"FirstArgumentDomainFunctionTypeResolver\"";
        }
    }
}
