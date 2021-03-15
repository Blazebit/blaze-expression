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
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.blazebit.persistence.spi.ServiceProvider;
import com.blazebit.persistence.view.CorrelationProvider;
import com.blazebit.persistence.view.metamodel.CorrelatedAttribute;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class CorrelationProviderCorrelationRendererImpl implements CorrelationRenderer, MetadataDefinition<CorrelationRenderer>, Serializable {

    private final CorrelatedAttribute<?, ?> correlatedAttribute;

    /**
     * Creates a new correlation renderer for a correlatedAttribute attribute.
     *
     * @param correlatedAttribute The correlated attribute
     */
    public CorrelationProviderCorrelationRendererImpl(CorrelatedAttribute<?, ?> correlatedAttribute) {
        this.correlatedAttribute = correlatedAttribute;
    }

    @Override
    public String correlate(CriteriaBuilder<?> cb, String parentAlias, PersistenceExpressionSerializer serializer) {
        DefaultViewRootJpqlMacro.registerIfAbsent(serializer, parentAlias);
        MutableEmbeddingViewJpqlMacro.withEmbeddingViewPath(serializer, parentAlias);
        Map<String, Object> optionalParameters = Collections.emptyMap();
        String correlationExternalAlias = serializer.nextCorrelationAlias();
        String correlationAlias;
        if (correlatedAttribute.getLimitExpression() == null) {
            correlationAlias = correlationExternalAlias;
        } else {
            correlationAlias = "_sub" + correlationExternalAlias;
        }
        MutableViewJpqlMacro.withViewPath(serializer, correlationAlias);
        ManagedViewTypeCollection.add(serializer, correlatedAttribute.getDeclaringType(), parentAlias);
        CorrelationBuilderImpl correlationBuilder = new CorrelationBuilderImpl(cb, optionalParameters, parentAlias, correlationAlias, correlationExternalAlias, JoinType.LEFT, correlatedAttribute);
        StringBuilder sb = new StringBuilder();
        correlatedAttribute.renderCorrelationBasis(parentAlias, (ServiceProvider) serializer.getWhereBuilder(), sb);
        CorrelationProvider correlationProvider = correlatedAttribute.getCorrelationProviderFactory().create(null, optionalParameters);
        correlationProvider.applyCorrelation(correlationBuilder, sb.toString());
        sb.setLength(0);
        correlationBuilder.finish();
        correlatedAttribute.renderCorrelationResult(correlationBuilder.getCorrelationAlias(), (ServiceProvider) serializer.getWhereBuilder(), sb);
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
    public CorrelationRenderer build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }

}
