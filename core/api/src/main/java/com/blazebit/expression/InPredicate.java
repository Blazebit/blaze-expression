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

import java.util.List;

/**
 * The in predicate which is semantically equivalent to <code>left = item1 OR ... OR left = itemN</code>.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class InPredicate extends AbstractPredicate {
    private final ArithmeticExpression left;
    private final List<ArithmeticExpression> inItems;
    private final int hash;

    /**
     * Constructs a new possibly negated in predicate for the given expressions returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left or reference expression
     * @param inItems The in item expressions to check against
     */
    public InPredicate(DomainType type, ArithmeticExpression left, List<ArithmeticExpression> inItems) {
        super(type);
        this.left = left;
        this.inItems = inItems;
        this.hash = computeHashCode();
    }

    /**
     * Constructs a new possibly negated in predicate for the given expressions returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left or reference expression
     * @param inItems The in item expressions to check against
     * @param negated <code>true</code> if the predicate should be negated, <code>false</code> otherwise
     */
    public InPredicate(DomainType type, ArithmeticExpression left, List<ArithmeticExpression> inItems, boolean negated) {
        super(type, negated);
        this.left = left;
        this.inItems = inItems;
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
     * Returns the in item expressions.
     *
     * @return the in item expressions
     */
    public List<ArithmeticExpression> getInItems() {
        return inItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InPredicate negated() {
        return new InPredicate(getType(), left, inItems, !isNegated());
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
        InPredicate that = (InPredicate) o;
        return isNegated() == that.isNegated()
            && getType().equals(that.getType())
            && left.equals(that.left)
            && inItems.equals(that.inItems);
    }

    private int computeHashCode() {
        int result = isNegated() ? 1 : 0;
        result = 31 * result + left.hashCode();
        result = 31 * result + inItems.hashCode();
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
