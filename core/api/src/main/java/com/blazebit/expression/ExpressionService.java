/*
 * Copyright 2019 - 2022 Blazebit.
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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.spi.BooleanLiteralResolver;
import com.blazebit.expression.spi.CollectionLiteralResolver;
import com.blazebit.expression.spi.EntityLiteralResolver;
import com.blazebit.expression.spi.EnumLiteralResolver;
import com.blazebit.expression.spi.ExpressionSerializerFactory;
import com.blazebit.expression.spi.ExpressionServiceSerializer;
import com.blazebit.expression.spi.NumericLiteralResolver;
import com.blazebit.expression.spi.StringLiteralResolver;
import com.blazebit.expression.spi.TemporalLiteralResolver;
import com.blazebit.expression.spi.TypeConverter;

import java.util.List;
import java.util.Map;

/**
 * A factory for expression related functionality based on a domain model.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionService {

    /**
     * The domain model that is used for the expression services.
     *
     * @return the domain model
     */
    public DomainModel getDomainModel();

    /**
     * Creates a new expression service for the given sub domain model.
     *
     * @param subDomainModel the sub domain model
     * @return A new expression service with the given sub domain model
     */
    default ExpressionService withSubDomainModel(DomainModel subDomainModel) {
        return Expressions.getDefaultProvider().createBuilder(this, subDomainModel).build();
    }

    /**
     * The expression serializer factories.
     *
     * @return the expression serializer factories
     */
    Map<Class<?>, Map<String, ExpressionSerializerFactory<?>>> getExpressionSerializerFactories();

    /**
     * The expression service serializers.
     *
     * @return the expression service serializers
     */
    List<ExpressionServiceSerializer<?>> getExpressionServiceSerializers();

    /**
     * Creates and returns an expression compiler to parse and type check expression strings.
     *
     * @return the expression compiler
     */
    public ExpressionCompiler createCompiler();

    /**
     * Creates and returns an expression interpreter to interpret a compiled expression.
     *
     * @return the expression interpreter
     */
    public ExpressionInterpreter createInterpreter();

    /**
     * Creates and returns an expression serializer that produces an expression, which can be compiled again.
     *
     * @return the expression serializer
     */
    public ExpressionSerializer<StringBuilder> createSerializer();

    /**
     * Creates and returns an expression serializer that produces a template expression, which can be compiled again.
     *
     * @return the expression serializer
     */
    public ExpressionSerializer<StringBuilder> createTemplateSerializer();

    /**
     * Creates and returns an expression serializer to serialize a compiled expression.
     *
     * @param serializationTarget The serialization target type
     * @param <T> The serialization target type
     * @return the expression serializer
     */
    public <T> ExpressionSerializer<T> createSerializer(Class<T> serializationTarget);

    /**
     * Creates and returns an expression serializer to serialize a compiled expression.
     *
     * @param serializationTarget The serialization target type
     * @param serializationFormat The serialization format
     * @param <T> The serialization target type
     * @return the expression serializer
     */
    public <T> ExpressionSerializer<T> createSerializer(Class<T> serializationTarget, String serializationFormat);

    /**
     * Returns the numeric literal resolver.
     *
     * @return the numeric literal resolver
     */
    public NumericLiteralResolver getNumericLiteralResolver();

    /**
     * Returns the boolean literal resolver.
     *
     * @return the boolean literal resolver
     */
    public BooleanLiteralResolver getBooleanLiteralResolver();

    /**
     * Returns the string literal resolver.
     *
     * @return the string literal resolver
     */
    public StringLiteralResolver getStringLiteralResolver();

    /**
     * Returns the temporal literal resolver.
     *
     * @return the temporal literal resolver
     */
    public TemporalLiteralResolver getTemporalLiteralResolver();

    /**
     * Returns the enum literal resolver.
     *
     * @return the enum literal resolver
     */
    public EnumLiteralResolver getEnumLiteralResolver();

    /**
     * Returns the entity literal resolver.
     *
     * @return the entity literal resolver
     */
    public EntityLiteralResolver getEntityLiteralResolver();

    /**
     * Returns the collection literal resolver.
     *
     * @return the collection literal resolver
     */
    public CollectionLiteralResolver getCollectionLiteralResolver();

    /**
     * Returns the registered converters, mapped from target type to a map of source type to converter mapping.
     *
     * @return the registered converters
     */
    public Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> getConverters();

    /**
     * Serializes the expression service to the given target type with the given format.
     *
     * @param targetType The target type
     * @param format The serialization format
     * @param properties Serialization properties
     * @param <T> The target type
     * @return The serialized form
     */
    default <T> T serialize(Class<T> targetType, String format, Map<String, Object> properties) {
        return serialize(null, targetType, format, properties);
    }

    /**
     * Serializes the expression service to the given target type with the given format.
     * It only serializes elements that do not belong to the given base model already or are overridden.
     *
     * @param baseModel The base expression service
     * @param targetType The target type
     * @param format The serialization format
     * @param properties Serialization properties
     * @param <T> The target type
     * @return The serialized form
     */
    public <T> T serialize(ExpressionService baseModel, Class<T> targetType, String format, Map<String, Object> properties);

    /**
     * Serializes the given compiled expression to a string.
     *
     * @param expression The expression to serialize
     * @return the string serialized form of the expression
     */
    public String serialize(Expression expression);

    /**
     * Serializes the given compiled expression to a string.
     *
     * @param expression The expression to serialize
     * @param serializationFormat The serialization format
     * @return the string serialized form of the expression
     */
    public String serialize(Expression expression, String serializationFormat);
}
