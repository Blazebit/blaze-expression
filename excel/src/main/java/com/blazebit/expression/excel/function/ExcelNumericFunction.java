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
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelNumericFunction implements ExcelFunctionRenderer, Serializable {

    private static final List<ExcelNumericFunction> FUNCTIONS = Arrays.asList(
            new ExcelNumericFunction("SQRT"),
            new ExcelNumericFunction("SIN"),
            new ExcelNumericFunction("COS"),
            new ExcelNumericFunction("TAN"),
            new ExcelNumericFunction("ASIN"),
            new ExcelNumericFunction("ACOS"),
            new ExcelNumericFunction("ATAN"),
            new ExcelNumericFunction("EXP"),
            new ExcelNumericFunction("RADIANS"),
            new ExcelNumericFunction("DEGREES")
    );

    private final String name;

    private ExcelNumericFunction(String name) {
        this.name = name;
    }

    /**
     * Adds the numeric functions SQRT, SIN, COS, TAN, LOG, EXP, RADIANS and DEGREES to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        for (ExcelNumericFunction f : FUNCTIONS) {
            domainBuilder.extendFunction(f.name, new ExcelFunctionRendererMetadataDefinition(f));
        }
        ExcelLogFunction.addFunction(domainBuilder);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, ExcelDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, ExcelExpressionSerializer serializer) {
        sb.append(name).append("(");
        argumentRenderers.renderArguments(sb);
        sb.append(')');
    }
}
