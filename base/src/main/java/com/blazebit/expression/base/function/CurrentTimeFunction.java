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

package com.blazebit.expression.base.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionVolatility;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalTime;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class CurrentTimeFunction implements FunctionInvoker, Serializable {

    private static final CurrentTimeFunction INSTANCE = new CurrentTimeFunction();
    private static final long SECONDS_PER_DAY = 86400;

    private CurrentTimeFunction() {
    }

    /**
     * Adds the CURRENT_TIMESTAMP function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("CURRENT_TIME")
                .withVolatility(DomainFunctionVolatility.STABLE)
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("CURRENT_TIME", classLoader))
                .withExactArgumentCount(0)
                .withResultType(BaseContributor.TIME_TYPE_NAME)
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Instant instant = CurrentTimestampFunction.get(context);
        return LocalTime.ofSecondOfDay(Math.floorMod(instant.getEpochSecond(), SECONDS_PER_DAY));
    }
}
