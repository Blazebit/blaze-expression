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
 * A base implementation for predicates.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public abstract class AbstractPredicate extends AbstractExpression implements Predicate {
    private final boolean negated;

    /**
     * Constructs an abstract predicate producing the given domain type as result type.
     *
     * @param type The result type of the predicate
     */
    public AbstractPredicate(DomainType type) {
        super(type);
        this.negated = false;
    }

    /**
     * Constructs an abstract predicate producing the given domain type as result type and negated as given by the boolean.
     *
     * @param type The result type of the predicate
     * @param negated Whether the predicate should be negated
     */
    public AbstractPredicate(DomainType type, boolean negated) {
        super(type);
        this.negated = negated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNegated() {
        return this.negated;
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
        AbstractPredicate that = (AbstractPredicate) o;
        return isNegated() == that.isNegated()
            && getType().equals(that.getType());
    }

    @Override
    public int hashCode() {
        return negated ? 1 : 0;
    }
}
