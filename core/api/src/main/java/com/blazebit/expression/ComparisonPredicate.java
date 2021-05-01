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
 * A predicate for doing relational or equality comparisons between arithmetic expressions.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class ComparisonPredicate extends AbstractPredicate {
    private final ArithmeticExpression left;
    private final ArithmeticExpression right;
    private final ComparisonOperator operator;
    private final int hash;

    /**
     * Creates a new comparison predicate from the given operands and the given operator returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left operand
     * @param right The right operand
     * @param operator The operator
     */
    public ComparisonPredicate(DomainType type, ArithmeticExpression left, ArithmeticExpression right, ComparisonOperator operator) {
        super(type);
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.hash = computeHashCode();
    }

    /**
     * Creates a new comparison predicate from the given operands and the given operator returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left operand
     * @param right The right operand
     * @param operator The operator
     * @param negated <code>true</code> if the predicate should be negated, <code>false</code> otherwise
     */
    public ComparisonPredicate(DomainType type, ArithmeticExpression left, ArithmeticExpression right, ComparisonOperator operator, boolean negated) {
        super(type, negated);
        this.left = left;
        this.right = right;
        this.operator = operator;
        this.hash = computeHashCode();
    }

    /**
     * Returns the left operand.
     *
     * @return the left operand
     */
    public ArithmeticExpression getLeft() {
        return left;
    }

    /**
     * Returns the right operand.
     *
     * @return the right operand
     */
    public ArithmeticExpression getRight() {
        return right;
    }

    /**
     * Returns the comparison operator.
     *
     * @return the comparison operator
     */
    public ComparisonOperator getOperator() {
        return operator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComparisonPredicate negated() {
        return new ComparisonPredicate(getType(), left, right, operator, !isNegated());
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
        ComparisonPredicate that = (ComparisonPredicate) o;
        return isNegated() == that.isNegated()
            && getType().equals(that.getType())
            && left.equals(that.left)
            && right.equals(that.right)
            && operator == that.operator;
    }

    private int computeHashCode() {
        int result = isNegated() ? 1 : 0;
        result = 31 * result + left.hashCode();
        result = 31 * result + right.hashCode();
        result = 31 * result + operator.hashCode();
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
