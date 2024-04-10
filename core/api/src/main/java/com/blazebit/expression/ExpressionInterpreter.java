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

/**
 * An interpreter for expressions based on a set of root variable object assignments.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionInterpreter {

    /**
     * Evaluates the given expression to the call site defined type with an empty interpreter context.
     *
     * @param expression The expression to evaluate
     * @param <T> The result type
     * @return The evaluation result
     */
    public default <T> T evaluate(Expression expression) {
        return evaluate(expression, null);
    }

    /**
     * Evaluates the given expression to the call site defined type with an empty interpreter context and converts it using the {@link com.blazebit.expression.spi.TypeAdapter} defined for the returned attribute.
     *
     * @param expression The expression to evaluate
     * @param <T> The result type
     * @return The evaluation result
     */
    public default <T> T evaluateAsModelType(Expression expression) {
        return evaluateAsModelType(expression, null);
    }

    /**
     * Evaluates the given expression to the given type, converting the result if necessary, with an empty interpreter context.
     *
     * @param expression The expression to evaluate
     * @param resultClass The requested result class
     * @param <T> The result type
     * @return The evaluation result
     */
    public default <T> T evaluateAs(Expression expression, Class<T> resultClass) {
        return evaluateAs(expression, null, resultClass);
    }

    /**
     * Evaluates the given expression to the call site defined type based on the given interpreter context.
     *
     * @param expression The expression to evaluate
     * @param interpreterContext The interpreter context to evaluate the expression against
     * @param <T> The result type
     * @return The evaluation result
     */
    public <T> T evaluate(Expression expression, Context interpreterContext);

    /**
     * Evaluates the given expression to the call site defined type based on the given interpreter context and converts it using the {@link com.blazebit.expression.spi.TypeAdapter} defined for the returned attribute.
     *
     * @param expression The expression to evaluate
     * @param interpreterContext The interpreter context to evaluate the expression against
     * @param <T> The result type
     * @return The evaluation result
     */
    public <T> T evaluateAsModelType(Expression expression, Context interpreterContext);

    /**
     * Evaluates the given expression to the given type, converting the result if necessary, based on the given interpreter context.
     *
     * @param expression The expression to evaluate
     * @param interpreterContext The interpreter context to evaluate the expression against
     * @param resultClass The requested result class
     * @param <T> The result type
     * @return The evaluation result
     */
    public <T> T evaluateAs(Expression expression, Context interpreterContext, Class<T> resultClass);

    /**
     * Evaluates the given predicate with an empty interpreter context.
     *
     * @param expression The predicate to evaluate
     * @return The evaluation result
     */
    public default Boolean evaluate(Predicate expression) {
        return evaluate(expression, null);
    }

    /**
     * Evaluates the given predicate based on the given interpreter context.
     *
     * @param expression The predicate to evaluate
     * @param interpreterContext The interpreter context to evaluate the predicate against
     * @return The evaluation result
     */
    public Boolean evaluate(Predicate expression, Context interpreterContext);

    /**
     * An interpreter context that gives access to root variable domain type mappings, root variable object assignments and configuration properties.
     *
     * @author Christian Beikov
     * @since 1.0.0
     */
    public interface Context {

        /**
         * Returns the expression service.
         *
         * @return the expression service
         */
        public ExpressionService getExpressionService();

        /**
         * Returns the property value for the given key or <code>null</code>.
         *
         * @param key The property key
         * @param <X> The return type
         * @return the property value or <code>null</code>
         */
        public <X> X getProperty(String key);

        /**
         * Sets the given property value for the given key.
         *
         * @param key The key for which to set the value
         * @param value The property value to set
         */
        public void setProperty(String key, Object value);

        /**
         * Returns the object assignment of the root variable with the given name or <code>null</code>.
         *
         * @param alias The root variable name
         * @param <X> The return type
         * @return the object assignment or <code>null</code>
         */
        public <X> X getRoot(String alias);

    }
}
