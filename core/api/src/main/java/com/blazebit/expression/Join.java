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
 * A join between data sources.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class Join extends FromNode {

    private final JoinType joinType;
    private final Predicate joinPredicate;

    /**
     * Creates a new join.
     *
     * @param type The domain type
     * @param alias The alias
     * @param joinType The join type
     * @param joinPredicate The join predicate
     */
    public Join(DomainType type, String alias, JoinType joinType, Predicate joinPredicate) {
        super( type, alias );
        this.joinType = joinType;
        this.joinPredicate = joinPredicate;
    }

    /**
     * Returns the join type of this join.
     *
     * @return the join type of this join
     */
    public JoinType getJoinType() {
        return joinType;
    }

    /**
     * Returns the join predicate of this join.
     *
     * @return the join predicate of this join
     */
    public Predicate getJoinPredicate() {
        return joinPredicate;
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
