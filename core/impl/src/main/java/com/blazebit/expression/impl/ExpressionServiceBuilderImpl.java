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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.ExpressionServiceBuilder;
import com.blazebit.expression.impl.spi.ExpressionServiceBuilderProviderImpl;
import com.blazebit.expression.spi.BooleanLiteralResolver;
import com.blazebit.expression.spi.CollectionLiteralResolver;
import com.blazebit.expression.spi.EntityLiteralResolver;
import com.blazebit.expression.spi.EnumLiteralResolver;
import com.blazebit.expression.spi.ExpressionSerializerFactory;
import com.blazebit.expression.spi.ExpressionServiceContributor;
import com.blazebit.expression.spi.ExpressionServiceSerializer;
import com.blazebit.expression.spi.NumericLiteralResolver;
import com.blazebit.expression.spi.StringLiteralResolver;
import com.blazebit.expression.spi.TemporalLiteralResolver;
import com.blazebit.expression.spi.TypeConverter;
import com.blazebit.reflection.ReflectionUtils;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionServiceBuilderImpl implements ExpressionServiceBuilder {

    private static final Logger LOG = Logger.getLogger(ExpressionServiceBuilderProviderImpl.class.getName());
    private static final ReferenceQueue<ClassLoader> REFERENCE_QUEUE = new ReferenceQueue<>();
    private static final ConcurrentMap<WeakClassLoaderKey, Providers> PROVIDERS = new ConcurrentHashMap<>();

    private final ExpressionService baseExpressionService;
    private final DomainModel domainModel;
    private Map<Class<?>, Map<String, ExpressionSerializerFactory<?>>> expressionSerializers;
    private Set<ExpressionServiceSerializer<?>> expressionServiceSerializers;
    private NumericLiteralResolver numericLiteralResolver;
    private BooleanLiteralResolver booleanLiteralResolver;
    private StringLiteralResolver stringLiteralResolver;
    private TemporalLiteralResolver temporalLiteralResolver;
    private EnumLiteralResolver enumLiteralResolver;
    private EntityLiteralResolver entityLiteralResolver;
    private CollectionLiteralResolver collectionLiteralResolver;
    private Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> converters;

    public ExpressionServiceBuilderImpl(DomainModel domainModel) {
        this.baseExpressionService = null;
        this.domainModel = domainModel;
    }

    public ExpressionServiceBuilderImpl(ExpressionService baseExpressionService, DomainModel domainModel) {
        this.baseExpressionService = baseExpressionService;
        DomainModel baseModel = baseExpressionService.getDomainModel();
        while (baseModel != null) {
            if (domainModel == baseModel) {
                break;
            }
            baseModel = baseModel.getParentDomainModel();
        }
        if (domainModel != baseModel) {
            throw new IllegalArgumentException("The given domain model is not an extension to the domain model of the parent expression service!");
        }
        this.domainModel = domainModel;
        this.numericLiteralResolver = baseExpressionService.getNumericLiteralResolver();
        this.booleanLiteralResolver = baseExpressionService.getBooleanLiteralResolver();
        this.stringLiteralResolver = baseExpressionService.getStringLiteralResolver();
        this.temporalLiteralResolver = baseExpressionService.getTemporalLiteralResolver();
        this.enumLiteralResolver = baseExpressionService.getEnumLiteralResolver();
        this.entityLiteralResolver = baseExpressionService.getEntityLiteralResolver();
        this.collectionLiteralResolver = baseExpressionService.getCollectionLiteralResolver();
    }

    @Override
    public DomainModel getDomainModel() {
        return domainModel;
    }

    @Override
    public ExpressionServiceBuilder withBooleanLiteralResolver(BooleanLiteralResolver literalResolver) {
        this.booleanLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public ExpressionServiceBuilder withNumericLiteralResolver(NumericLiteralResolver literalResolver) {
        this.numericLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public ExpressionServiceBuilder withStringLiteralResolver(StringLiteralResolver literalResolver) {
        this.stringLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public ExpressionServiceBuilder withTemporalLiteralResolver(TemporalLiteralResolver literalResolver) {
        this.temporalLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public ExpressionServiceBuilder withEnumLiteralResolver(EnumLiteralResolver literalResolver) {
        this.enumLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public ExpressionServiceBuilder withEntityLiteralResolver(EntityLiteralResolver literalResolver) {
        this.entityLiteralResolver = literalResolver;
        return this;
    }

    @Override
    public ExpressionServiceBuilder withCollectionLiteralResolver(CollectionLiteralResolver literalResolver) {
        this.collectionLiteralResolver = literalResolver;
        return this;
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
    public Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> getConverters() {
        return converters;
    }

    public ExpressionServiceBuilder withContributors() {
        Providers providers = getProviders();
        for (ExpressionServiceContributor expressionServiceContributor : providers.expressionServiceContributors) {
            expressionServiceContributor.contribute(this);
        }
        return this;
    }

    @Override
    public ExpressionServiceBuilder withDefaults() {
        Providers providers = getProviders();
        for (ExpressionServiceContributor expressionServiceContributor : providers.expressionServiceContributors) {
            expressionServiceContributor.contribute(this);
        }
        for (ExpressionServiceSerializer<?> expressionServiceSerializer : providers.expressionServiceSerializers) {
            withSerializer(expressionServiceSerializer);
        }
        if (!providers.expressionSerializerFactories.isEmpty()) {
            if (expressionSerializers == null) {
                if (baseExpressionService == null) {
                    expressionSerializers = new HashMap<>();
                } else {
                    expressionSerializers = new HashMap<>(baseExpressionService.getExpressionSerializerFactories());
                }
            }
            expressionSerializers.putAll(providers.expressionSerializerFactories);
        }
        for (TypeConverter<?, ?> typeConverter : providers.typeConverters) {
            withConverter(typeConverter);
        }
        return this;
    }

    @Override
    public ExpressionServiceBuilder withSerializer(ExpressionServiceSerializer<?> serializer) {
        if (expressionServiceSerializers == null) {
            if (baseExpressionService == null) {
                expressionServiceSerializers = new LinkedHashSet<>();
            } else {
                expressionServiceSerializers = new LinkedHashSet<>(baseExpressionService.getExpressionServiceSerializers());
            }
        }
        expressionServiceSerializers.add(serializer);
        return this;
    }

    @Override
    public ExpressionServiceBuilder withSerializerFactory(ExpressionSerializerFactory<?> serializer) {
        if (expressionSerializers == null) {
            if (baseExpressionService == null) {
                expressionSerializers = new HashMap<>();
            } else {
                expressionSerializers = new HashMap<>(baseExpressionService.getExpressionSerializerFactories());
            }
        }
        expressionSerializers.computeIfAbsent(serializer.getSerializationTargetType(), k -> new HashMap<>())
            .put(serializer.getSerializationFormat(), serializer);
        return this;
    }

    @Override
    public ExpressionServiceBuilder withConverter(TypeConverter<?, ?> converter) {
        TypeVariable<Class<TypeConverter>>[] typeParameters = TypeConverter.class.getTypeParameters();
        //noinspection unchecked
        return withConverter(
            ReflectionUtils.resolveTypeVariable(converter.getClass(), typeParameters[0]),
            ReflectionUtils.resolveTypeVariable(converter.getClass(), typeParameters[1]),
            (TypeConverter) converter
        );
    }

    @Override
    public <X, Y> ExpressionServiceBuilder withConverter(Class<X> sourceType, Class<Y> targetType, TypeConverter<X, Y> converter) {
        if (converters == null) {
            converters = new HashMap<>();
        }
        converters.computeIfAbsent(targetType, k -> new HashMap<>())
            .put(sourceType, converter);
        return this;
    }

    @Override
    public ExpressionService build() {
        return new ExpressionServiceImpl(
            this,
            getImmutableSerializerFactories(),
            getImmutableExpressionServiceSerializers(),
            getImmutableTypeConverters()
        );
    }

    private Map<Class<?>, Map<String, ExpressionSerializerFactory<?>>> getImmutableSerializerFactories() {
        if (expressionSerializers == null) {
            if (baseExpressionService == null) {
                return Collections.emptyMap();
            }
            return baseExpressionService.getExpressionSerializerFactories();
        }
        Map<Class<?>, Map<String, ExpressionSerializerFactory<?>>> serializerFactories = new HashMap<>(expressionSerializers.size());
        for (Map.Entry<Class<?>, Map<String, ExpressionSerializerFactory<?>>> entry : expressionSerializers.entrySet()) {
            serializerFactories.put(entry.getKey(), Collections.unmodifiableMap(new HashMap<>(entry.getValue())));
        }
        return Collections.unmodifiableMap(serializerFactories);
    }

    private List<ExpressionServiceSerializer<?>> getImmutableExpressionServiceSerializers() {
        if (expressionServiceSerializers == null) {
            if (baseExpressionService == null) {
                return Collections.emptyList();
            }
            return baseExpressionService.getExpressionServiceSerializers();
        }
        return Collections.unmodifiableList(new ArrayList<>(expressionServiceSerializers));
    }

    private Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> getImmutableTypeConverters() {
        if (converters == null) {
            if (baseExpressionService == null) {
                return Collections.emptyMap();
            }
            return baseExpressionService.getConverters();
        }
        Map<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> converters = new HashMap<>(this.converters.size());
        for (Map.Entry<Class<?>, Map<Class<?>, TypeConverter<?, ?>>> entry : this.converters.entrySet()) {
            converters.put(entry.getKey(), Collections.unmodifiableMap(new HashMap<>(entry.getValue())));
        }
        return Collections.unmodifiableMap(converters);
    }

    private static Providers getProviders() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ExpressionServiceBuilderImpl.class.getClassLoader();
        }
        // Cleanup old references
        Reference<? extends ClassLoader> reference;
        while ((reference = REFERENCE_QUEUE.poll()) != null) {
            PROVIDERS.remove(reference);
        }
        WeakClassLoaderKey key = new WeakClassLoaderKey(classLoader, REFERENCE_QUEUE);
        Providers providers = PROVIDERS.get(key);
        if (providers == null) {
            PROVIDERS.put(key, providers = new Providers());
        }
        return providers;
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class WeakClassLoaderKey extends WeakReference<ClassLoader> {

        private final int hash;

        public WeakClassLoaderKey(ClassLoader referent, ReferenceQueue<ClassLoader> referenceQueue) {
            super(referent, referenceQueue);
            this.hash = referent.hashCode();
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || obj instanceof WeakClassLoaderKey && ((WeakClassLoaderKey) obj).get() == get();
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class Providers {
        private final Iterable<ExpressionServiceContributor> expressionServiceContributors;
        private final Map<Class<?>, Map<String, ExpressionSerializerFactory<?>>> expressionSerializerFactories;
        private final Iterable<ExpressionServiceSerializer<?>> expressionServiceSerializers;
        private final Iterable<TypeConverter<?, ?>> typeConverters;

        public Providers() {
            this.expressionServiceContributors = StreamSupport.stream(ServiceLoader.load(ExpressionServiceContributor.class).spliterator(), false)
                .sorted(Comparator.comparing(ExpressionServiceContributor::priority))
                .collect(Collectors.toList());
            Map<Class<?>, Map<String, ExpressionSerializerFactory<?>>> expressionSerializers = new HashMap<>();
            @SuppressWarnings("rawtypes")
            Iterator<ExpressionSerializerFactory> iterator = ServiceLoader.load(ExpressionSerializerFactory.class).iterator();
            while (iterator.hasNext()) {
                String name = null;
                try {
                    ExpressionSerializerFactory<?> expressionSerializerFactory = iterator.next();
                    name = expressionSerializerFactory.getClass().getName();
                    expressionSerializers.computeIfAbsent(expressionSerializerFactory.getSerializationTargetType(), k -> new HashMap<>())
                        .put(expressionSerializerFactory.getSerializationFormat(), expressionSerializerFactory);
                } catch (Throwable ex) {
                    if (name == null) {
                        LOG.log(Level.WARNING, "Ignoring expression serializer due to exception", ex);
                    } else {
                        LOG.log(Level.WARNING, "Ignoring expression serializer " + name + " due to exception", ex);
                    }
                }
            }
            this.expressionSerializerFactories = expressionSerializers;
            this.expressionServiceSerializers = load(ExpressionServiceSerializer.class);
            this.typeConverters = load(TypeConverter.class);
        }

        @SuppressWarnings("unchecked")
        private static <T> Iterable<T> load(Class<? super T> clazz) {
            return (Iterable<T>) StreamSupport.stream(ServiceLoader.load(clazz).spliterator(), false).collect(Collectors.toList());
        }
    }
}
