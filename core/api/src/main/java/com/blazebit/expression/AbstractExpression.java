/*
 * Copyright 2019 - 2022 Blazebit.
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
 * A base implementation for expressions.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public abstract class AbstractExpression implements Expression {
    private final DomainType type;

    /**
     * Constructs an abstract expression producing the given domain type as result type.
     *
     * @param type The result type of the expression
     */
    public AbstractExpression(DomainType type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainType getType() {
        return type;
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
        AbstractExpression that = (AbstractExpression) o;
        return type.equals(that.type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
