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

package com.blazebit.expression.excel;

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.base.GlobalStringlyTypeDestructorFunctionInvoker;
import com.blazebit.expression.base.StringlyTypeHandler;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
class ExcelGlobalStringlyTypeDestructorFunctionRenderer implements ExcelFunctionRenderer, Serializable {

    final Map<String, ExcelStringlyTypeHandler> destructors = new ConcurrentHashMap<>();
    private final GlobalStringlyTypeDestructorFunctionInvoker functionInvoker;
    private final String name;

    /**
     * Create a new stringly type destructor function renderer.
     *
     * @param functionInvoker The global stringly type function invoker
     * @param name The name of the function
     */
    public ExcelGlobalStringlyTypeDestructorFunctionRenderer(GlobalStringlyTypeDestructorFunctionInvoker functionInvoker, String name) {
        this.functionInvoker = functionInvoker;
        this.name = name;
    }

    /**
     * Returns the stringly type handler for the given type name.
     *
     * @param typeName The type name
     * @return The stringly type handler or <code>null</code>
     */
    public ExcelStringlyTypeHandler getHandler(String typeName) {
        return destructors.get(typeName);
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, ExcelDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, ExcelExpressionSerializer serializer) {
        String argumentTypeName = argumentRenderers.getType(0).getName();
        ExcelStringlyTypeHandler excelStringlyTypeHandler = destructors.get(argumentTypeName);
        if (excelStringlyTypeHandler == null) {
            StringlyTypeHandler<?> handler = functionInvoker.getHandler(argumentTypeName);
            if (handler == null) {
                throw new DomainModelException("Unsupported type for destructure function " + this.name + " function: " + argumentTypeName);
            }
            excelStringlyTypeHandler = ExcelStringlyTypeHandler.INSTANCE;
        }
        excelStringlyTypeHandler.appendExcelDestructTo(sb, subBuilder -> argumentRenderers.renderArgument(subBuilder, 0));
    }
}
