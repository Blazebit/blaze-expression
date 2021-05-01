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

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainType;

import java.util.Map;

/**
 * A function invocation expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class FunctionInvocation extends AbstractExpression implements ArithmeticExpression {
    private final DomainFunction function;
    private final Map<DomainFunctionArgument, Expression> arguments;
    private final int hash;

    /**
     * Creates a new function invocation expression from the given domain function and function argument assignments returning a result of the given domain type.
     *
     * @param function The domain function
     * @param arguments The function argument assignments
     * @param type The result domain type
     */
    public FunctionInvocation(DomainFunction function, Map<DomainFunctionArgument, Expression> arguments, DomainType type) {
        super(type);
        this.function = function;
        this.arguments = arguments;
        this.hash = computeHashCode();
    }

    /**
     * Returns the domain function.
     *
     * @return the domain function
     */
    public DomainFunction getFunction() {
        return function;
    }

    /**
     * Returns the function argument assignments.
     *
     * @return the function argument assignments
     */
    public Map<DomainFunctionArgument, Expression> getArguments() {
        return arguments;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FunctionInvocation that = (FunctionInvocation) o;
        return function.equals(that.function) &&
                arguments.equals(that.arguments) &&
                getType().equals(that.getType());
    }

    private int computeHashCode() {
        int result = getType().hashCode();
        result = 31 * result + function.hashCode();
        result = 31 * result + arguments.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }
}
