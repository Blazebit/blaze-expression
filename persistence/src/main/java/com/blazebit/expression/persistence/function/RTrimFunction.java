/*
 * Copyright 2019 Blazebit.
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
import com.blazebit.expression.spi.FunctionInvoker;
import com.blazebit.expression.persistence.FunctionRenderer;

import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.STRING;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class RTrimFunction implements FunctionRenderer, FunctionInvoker {

    private static final RTrimFunction INSTANCE = new RTrimFunction();

    private RTrimFunction() {
    }

    /**
     * Adds the RTRIM function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.createFunction("RTRIM")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMinArgumentCount(1)
                .withResultType(STRING)
                .withArgument("string", STRING)
                .withArgument("character", STRING)
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        Object string = arguments.get(function.getArgument(0));
        if (string == null) {
            return null;
        }
        Object character = arguments.getOrDefault(function.getArgument(1), ' ');
        if (character == null) {
            return null;
        }

        String s = string.toString();
        char c = (char) character;
        int end = s.length() - 1;
        for (; end > 0; end--) {
            if (c != s.charAt(end)) {
                break;
            }
        }

        return s.substring(0, end);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb) {
        sb.append("RTRIM(");
        argumentRenderers.get(function.getArgument(0)).accept(sb);
        Consumer<StringBuilder> secondArg = argumentRenderers.get(function.getArgument(1));
        if (secondArg != null) {
            sb.append(", ");
            secondArg.accept(sb);
        }
        sb.append(')');
    }
}
