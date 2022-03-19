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

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.declarative.spi.TypeResolver;
import com.blazebit.expression.spi.TypeAdapter;
import com.blazebit.reflection.ReflectionUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
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
    private static final Class<BigInteger> EXACT_INTEGER = BigInteger.class;
    private static final Class<BigDecimal> EXACT_NUMERIC = BigDecimal.class;
    private static final Class<Long> APPROXIMATE_INTEGER = Long.class;
    private static final Class<Double> APPROXIMATE_NUMERIC = Double.class;
    private static final Class<Instant> TIMESTAMP = Instant.class;
    private static final Class<LocalTime> TIME = LocalTime.class;
    private static final Class<String> STRING = String.class;
    private static final Class<LocalDate> DATE = LocalDate.class;

    private static final Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> EXACT_TYPE_ADAPTERS;
    private static final Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> APPROXIMATE_TYPE_ADAPTERS;

    static {
        Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> exactTypeAdapters = new HashMap<>();
        Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> approximateTypeAdapters = new HashMap<>();
        put(exactTypeAdapters, approximateTypeAdapters, boolean.class, new TypeAdapterMetadataDefinition<>(BooleanTypeAdapter.INSTANCE, BOOLEAN));
        put(exactTypeAdapters, approximateTypeAdapters, char.class, new TypeAdapterMetadataDefinition<>(CharacterTypeAdapter.INSTANCE, STRING));
        put(exactTypeAdapters, approximateTypeAdapters, Character.class, new TypeAdapterMetadataDefinition<>(CharacterTypeAdapter.INSTANCE, STRING));
        put(exactTypeAdapters, approximateTypeAdapters, Calendar.class, new TypeAdapterMetadataDefinition<>(CalendarTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        put(exactTypeAdapters, approximateTypeAdapters, GregorianCalendar.class, new TypeAdapterMetadataDefinition<>(GregorianCalendarTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        put(exactTypeAdapters, approximateTypeAdapters, java.sql.Date.class, new TypeAdapterMetadataDefinition<>(JavaSqlDateDateTypeAdapter.INSTANCE, DATE));
        put(exactTypeAdapters, approximateTypeAdapters, java.sql.Timestamp.class, new TypeAdapterMetadataDefinition<>(JavaSqlTimestampTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        put(exactTypeAdapters, approximateTypeAdapters, java.util.Date.class, new TypeAdapterMetadataDefinition<>(JavaUtilDateTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        put(exactTypeAdapters, approximateTypeAdapters, LocalDateTime.class, new TypeAdapterMetadataDefinition<>(LocalDateTimeTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        put(exactTypeAdapters, approximateTypeAdapters, ZonedDateTime.class, new TypeAdapterMetadataDefinition<>(ZonedDateTimeTimestampTypeAdapter.INSTANCE, TIMESTAMP));
        put(exactTypeAdapters, approximateTypeAdapters, java.sql.Time.class, new TypeAdapterMetadataDefinition<>(JavaSqlTimeTimeTypeAdapter.INSTANCE, TIME));

        exactTypeAdapters.put(byte.class, new TypeAdapterMetadataDefinition<>(ExactByteTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(Byte.class, new TypeAdapterMetadataDefinition<>(ExactByteTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(short.class, new TypeAdapterMetadataDefinition<>(ExactShortTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(Short.class, new TypeAdapterMetadataDefinition<>(ExactShortTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(int.class, new TypeAdapterMetadataDefinition<>(ExactIntegerTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(Integer.class, new TypeAdapterMetadataDefinition<>(ExactIntegerTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(long.class, new TypeAdapterMetadataDefinition<>(ExactLongTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(Long.class, new TypeAdapterMetadataDefinition<>(ExactLongTypeAdapter.INSTANCE, EXACT_INTEGER));
        exactTypeAdapters.put(float.class, new TypeAdapterMetadataDefinition<>(ExactFloatTypeAdapter.INSTANCE, EXACT_NUMERIC));
        exactTypeAdapters.put(Float.class, new TypeAdapterMetadataDefinition<>(ExactFloatTypeAdapter.INSTANCE, EXACT_NUMERIC));
        exactTypeAdapters.put(double.class, new TypeAdapterMetadataDefinition<>(ExactDoubleTypeAdapter.INSTANCE, EXACT_NUMERIC));
        exactTypeAdapters.put(Double.class, new TypeAdapterMetadataDefinition<>(ExactDoubleTypeAdapter.INSTANCE, EXACT_NUMERIC));

        approximateTypeAdapters.put(byte.class, new TypeAdapterMetadataDefinition<>(ApproximateByteTypeAdapter.INSTANCE, APPROXIMATE_INTEGER));
        approximateTypeAdapters.put(Byte.class, new TypeAdapterMetadataDefinition<>(ApproximateByteTypeAdapter.INSTANCE, APPROXIMATE_INTEGER));
        approximateTypeAdapters.put(short.class, new TypeAdapterMetadataDefinition<>(ApproximateShortTypeAdapter.INSTANCE, APPROXIMATE_INTEGER));
        approximateTypeAdapters.put(Short.class, new TypeAdapterMetadataDefinition<>(ApproximateShortTypeAdapter.INSTANCE, APPROXIMATE_INTEGER));
        approximateTypeAdapters.put(int.class, new TypeAdapterMetadataDefinition<>(ApproximateIntegerTypeAdapter.INSTANCE, APPROXIMATE_INTEGER));
        approximateTypeAdapters.put(Integer.class, new TypeAdapterMetadataDefinition<>(ApproximateIntegerTypeAdapter.INSTANCE, APPROXIMATE_INTEGER));
        approximateTypeAdapters.put(float.class, new TypeAdapterMetadataDefinition<>(ApproximateFloatTypeAdapter.INSTANCE, APPROXIMATE_NUMERIC));
        approximateTypeAdapters.put(Float.class, new TypeAdapterMetadataDefinition<>(ApproximateFloatTypeAdapter.INSTANCE, APPROXIMATE_NUMERIC));
        approximateTypeAdapters.put(BigDecimal.class, new TypeAdapterMetadataDefinition<>(ApproximateBigDecimalTypeAdapter.INSTANCE, APPROXIMATE_NUMERIC));
        approximateTypeAdapters.put(BigInteger.class, new TypeAdapterMetadataDefinition<>(ApproximateBigIntegerTypeAdapter.INSTANCE, APPROXIMATE_INTEGER));
        for (TypeAdapter<?, ?> typeAdapter : ServiceLoader.load(TypeAdapter.class)) {
            TypeVariable<Class<TypeAdapter>>[] typeParameters = TypeAdapter.class.getTypeParameters();
            Class<?> modelClass = ReflectionUtils.resolveTypeVariable(typeAdapter.getClass(), typeParameters[0]);
            Class<?> internalClass = ReflectionUtils.resolveTypeVariable(typeAdapter.getClass(), typeParameters[1]);
            if (internalClass == BigDecimal.class || internalClass == BigInteger.class) {
                exactTypeAdapters.put(modelClass, new TypeAdapterMetadataDefinition(typeAdapter, internalClass));
            } else if (internalClass == Double.class || internalClass == Long.class) {
                approximateTypeAdapters.put(modelClass, new TypeAdapterMetadataDefinition(typeAdapter, internalClass));
            } else {
                exactTypeAdapters.put(modelClass, new TypeAdapterMetadataDefinition(typeAdapter, internalClass));
                approximateTypeAdapters.put(modelClass, new TypeAdapterMetadataDefinition(typeAdapter, internalClass));
            }
        }

        EXACT_TYPE_ADAPTERS = exactTypeAdapters;
        APPROXIMATE_TYPE_ADAPTERS = approximateTypeAdapters;
    }

    private final TypeResolver delegate;

    public TypeAdapterRegistry(TypeResolver delegate) {
        this.delegate = delegate;
    }

    private static void put(Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> exactTypeAdapters, Map<Class<?>, TypeAdapterMetadataDefinition<?, ?>> approximateTypeAdapters, Class<?> modelClass, TypeAdapterMetadataDefinition<?, ?> typeAdapterMetadataDefinition) {
        exactTypeAdapters.put(modelClass, typeAdapterMetadataDefinition);
        approximateTypeAdapters.put(modelClass, typeAdapterMetadataDefinition);
    }

    public static MetadataDefinition<?> getTypeAdapter(Class<?> clazz) {
        return EXACT_TYPE_ADAPTERS.get(clazz);
    }

    @Override
    public Object resolve(Class<?> contextClass, Type type, DomainBuilder domainBuilder) {
        boolean exact = domainBuilder.getType("Numeric").getJavaType() == BigDecimal.class;
        TypeAdapterMetadataDefinition<?, ?> metadataDefinition;
        if (exact) {
            metadataDefinition = EXACT_TYPE_ADAPTERS.get(type);
        } else {
            metadataDefinition = APPROXIMATE_TYPE_ADAPTERS.get(type);
        }
        if (metadataDefinition == null) {
            return delegate.resolve(contextClass, type, domainBuilder);
        }
        return metadataDefinition.getInternalType();
    }
}
