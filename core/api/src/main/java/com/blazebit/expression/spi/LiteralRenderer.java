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

package com.blazebit.expression.spi;

import com.blazebit.expression.ExpressionSerializer;

/**
 * A StringBuilder serializer for domain literals that is registered as metadata on a domain type.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface LiteralRenderer {

    /**
     * Serializes the given resolved literal value to the given StringBuilder.
     *
     * @param context The expression serializer context
     * @param value The resolved literal value to serialize
     * @param sb The StringBuilder to serialize the literal to
     */
    public void render(ExpressionSerializer.Context context, Object value, StringBuilder sb);
}
