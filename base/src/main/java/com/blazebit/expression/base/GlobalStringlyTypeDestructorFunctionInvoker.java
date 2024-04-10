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

package com.blazebit.expression.base;

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class GlobalStringlyTypeDestructorFunctionInvoker implements FunctionInvoker, Serializable {

    final Map<String, StringlyTypeHandler<Object>> destructors = new ConcurrentHashMap<>();
    private final String name;

    /**
     * Create a new stringly type destructor function invoker.
     *
     * @param name The name of the function
     */
    public GlobalStringlyTypeDestructorFunctionInvoker(String name) {
        this.name = name;
    }

    /**
     * Returns the stringly type handler for the given type name.
     *
     * @param typeName The type name
     * @return The stringly type handler or <code>null</code>
     */
    public StringlyTypeHandler<?> getHandler(String typeName) {
        return destructors.get(typeName);
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        DomainType type = arguments.getType(0);
        if (type == null) {
            return null;
        }
        StringlyTypeHandler<Object> toString = destructors.get(type.getName());
        if (toString == null) {
            throw new DomainModelException("Unsupported type for destructure function " + name + " function: " + type.getName());
        }
        return toString.destruct(arguments.getValue(0));
    }
}
