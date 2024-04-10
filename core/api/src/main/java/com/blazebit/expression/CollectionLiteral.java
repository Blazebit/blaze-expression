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

import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.expression.spi.ResolvedLiteral;

import java.util.Collection;

/**
 * A collection literal expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class CollectionLiteral extends Literal {

    private final Collection<? extends Literal> values;

    /**
     * Creates a new collection literal expression from the given collection values and resolved literal.
     *
     * @param values The collection values
     * @param resolvedLiteral The resolved literal
     */
    public CollectionLiteral(Collection<? extends Literal> values, ResolvedLiteral resolvedLiteral) {
        super(resolvedLiteral);
        this.values = values;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CollectionDomainType getType() {
        return (CollectionDomainType) super.getType();
    }

    /**
     * Returns the collection values.
     *
     * @return the collection values
     */
    public Collection<? extends Literal> getValues() {
        return values;
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
