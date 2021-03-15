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

package com.blazebit.expression.declarative.impl;

import com.blazebit.apt.service.ServiceProvider;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.declarative.spi.DeclarativeFunctionMetadataProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@ServiceProvider(DeclarativeFunctionMetadataProcessor.class)
public class FunctionInvokerDeclarativeFunctionMetadataProcessor implements DeclarativeFunctionMetadataProcessor<Annotation> {

    @Override
    public Class<Annotation> getProcessingAnnotation() {
        return null;
    }

    @Override
    public MetadataDefinition<?> process(Class<?> annotatedClass, Method method, Annotation annotation, com.blazebit.domain.spi.ServiceProvider serviceProvider) {
        if (isFunctionInvokerMethod(method)) {
            return new MethodFunctionInvoker(method, method.getParameterCount());
        }
        return null;
    }

    static boolean isFunctionInvokerMethod(Method method) {
        return Modifier.isStatic(method.getModifiers()) && !Modifier.isPrivate(method.getModifiers());
    }
}
