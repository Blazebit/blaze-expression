/*
 * Copyright 2019 Blazebit.
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

import java.util.HashSet;
import java.util.Set;

/**
 * The base interface for an expression.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface Expression {

    /**
     * A visitor for an expression.
     *
     * @author Christian Beikov
     * @since 1.0.0
     */
    interface Visitor {
        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(ArithmeticFactor e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(ExpressionPredicate e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(BetweenPredicate e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(InPredicate e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(ChainingArithmeticExpression e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(CompoundPredicate e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(ComparisonPredicate e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(IsNullPredicate e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(Path e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(FunctionInvocation e);

        /**
         * Visits the given expression.
         *
         * @param e The expression to visit
         */
        void visit(Literal e);
    }

    /**
     * A visitor for an expression that produces a result as part of the visitation.
     *
     * @param <T> The result type
     * @author Christian Beikov
     * @since 1.0.0
     */
    interface ResultVisitor<T> {
        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(ArithmeticFactor e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(ExpressionPredicate e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(BetweenPredicate e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(InPredicate e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(ChainingArithmeticExpression e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(CompoundPredicate e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(ComparisonPredicate e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(IsNullPredicate e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(Path e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(FunctionInvocation e);

        /**
         * Visits the given expression and returns a result.
         *
         * @param e The expression to visit
         * @return the result
         */
        T visit(Literal e);
    }

    /**
     * Returns the domain type of this expression.
     *
     * @return the domain type of this expression
     */
    DomainType getType();

    /**
     * Dynamic dispatch to one of the visit methods of the visitor, based on the runtime type.
     *
     * @param visitor The visitor to call visit on
     */
    void accept(Visitor visitor);

    /**
     * Dynamic dispatch to one of the visit methods of the visitor, based on the runtime type.
     *
     * @param visitor The visitor to call visit on
     * @param <T> The result type
     * @return the value as returned by the result visitor
     */
    <T> T accept(ResultVisitor<T> visitor);

    /**
     * Returns the paths that are used in this expression.
     *
     * @return the paths that are used
     */
    default Set<Path> getUsedPaths() {
        Set<Path> paths = new HashSet<>();
        accept(new PathCollectingVisitor(paths));
        return paths;
    }
}
