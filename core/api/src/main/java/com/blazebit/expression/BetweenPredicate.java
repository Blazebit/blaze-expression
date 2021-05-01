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

/**
 * The between predicate which is semantically equivalent to <code>left &gt;= lower AND left &lt;= upper</code>.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class BetweenPredicate extends AbstractPredicate {
    private final ArithmeticExpression left;
    private final ArithmeticExpression upper;
    private final ArithmeticExpression lower;
    private final int hash;

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
        this.hash = computeHashCode();
    }

    /**
     * Constructs a new between predicate for the given arithmetic expressions returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left or reference expression
     * @param upper The upper bound(inclusive)
     * @param lower The lower bound(inclusive)
     * @param negated <code>true</code> if the predicate should be negated, <code>false</code> otherwise
     */
    public BetweenPredicate(DomainType type, ArithmeticExpression left, ArithmeticExpression upper, ArithmeticExpression lower, boolean negated) {
        super(type, negated);
        this.left = left;
        this.upper = upper;
        this.lower = lower;
        this.hash = computeHashCode();
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
    public BetweenPredicate negated() {
        return new BetweenPredicate(getType(), left, upper, lower, !isNegated());
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
        BetweenPredicate that = (BetweenPredicate) o;
        return isNegated() == that.isNegated()
            && getType().equals(that.getType())
            && left.equals(that.left)
            && upper.equals(that.upper)
            && lower.equals(that.lower);
    }

    private int computeHashCode() {
        int result = isNegated() ? 1 : 0;
        result = 31 * result + left.hashCode();
        result = 31 * result + upper.hashCode();
        result = 31 * result + lower.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }
}
