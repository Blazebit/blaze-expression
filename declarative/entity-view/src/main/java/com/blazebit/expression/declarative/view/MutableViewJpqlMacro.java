/*
 * Copyright 2019 - 2022 Blazebit.
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

package com.blazebit.expression.declarative.view;

import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.persistence.CommonQueryBuilder;
import com.blazebit.persistence.spi.FunctionRenderContext;
import com.blazebit.persistence.view.spi.ViewJpqlMacro;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MutableViewJpqlMacro implements ViewJpqlMacro {

    private static final String NAME = "VIEW";

    private String viewPath;

    /**
     * Registers a new view macro if none is present and returns it.
     *
     * @param serializer The serializer
     * @return The view macro
     */
    public static MutableViewJpqlMacro registerIfAbsent(PersistenceExpressionSerializer serializer) {
        MutableViewJpqlMacro macro = (MutableViewJpqlMacro) serializer.getProperties().get(NAME);
        if (macro == null) {
            macro = new MutableViewJpqlMacro();
            CommonQueryBuilder<?> queryBuilder = (CommonQueryBuilder<?>) serializer.getWhereBuilder();
            queryBuilder.registerMacro(NAME, macro);
            serializer.getProperties().put(NAME, macro);
        }
        return macro;
    }

    /**
     * Registers a new embedding view macro if none is present or updates an existing one with the given alias.
     *
     * @param serializer The serializer
     * @param alias The alias
     * @return The embedding view macro
     */
    public static MutableViewJpqlMacro withViewPath(PersistenceExpressionSerializer serializer, String alias) {
        MutableViewJpqlMacro macro = registerIfAbsent(serializer);
        macro.setViewPath(alias);
        return macro;
    }

    @Override
    public String getViewPath() {
        return viewPath;
    }

    @Override
    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    @Override
    public void render(FunctionRenderContext context) {
        if (context.getArgumentsSize() > 1) {
            throw new IllegalArgumentException("The VIEW macro allows maximally one argument: <expression>!");
        }

        if (viewPath == null) {
            throw new IllegalArgumentException("The VIEW macro is not supported in this context!");
        } else if (viewPath.isEmpty()) {
            if (context.getArgumentsSize() > 0) {
                context.addArgument(0);
            }
        } else {
            context.addChunk(viewPath);
            if (context.getArgumentsSize() > 0) {
                context.addChunk(".");
                context.addArgument(0);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MutableViewJpqlMacro)) {
            return false;
        }

        MutableViewJpqlMacro that = (MutableViewJpqlMacro) o;

        return viewPath != null ? viewPath.equals(that.viewPath) : that.viewPath == null;
    }

    @Override
    public int hashCode() {
        return viewPath != null ? viewPath.hashCode() : 0;
    }
}
