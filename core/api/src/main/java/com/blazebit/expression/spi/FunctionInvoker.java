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

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.expression.ExpressionInterpreter;

/**
 * An interpreter for invoking domain functions that is registered as metadata on a domain function.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface FunctionInvoker {

    /**
     * Interprets the domain function as applied on the given arguments for the given interpreter context.
     *
     * @param context The interpreter context
     * @param function The domain function to invoke
     * @param arguments The domain function argument assignments
     * @return the function invocation result
     */
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments);
}
