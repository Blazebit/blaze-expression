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

package com.blazebit.expression.impl.spi;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionServiceFactory;
import com.blazebit.expression.impl.ExpressionServiceFactoryImpl;
import com.blazebit.expression.spi.ExpressionSerializerFactory;
import com.blazebit.expression.spi.ExpressionServiceFactoryProvider;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionServiceFactoryProviderImpl implements ExpressionServiceFactoryProvider {

    private static final Logger LOG = Logger.getLogger(ExpressionServiceFactoryProviderImpl.class.getName());

    private static final ReferenceQueue<ClassLoader> REFERENCE_QUEUE = new ReferenceQueue<>();
    private static final ConcurrentMap<WeakClassLoaderKey, Providers> PROVIDERS = new ConcurrentHashMap<>();
    private final Map<Class<?>, ExpressionSerializerFactory> expressionSerializers = new HashMap<>();

    public ExpressionServiceFactoryProviderImpl() {
        loadDefaults();
    }

    private void loadDefaults() {
        Providers providers = getProviders();
        this.expressionSerializers.putAll(providers.expressionSerializers);
    }

    @Override
    public ExpressionServiceFactory create(DomainModel model) {
        return new ExpressionServiceFactoryImpl(model, expressionSerializers);
    }

    private static Providers getProviders() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ExpressionServiceFactoryProviderImpl.class.getClassLoader();
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
        private final Map<Class<?>, ExpressionSerializerFactory> expressionSerializers;

        public Providers() {
            Map<Class<?>, ExpressionSerializerFactory> expressionSerializers = new HashMap<>();
            Iterator<ExpressionSerializerFactory> iterator = ServiceLoader.load(ExpressionSerializerFactory.class).iterator();
            while (iterator.hasNext()) {
                String name = null;
                try {
                    ExpressionSerializerFactory expressionSerializerFactory = iterator.next();
                    name = expressionSerializerFactory.getClass().getName();
                    expressionSerializers.put(expressionSerializerFactory.getSerializationTargetType(), expressionSerializerFactory);
                } catch (Throwable ex) {
                    if (name == null) {
                        LOG.log(Level.WARNING, "Ignoring expression serializer due to exception", ex);
                    } else {
                        LOG.log(Level.WARNING, "Ignoring expression serializer " + name + " due to exception", ex);
                    }
                }
            }
            this.expressionSerializers = expressionSerializers;
        }
    }

}
