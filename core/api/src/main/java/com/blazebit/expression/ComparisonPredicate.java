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
 * A predicate for doing relational or equality comparisons between arithmetic expressions.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ComparisonPredicate extends AbstractPredicate {
    private final ArithmeticExpression left;
    private final ArithmeticExpression right;
    private final ComparisonOperator operator;

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
        if (!super.equals(o)) {
            return false;
        }
        ComparisonPredicate that = (ComparisonPredicate) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(right, that.right) &&
                operator == that.operator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), left, right, operator);
    }
}
