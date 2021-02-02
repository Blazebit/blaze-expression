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

package com.blazebit.expression.declarative.view;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.persistence.CorrelationRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.persistence.SubqueryProvider;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.spi.ServiceProvider;
import com.blazebit.persistence.view.metamodel.SubqueryAttribute;

import java.io.Serializable;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class SubqueryCorrelationRendererImpl implements CorrelationRenderer, MetadataDefinition<CorrelationRenderer>, Serializable {

    private static final String[] EMPTY = new String[0];

    private final SubqueryAttribute<?, ?> subqueryAttribute;
    private final String[] subqueryExpressionChunks;
    private final boolean preChunks;
    private final boolean postChunks;

    /**
     * Creates a new correlation renderer for a subquery attribute.
     *
     * @param subqueryAttribute The subquery attribute
     */
    public SubqueryCorrelationRendererImpl(SubqueryAttribute<?, ?> subqueryAttribute) {
        this.subqueryAttribute = subqueryAttribute;
        String subqueryExpression = subqueryAttribute.getSubqueryExpression().trim();
        String subqueryAlias = subqueryAttribute.getSubqueryAlias();
        if (subqueryExpression.isEmpty() || subqueryExpression.equals(subqueryAlias)) {
            this.subqueryExpressionChunks = EMPTY;
            this.preChunks = true;
            this.postChunks = false;
        } else {
            String[] chunks = subqueryExpression.split(Pattern.quote(subqueryAlias));
            this.preChunks = subqueryExpression.startsWith(subqueryAlias) && !Character.isJavaIdentifierPart(subqueryExpression.charAt(subqueryAlias.length()));
            this.postChunks = subqueryExpression.endsWith(subqueryAlias) && !Character.isJavaIdentifierPart(subqueryExpression.charAt(subqueryExpression.length() - (subqueryAlias.length() + 1)));
            int start = preChunks ? 1 : 0;
            int end = chunks.length - (postChunks ? 1 : 0);
            String[] subqueryExpressionChunks = new String[end - start];
            for (int i = 0; start < end; start++, i++) {
                subqueryExpressionChunks[i] = chunks[start];
            }
            this.subqueryExpressionChunks = subqueryExpressionChunks;
        }
    }

    @Override
    public String correlate(CriteriaBuilder<?> cb, String parentAlias, PersistenceExpressionSerializer serializer) {
        DefaultViewRootJpqlMacro.registerIfAbsent(serializer, parentAlias);
        MutableEmbeddingViewJpqlMacro embeddingViewJpqlMacro = MutableEmbeddingViewJpqlMacro.withEmbeddingViewPath(serializer, parentAlias);
        // NOTE: The view macro is not supported in subquery providers
        // So we set null in order to cause an exception if it is used
        MutableViewJpqlMacro.withViewPath(serializer, null);
        String alias = serializer.registerSubqueryProvider(new SubqueryProviderWrapper(embeddingViewJpqlMacro, parentAlias, subqueryAttribute.getSubqueryProviderFactory().create(null, Collections.emptyMap())));
        StringBuilder sb = new StringBuilder();

        if (preChunks) {
            sb.append(alias);
        }

        for (int i = 0; i < subqueryExpressionChunks.length; i++) {
            if (i != 0) {
                sb.append(alias);
            }
            sb.append(subqueryExpressionChunks[i]);
        }

        if (postChunks) {
            sb.append(alias);
        }

        String expression = sb.toString();
        sb.setLength(0);
        ManagedViewTypeCollection.add(serializer, subqueryAttribute.getDeclaringType(), parentAlias);
        subqueryAttribute.renderSubqueryExpression(parentAlias, expression, alias, (ServiceProvider) serializer.getWhereBuilder(), sb);
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<CorrelationRenderer> getJavaType() {
        return CorrelationRenderer.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CorrelationRenderer build(MetadataDefinitionHolder<?> definitionHolder) {
        return this;
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static final class SubqueryProviderWrapper implements SubqueryProvider {

        private final MutableEmbeddingViewJpqlMacro embeddingViewJpqlMacro;
        private final String embeddingViewPath;
        private final com.blazebit.persistence.view.SubqueryProvider subqueryProvider;

        /**
         * Creates a wrapper.
         *
         * @param embeddingViewJpqlMacro The embedding view macro
         * @param embeddingViewPath The embedding view path
         * @param subqueryProvider The subquery provider
         */
        public SubqueryProviderWrapper(MutableEmbeddingViewJpqlMacro embeddingViewJpqlMacro, String embeddingViewPath, com.blazebit.persistence.view.SubqueryProvider subqueryProvider) {
            this.embeddingViewJpqlMacro = embeddingViewJpqlMacro;
            this.embeddingViewPath = embeddingViewPath;
            this.subqueryProvider = subqueryProvider;
        }

        @Override
        public <T> T createSubquery(SubqueryInitiator<T> subqueryInitiator) {
            embeddingViewJpqlMacro.setEmbeddingViewPath(embeddingViewPath);
            return subqueryProvider.createSubquery(subqueryInitiator);
        }
    }
}
