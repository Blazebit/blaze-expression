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

package com.blazebit.expression.spi;

import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainType;

/**
 * An interface that gives access to the domain function argument values.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface DomainFunctionArguments {

    /**
     * Empty arguments.
     */
    DomainFunctionArguments EMPTY = new DomainFunctionArguments() {
        @Override
        public Object getValue(int position) {
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
    };

    /**
     * Returns the value of the argument at the given position.
     *
     * @param position The position
     * @return The value
     */
    Object getValue(int position);

    /**
     * Returns the value of the argument at the position of the given argument.
     *
     * @param domainFunctionArgument The domain function argument
     * @return The value
     */
    default Object getValue(DomainFunctionArgument domainFunctionArgument) {
        return getValue(domainFunctionArgument.getPosition());
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
}
