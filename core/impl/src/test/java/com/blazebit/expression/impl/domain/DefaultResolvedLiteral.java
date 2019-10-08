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
package com.blazebit.expression.impl.domain;

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.ResolvedLiteral;

import java.util.Objects;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DefaultResolvedLiteral implements ResolvedLiteral {
    private final DomainType type;
    private final Object value;

    public DefaultResolvedLiteral(DomainType type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public DomainType getType() {
        return type;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultResolvedLiteral that = (DefaultResolvedLiteral) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
