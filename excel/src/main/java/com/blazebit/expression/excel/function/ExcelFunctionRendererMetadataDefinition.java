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

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.excel.ExcelFunctionRenderer;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelFunctionRendererMetadataDefinition implements MetadataDefinition<ExcelFunctionRenderer>, Serializable {

    private final ExcelFunctionRenderer excelFunctionRenderer;

    /**
     * Creates a new function renderer metadata definition.
     *
     * @param excelFunctionRenderer The function renderer
     */
    public ExcelFunctionRendererMetadataDefinition(ExcelFunctionRenderer excelFunctionRenderer) {
        this.excelFunctionRenderer = excelFunctionRenderer;
    }

    @Override
    public Class<ExcelFunctionRenderer> getJavaType() {
        return ExcelFunctionRenderer.class;
    }

    @Override
    public ExcelFunctionRenderer build(MetadataDefinitionHolder definitionHolder) {
        return excelFunctionRenderer;
    }
}
