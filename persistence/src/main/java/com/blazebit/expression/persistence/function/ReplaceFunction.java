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
import com.blazebit.expression.persistence.DomainFunctionArgumentRenderers;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;

import static com.blazebit.expression.persistence.PersistenceContributor.STRING_TYPE_NAME;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ReplaceFunction implements FunctionRenderer, FunctionInvoker, Serializable {

    private static final ReplaceFunction INSTANCE = new ReplaceFunction();

    private ReplaceFunction() {
    }

    /**
     * Adds the REPLACE function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("REPLACE")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("REPLACE", classLoader))
                .withResultType(STRING_TYPE_NAME)
                .withArgument("string", STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("REPLACE_STRING", classLoader))
                .withArgument("target", STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("REPLACE_TARGET", classLoader))
                .withArgument("replacement", STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("REPLACE_REPLACEMENT", classLoader))
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        String string = (String) arguments.getValue(0);
        if (string == null) {
            return null;
        }
        String target = (String) arguments.getValue(1);
        if (target == null) {
            return null;
        }
        String replacement = (String) arguments.getValue(2);
        if (replacement == null) {
            return null;
        }
        return string.replace(target, replacement);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("REPLACE(");
        argumentRenderers.renderArguments(sb);
        sb.append(')');
    }
}
