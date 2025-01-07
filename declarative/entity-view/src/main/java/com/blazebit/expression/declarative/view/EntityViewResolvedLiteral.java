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

package com.blazebit.expression.declarative.view;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.expression.declarative.persistence.EntityLiteralPersistenceRestrictionProvider;
import com.blazebit.expression.spi.ResolvedLiteral;

import java.io.Serializable;

/**
 * The resolved literal for an entity view constructor expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EntityViewResolvedLiteral implements ResolvedLiteral, Serializable {

    private final DomainModel domainModel;
    private final EntityDomainType entityDomainType;
    private final Object value;
    private final Object viewId;
    private final boolean isViewIdString;
    private final String expressionPrefix;
    private final EntityLiteralPersistenceRestrictionProvider entityLiteralRestrictionProvider;
    private transient String expression;

    /**
     * Creates a new entity view resolved literal.
     *
     * @param domainModel The domain model
     * @param entityDomainType The entity view domain type
     * @param value The entity view reference
     * @param viewId The entity view id
     * @param isViewIdString Whether the view id is a string i.e. should be encoded as such in JPQL
     * @param expressionPrefix The JPQL expression prefix
     * @param restrictionProvider The restriction provider
     */
    public EntityViewResolvedLiteral(DomainModel domainModel, EntityDomainType entityDomainType, Object value, Object viewId, boolean isViewIdString, String expressionPrefix, EntityLiteralPersistenceRestrictionProvider restrictionProvider) {
        this.domainModel = domainModel;
        this.entityDomainType = entityDomainType;
        this.value = value;
        this.viewId = viewId;
        this.isViewIdString = isViewIdString;
        this.expressionPrefix = expressionPrefix;
        this.entityLiteralRestrictionProvider = restrictionProvider;
    }

    @Override
    public DomainType getType() {
        return entityDomainType;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        String expression = this.expression;
        if (expression == null) {
            StringBuilder sb = new StringBuilder(expressionPrefix + 20);
            if (isViewIdString) {
                sb.append('\'');
                String value = (String) viewId;
                for (int i = 0; i < value.length(); i++) {
                    final char c = value.charAt(i);
                    if (c == '\'') {
                        sb.append('\'');
                    }
                    sb.append(c);
                }
                sb.append('\'');
            } else {
                sb.append(viewId);
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
        if (!(o instanceof EntityViewResolvedLiteral)) {
            return false;
        }

        EntityViewResolvedLiteral that = (EntityViewResolvedLiteral) o;

        if (!entityDomainType.equals(that.entityDomainType)) {
            return false;
        }
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        int result = entityDomainType.hashCode();
        result = 31 * result + getValue().hashCode();
        return result;
    }
}
