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
public class ExcelLocateLastFunction implements ExcelFunctionRenderer, Serializable {

    private static final ExcelLocateLastFunction INSTANCE = new ExcelLocateLastFunction();

    private ExcelLocateLastFunction() {
    }

    /**
     * Adds the LOCATE_LAST function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.extendFunction("LOCATE_LAST", new ExcelFunctionRendererMetadataDefinition(INSTANCE));
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, ExcelDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, ExcelExpressionSerializer serializer) {
        String separator = serializer.getArgumentSeparator();
        // See https://trumpexcel.com/find-characters-last-position/ for a deeper explanation
        sb.append("FIND(UNICHAR(0)").append(separator).append(" SUBSTITUTE(");
        argumentRenderers.renderArgument(sb, 1);
        sb.append(separator).append(" ");
        argumentRenderers.renderArgument(sb, 0);
        sb.append(separator).append(" UNICHAR(0)").append(separator).append(" (LEN(");
        argumentRenderers.renderArgument(sb, 1);
        sb.append(") - LEN(SUBSTITUTE(");
        argumentRenderers.renderArgument(sb, 1);
        sb.append(separator).append(" ");
        argumentRenderers.renderArgument(sb, 0);
        sb.append(separator).append(" \"\"))) / LEN(");
        argumentRenderers.renderArgument(sb, 0);
        sb.append("))").append(separator).append(" 1)");
    }
}
