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

import com.blazebit.apt.service.ServiceProvider;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.DomainTypeDefinition;
import com.blazebit.domain.boot.model.EntityDomainTypeDefinition;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EntityLiteralResolver;
import com.blazebit.domain.runtime.model.ResolvedLiteral;
import com.blazebit.domain.spi.DomainContributor;
import com.blazebit.domain.spi.DomainSerializer;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.metamodel.ViewMetamodel;
import com.blazebit.persistence.view.metamodel.ViewType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
@ServiceProvider(DomainContributor.class)
public class EntityViewDomainContributor implements DomainContributor {

    @Override
    public void contribute(DomainBuilder domainBuilder) {
        com.blazebit.domain.declarative.spi.ServiceProvider<?> userServiceProvider = (com.blazebit.domain.declarative.spi.ServiceProvider<?>) domainBuilder.getProperty(com.blazebit.domain.declarative.spi.ServiceProvider.USER_SERVICE_PROVIDER);
        if (userServiceProvider == null) {
            return;
        }
        EntityViewManager entityViewManager = userServiceProvider.getService(EntityViewManager.class);
        if (entityViewManager == null) {
            return;
        }

        // Find out the domain types that are entity view type so that we can register them in an entity literal resolver
        Map<String, String> entityViewDomainJavaTypeIds = new HashMap<>();
        ViewMetamodel viewMetamodel = entityViewManager.getMetamodel();
        for (DomainTypeDefinition<?> typeDefinition : domainBuilder.getTypes().values()) {
            if (typeDefinition instanceof EntityDomainTypeDefinition) {
                ViewType<?> viewType;
                if (typeDefinition.getJavaType() != null && (viewType = viewMetamodel.view(typeDefinition.getJavaType()))  != null) {
                    entityViewDomainJavaTypeIds.put(typeDefinition.getName(), viewType.getIdAttribute().getName());
                }
            }
        }

        if (!entityViewDomainJavaTypeIds.isEmpty()) {
            domainBuilder.withEntityLiteralResolver(new EntityViewEntityLiteralResolver(domainBuilder.getEntityLiteralResolver(), entityViewManager, entityViewDomainJavaTypeIds));
        }
    }

    @Override
    public int priority() {
        // Ensure this runs after the PersistenceDomainContributor and EntityDomainContributor
        return 600;
    }

    /**
     *
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class EntityViewEntityLiteralResolver implements DomainSerializer<EntityLiteralResolver>, EntityLiteralResolver, Serializable {

        private final EntityLiteralResolver delegate;
        private final EntityViewManager entityViewManager;
        private final Map<String, String> entityViewDomainJavaTypes;

        public EntityViewEntityLiteralResolver(EntityLiteralResolver delegate, EntityViewManager entityViewManager, Map<String, String> entityViewDomainJavaTypeIds) {
            this.delegate = delegate;
            this.entityViewManager = entityViewManager;
            this.entityViewDomainJavaTypes = entityViewDomainJavaTypeIds;
        }

        @Override
        public ResolvedLiteral resolveLiteral(DomainModel domainModel, EntityDomainType entityDomainType, Map<EntityDomainTypeAttribute, ?> attributeValues) {
            String idName = entityViewDomainJavaTypes.get(entityDomainType.getName());
            if (idName != null) {
                Object reference = entityViewManager.getReference(entityDomainType.getJavaType(), attributeValues.get(entityDomainType.getAttribute(idName)));
                return new EntityViewResolvedLiteral(entityDomainType, reference);
            }
            return delegate == null ? null : delegate.resolveLiteral(domainModel, entityDomainType, attributeValues);
        }

        @Override
        public <T> T serialize(DomainModel domainModel, EntityLiteralResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            StringBuilder sb = new StringBuilder();
            if (delegate != null) {
                sb.append("{\"DelegatingEntityLiteralResolver\":[");
            }
            sb.append("{\"FixedEntityLiteralResolver\":[{");
            for (Map.Entry<String, String> entry : entityViewDomainJavaTypes.entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
            }
            sb.setCharAt(sb.length() - 1, '}');
            sb.append("]}");
            if (delegate != null) {
                sb.append(",");
                sb.append(((DomainSerializer<?>) delegate).serialize(domainModel, null, targetType, format, properties));
                sb.append("]}");
            }
            return (T) sb.toString();
        }
    }
}
