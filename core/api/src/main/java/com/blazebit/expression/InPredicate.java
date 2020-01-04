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

import java.util.List;
import java.util.Objects;

/**
 * The in predicate which is semantically equivalent to <code>left = item1 OR ... OR left = itemN</code>.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class InPredicate extends AbstractPredicate {
    private final ArithmeticExpression left;
    private final List<ArithmeticExpression> inItems;

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
        InPredicate that = (InPredicate) o;
        return Objects.equals(left, that.left) &&
                Objects.equals(inItems, that.inItems);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), left, inItems);
    }
}
