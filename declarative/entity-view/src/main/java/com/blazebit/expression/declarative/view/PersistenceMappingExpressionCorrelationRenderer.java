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

package com.blazebit.expression.declarative.view;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.persistence.PersistenceCorrelationRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.JoinType;
import com.blazebit.persistence.spi.ServiceProvider;
import com.blazebit.persistence.view.metamodel.MappingAttribute;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceMappingExpressionCorrelationRenderer implements PersistenceCorrelationRenderer, MetadataDefinition<PersistenceCorrelationRenderer>, Serializable {

    private final MappingAttribute<?, ?> mappingAttribute;

    /**
     * Creates a new correlation renderer for a mappingAttribute attribute.
     *
     * @param mappingAttribute The mapping attribute
     */
    public PersistenceMappingExpressionCorrelationRenderer(MappingAttribute<?, ?> mappingAttribute) {
        this.mappingAttribute = mappingAttribute;
    }

    @Override
    public String correlate(CriteriaBuilder<?> cb, String parentAlias, PersistenceExpressionSerializer serializer) {
        DefaultViewRootJpqlMacro.registerIfAbsent(serializer, parentAlias);
        MutableEmbeddingViewJpqlMacro.withEmbeddingViewPath(serializer, parentAlias);
        Map<String, Object> optionalParameters = Collections.emptyMap();
        String correlationExternalAlias = serializer.nextCorrelationAlias();
        String correlationAlias;
        if (mappingAttribute.getLimitExpression() == null) {
            correlationAlias = correlationExternalAlias;
        } else {
            correlationAlias = "_sub" + correlationExternalAlias;
        }
        MutableViewJpqlMacro.withViewPath(serializer, correlationAlias);
        ManagedViewTypeCollection.add(serializer, mappingAttribute.getDeclaringType(), parentAlias);
        CorrelationBuilderImpl correlationBuilder = new CorrelationBuilderImpl(cb, optionalParameters, parentAlias, correlationAlias, correlationExternalAlias, JoinType.LEFT, mappingAttribute);
        StringBuilder sb = new StringBuilder();
        mappingAttribute.renderMapping(parentAlias, (ServiceProvider) serializer.getWhereBuilder(), sb);
        correlationBuilder.correlate(sb.toString()).end();
        correlationBuilder.finish();
        return correlationBuilder.getCorrelationAlias();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<PersistenceCorrelationRenderer> getJavaType() {
        return PersistenceCorrelationRenderer.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersistenceCorrelationRenderer build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }

}
