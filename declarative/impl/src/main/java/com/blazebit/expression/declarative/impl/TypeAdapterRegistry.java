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

package com.blazebit.expression.declarative.impl;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.declarative.spi.TypeResolver;
import com.blazebit.expression.spi.TypeAdapter;
import com.blazebit.reflection.ReflectionUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class TypeAdapterRegistry implements TypeResolver {

    // Copy from BaseContributor. Keep in sync
    private static final Class<Boolean> BOOLEAN = Boolean.class;
    private static final Class<BigInteger> INTEGER = BigInteger.class;
    private static final Class<BigDecimal> NUMERIC = BigDecimal.class;
    private static final Class<Instant> TIMESTAMP = Instant.class;
    private static final Class<LocalTime> TIME = LocalTime.class;
    private static final Class<String> STRING = String.class;

    private static final Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> TYPE_ADAPTERS;

    static {
        Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> typeAdapters = new HashMap<>();
        typeAdapters.put(boolean.class, new TypeAdapterMetadataDefinition<>(BooleanTypeAdapter.INSTANCE, BOOLEAN));
        typeAdapters.put(char.class, new TypeAdapterMetadataDefinition<>(CharacterTypeAdapter.INSTANCE, STRING));
        typeAdapters.put(Character.class, new TypeAdapterMetadataDefinition<>(CharacterTypeAdapter.INSTANCE, STRING));
        typeAdapters.put(byte.class, new TypeAdapterMetadataDefinition<>(ByteTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(Byte.class, new TypeAdapterMetadataDefinition<>(ByteTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(short.class, new TypeAdapterMetadataDefinition<>(ShortTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(Short.class, new TypeAdapterMetadataDefinition<>(ShortTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(int.class, new TypeAdapterMetadataDefinition<>(IntegerTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(Integer.class, new TypeAdapterMetadataDefinition<>(IntegerTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(long.class, new TypeAdapterMetadataDefinition<>(LongTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(Long.class, new TypeAdapterMetadataDefinition<>(LongTypeAdapter.INSTANCE, INTEGER));
        typeAdapters.put(float.class, new TypeAdapterMetadataDefinition<>(FloatTypeAdapter.INSTANCE, NUMERIC));
        typeAdapters.put(Float.class, new TypeAdapterMetadataDefinition<>(FloatTypeAdapter.INSTANCE, NUMERIC));
        typeAdapters.put(double.class, new TypeAdapterMetadataDefinition<>(DoubleTypeAdapter.INSTANCE, NUMERIC));
        typeAdapters.put(Double.class, new TypeAdapterMetadataDefinition<>(DoubleTypeAdapter.INSTANCE, NUMERIC));
        typeAdapters.put(Calendar.class, new TypeAdapterMetadataDefinition<>(CalendarTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        typeAdapters.put(GregorianCalendar.class, new TypeAdapterMetadataDefinition<>(GregorianCalendarTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        typeAdapters.put(java.sql.Date.class, new TypeAdapterMetadataDefinition<>(JavaSqlDateTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        typeAdapters.put(java.sql.Timestamp.class, new TypeAdapterMetadataDefinition<>(JavaSqlTimestampTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        typeAdapters.put(java.util.Date.class, new TypeAdapterMetadataDefinition<>(JavaUtilDateTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        typeAdapters.put(LocalDateTime.class, new TypeAdapterMetadataDefinition<>(LocalDateTimeTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        typeAdapters.put(ZonedDateTime.class, new TypeAdapterMetadataDefinition<>(ZonedDateTimeTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        typeAdapters.put(java.sql.Time.class, new TypeAdapterMetadataDefinition<>(JavaSqlTimeTimeTypeAdapter.INSTANCE, TIME));
        for (TypeAdapter<?, ?> typeAdapter : ServiceLoader.load(TypeAdapter.class)) {
            TypeVariable<Class<TypeAdapter>>[] typeParameters = TypeAdapter.class.getTypeParameters();
            Class<?> modelClass = ReflectionUtils.resolveTypeVariable(typeAdapter.getClass(), typeParameters[0]);
            Class<?> internalClass = ReflectionUtils.resolveTypeVariable(typeAdapter.getClass(), typeParameters[1]);
            typeAdapters.put(modelClass, new TypeAdapterMetadataDefinition(typeAdapter, internalClass));
        }

        TYPE_ADAPTERS = typeAdapters;
    }

    private final TypeResolver delegate;

    public TypeAdapterRegistry(TypeResolver delegate) {
        this.delegate = delegate;
    }

    public static MetadataDefinition<?> getTypeAdapter(Class<?> clazz) {
        return TYPE_ADAPTERS.get(clazz);
    }

    @Override
    public Object resolve(Class<?> contextClass, Type type) {
        TypeAdapterMetadataDefinition<?, ?> metadataDefinition = TYPE_ADAPTERS.get(type);
        if (metadataDefinition == null) {
            return delegate.resolve(contextClass, type);
        }
        return metadataDefinition.getInternalType();
    }
}
