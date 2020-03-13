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
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.persistence.FunctionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.spi.FunctionInvoker;

import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.TIMESTAMP;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class CurrentTimestampFunction implements FunctionRenderer, FunctionInvoker {

    public static final String INSTANT_PROPERTY = "instant";
    private static final CurrentTimestampFunction INSTANCE = new CurrentTimestampFunction();

    private CurrentTimestampFunction() {
    }

    /**
     * Adds the CURRENT_TIMESTAMP function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.createFunction("CURRENT_TIMESTAMP")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withExactArgumentCount(0)
                .withResultType(TIMESTAMP)
                .build();
    }

    /**
     * Returns the current instant for the interpreter context something like the <i>transaction time</i>.
     *
     * @param context The interpreter context
     * @return The current instant
     */
    public static Instant get(ExpressionInterpreter.Context context) {
        Object o = context.getProperty(INSTANT_PROPERTY);
        if (o instanceof Instant) {
            return (Instant) o;
        }
        o = Instant.now();
        context.setProperty(INSTANT_PROPERTY, o);
        return (Instant) o;
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        return get(context);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("CURRENT_TIMESTAMP");
    }
}
