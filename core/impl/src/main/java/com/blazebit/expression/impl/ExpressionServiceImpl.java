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
import com.blazebit.expression.ExpressionServiceBuilder;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.spi.BooleanLiteralResolver;
import com.blazebit.expression.spi.CollectionLiteralResolver;
import com.blazebit.expression.spi.EntityLiteralResolver;
import com.blazebit.expression.spi.EnumLiteralResolver;
import com.blazebit.expression.spi.ExpressionSerializerFactory;
import com.blazebit.expression.spi.ExpressionServiceSerializer;
import com.blazebit.expression.spi.NumericLiteralResolver;
import com.blazebit.expression.spi.StringLiteralResolver;
import com.blazebit.expression.spi.TemporalLiteralResolver;

import java.util.List;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionServiceImpl implements ExpressionService {

    private final DomainModel domainModel;
    private final NumericLiteralResolver numericLiteralResolver;
    private final BooleanLiteralResolver booleanLiteralResolver;
    private final StringLiteralResolver stringLiteralResolver;
    private final TemporalLiteralResolver temporalLiteralResolver;
    private final EnumLiteralResolver enumLiteralResolver;
    private final EntityLiteralResolver entityLiteralResolver;
    private final CollectionLiteralResolver collectionLiteralResolver;
    private final LiteralFactory literalFactory;
    private final Map<Class<?>, ExpressionSerializerFactory<?>> expressionSerializers;
    private final List<ExpressionServiceSerializer<?>> expressionServiceSerializers;

    public ExpressionServiceImpl(ExpressionServiceBuilder builder, Map<Class<?>, ExpressionSerializerFactory<?>> expressionSerializers, List<ExpressionServiceSerializer<?>> expressionServiceSerializers) {
        this.domainModel = builder.getDomainModel();
        this.numericLiteralResolver = builder.getNumericLiteralResolver();
        this.booleanLiteralResolver = builder.getBooleanLiteralResolver();
        this.stringLiteralResolver = builder.getStringLiteralResolver();
        this.temporalLiteralResolver = builder.getTemporalLiteralResolver();
        this.enumLiteralResolver = builder.getEnumLiteralResolver();
        this.entityLiteralResolver = builder.getEntityLiteralResolver();
        this.collectionLiteralResolver = builder.getCollectionLiteralResolver();
        this.expressionSerializers = expressionSerializers;
        this.expressionServiceSerializers = expressionServiceSerializers;
        this.literalFactory = new LiteralFactory(this);
    }

    private ExpressionServiceImpl(ExpressionService parent, DomainModel domainModel) {
        this.domainModel = domainModel;
        this.numericLiteralResolver = parent.getNumericLiteralResolver();
        this.booleanLiteralResolver = parent.getBooleanLiteralResolver();
        this.stringLiteralResolver = parent.getStringLiteralResolver();
        this.temporalLiteralResolver = parent.getTemporalLiteralResolver();
        this.enumLiteralResolver = parent.getEnumLiteralResolver();
        this.entityLiteralResolver = parent.getEntityLiteralResolver();
        this.collectionLiteralResolver = parent.getCollectionLiteralResolver();
        this.expressionSerializers = parent.getExpressionSerializerFactories();
        this.expressionServiceSerializers = parent.getExpressionServiceSerializers();
        this.literalFactory = new LiteralFactory(this);
    }

    @Override
    public DomainModel getDomainModel() {
        return domainModel;
    }

    @Override
    public ExpressionService withSubDomainModel(DomainModel subDomainModel) {
        DomainModel baseModel = subDomainModel;
        while (baseModel != null) {
            if (domainModel == baseModel) {
                break;
            }
            baseModel = baseModel.getParentDomainModel();
        }
        if (domainModel != baseModel) {
            throw new IllegalArgumentException("The given domain model is not an extension to the domain model of the parent expression service!");
        }
        return new ExpressionServiceImpl(this, subDomainModel);
    }

    @Override
    public Map<Class<?>, ExpressionSerializerFactory<?>> getExpressionSerializerFactories() {
        return expressionSerializers;
    }

    @Override
    public List<ExpressionServiceSerializer<?>> getExpressionServiceSerializers() {
        return expressionServiceSerializers;
    }

    @Override
    public NumericLiteralResolver getNumericLiteralResolver() {
        return numericLiteralResolver;
    }

    @Override
    public BooleanLiteralResolver getBooleanLiteralResolver() {
        return booleanLiteralResolver;
    }

    @Override
    public StringLiteralResolver getStringLiteralResolver() {
        return stringLiteralResolver;
    }

    @Override
    public TemporalLiteralResolver getTemporalLiteralResolver() {
        return temporalLiteralResolver;
    }

    @Override
    public EnumLiteralResolver getEnumLiteralResolver() {
        return enumLiteralResolver;
    }

    @Override
    public EntityLiteralResolver getEntityLiteralResolver() {
        return entityLiteralResolver;
    }

    @Override
    public CollectionLiteralResolver getCollectionLiteralResolver() {
        return collectionLiteralResolver;
    }

    @Override
    public ExpressionCompiler createCompiler() {
        return new ExpressionCompilerImpl(this, literalFactory);
    }

    @Override
    public ExpressionInterpreter createInterpreter() {
        return new ExpressionInterpreterImpl(this);
    }

    @Override
    public ExpressionSerializer<StringBuilder> createSerializer() {
        return new ExpressionSerializerImpl(this, literalFactory, false);
    }

    @Override
    public ExpressionSerializer<StringBuilder> createTemplateSerializer() {
        return new ExpressionSerializerImpl(this, literalFactory, true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ExpressionSerializer<T> createSerializer(Class<T> serializationTarget) {
        ExpressionSerializerFactory<?> serializerFactory = expressionSerializers.get(serializationTarget);
        if (serializerFactory == null) {
            if (serializationTarget == StringBuilder.class) {
                return (ExpressionSerializer<T>) new ExpressionSerializerImpl(this, literalFactory, false);
            }
            return null;
        }
        return (ExpressionSerializer<T>) serializerFactory.createSerializer(this);
    }

    @Override
    public <T> T serialize(ExpressionService baseModel, Class<T> targetType, String format, Map<String, Object> properties) {
        for (ExpressionServiceSerializer<?> expressionServiceSerializer : expressionServiceSerializers) {
            if (expressionServiceSerializer.canSerialize(this)) {
                T result = ((ExpressionServiceSerializer<ExpressionService>) expressionServiceSerializer).serialize(this, baseModel, this, targetType, format, properties);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }
}
