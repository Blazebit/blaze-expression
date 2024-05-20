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

package com.blazebit.expression.azure.vm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.azure.invoker.ApiClient;
import com.blazebit.expression.azure.invoker.ApiException;
import com.blazebit.expression.azure.subscription.AzureQueryConfig;
import com.blazebit.expression.azure.subscription.api.SubscriptionsApi;
import com.blazebit.expression.azure.subscription.model.Subscription;
import com.blazebit.expression.azure.subscription.model.SubscriptionListResult;
import com.blazebit.expression.azure.vm.api.VirtualMachinesApi;
import com.blazebit.expression.azure.vm.model.VirtualMachine;
import com.blazebit.expression.azure.vm.model.VirtualMachineListResult;
import com.blazebit.expression.spi.DataFetcher;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class VirtualMachineDataFetcher implements DataFetcher, Serializable {

    public static final VirtualMachineDataFetcher INSTANCE = new VirtualMachineDataFetcher();

    private VirtualMachineDataFetcher() {
    }

    @Override
    public List<?> fetch(ExpressionInterpreter.Context context) {
        try {
            ApiClient apiClient = context.getProperty( AzureQueryConfig.API_CLIENT );
            SubscriptionListResult subscriptionListResult = new SubscriptionsApi( apiClient ).subscriptionsList( "2022-12-01" );
            VirtualMachinesApi virtualMachinesApi = new VirtualMachinesApi( apiClient );
            List<VirtualMachine> list = new ArrayList<>();
            for ( Subscription subscription : subscriptionListResult.getValue() ) {
                VirtualMachineListResult virtualMachineListResult = virtualMachinesApi.virtualMachinesListAll(
                        "2024-03-01",
                        subscription.getSubscriptionId(),
                        null,
                        null,
                        null
                );
                list.addAll(virtualMachineListResult.getValue());
            }
            return list;
        } catch (ApiException e) {
            throw new RuntimeException( "Could not fetch virtual machine list", e );
        }
    }
}
