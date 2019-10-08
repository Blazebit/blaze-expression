/*
 * Copyright 2019 Blazebit.
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
 * An arithmetic expression connecting two arithmetic expression operands with an arithmetic operator.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ChainingArithmeticExpression extends AbstractExpression implements ArithmeticExpression {
    private final ArithmeticExpression left;
    private final ArithmeticExpression right;
    private final ArithmeticOperatorType operator;

    /**
     * Creates a new expression from the given operands and the given operator returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param left The left operand
     * @param right The right operand
     * @param operator The operator
     */
    public ChainingArithmeticExpression(DomainType type, ArithmeticExpression left, ArithmeticExpression right, ArithmeticOperatorType operator) {
        super(type);
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
     * Returns the arithmetic operator.
     *
     * @return the arithmetic operator
     */
    public ArithmeticOperatorType getOperator() {
        return operator;
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
        ChainingArithmeticExpression that = (ChainingArithmeticExpression) o;
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