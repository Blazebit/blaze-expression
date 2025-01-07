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

package com.blazebit.expression.azure.subscription;

/**
 * Configuration properties for the Azure {@link com.blazebit.expression.spi.DataFetcher} instances.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class AzureQueryConfig {

    /**
     * The property name to use for a lookup in {@link com.blazebit.expression.ExpressionInterpreter.Context#getProperty(String)}
     * to acquire the {@link com.blazebit.expression.azure.invoker.ApiClient}.
     */
    public static final String API_CLIENT = "apiClient";

    private AzureQueryConfig() {
    }
}
