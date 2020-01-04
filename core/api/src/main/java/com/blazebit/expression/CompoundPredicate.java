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

/**
 * An conjunction or disjunction of multiple predicates.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class CompoundPredicate extends AbstractPredicate {

    private final boolean conjunction;
    private final List<Predicate> predicates;

    /**
     * Creates a new compound predicate from the given predicates as conjunction or disjunction returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param predicates The predicates
     * @param conjunction <code>true</code> for conjunction, <code>false</code> for disjunction
     */
    public CompoundPredicate(DomainType type, List<Predicate> predicates, boolean conjunction) {
        this(type, predicates, conjunction, false);
    }

    /**
     * Creates a new possibly negated compound predicate from the given predicates as conjunction or disjunction returning a result of the given domain type.
     *
     * @param type The result domain type
     * @param predicates The predicates
     * @param conjunction <code>true</code> for conjunction, <code>false</code> for disjunction
     * @param negated <code>true</code> if the predicate should be negated, <code>false</code> otherwise
     */
    public CompoundPredicate(DomainType type, List<Predicate> predicates, boolean conjunction, boolean negated) {
        super(type, negated);
        this.predicates = predicates;
        this.conjunction = conjunction;
    }

    /**
     * Returns <code>true</code> if this is a conjunction, <code>false</code> otherwise.
     *
     * @return <code>true</code> if this is a conjunction, <code>false</code> otherwise
     */
    public boolean isConjunction() {
        return conjunction;
    }

    /**
     * Returns the predicates that are part of this compound predicate.
     *
     * @return the predicates that are part of this compound predicate
     */
    public List<Predicate> getPredicates() {
        return predicates;
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
        if (!(o instanceof CompoundPredicate)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        CompoundPredicate that = (CompoundPredicate) o;

        if (conjunction != that.conjunction) {
            return false;
        }
        return getPredicates().equals(that.getPredicates());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (conjunction ? 1 : 0);
        result = 31 * result + getPredicates().hashCode();
        return result;
    }
}
