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

package com.blazebit.expression.declarative;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MethodFunctionInvoker implements MetadataDefinition<FunctionInvoker>, FunctionInvoker, Serializable {

    private final Method function;
    private final boolean usesInterpreterContext;
    private final Class<?> varArgComponentType;
    private final int parameterCount;

    public MethodFunctionInvoker(Method function, int parameterCount) {
        function.setAccessible(true);
        this.function = function;
        this.usesInterpreterContext = function.getParameterCount() > 0 && function.getParameterTypes()[0] == ExpressionInterpreter.Context.class;
        this.varArgComponentType = function.isVarArgs() ? function.getParameterTypes()[function.getParameterCount() - 1].getComponentType() : null;
        this.parameterCount = parameterCount;
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        try {
            Object[] args = new Object[parameterCount];
            int i = 0;
            int offset = 0;
            if (usesInterpreterContext) {
                args[i] = context;
                offset = 1;
            }
            int argumentCount = function.getArguments().size();
            int end = Math.min(parameterCount - offset, argumentCount) - 1;
            for (; i < end; i++) {
                args[i + offset] = arguments.get(function.getArgument(i));
            }
            if (varArgComponentType == null) {
                args[i + offset] = arguments.get(function.getArgument(i));
            } else {
                Collection<Object> varArgs = (Collection<Object>) arguments.get(function.getArgument(i));
                args[i + offset] = varArgs.toArray((Object[]) Array.newInstance(varArgComponentType, varArgs.size()));
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
