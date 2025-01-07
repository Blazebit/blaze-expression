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

package com.blazebit.expression.base;

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class StringlyTypeConstructorFunctionInvoker implements FunctionInvoker, Serializable {

    private final StringlyTypeHandler<Object> stringlyTypeHandler;

    /**
     * Create a new constructor function invoker with the given stringly type handler.
     *
     * @param stringlyTypeHandler The stringly type handler
     */
    public StringlyTypeConstructorFunctionInvoker(StringlyTypeHandler<?> stringlyTypeHandler) {
        this.stringlyTypeHandler = (StringlyTypeHandler<Object>) stringlyTypeHandler;
    }

    /**
     * Returns the stringly type handler.
     *
     * @return the stringly type handler
     */
    public StringlyTypeHandler<?> getHandler() {
        return stringlyTypeHandler;
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Object value = arguments.getValue(0);
        if (value == null) {
            return null;
        }
        return stringlyTypeHandler.construct((String) value);
    }
}
