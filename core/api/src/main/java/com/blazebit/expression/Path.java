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
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;

import java.util.List;

/**
 * A path expression dereferencing entity attributes of an entity type.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class Path implements ArithmeticExpression {
    private final String alias;
    private final ArithmeticExpression base;
    private final List<EntityDomainTypeAttribute> attributes;
    private final DomainType type;

    /**
     * Creates a new path expression from the given root alias and attribute dereference chain returning a result of the given domain type.
     *
     * @param alias The root alias
     * @param attributes The entity attribute dereference chain
     * @param type The result domain type
     */
    public Path(String alias, List<EntityDomainTypeAttribute> attributes, DomainType type) {
        this.alias = alias;
        this.base = null;
        this.attributes = attributes;
        this.type = type;
    }

    /**
     * Creates a new path expression from the given root alias and attribute dereference chain returning a result of the given domain type.
     *
     * @param base The base expression
     * @param attributes The entity attribute dereference chain
     * @param type The result domain type
     */
    public Path(ArithmeticExpression base, List<EntityDomainTypeAttribute> attributes, DomainType type) {
        this.alias = null;
        this.base = base;
        this.attributes = attributes;
        this.type = type;
    }

    /**
     * Returns the root alias. May be null if a base expression is set.
     *
     * @return the root alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Returns the base expression. May be null if a root alias is set.
     *
     * @return the base expression
     */
    public ArithmeticExpression getBase() {
        return base;
    }

    /**
     * Returns the entity attribute dereference chain.
     *
     * @return the entity attribute dereference chain
     */
    public List<EntityDomainTypeAttribute> getAttributes() {
        return attributes;
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
        if (!(o instanceof Path)) {
            return false;
        }

        Path path = (Path) o;

        if (getAlias() != null ? !getAlias().equals(path.getAlias()) : path.getAlias() != null) {
            return false;
        }
        if (getBase() != null ? !getBase().equals(path.getBase()) : path.getBase() != null) {
            return false;
        }
        if (!getAttributes().equals(path.getAttributes())) {
            return false;
        }
        return getType().equals(path.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = getAlias() != null ? getAlias().hashCode() : 0;
        result = 31 * result + (getBase() != null ? getBase().hashCode() : 0);
        result = 31 * result + getAttributes().hashCode();
        result = 31 * result + getType().hashCode();
        return result;
    }
}
