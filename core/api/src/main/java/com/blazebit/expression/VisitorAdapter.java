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

import java.util.List;

/**
 * An abstract visitor adapter that visits all nodes.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public abstract class VisitorAdapter implements Expression.Visitor {

    @Override
    public void visit(ArithmeticFactor e) {
        e.getExpression().accept(this);
    }

    @Override
    public void visit(ExpressionPredicate e) {
        e.getExpression().accept(this);
    }

    @Override
    public void visit(BetweenPredicate e) {
        e.getLeft().accept(this);
        e.getLower().accept(this);
        e.getUpper().accept(this);
    }

    @Override
    public void visit(InPredicate e) {
        e.getLeft().accept(this);
        List<ArithmeticExpression> inItems = e.getInItems();
        for (int i = 0; i < inItems.size(); i++) {
            inItems.get(i).accept(this);
        }
    }

    @Override
    public void visit(ChainingArithmeticExpression e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);
    }

    @Override
    public void visit(CompoundPredicate e) {
        List<Predicate> predicates = e.getPredicates();
        for (int i = 0; i < predicates.size(); i++) {
            predicates.get(i).accept(this);
        }
    }

    @Override
    public void visit(ComparisonPredicate e) {
        e.getLeft().accept(this);
        e.getRight().accept(this);
    }

    @Override
    public void visit(IsNullPredicate e) {
        e.getLeft().accept(this);
    }

    @Override
    public void visit(IsEmptyPredicate e) {
        e.getLeft().accept(this);
    }

    @Override
    public void visit(FunctionInvocation e) {
        for (Expression value : e.getArguments().values()) {
            value.accept(this);
        }
    }

    @Override
    public void visit(Path e) {

    }

    @Override
    public void visit(Literal e) {

    }

    @Override
    public void visit(EnumLiteral e) {

    }

    @Override
    public void visit(EntityLiteral e) {

    }

    @Override
    public void visit(CollectionLiteral e) {

    }
}
