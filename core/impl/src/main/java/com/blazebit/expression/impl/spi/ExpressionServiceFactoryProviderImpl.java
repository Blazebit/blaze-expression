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

package com.blazebit.expression.impl.spi;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.impl.ExpressionServiceFactoryImpl;
import com.blazebit.expression.ExpressionServiceFactory;
import com.blazebit.expression.spi.ExpressionSerializerFactory;
import com.blazebit.expression.spi.ExpressionServiceFactoryProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionServiceFactoryProviderImpl implements ExpressionServiceFactoryProvider {

    private final Map<Class<?>, ExpressionSerializerFactory> expressionSerializers = new HashMap<>();

    public ExpressionServiceFactoryProviderImpl() {
        loadDefaults();
    }

    private void loadDefaults() {
        for (ExpressionSerializerFactory expressionSerializerFactory : ServiceLoader.load(ExpressionSerializerFactory.class)) {
            expressionSerializers.put(expressionSerializerFactory.getSerializationTargetType(), expressionSerializerFactory);
        }
    }

    @Override
    public ExpressionServiceFactory create(DomainModel model) {
        return new ExpressionServiceFactoryImpl(model, expressionSerializers);
    }

}
