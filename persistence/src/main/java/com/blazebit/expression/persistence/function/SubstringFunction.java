/*
 * Copyright 2019 - 2021 Blazebit.
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

import static com.blazebit.expression.persistence.PersistenceDomainContributor.INTEGER_TYPE_NAME;
import static com.blazebit.expression.persistence.PersistenceDomainContributor.STRING_TYPE_NAME;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class SubstringFunction implements FunctionRenderer, FunctionInvoker, Serializable {

    private static final SubstringFunction INSTANCE = new SubstringFunction();

    private SubstringFunction() {
    }

    /**
     * Adds the SUBSTRING function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("SUBSTRING")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("SUBSTRING", classLoader))
                .withMinArgumentCount(2)
                .withResultType(STRING_TYPE_NAME)
                .withArgument("string", STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("SUBSTRING_STRING", classLoader))
                .withArgument("start", INTEGER_TYPE_NAME, DocumentationMetadataDefinition.localized("SUBSTRING_START", classLoader))
                .withArgument("count", INTEGER_TYPE_NAME, DocumentationMetadataDefinition.localized("SUBSTRING_COUNT", classLoader))
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Object string = arguments.getValue(0);
        if (string == null) {
            return null;
        }
        Object start = arguments.getValue(1);
        if (start == null) {
            return null;
        }
        int startIndex = ((Number) start).intValue() - 1;
        int endIndexOffset = 0;
        if (startIndex < 0) {
            endIndexOffset = -startIndex;
            startIndex = 0;
        }
        String s = string.toString();
        int endIndex;
        Object count = arguments.getValue(2);
        if (count == null) {
            endIndex = s.length() - endIndexOffset;
        } else {
            endIndex = startIndex + ((Number) count).intValue() - endIndexOffset;
        }
        if (endIndex > s.length()) {
            endIndex = s.length();
        }
        return s.substring(startIndex, endIndex);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("SUBSTRING(");
        argumentRenderers.renderArguments(sb);
        sb.append(')');
    }
}
