/*
 * Copyright 2019 - 2020 Blazebit.
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

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.ResolvedLiteral;

import java.io.Serializable;

/**
 * The resolved literal for an entity view constructor expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EntityViewResolvedLiteral implements ResolvedLiteral, Serializable {

    private final EntityDomainType entityDomainType;
    private final Object value;

    /**
     * Creates a new entity view resolved literal.
     *
     * @param entityDomainType The entity view domain type
     * @param value The entity view reference
     */
    public EntityViewResolvedLiteral(EntityDomainType entityDomainType, Object value) {
        this.entityDomainType = entityDomainType;
        this.value = value;
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
