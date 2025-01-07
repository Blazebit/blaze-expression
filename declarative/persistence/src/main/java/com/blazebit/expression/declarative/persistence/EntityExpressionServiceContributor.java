/*
 * Copyright 2019 - 2025 Blazebit.
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
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.ExpressionServiceBuilder;
import com.blazebit.expression.Literal;
import com.blazebit.expression.spi.EntityLiteralResolver;
import com.blazebit.expression.spi.ExpressionServiceContributor;
import com.blazebit.expression.spi.ExpressionServiceSerializer;
import com.blazebit.expression.spi.ResolvedLiteral;
import com.blazebit.expression.spi.TypeAdapter;
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
@ServiceProvider(ExpressionServiceContributor.class)
public class EntityExpressionServiceContributor implements ExpressionServiceContributor {

    /**
     * The default entity literal capable mode for domain type that are entities.
     * Valid values are <code>none</code>, <code>all</code> or the default <code>annotated</code>.
     */
    public static final String ENTITY_LITERAL_CAPABLE_MODE = "com.blazebit.expression.declarative.persistence.entity_literal_capable_mode";

    @Override
    public void contribute(ExpressionServiceBuilder expressionServiceBuilder) {
        DomainModel domainModel = expressionServiceBuilder.getDomainModel();
        CriteriaBuilderFactory criteriaBuilderFactory = domainModel.getService(CriteriaBuilderFactory.class);
        Mode mode = Mode.of((String) domainModel.getProperty(ENTITY_LITERAL_CAPABLE_MODE));
        if (criteriaBuilderFactory == null || mode == Mode.NONE) {
            return;
        }

        // Find out the domain types that are entity types so that we can register them in an entity literal resolver
        Map<String, Resolver> entityResolvers = new HashMap<>();
        Metamodel metamodel = criteriaBuilderFactory.getService(Metamodel.class);
        for (DomainType domainType : domainModel.getTypes().values()) {
            if (domainType instanceof EntityDomainType) {
                if (domainType.getJavaType() != null) {
                    EntityLiteralPersistenceRestrictionProvider restrictionProvider = domainType.getMetadata(EntityLiteralPersistenceRestrictionProvider.class);
                    if (restrictionProvider != null || mode == Mode.ALL) {
                        EntityType<?> entityType;
                        try {
                            entityType = metamodel.entity(domainType.getJavaType());
                        } catch (IllegalArgumentException ex) {
                            // ignore non-entity types
                            continue;
                        }
                        entityResolvers.put(domainType.getName(), new Resolver((EntityDomainType) domainType, entityType, restrictionProvider));
                    }
                }
            }
        }

        if (!entityResolvers.isEmpty()) {
            expressionServiceBuilder.withEntityLiteralResolver(new EntityEntityLiteralResolver(expressionServiceBuilder.getEntityLiteralResolver(), entityResolvers));
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
    private static class EntityEntityLiteralResolver implements ExpressionServiceSerializer<EntityLiteralResolver>, EntityLiteralResolver, Serializable {

        private final EntityLiteralResolver delegate;
        private final Map<String, Resolver> entityResolvers;

        public EntityEntityLiteralResolver(EntityLiteralResolver delegate, Map<String, Resolver> entityResolvers) {
            this.delegate = delegate;
            this.entityResolvers = entityResolvers;
        }

        @Override
        public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, EntityDomainType entityDomainType, Map<EntityDomainTypeAttribute, ? extends Literal> attributeValues) {
            Resolver resolver = entityResolvers.get(entityDomainType.getName());
            if (resolver != null) {
                return new EntityResolvedLiteral(context.getExpressionService().getDomainModel(), entityDomainType, resolver.getEntityId(attributeValues), resolver.isIdString, resolver.expressionPrefix, resolver.restrictionProvider);
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
            for (Map.Entry<String, Resolver> entry : entityResolvers.entrySet()) {
                sb.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue().idAttributeName).append("\",");
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
        final String idAttributeName;
        final boolean isIdString;
        final TypeAdapter typeAdapter;
        final String expressionPrefix;
        final EntityLiteralPersistenceRestrictionProvider restrictionProvider;

        public Resolver(EntityDomainType entityDomainType, EntityType<?> entityType, EntityLiteralPersistenceRestrictionProvider restrictionProvider) {
            SingularAttribute<?, ?> idAttribute = null;
            try {
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
            } catch (IllegalArgumentException ex) {
                // Ignore
            }
            if (idAttribute == null) {
                throw new IllegalArgumentException("Couldn't determine the id attribute for the entity type: " + entityType.getName());
            }
            this.idAttributeName = idAttribute.getName();
            this.isIdString = idAttribute.getJavaType() == String.class;
            this.idDomainAttribute = entityDomainType.getAttribute(idAttributeName);
            this.typeAdapter = idDomainAttribute.getMetadata(TypeAdapter.class);
            this.expressionPrefix =  entityType.getName() + "[" + idAttributeName + " = ";
            this.restrictionProvider = restrictionProvider;

        }

        public Object getEntityId(Map<EntityDomainTypeAttribute, ?> attributeValues) {
            Literal literal = (Literal) attributeValues.get(idDomainAttribute);
            if (typeAdapter == null) {
                return literal.getValue();
            } else {
                return typeAdapter.toModelType(null, literal.getValue(), idDomainAttribute.getType());
            }
        }
    }
}
