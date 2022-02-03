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

package com.blazebit.expression.declarative.persistence;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.expression.spi.ResolvedLiteral;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.Serializable;

/**
 * The resolved literal for an entity constructor expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EntityResolvedLiteral implements ResolvedLiteral, Serializable {

    private final DomainModel domainModel;
    private final EntityDomainType entityDomainType;
    private final Object idValue;
    private final boolean isIdString;
    private final String expressionPrefix;
    private final EntityLiteralPersistenceRestrictionProvider entityLiteralRestrictionProvider;
    private transient String expression;

    /**
     * Creates a new entity resolved literal.
     *
     * @param domainModel The domain model
     * @param entityDomainType The entity domain type
     * @param idValue The entity id value
     * @param isIdString Whether the id is a string i.e. should be encoded as such in JPQL
     * @param expressionPrefix The JPQL expression prefix
     * @param restrictionProvider The restriction provider
     */
    public EntityResolvedLiteral(DomainModel domainModel, EntityDomainType entityDomainType, Object idValue, boolean isIdString, String expressionPrefix, EntityLiteralPersistenceRestrictionProvider restrictionProvider) {
        this.domainModel = domainModel;
        this.entityDomainType = entityDomainType;
        this.idValue = idValue;
        this.isIdString = isIdString;
        this.expressionPrefix = expressionPrefix;
        this.entityLiteralRestrictionProvider = restrictionProvider;
    }

    @Override
    public DomainType getType() {
        return entityDomainType;
    }

    @Override
    public Object getValue() {
        EntityManager entityManager = domainModel.getService(EntityManager.class);
        boolean created = false;
        if (entityManager == null) {
            entityManager = domainModel.getService(EntityManagerFactory.class).createEntityManager();
            created = true;
        }
        try {
            return entityManager.getReference(entityDomainType.getJavaType(), idValue);
        } finally {
            if (created) {
                entityManager.close();
            }
        }
    }

    @Override
    public String toString() {
        String expression = this.expression;
        if (expression == null) {
            StringBuilder sb = new StringBuilder(expressionPrefix + 20);
            if (isIdString) {
                sb.append('\'');
                String value = (String) idValue;
                for (int i = 0; i < value.length(); i++) {
                    final char c = value.charAt(i);
                    if (c == '\'') {
                        sb.append('\'');
                    }
                    sb.append(c);
                }
                sb.append('\'');
            } else {
                sb.append(idValue);
            }
            String restriction = entityLiteralRestrictionProvider.getRestriction(domainModel, entityDomainType);
            if (restriction != null && !restriction.isEmpty()) {
                sb.append(" AND ").append(restriction);
            }
            sb.append(']');
            this.expression = expression = sb.toString();
        }
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityResolvedLiteral)) {
            return false;
        }

        EntityResolvedLiteral that = (EntityResolvedLiteral) o;

        if (!entityDomainType.equals(that.entityDomainType)) {
            return false;
        }
        return idValue.equals(that.idValue);
    }

    @Override
    public int hashCode() {
        int result = entityDomainType.hashCode();
        result = 31 * result + idValue.hashCode();
        return result;
    }
}
