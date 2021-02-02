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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionServiceFactory;
import com.blazebit.expression.spi.ExpressionSerializerFactory;

import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionServiceFactoryImpl implements ExpressionServiceFactory {

    private final DomainModel domainModel;
    private final LiteralFactory literalFactory;
    private final Map<Class<?>, ExpressionSerializerFactory> expressionSerializers;

    public ExpressionServiceFactoryImpl(DomainModel domainModel, Map<Class<?>, ExpressionSerializerFactory> expressionSerializers) {
        this.domainModel = domainModel;
        this.literalFactory = new LiteralFactory(domainModel);
        this.expressionSerializers = expressionSerializers;
    }

    @Override
    public DomainModel getDomainModel() {
        return domainModel;
    }

    @Override
    public ExpressionCompiler createCompiler() {
        return new ExpressionCompilerImpl(domainModel, literalFactory);
    }

    @Override
    public ExpressionInterpreter createInterpreter() {
        return new ExpressionInterpreterImpl(domainModel);
    }

    @Override
    public <T> ExpressionSerializer<T> createSerializer(Class<T> serializationTarget) {
        if (serializationTarget == StringBuilder.class) {
            return (ExpressionSerializer<T>) new ExpressionSerializerImpl(domainModel, literalFactory);
        }
        return (ExpressionSerializer<T>) expressionSerializers.get(serializationTarget).createSerializer(domainModel);
    }
}
