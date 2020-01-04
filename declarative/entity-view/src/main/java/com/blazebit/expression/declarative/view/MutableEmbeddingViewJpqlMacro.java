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

package com.blazebit.expression.declarative.view;

import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.persistence.CommonQueryBuilder;
import com.blazebit.persistence.spi.FunctionRenderContext;
import com.blazebit.persistence.view.spi.EmbeddingViewJpqlMacro;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MutableEmbeddingViewJpqlMacro implements EmbeddingViewJpqlMacro {

    private static final String NAME = "EMBEDDING_VIEW";

    private String embeddingViewPath;
    private boolean used;

    /**
     * Creates a new embedding view macro.
     *
     * @param embeddingViewPath The embedding view path
     */
    public MutableEmbeddingViewJpqlMacro(String embeddingViewPath) {
        this.embeddingViewPath = embeddingViewPath;
    }

    /**
     * Registers a new embedding view macro if none is present or updates an existing one with the given alias.
     *
     * @param serializer The serializer
     * @param alias The alias
     */
    public static void withEmbeddingViewPath(PersistenceExpressionSerializer serializer, String alias) {
        MutableEmbeddingViewJpqlMacro macro = (MutableEmbeddingViewJpqlMacro) serializer.getProperties().get(NAME);
        if (macro == null) {
            macro = new MutableEmbeddingViewJpqlMacro(alias);
            CommonQueryBuilder<?> queryBuilder = (CommonQueryBuilder<?>) serializer.getWhereBuilder();
            queryBuilder.registerMacro(NAME, macro);
            serializer.getProperties().put(NAME, macro);
        } else {
            macro.setEmbeddingViewPath(alias);
        }
    }

    @Override
    public boolean usesEmbeddingView() {
        return used;
    }

    @Override
    public String getEmbeddingViewPath() {
        return embeddingViewPath;
    }

    @Override
    public void setEmbeddingViewPath(String embeddingViewPath) {
        this.embeddingViewPath = embeddingViewPath;
    }

    @Override
    public void render(FunctionRenderContext context) {
        if (context.getArgumentsSize() > 1) {
            throw new IllegalArgumentException("The EMBEDDING_VIEW macro allows maximally one argument: <expression>!");
        }

        if (embeddingViewPath == null) {
            throw new IllegalArgumentException("The EMBEDDING_VIEW macro is not supported in this context!");
        } else if (embeddingViewPath.isEmpty()) {
            used = true;
            if (context.getArgumentsSize() > 0) {
                context.addArgument(0);
            }
        } else {
            used = true;
            context.addChunk(embeddingViewPath);
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
        if (!(o instanceof MutableEmbeddingViewJpqlMacro)) {
            return false;
        }

        MutableEmbeddingViewJpqlMacro that = (MutableEmbeddingViewJpqlMacro) o;

        return embeddingViewPath != null ? embeddingViewPath.equals(that.embeddingViewPath) : that.embeddingViewPath == null;
    }

    @Override
    public int hashCode() {
        return embeddingViewPath != null ? embeddingViewPath.hashCode() : 0;
    }
}
