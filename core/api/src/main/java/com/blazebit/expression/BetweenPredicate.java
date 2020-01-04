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

package com.blazebit.expression;


import com.blazebit.domain.runtime.model.DomainType;

import java.util.Objects;

/**
 * The between predicate which is semantically equivalent to <code>left &gt;= lower AND left &lt;= upper</code>.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class BetweenPredicate extends AbstractPredicate {
    private final ArithmeticExpression left;
    private final ArithmeticExpression upper;
    private final ArithmeticExpression lower;

    /**
     * Constructs a new between predicate for the given arithmetic expressions returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left or reference expression
     * @param upper The upper bound(inclusive)
     * @param lower The lower bound(inclusive)
     */
    public BetweenPredicate(DomainType type, ArithmeticExpression left, ArithmeticExpression upper, ArithmeticExpression lower) {
        super(type);
        this.left = left;
        this.upper = upper;
        this.lower = lower;
    }

    /**
     * Returns the left or reference expression.
     *
     * @return the left or reference expression
     */
    public ArithmeticExpression getLeft() {
        return left;
    }

    /**
     * Returns the upper bound expression.
     *
     * @return the upper bound expression
     */
    public ArithmeticExpression getUpper() {
        return upper;
    }

    /**
     * Returns the lower bound expression.
     *
     * @return the lower bound expression
     */
    public ArithmeticExpression getLower() {
        return lower;
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
        BetweenPredicate that = (BetweenPredicate) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(upper, that.upper) &&
                Objects.equals(lower, that.lower);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), left, upper, lower);
    }
}
