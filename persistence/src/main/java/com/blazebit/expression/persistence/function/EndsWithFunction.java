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
import com.blazebit.expression.persistence.DocumentationMetadataDefinition;
import com.blazebit.expression.persistence.FunctionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.BOOLEAN;
import static com.blazebit.expression.persistence.PersistenceDomainContributor.STRING;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EndsWithFunction implements FunctionRenderer, FunctionInvoker, Serializable {

    private static final EndsWithFunction INSTANCE = new EndsWithFunction();

    private EndsWithFunction() {
    }

    /**
     * Adds the ENDS_WITH function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.createFunction("ENDS_WITH")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("ENDS_WITH"))
                .withMinArgumentCount(2)
                .withResultType(BOOLEAN)
                .withArgument("string", STRING, DocumentationMetadataDefinition.localized("ENDS_WITH_STRING"))
                .withArgument("substring", STRING, DocumentationMetadataDefinition.localized("ENDS_WITH_SUBSTRING"))
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        String string = (String) arguments.get(function.getArgument(1));
        if (string == null) {
            return null;
        }
        String substring = (String) arguments.get(function.getArgument(0));
        if (substring == null) {
            return null;
        }
        return string.endsWith(substring);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("LOCATE(");
        argumentRenderers.get(function.getArgument(1)).accept(sb);
        sb.append(", ");
        argumentRenderers.get(function.getArgument(0)).accept(sb);
        sb.append(") = LENGTH(");
        argumentRenderers.get(function.getArgument(1)).accept(sb);
        sb.append(") - LENGTH(");
        argumentRenderers.get(function.getArgument(0)).accept(sb);
        sb.append(')');
    }

    @Override
    public boolean rendersPredicate() {
        return true;
    }
}
