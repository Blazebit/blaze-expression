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

package com.blazebit.expression.declarative.impl;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MethodFunctionInvoker implements MetadataDefinition<FunctionInvoker>, FunctionInvoker, Serializable {

    private static final Field FUNCTION;
    private static final Field VAR_ARG_COMPONENT_TYPE;

    static {
        try {
            Field field = MethodFunctionInvoker.class.getDeclaredField("function");
            field.setAccessible(true);
            FUNCTION = field;
            field = MethodFunctionInvoker.class.getDeclaredField("varArgComponentType");
            field.setAccessible(true);
            VAR_ARG_COMPONENT_TYPE = field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final transient Method function;
    private final boolean usesInterpreterContext;
    private final transient Class<?> varArgComponentType;
    private final int parameterCount;

    public MethodFunctionInvoker(Method function, int parameterCount) {
        function.setAccessible(true);
        this.function = function;
        this.usesInterpreterContext = function.getParameterCount() > 0 && function.getParameterTypes()[0] == ExpressionInterpreter.Context.class;
        this.varArgComponentType = function.isVarArgs() ? function.getParameterTypes()[function.getParameterCount() - 1].getComponentType() : null;
        this.parameterCount = parameterCount;
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
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
                args[i + offset] = arguments.getValue(i);
            }
            if (varArgComponentType == null) {
                args[i + offset] = arguments.getValue(i);
            } else {
                Collection<Object> varArgs = (Collection<Object>) arguments.getValue(i);
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
    public FunctionInvoker build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeUTF(function.getDeclaringClass().getName());
        out.writeUTF(function.getName());
        if (varArgComponentType == null) {
            out.writeUTF(null);
        } else {
            out.writeUTF(varArgComponentType.getName());
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        String className = in.readUTF();
        String methodName = in.readUTF();
        String varArgComponentType = in.readUTF();
        try {
            Method method = Class.forName(className).getDeclaredMethod(methodName);
            method.setAccessible(true);
            FUNCTION.set(this, method);
            VAR_ARG_COMPONENT_TYPE.set(this, Class.forName(varArgComponentType));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
