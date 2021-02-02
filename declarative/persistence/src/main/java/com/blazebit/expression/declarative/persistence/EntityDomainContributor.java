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

package com.blazebit.expression.declarative.persistence;

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
import com.blazebit.persistence.CriteriaBuilderFactory;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
@ServiceProvider(DomainContributor.class)
public class EntityDomainContributor implements DomainContributor {

    @Override
    public void contribute(DomainBuilder domainBuilder) {
        com.blazebit.domain.declarative.spi.ServiceProvider<?> userServiceProvider = (com.blazebit.domain.declarative.spi.ServiceProvider<?>) domainBuilder.getProperty(com.blazebit.domain.declarative.spi.ServiceProvider.USER_SERVICE_PROVIDER);
        if (userServiceProvider == null) {
            return;
        }
        CriteriaBuilderFactory criteriaBuilderFactory = userServiceProvider.getService(CriteriaBuilderFactory.class);
        if (criteriaBuilderFactory == null) {
            return;
        }

        // Find out the domain types that are entity view type so that we can register them in an entity literal resolver
        Map<String, String> entityViewDomainJavaTypeIds = new HashMap<>();
        Metamodel metamodel = criteriaBuilderFactory.getService(Metamodel.class);
        for (DomainTypeDefinition<?> typeDefinition : domainBuilder.getTypes().values()) {
            if (typeDefinition instanceof EntityDomainTypeDefinition) {
                if (typeDefinition.getJavaType() != null) {
                    try {
                        EntityType<?> entityType = metamodel.entity(typeDefinition.getJavaType());
                        SingularAttribute<?, ?> idAttribute = null;
                        for (SingularAttribute<?, ?> singularAttribute : entityType.getSingularAttributes()) {
                            if (singularAttribute.isId()) {
                                if (idAttribute == null) {
                                    idAttribute = singularAttribute;
                                } else {
                                    idAttribute = null;
                                    break;
                                }
                            }
                        }
                        if (idAttribute != null) {
                            entityViewDomainJavaTypeIds.put(typeDefinition.getName(), idAttribute.getName());
                        }
                    } catch (IllegalArgumentException ex) {
                        // Ignore
                    }
                }
            }
        }

        if (!entityViewDomainJavaTypeIds.isEmpty()) {
            domainBuilder.withEntityLiteralResolver(new EntityEntityLiteralResolver(domainBuilder.getEntityLiteralResolver(), userServiceProvider, entityViewDomainJavaTypeIds));
        }
    }

    @Override
    public int priority() {
        // Ensure this runs after the PersistenceDomainContributor
        return 550;
    }

    /**
     *
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class EntityEntityLiteralResolver implements DomainSerializer<EntityLiteralResolver>, EntityLiteralResolver, Serializable {

        private final EntityLiteralResolver delegate;
        private final com.blazebit.domain.declarative.spi.ServiceProvider<?> userServiceProvider;
        private final Map<String, String> entityDomainJavaTypes;

        public EntityEntityLiteralResolver(EntityLiteralResolver delegate, com.blazebit.domain.declarative.spi.ServiceProvider<?> userServiceProvider, Map<String, String> entityViewDomainJavaTypeIds) {
            this.delegate = delegate;
            this.userServiceProvider = userServiceProvider;
            this.entityDomainJavaTypes = entityViewDomainJavaTypeIds;
        }

        @Override
        public ResolvedLiteral resolveLiteral(DomainModel domainModel, EntityDomainType entityDomainType, Map<EntityDomainTypeAttribute, ?> attributeValues) {
            String idName = entityDomainJavaTypes.get(entityDomainType.getName());
            if (idName != null) {
                return new EntityResolvedLiteral(entityDomainType, userServiceProvider, attributeValues.get(entityDomainType.getAttribute(idName)));
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
            for (Map.Entry<String, String> entry : entityDomainJavaTypes.entrySet()) {
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
