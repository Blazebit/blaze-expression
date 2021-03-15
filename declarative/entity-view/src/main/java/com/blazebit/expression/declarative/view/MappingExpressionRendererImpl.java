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
import com.blazebit.expression.persistence.ExpressionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.persistence.spi.ServiceProvider;
import com.blazebit.persistence.view.metamodel.MappingAttribute;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MappingExpressionRendererImpl implements ExpressionRenderer, MetadataDefinition<ExpressionRenderer>, Serializable {

    private final MappingAttribute<?, ?> mappingAttribute;

    /**
     * Creates a new mapping attribute based expression renderer.
     *
     * @param mappingAttribute The mapping attribute
     */
    public MappingExpressionRendererImpl(MappingAttribute<?, ?> mappingAttribute) {
        this.mappingAttribute = mappingAttribute;
    }

    @Override
    public void render(StringBuilder sb, PersistenceExpressionSerializer serializer) {
        String parentAlias = sb.toString();
        DefaultViewRootJpqlMacro.registerIfAbsent(serializer, parentAlias);
        // NOTE: We don't support the embedding view macro on plain expressions as that would require us to parse expressions
        // So we set null in order to cause an exception if it is used
        MutableEmbeddingViewJpqlMacro.withEmbeddingViewPath(serializer, null);
        MutableViewJpqlMacro.withViewPath(serializer, parentAlias);
        sb.setLength(0);
        ManagedViewTypeCollection.add(serializer, mappingAttribute.getDeclaringType(), parentAlias);
        mappingAttribute.renderMapping(parentAlias, (ServiceProvider) serializer.getWhereBuilder(), sb);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<ExpressionRenderer> getJavaType() {
        return ExpressionRenderer.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpressionRenderer build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }
}
