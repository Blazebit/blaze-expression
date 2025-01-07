/*
 * Copyright 2019 - 2025 Blazebit.
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

import java.util.List;

import com.blazebit.domain.runtime.model.DomainType;

/**
 * A query to filter and select data from sources.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class Query extends AbstractExpression {

    private final List<Expression> selectItems;
    private final boolean distinct;
    private final List<FromItem> fromItems;
    private final Predicate wherePredicate;

    /**
     * Creates a new query.
     *
     * @param type The query result domain type
     * @param selectItems The select items
     * @param distinct Whether the query should produce distinct results
     * @param fromItems The from items
     * @param wherePredicate The where predicate
     */
    public Query(
            DomainType type,
            List<Expression> selectItems,
            boolean distinct,
            List<FromItem> fromItems,
            Predicate wherePredicate) {
        super( type );
        this.selectItems = selectItems;
        this.distinct = distinct;
        this.fromItems = fromItems;
        this.wherePredicate = wherePredicate;
    }

    /**
     * Returns the select items of this query.
     *
     * @return the select items of this query
     */
    public List<Expression> getSelectItems() {
        return selectItems;
    }

    /**
     * Returns whether this query should produce distinct results.
     *
     * @return whether this query should produce distinct results
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * Returns the from items of this query.
     *
     * @return the from items of this query
     */
    public List<FromItem> getFromItems() {
        return fromItems;
    }

    /**
     * Returns the where predicate of this query, or {@code null} if it doesn't have a predicate.
     *
     * @return the where predicate of this query
     */
    public Predicate getWherePredicate() {
        return wherePredicate;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit( this );
    }

    @Override
    public <T> T accept(ResultVisitor<T> visitor) {
        return visitor.visit( this );
    }
}
