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
 * The predicate wrapper for boolean expressions.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionPredicate extends AbstractPredicate {
    private final Expression expression;

    /**
     * Constructs a new possibly negated predicate wrapper for the given expression returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param expression The boolean expression
     * @param negated <code>true</code> if the predicate should be negated, <code>false</code> otherwise
     */
    public ExpressionPredicate(DomainType type, Expression expression, boolean negated) {
        super(type, negated);
        this.expression = expression;
    }

    /**
     * Returns the boolean expression.
     *
     * @return the boolean expression
     */
    public Expression getExpression() {
        return expression;
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
        ExpressionPredicate that = (ExpressionPredicate) o;
        return Objects.equals(expression, that.expression);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), expression);
    }
}
