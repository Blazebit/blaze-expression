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

import java.util.List;

/**
 * Provides access to data that {@link com.blazebit.expression.spi.DataFetcher} acquired during interpretation.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface DataFetcherData {

    /**
     * Returns the data that was read by a {@link com.blazebit.expression.spi.DataFetcher},
     * for a domain type with the given name.
     *
     * @param domainTypeName The domain type name for which to get data
     * @return the data that was read by a data fetcher
     */
    List<?> getDataForDomainType(String domainTypeName);

    /**
     * Sets the data that the {@link com.blazebit.expression.spi.DataFetcher} read for a domain type with the given name.
     *
     * @param domainTypeName The domain type name for which to set data
     * @param data The new data to set
     * @return The old data that was set
     */
    List<?> setDataForDomainType(String domainTypeName, List<?> data);

}
