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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.spi.ResolvedLiteral;

/**
 * A literal expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class Literal implements ArithmeticExpression {

    private final ResolvedLiteral resolvedLiteral;
    private final int hash;

    /**
     * Creates a new literal expression from the given resolved literal.
     *
     * @param resolvedLiteral The resolved literal
     */
    public Literal(ResolvedLiteral resolvedLiteral) {
        this.resolvedLiteral = resolvedLiteral;
        this.hash = resolvedLiteral.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getType() {
        return resolvedLiteral.getType();
    }

    /**
     * Returns the resolved literal.
     *
     * @return the resolved literal
     */
    public ResolvedLiteral getResolvedLiteral() {
        return resolvedLiteral;
    }

    /**
     * Returns the resolved literal value.
     *
     * @return the resolved literal value
     */
    public Object getValue() {
        return resolvedLiteral.getValue();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Literal literal = (Literal) o;
        return resolvedLiteral.equals(literal.resolvedLiteral);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }
}
