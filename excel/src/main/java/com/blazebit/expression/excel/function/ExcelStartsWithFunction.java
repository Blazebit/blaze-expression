/*
 * Copyright 2019 - 2025 Blazebit.
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

package com.blazebit.expression.excel.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.excel.ExcelDomainFunctionArgumentRenderers;
import com.blazebit.expression.excel.ExcelFunctionRenderer;
import com.blazebit.expression.excel.ExcelExpressionSerializer;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelStartsWithFunction implements ExcelFunctionRenderer, Serializable {

    private static final ExcelStartsWithFunction INSTANCE = new ExcelStartsWithFunction();

    private ExcelStartsWithFunction() {
    }

    /**
     * Adds the STARTS_WITH function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.extendFunction("STARTS_WITH", new ExcelFunctionRendererMetadataDefinition(INSTANCE));
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, ExcelDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, ExcelExpressionSerializer serializer) {
        String separator = serializer.getArgumentSeparator();
        if (argumentRenderers.assignedArguments() > 2) {
            sb.append("MID(");
            argumentRenderers.renderArgument(sb, 0);
            sb.append(separator).append(" ");
            argumentRenderers.renderArgument(sb, 2);
            sb.append(separator).append(" LEN(");
            argumentRenderers.renderArgument(sb, 1);
            sb.append(") = ");
            argumentRenderers.renderArgument(sb, 1);
        } else {
            sb.append("LEFT(");
            argumentRenderers.renderArgument(sb, 0);
            sb.append(separator).append(" LEN(");
            argumentRenderers.renderArgument(sb, 1);
            sb.append(") = ");
            argumentRenderers.renderArgument(sb, 1);
        }
    }
}
