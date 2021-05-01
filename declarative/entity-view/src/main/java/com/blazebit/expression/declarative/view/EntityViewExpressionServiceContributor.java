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
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.ExpressionServiceBuilder;
import com.blazebit.expression.Literal;
import com.blazebit.expression.declarative.persistence.EntityLiteralPersistenceRestrictionProvider;
import com.blazebit.expression.spi.EntityLiteralResolver;
import com.blazebit.expression.spi.ExpressionServiceContributor;
import com.blazebit.expression.spi.ExpressionServiceSerializer;
import com.blazebit.expression.spi.ResolvedLiteral;
import com.blazebit.expression.spi.TypeAdapter;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.metamodel.MappingAttribute;
import com.blazebit.persistence.view.metamodel.MethodAttribute;
import com.blazebit.persistence.view.metamodel.ViewMetamodel;
import com.blazebit.persistence.view.metamodel.ViewType;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
@ServiceProvider(ExpressionServiceContributor.class)
public class EntityViewExpressionServiceContributor implements ExpressionServiceContributor {

    /**
     * The default entity literal capable mode for domain type that are entity views.
     * Valid values are <code>none</code>, <code>all</code> or the default <code>annotated</code>.
     */
    public static final String ENTITY_LITERAL_CAPABLE_MODE = "com.blazebit.expression.declarative.view.entity_literal_capable_mode";

    @Override
    public void contribute(ExpressionServiceBuilder expressionServiceBuilder) {
        DomainModel domainModel = expressionServiceBuilder.getDomainModel();
        EntityViewManager entityViewManager = domainModel.getService(EntityViewManager.class);
        Mode mode = Mode.of((String) domainModel.getProperty(ENTITY_LITERAL_CAPABLE_MODE));
        if (entityViewManager == null || mode == Mode.NONE) {
            return;
        }
        // Find out the domain types that are entity view types so that we can register them in an entity literal resolver
        Map<String, Resolver> entityViewResolvers = new HashMap<>();
        ViewMetamodel viewMetamodel = entityViewManager.getMetamodel();
        for (DomainType domainType : domainModel.getTypes().values()) {
            if (domainType instanceof EntityDomainType) {
                ViewType<?> viewType;
                if (domainType.getJavaType() != null && (viewType = viewMetamodel.view(domainType.getJavaType())) != null) {
                    EntityLiteralPersistenceRestrictionProvider restrictionProvider = domainType.getMetadata(EntityLiteralPersistenceRestrictionProvider.class);
                    if (restrictionProvider != null || mode == Mode.ALL) {
                        entityViewResolvers.put(domainType.getName(), new Resolver(entityViewManager, (EntityDomainType) domainType, viewType, restrictionProvider));
                    }
                }
            }
        }

        if (!entityViewResolvers.isEmpty()) {
            expressionServiceBuilder.withEntityLiteralResolver(new EntityViewEntityLiteralResolver(expressionServiceBuilder.getEntityLiteralResolver(), entityViewManager, entityViewResolvers));
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
    private enum Mode {
        NONE,
        ALL,
        ANNOTATED;

        static Mode of(String value) {
            if (value == null || value.isEmpty() || "annotated".equalsIgnoreCase(value)) {
                return ANNOTATED;
            } else if ("all".equalsIgnoreCase(value)) {
                return ALL;
            } else {
                return NONE;
            }
        }
    }

    /**
     *
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class EntityViewEntityLiteralResolver implements ExpressionServiceSerializer<EntityLiteralResolver>, EntityLiteralResolver, Serializable {

        private final EntityLiteralResolver delegate;
        private final EntityViewManager entityViewManager;
        private final Map<String, Resolver> entityViewResolvers;

        public EntityViewEntityLiteralResolver(EntityLiteralResolver delegate, EntityViewManager entityViewManager, Map<String, Resolver> entityViewResolvers) {
            this.delegate = delegate;
            this.entityViewManager = entityViewManager;
            this.entityViewResolvers = entityViewResolvers;
        }

        @Override
        public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, EntityDomainType entityDomainType, Map<EntityDomainTypeAttribute, ? extends Literal> attributeValues) {
            Resolver resolver = entityViewResolvers.get(entityDomainType.getName());
            if (resolver != null) {
                Object viewId = resolver.getViewId(attributeValues);
                Object reference = entityViewManager.getReference(entityDomainType.getJavaType(), viewId);
                return new EntityViewResolvedLiteral(context.getExpressionService().getDomainModel(), entityDomainType, reference, viewId, resolver.isViewIdString, resolver.expressionPrefix, resolver.restrictionProvider);
            }
            return delegate == null ? null : delegate.resolveLiteral(context, entityDomainType, attributeValues);
        }

        @Override
        public <T> T serialize(ExpressionService expressionService, EntityLiteralResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            StringBuilder sb = new StringBuilder();
            if (delegate != null) {
                sb.append("{\"DelegatingEntityLiteralResolver\":[");
            }
            sb.append("{\"FixedEntityLiteralResolver\":[{");
            for (Map.Entry<String, Resolver> entry : entityViewResolvers.entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue().viewIdAttributeName).append("\",");
            }
            sb.setCharAt(sb.length() - 1, '}');
            sb.append("]}");
            if (delegate != null) {
                sb.append(",");
                sb.append(((ExpressionServiceSerializer<?>) delegate).serialize(expressionService, null, targetType, format, properties));
                sb.append("]}");
            }
            return (T) sb.toString();
        }
    }

    /**
     *
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class Resolver {

        final EntityDomainTypeAttribute idDomainAttribute;
        final String viewIdAttributeName;
        final boolean isViewIdString;
        final TypeAdapter typeAdapter;
        final String expressionPrefix;
        final EntityLiteralPersistenceRestrictionProvider restrictionProvider;

        public Resolver(EntityViewManager evm, EntityDomainType entityDomainType, ViewType<?> viewType, EntityLiteralPersistenceRestrictionProvider restrictionProvider) {
            MethodAttribute<?, ?> viewIdAttribute = viewType.getIdAttribute();
            this.viewIdAttributeName = viewIdAttribute.getName();
            this.isViewIdString = viewIdAttribute.getJavaType() == String.class;
            this.idDomainAttribute = entityDomainType.getAttribute(viewIdAttributeName);
            this.typeAdapter = idDomainAttribute.getMetadata(TypeAdapter.class);
            EntityType<?> entityType = evm.getService(Metamodel.class).entity(viewType.getEntityClass());
            StringBuilder sb = new StringBuilder(entityType.getName() + "[");
            ((MappingAttribute<?, ?>) viewIdAttribute).renderMapping("", evm, sb);
            sb.append(" = ");
            this.expressionPrefix = sb.toString();
            this.restrictionProvider = restrictionProvider;
        }

        public Object getViewId(Map<EntityDomainTypeAttribute, ?> attributeValues) {
            Literal literal = (Literal) attributeValues.get(idDomainAttribute);
            if (typeAdapter == null) {
                return literal.getValue();
            } else {
                return typeAdapter.toModelType(null, literal.getValue(), idDomainAttribute.getType());
            }
        }
    }
}
