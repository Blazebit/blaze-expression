/*
 * Copyright 2019 - 2024 Blazebit.
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

package com.blazebit.expression.excel;

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExcelFunctionRenderer {

    /**
     * Renders the given domain function with the given return type and arguments to the given StringBuilder.
     *
     * @param function The domain function
     * @param returnType The function return type
     * @param argumentRenderers The argument renderers for the arguments
     * @param sb The StringBuilder to render to
     * @param serializer The serializer
     */
    void render(DomainFunction function, DomainType returnType, ExcelDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, ExcelExpressionSerializer serializer);

    /**
     * Returns a function renderer that renders a function as builtin function.
     *
     * @param excelFunctionName The builtin function name
     * @return the function renderer
     */
    static ExcelFunctionRenderer builtin(String excelFunctionName) {
        return (ExcelFunctionRenderer & Serializable) (function, returnType, argumentRenderers, sb, serializer) -> {
            sb.append(excelFunctionName);
            sb.append('(');
            if (argumentRenderers.assignedArguments() != 0) {
                argumentRenderers.renderArguments(sb);
            }
            sb.append(')');
        };
    }

}
