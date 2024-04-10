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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.spi.ResolvedLiteral;

import java.util.Map;

/**
 * An entity literal expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class EntityLiteral extends Literal {

    private final Map<EntityDomainTypeAttribute, ? extends Literal> attributeValues;

    /**
     * Creates a new entity literal expression from the given entity attributes values and resolved literal.
     *
     * @param attributeValues The entity attribute value
     * @param resolvedLiteral The resolved literal
     */
    public EntityLiteral(Map<EntityDomainTypeAttribute, ? extends Literal> attributeValues, ResolvedLiteral resolvedLiteral) {
        super(resolvedLiteral);
        this.attributeValues = attributeValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EntityDomainType getType() {
        return (EntityDomainType) super.getType();
    }

    /**
     * Returns the entity attribute value.
     *
     * @return the entity attribute value
     */
    public Map<EntityDomainTypeAttribute, ? extends Literal> getAttributeValues() {
        return attributeValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T accept(ResultVisitor<T> visitor) {
        return visitor.visit(this);
    }

}
