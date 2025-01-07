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
 * A from item to represent a data source.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class FromItem extends FromNode {
    private final List<Join> joins;

    /**
     * Creates a new from item.
     *
     * @param type The domain type
     * @param alias The alias
     * @param joins The joins
     */
    public FromItem(DomainType type, String alias, List<Join> joins) {
        super( type, alias );
        this.joins = joins;
    }

    /**
     * Returns the joins of this from item.
     *
     * @return the joins of this from item
     */
    public List<Join> getJoins() {
        return joins;
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
