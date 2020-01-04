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

import java.util.Map;

/**
 * A serializer for expressions that serializes to a serialization target.
 *
 * @param <T> The serialization target type
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionSerializer<T> {

    /**
     * Creates a serialization context based on the given context parameter map.
     *
     * @param contextParameters The context parameters
     * @return a new serialization context
     */
    public Context createContext(Map<String, Object> contextParameters);

    /**
     * Serializes the given expression without a serialization context to the given target.
     *
     * @param expression The expression to serialize
     * @param target The serialization target
     */
    public void serializeTo(Expression expression, T target);

    /**
     * Serializes the given expression based on the given serialization context to the given target.
     *
     * @param context The serialization context
     * @param expression The expression to serialize
     * @param target The serialization target
     */
    public void serializeTo(Context context, Expression expression, T target);

    /**
     * A serialization context that gives access to context parameters.
     *
     * @author Christian Beikov
     * @since 1.0.0
     */
    interface Context {
        /**
         * Returns the context parameter value for the given context parameter name or <code>null</code>.
         *
         * @param contextParameterName The context parameter name
         * @return the context parameter value for the given context parameter name or <code>null</code>
         */
        public Object getContextParameter(String contextParameterName);
    }

}
