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

package com.blazebit.expression.azure.subscription;

import java.io.Serializable;
import java.util.List;

import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.azure.invoker.ApiException;
import com.blazebit.expression.azure.subscription.api.TenantsApi;
import com.blazebit.expression.spi.DataFetcher;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class TenantIdDescriptionDataFetcher implements DataFetcher, Serializable {

    public static final TenantIdDescriptionDataFetcher INSTANCE = new TenantIdDescriptionDataFetcher();

    private TenantIdDescriptionDataFetcher() {
    }

    @Override
    public List<?> fetch(ExpressionInterpreter.Context context) {
        try {
            return new TenantsApi( context.getProperty( AzureQueryConfig.API_CLIENT ) )
                    .tenantsList( "2022-12-01" ).getValue();
        } catch (ApiException e) {
            throw new RuntimeException( "Could not fetch tenant list", e );
        }
    }
}
