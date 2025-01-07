/*
 * Copyright 2019 - 2025 Blazebit.
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
import com.blazebit.expression.spi.ExpressionServiceBuilderProvider;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Bootstrap class that is used to obtain a {@linkplain ExpressionService}.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class Expressions {

    private static volatile ExpressionServiceBuilderProvider defaultProvider;

    private Expressions() {
    }

    /**
     * Creates a {@linkplain ExpressionService} based on the given model with the {@link #getDefaultProvider()}.
     *
     * @param model The domain model to use
     * @return the expression service factory
     */
    public static ExpressionService forModel(DomainModel model) {
        return getDefaultProvider().createDefaultBuilder(model).build();
    }

    /**
     * Returns the first {@linkplain ExpressionServiceBuilderProvider} that is found.
     *
     * @return The first {@linkplain ExpressionServiceBuilderProvider} that is found
     */
    public static ExpressionServiceBuilderProvider getDefaultProvider() {
        ExpressionServiceBuilderProvider provider = defaultProvider;
        if (provider == null) {
            Iterator<ExpressionServiceBuilderProvider> iterator = ServiceLoader.load(ExpressionServiceBuilderProvider.class).iterator();
            if (iterator.hasNext()) {
                return defaultProvider = iterator.next();
            }

            throw new IllegalStateException("No expression service factory provider available. Did you forget to add the expression-impl dependency?");
        }
        return provider;
    }

}
