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

import com.blazebit.domain.runtime.model.DomainType;

/**
 * The emptyness predicate that checks if the expression evaluates to an empty value.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class IsEmptyPredicate extends AbstractPredicate {
    private final Expression left;
    private final int hash;

    /**
     * Constructs a new possibly negated nullness predicate for the given expressions returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left or reference expression
     */
    public IsEmptyPredicate(DomainType type, Expression left) {
        super(type);
        this.left = left;
        this.hash = computeHashCode();
    }

    /**
     * Constructs a new possibly negated nullness predicate for the given expressions returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left or reference expression
     * @param negated <code>true</code> if the predicate should be negated, <code>false</code> otherwise
     */
    public IsEmptyPredicate(DomainType type, Expression left, boolean negated) {
        super(type, negated);
        this.left = left;
        this.hash = computeHashCode();
    }

    /**
     * Returns the left or reference expression.
     *
     * @return the left or reference expression
     */
    public Expression getLeft() {
        return left;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IsEmptyPredicate negated() {
        return new IsEmptyPredicate(getType(), left, !isNegated());
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
        IsEmptyPredicate that = (IsEmptyPredicate) o;
        return isNegated() == that.isNegated()
            && getType().equals(that.getType())
            && left.equals(that.left);
    }

    private int computeHashCode() {
        int result = isNegated() ? 1 : 0;
        result = 31 * result + left.hashCode();
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
