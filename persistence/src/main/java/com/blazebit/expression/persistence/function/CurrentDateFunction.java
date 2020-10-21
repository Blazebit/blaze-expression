/*
 * Copyright 2019 - 2020 Blazebit.
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

package com.blazebit.expression.persistence.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.persistence.FunctionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.spi.DomainFunctionArgumentRenderers;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.time.Instant;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.TIMESTAMP_TYPE_NAME;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class CurrentDateFunction implements FunctionRenderer, FunctionInvoker, Serializable {

    private static final CurrentDateFunction INSTANCE = new CurrentDateFunction();
    private static final int SECONDS_PER_DAY = 86400;

    private CurrentDateFunction() {
    }

    /**
     * Adds the CURRENT_TIMESTAMP function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("CURRENT_DATE")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("CURRENT_DATE", classLoader))
                .withExactArgumentCount(0)
                .withResultType(TIMESTAMP_TYPE_NAME)
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Instant instant = CurrentTimestampFunction.get(context);
        return Instant.ofEpochSecond(Math.floorDiv(instant.getEpochSecond(), SECONDS_PER_DAY) * SECONDS_PER_DAY);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("CURRENT_DATE");
    }
}
