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

package com.blazebit.expression.persistence;

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.spi.DomainFunctionArgumentRenderers;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface FunctionRenderer {

    /**
     * Returns whether the renderer renders a predicate or an expression.
     *
     * @return whether a predicate or expresion is rendered
     */
    default boolean rendersPredicate() {
        return false;
    }

    /**
     * Renders the given domain function with the given return type and arguments to the given StringBuilder.
     * @param function The domain function
     * @param returnType The function return type
     * @param argumentRenderers The argument renderers for the arguments
     * @param sb The StringBuilder to render to
     * @param serializer The serializer
     */
    void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer);

    /**
     * Returns a function renderer that renders a function as builtin function.
     *
     * @param persistenceFunctionName The builtin function name
     * @return the function renderer
     */
    static FunctionRenderer builtin(String persistenceFunctionName) {
        return (FunctionRenderer & Serializable) (function, returnType, argumentRenderers, sb, serializer) -> {
            sb.append(persistenceFunctionName);
            if (argumentRenderers.assignedArguments() != 0) {
                sb.append(", ");
                argumentRenderers.renderArguments(sb);
            }
            sb.append(')');
        };
    }

    /**
     * Returns a function renderer that renders a function with the JPQL function wrapper.
     *
     * @param persistenceFunctionName The function name
     * @return the function renderer
     */
    static FunctionRenderer function(String persistenceFunctionName) {
        return (FunctionRenderer & Serializable) (function, returnType, argumentRenderers, sb, serializer) -> {
            sb.append("FUNCTION('").append(persistenceFunctionName).append('\'');
            if (argumentRenderers.assignedArguments() != 0) {
                sb.append(", ");
                argumentRenderers.renderArguments(sb);
            }
            sb.append(')');
        };
    }

}
