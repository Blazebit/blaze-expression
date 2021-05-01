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

package com.blazebit.expression.persistence;

import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.Expression;

/**
 * An interface that gives access to the domain function argument values.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface PersistenceDomainFunctionArgumentRenderers {

    /**
     * Empty arguments.
     */
    PersistenceDomainFunctionArgumentRenderers EMPTY = new PersistenceDomainFunctionArgumentRenderers() {
        @Override
        public Expression getExpression(int position) {
            return null;
        }

        @Override
        public DomainType getType(int position) {
            return null;
        }

        @Override
        public int assignedArguments() {
            return 0;
        }

        @Override
        public boolean renderArgument(StringBuilder sb, int position) {
            throw new IndexOutOfBoundsException(Integer.toString(position));
        }
    };

    /**
     * Returns the expression of the argument at the given position.
     *
     * @param position The position
     * @return The expression
     */
    Expression getExpression(int position);

    /**
     * Returns the expression of the argument at the position of the given argument.
     *
     * @param domainFunctionArgument The domain function argument
     * @return The expression
     */
    default Expression getExpression(DomainFunctionArgument domainFunctionArgument) {
        return getExpression(domainFunctionArgument.getPosition());
    }

    /**
     * Returns the actual type of the argument at the given position.
     *
     * @param position The position
     * @return The actual type
     */
    DomainType getType(int position);

    /**
     * Returns the actual of the argument at the position of the given argument.
     *
     * @param domainFunctionArgument The domain function argument
     * @return The actual type
     */
    default DomainType getType(DomainFunctionArgument domainFunctionArgument) {
        return getType(domainFunctionArgument.getPosition());
    }

    /**
     * Returns the amount of assigned arguments.
     *
     * @return the amount of assigned arguments
     */
    int assignedArguments();

    /**
     * Renders the argument with the given position to the given string builder.
     *
     * @param sb The string builder to render to
     * @param position The position
     * @return Whether the argument was constant
     */
    boolean renderArgument(StringBuilder sb, int position);

    /**
     * Renders all arguments to the given string builder.
     *
     * @param sb The string builder to render to
     */
    default void renderArguments(StringBuilder sb) {
        int size = assignedArguments();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                renderArgument(sb, i);
                sb.append(", ");
            }
            sb.setLength(sb.length() - 2);
        }
    }
}
