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

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.persistence.PersistenceExpressionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceStringExpressionRenderer implements PersistenceExpressionRenderer, MetadataDefinition<PersistenceExpressionRenderer>, Serializable {

    private final String[] expressionChunks;
    private final boolean preChunks;
    private final boolean postChunks;

    /**
     * Creates a new expression renderer for the given chunks.
     * All chunks are prefixed with the parent alias except for the first.
     * Whether to prefix the first chunk depends on the <code>preChunks</code> argument.
     * The <code>postChunks</code> argument controls whether the plain parent alias is rendered after the chunks
     *
     * @param preChunks Whether to render the parent before the first chunk
     * @param postChunks Whether to render the parent after the last chunk
     * @param chunks The chunks to prefix with the parent alias
     */
    public PersistenceStringExpressionRenderer(boolean preChunks, boolean postChunks, String... chunks) {
        if (chunks == null) {
            throw new IllegalArgumentException("Illegal empty chunks!");
        }
        this.expressionChunks = chunks;
        this.preChunks = preChunks;
        this.postChunks = postChunks;
    }

    @Override
    public void render(StringBuilder sb, PersistenceExpressionSerializer serializer) {
        String alias = sb.toString();
        DefaultViewRootJpqlMacro.registerIfAbsent(serializer, alias);
        // NOTE: We don't support the embedding view macro on plain expressions as that would require us to parse expressions
        // So we set null in order to cause an exception if it is used
        MutableEmbeddingViewJpqlMacro.withEmbeddingViewPath(serializer, null);
        MutableViewJpqlMacro.withViewPath(serializer, alias);
        sb.setLength(0);
        if (preChunks) {
            sb.append(alias).append('.');
        }

        for (int i = 0; i < expressionChunks.length; i++) {
            if (i != 0) {
                sb.append(alias).append('.');
            }
            sb.append(expressionChunks[i]);
        }

        if (postChunks) {
            sb.append(alias);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<PersistenceExpressionRenderer> getJavaType() {
        return PersistenceExpressionRenderer.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersistenceExpressionRenderer build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }

}
