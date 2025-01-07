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

package com.blazebit.expression.spi;

import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ExpressionInterpreter;

/**
 * An interpreter for dereferencing entity attributes on a value that is registered as metadata on a domain type.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface AttributeAccessor {

    /**
     * Interprets the entity attribute dereference operation as applied on the given value.
     *
     * @param context The expression interpreter context
     * @param value The value on which to dereference the attribute
     * @param attribute The entity attribute to dereference
     * @return the interpretation result
     */
    public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute);
}
