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

import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.STRING;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ReplaceFunction implements FunctionRenderer, FunctionInvoker {

    private static final ReplaceFunction INSTANCE = new ReplaceFunction();

    private ReplaceFunction() {
    }

    /**
     * Adds the REPLACE function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.createFunction("REPLACE")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withResultType(STRING)
                .withArgument("string", STRING)
                .withArgument("target", STRING)
                .withArgument("replacement", STRING)
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        String string = (String) arguments.get(function.getArgument(0));
        if (string == null) {
            return null;
        }
        String target = (String) arguments.get(function.getArgument(1));
        if (target == null) {
            return null;
        }
        String replacement = (String) arguments.get(function.getArgument(2));
        if (replacement == null) {
            return null;
        }
        return string.replace(target, replacement);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb) {
        sb.append("REPLACE(");
        argumentRenderers.get(function.getArgument(0)).accept(sb);
        sb.append(", ");
        argumentRenderers.get(function.getArgument(1)).accept(sb);
        sb.append(", ");
        argumentRenderers.get(function.getArgument(2)).accept(sb);
        sb.append(')');
    }
}
