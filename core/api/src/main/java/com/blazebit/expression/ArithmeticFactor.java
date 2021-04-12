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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.DomainType;

import java.util.Objects;

/**
 * An arithmetic factor capturing the signum of an arithmetic expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class ArithmeticFactor extends AbstractExpression implements ArithmeticExpression {
    private final ArithmeticExpression expression;
    private final boolean invertSignum;

    /**
     * Creates a new arithmetic factor expression for the given arithmetic expression with the given signum returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param expression The arithmetic expression
     * @param invertSignum <code>true</code> for unary minus, <code>false</code> for unary plus
     */
    public ArithmeticFactor(DomainType type, ArithmeticExpression expression, boolean invertSignum) {
        super(type);
        this.expression = expression;
        this.invertSignum = invertSignum;
    }

    /**
     * Returns the arithmetic expression.
     *
     * @return the arithmetic expression
     */
    public ArithmeticExpression getExpression() {
        return expression;
    }

    /**
     * Returns whether the signum is inverted or not.
     *
     * @return <code>true</code> for unary minus, <code>false</code> for unary plus
     */
    public boolean isInvertSignum() {
        return invertSignum;
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
        if (!super.equals(o)) {
            return false;
        }
        ArithmeticFactor that = (ArithmeticFactor) o;
        return invertSignum == that.invertSignum &&
                Objects.equals(expression, that.expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expression, invertSignum);
    }
}
