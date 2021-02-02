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

import com.blazebit.domain.declarative.spi.ServiceProvider;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.ResolvedLiteral;

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

    private final EntityDomainType entityDomainType;
    private final ServiceProvider<?> userServiceProvider;
    private final Object idValue;

    /**
     * Creates a new entity resolved literal.
     *
     * @param entityDomainType The entity domain type
     * @param userServiceProvider The service provider
     * @param idValue The entity id value
     */
    public EntityResolvedLiteral(EntityDomainType entityDomainType, ServiceProvider<?> userServiceProvider, Object idValue) {
        this.entityDomainType = entityDomainType;
        this.userServiceProvider = userServiceProvider;
        this.idValue = idValue;
    }

    @Override
    public DomainType getType() {
        return entityDomainType;
    }

    @Override
    public Object getValue() {
        EntityManager entityManager = userServiceProvider.getService(EntityManager.class);
        boolean created = false;
        if (entityManager == null) {
            entityManager = userServiceProvider.getService(EntityManagerFactory.class).createEntityManager();
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
