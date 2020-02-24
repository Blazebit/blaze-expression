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
import com.blazebit.expression.spi.FunctionInvoker;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.NUMERIC;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class Atan2Function implements FunctionRenderer, FunctionInvoker {

    private static final Atan2Function INSTANCE = new Atan2Function();

    private Atan2Function() {
    }

    /**
     * Adds the ATAN2 function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.createFunction("ATAN2")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withArgument("y", NUMERIC)
                .withArgument("x", NUMERIC)
                .build();
        domainBuilder.withFunctionTypeResolver("ATAN2", StaticDomainFunctionTypeResolvers.returning(NUMERIC));
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        Object y = arguments.get(function.getArgument(0));
        if (y == null) {
            return null;
        }
        Object x = arguments.get(function.getArgument(1));
        if (x == null) {
            return null;
        }

        return new BigDecimal(Math.atan2(((Number) y).doubleValue(), ((Number) x).doubleValue()));
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb) {
        sb.append("ATAN2(");
        argumentRenderers.get(function.getArgument(0)).accept(sb);
        sb.append(", ");
        argumentRenderers.get(function.getArgument(1)).accept(sb);
        sb.append(')');
    }
}
