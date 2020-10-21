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
import java.math.BigInteger;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.INTEGER_TYPE_NAME;
import static com.blazebit.expression.persistence.PersistenceDomainContributor.STRING_TYPE_NAME;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class LocateLastFunction implements FunctionRenderer, FunctionInvoker, Serializable {

    private static final LocateLastFunction INSTANCE = new LocateLastFunction();

    private LocateLastFunction() {
    }

    /**
     * Adds the LOCATE_LAST function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("LOCATE_LAST")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("LOCATE_LAST", classLoader))
                .withMinArgumentCount(2)
                .withResultType(INTEGER_TYPE_NAME)
                .withArgument("substring", STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("LOCATE_LAST_SUBSTRING", classLoader))
                .withArgument("string", STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("LOCATE_LAST_STRING", classLoader))
                .withArgument("start", INTEGER_TYPE_NAME, DocumentationMetadataDefinition.localized("LOCATE_LAST_START", classLoader))
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Object substring = arguments.getValue(0);
        if (substring == null) {
            return null;
        }
        Object string = arguments.getValue(1);
        if (string == null) {
            return null;
        }
        Object start = arguments.getValue(2);
        if (start == null) {
            if (arguments.assignedArguments() < 3) {
                start = 0;
            } else {
                return null;
            }
        }

        String needle = substring.toString();
        String s = string.toString();
        int startIndex = ((Number) start).intValue();
        return BigInteger.valueOf(s.lastIndexOf(needle, startIndex));
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("LENGTH(");
        argumentRenderers.renderArgument(sb, 1);
        sb.append(") - LOCATE(REVERSE(");
        argumentRenderers.renderArgument(sb, 0);
        sb.append("), REVERSE(");
        argumentRenderers.renderArgument(sb, 1);
        sb.append(')');
        if (argumentRenderers.assignedArguments() > 2) {
            sb.append(", ");
            argumentRenderers.renderArgument(sb, 2);
        }
        sb.append(')');
    }
}
