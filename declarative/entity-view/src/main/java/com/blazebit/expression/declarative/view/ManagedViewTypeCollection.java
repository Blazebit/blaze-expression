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

import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.persistence.BaseQueryBuilder;
import com.blazebit.persistence.view.metamodel.ManagedViewType;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ManagedViewTypeCollection {

    private static final String NAME = "MANAGED_VIEW_TYPE_COLLECTION";

    private final Set<ManagedViewType<?>> managedViewTypes = new HashSet<>();

    /**
     * Registers the managed view type and renders secondary mappings if it wasn't registered yet.
     *
     * @param serializer The serializer
     * @param managedViewType The managed view type to add
     * @param viewPath The path to the view
     */
    public static void add(PersistenceExpressionSerializer serializer, ManagedViewType<?> managedViewType, String viewPath) {
        ManagedViewTypeCollection managedViewTypeCollection = (ManagedViewTypeCollection) serializer.getProperties().get(NAME);
        if (managedViewTypeCollection == null) {
            serializer.getProperties().put(NAME, managedViewTypeCollection = new ManagedViewTypeCollection());
        }
        if (managedViewTypeCollection.managedViewTypes.add(managedViewType)) {
            Map<String, Object> optionalParameters = Collections.emptyMap();
            MutableEmbeddingViewJpqlMacro embeddingViewMacro = MutableEmbeddingViewJpqlMacro.registerIfAbsent(serializer);
            MutableViewJpqlMacro viewMacro = MutableViewJpqlMacro.registerIfAbsent(serializer);
            String oldViewPath = viewMacro.getViewPath();
            String oldEmbeddingViewPath = embeddingViewMacro.getEmbeddingViewPath();

            // NOTE: The embedding view macro is not supported in @EntityViewRoot
            // So we set null in order to cause an exception if it is used
            embeddingViewMacro.setEmbeddingViewPath(null);
            viewMacro.setViewPath(viewPath);
            managedViewType.renderSecondaryMappings(viewPath, (BaseQueryBuilder<?, ?>) serializer.getWhereBuilder(), optionalParameters, false);
            viewMacro.setViewPath(oldViewPath);
            embeddingViewMacro.setEmbeddingViewPath(oldEmbeddingViewPath);
        }
    }
}
