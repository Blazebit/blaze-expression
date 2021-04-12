/*
 * Copyright 2019 - 2021 Blazebit.
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

import java.util.Collections;
import java.util.Map;

/**
 * A compiler for expression strings based on a domain model.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionCompiler {

    /**
     * Creates a compile context based on the given root variable domain type mapping.
     *
     * @param rootDomainTypes The root variable domain type mapping
     * @return a new compile context
     */
    public Context createContext(Map<String, DomainType> rootDomainTypes);

    /**
     * Creates and compiles the given expression string with an empty compile context.
     *
     * @param expressionString The expression string to compile
     * @return The compiled expression
     */
    public default Expression createExpression(String expressionString) {
        return createExpression(expressionString, createContext(Collections.emptyMap()));
    }

    /**
     * Creates and compiles the given expression string with the given compile context.
     *
     * @param expressionString The expression string to compile
     * @param compileContext The compile context to use
     * @return The compiled expression
     */
    public Expression createExpression(String expressionString, Context compileContext);

    /**
     * Creates and compiles the given predicate string with an empty compile context.
     *
     * @param expressionString The predicate string to compile
     * @return The compiled predicate
     */
    public default Predicate createPredicate(String expressionString) {
        return createPredicate(expressionString, createContext(Collections.emptyMap()));
    }

    /**
     * Creates and compiles the given predicate string with the given compile context.
     *
     * @param expressionString The predicate string to compile
     * @param compileContext The compile context to use
     * @return The compiled predicate
     */
    public Predicate createPredicate(String expressionString, Context compileContext);

    /**
     * Creates and compiles the given expression string with an empty compile context.
     *
     * @param expressionString The expression string to compile
     * @return The compiled expression
     */
    public default Expression createExpressionOrPredicate(String expressionString) {
        return createExpressionOrPredicate(expressionString, createContext(Collections.emptyMap()));
    }

    /**
     * Creates and compiles the given expression string with the given compile context.
     *
     * @param expressionString The expression string to compile
     * @param compileContext The compile context to use
     * @return The compiled expression
     */
    public Expression createExpressionOrPredicate(String expressionString, Context compileContext);

    /**
     * Creates and compiles the given template expression string with an empty compile context.
     *
     * @param templateString The template expression string to compile
     * @return The compiled expression
     */
    public default Expression createTemplateExpression(String templateString) {
        return createTemplateExpression(templateString, createContext(Collections.emptyMap()));
    }

    /**
     * Creates and compiles the given template expression string with the given compile context.
     *
     * @param templateString The template expression string to compile
     * @param compileContext The compile context to use
     * @return The compiled expression
     */
    public Expression createTemplateExpression(String templateString, Context compileContext);

    /**
     * A compiler context that returns the domain type for available root variables.
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
         * Returns the domain type of the root variable with the given name or <code>null</code>.
         *
         * @param alias The root variable name
         * @return the domain type or <code>null</code>
         */
        public DomainType getRootDomainType(String alias);

    }
}
