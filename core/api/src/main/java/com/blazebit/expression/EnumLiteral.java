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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.expression.spi.ResolvedLiteral;

/**
 * An enum literal expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class EnumLiteral extends Literal {

    private final EnumDomainTypeValue enumValue;

    /**
     * Creates a new enum literal expression from the given enum value and resolved literal.
     *
     * @param enumValue The enum value
     * @param resolvedLiteral The resolved literal
     */
    public EnumLiteral(EnumDomainTypeValue enumValue, ResolvedLiteral resolvedLiteral) {
        super(resolvedLiteral);
        this.enumValue = enumValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EnumDomainType getType() {
        return enumValue.getOwner();
    }

    /**
     * Returns the enum value.
     *
     * @return the enum value
     */
    public EnumDomainTypeValue getEnumValue() {
        return enumValue;
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
