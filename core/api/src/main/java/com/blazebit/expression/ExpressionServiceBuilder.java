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

import java.util.Map;

/**
 * A builder for an expression service based on a domain model.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionServiceBuilder {

    /**
     * The associated domain model.
     *
     * @return the domain model
     */
    public DomainModel getDomainModel();

    /**
     * Runs the discovered contributors on this expression service builder.
     *
     * @return this for chaining
     */
    public ExpressionServiceBuilder withContributors();

    /**
     * Loads the default settings in this expression service builder.
     *
     * @return this for chaining
     */
    public ExpressionServiceBuilder withDefaults();

    /**
     * Adds the given boolean literal resolver.
     *
     * @param literalResolver The boolean literal resolver to add
     * @return this for chaining
     */
    public ExpressionServiceBuilder withBooleanLiteralResolver(BooleanLiteralResolver literalResolver);

    /**
     * Adds the given numeric literal resolver.
     *
     * @param literalResolver The numeric literal resolver to add
     * @return this for chaining
     */
    public ExpressionServiceBuilder withNumericLiteralResolver(NumericLiteralResolver literalResolver);

    /**
     * Adds the given string literal resolver.
     *
     * @param literalResolver The string literal resolver to add
     * @return this for chaining
     */
    public ExpressionServiceBuilder withStringLiteralResolver(StringLiteralResolver literalResolver);

    /**
     * Adds the given temporal literal resolver.
     *
     * @param literalResolver The temporal literal resolver to add
     * @return this for chaining
     */
    public ExpressionServiceBuilder withTemporalLiteralResolver(TemporalLiteralResolver literalResolver);

    /**
     * Adds the given enum literal resolver.
     *
     * @param literalResolver The enum literal resolver to add
     * @return this for chaining
     */
    public ExpressionServiceBuilder withEnumLiteralResolver(EnumLiteralResolver literalResolver);

    /**
     * Adds the given entity literal resolver.
     *
     * @param literalResolver The entity literal resolver to add
     * @return this for chaining
     */
    public ExpressionServiceBuilder withEntityLiteralResolver(EntityLiteralResolver literalResolver);

    /**
     * Adds the given collection literal resolver.
     *
     * @param literalResolver The collection literal resolver to add
     * @return this for chaining
     */
    public ExpressionServiceBuilder withCollectionLiteralResolver(CollectionLiteralResolver literalResolver);

    /**
     * Returns the boolean literal resolver.
     *
     * @return The boolean literal resolver
     */
    public BooleanLiteralResolver getBooleanLiteralResolver();

    /**
     * Returns the numeric literal resolver.
     *
     * @return the numeric literal resolver
     */
    public NumericLiteralResolver getNumericLiteralResolver();

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
     * Returns the registered converters.
     *
     * @return the registered converters
     */
    public Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> getConverters();

    /**
     * Adds the given expression serializer factory.
     *
     * @param serializer The expression serializer factory serializer
     * @return this for chaining
     */
    public ExpressionServiceBuilder withSerializerFactory(ExpressionSerializerFactory<?> serializer);

    /**
     * Adds the given expression service serializer.
     *
     * @param serializer The expression service serializer serializer
     * @return this for chaining
     */
    public ExpressionServiceBuilder withSerializer(ExpressionServiceSerializer<?> serializer);

    /**
     * Adds the given converter.
     *
     * @param converter The converter
     * @return this for chaining
     */
    public ExpressionServiceBuilder withConverter(TypeConverter<?, ?> converter);

    /**
     * Adds the given converter.
     *
     * @param sourceType The source type
     * @param targetType The target type
     * @param converter The converter
     * @param <X> The source type
     * @param <Y> The target type
     * @return this for chaining
     */
    public <X, Y> ExpressionServiceBuilder withConverter(Class<X> sourceType, Class<Y> targetType, TypeConverter<X, Y> converter);

    /**
     * Builds and validates the expression service factory as defined via this builder.
     *
     * @return The expression service factory
     */
    public ExpressionService build();
}
