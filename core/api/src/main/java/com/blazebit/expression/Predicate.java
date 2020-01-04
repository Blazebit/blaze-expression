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

/**
 * The base interface for predicates.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface Predicate extends Expression {

    /**
     * Returns whether the predicate is negated.
     *
     * @return whether the predicate is negated
     */
    boolean isNegated();

    /**
     * Sets the negation of the predicate as defined by the given boolean.
     *
     * @param negated Whether the predicate should be negated
     */
    void setNegated(boolean negated);
}
