/*
 * Copyright 2019 Blazebit.
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

package com.blazebit.expression.declarative;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.FunctionInvoker;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MethodFunctionInvoker implements MetadataDefinition<FunctionInvoker>, FunctionInvoker {

    private final Method function;
    private final int parameterCount;

    public MethodFunctionInvoker(Method function, int parameterCount) {
        this.function = function;
        this.parameterCount = parameterCount;
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        try {
            Object[] args = new Object[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                args[i] = arguments.get(function.getArgument(i));
            }
            return this.function.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't invoke function " + function + " with arguments [" + arguments + "]", e);
        }
    }

    @Override
    public Class<FunctionInvoker> getJavaType() {
        return FunctionInvoker.class;
    }

    @Override
    public FunctionInvoker build(MetadataDefinitionHolder<?> definitionHolder) {
        return this;
    }
}
