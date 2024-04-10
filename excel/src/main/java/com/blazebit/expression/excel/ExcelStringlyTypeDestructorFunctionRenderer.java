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
class ExcelStringlyTypeDestructorFunctionRenderer implements ExcelFunctionRenderer, Serializable {

    private final ExcelStringlyTypeHandler stringlyTypeHandler;

    /**
     * Create a new destructor function renderer with the given stringly type handler.
     *
     * @param stringlyTypeHandler The stringly type handler
     */
    public ExcelStringlyTypeDestructorFunctionRenderer(ExcelStringlyTypeHandler stringlyTypeHandler) {
        this.stringlyTypeHandler = stringlyTypeHandler;
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, ExcelDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, ExcelExpressionSerializer serializer) {
        stringlyTypeHandler.appendExcelDestructTo(sb, subBuilder -> argumentRenderers.renderArgument(subBuilder, 0));
    }
}
