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

import com.blazebit.expression.ChainingArithmeticExpression;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface DomainOperatorRenderer {

    public static final DomainOperatorRenderer SIMPLE = new DomainOperatorRenderer() {
        @Override
        public void render(ChainingArithmeticExpression e, PersistenceExpressionSerializer serializer) {
            StringBuilder sb = serializer.getStringBuilder();
            e.getLeft().accept(serializer);
            sb.append(' ');
            sb.append(e.getOperator().getOperator());
            sb.append(' ');
            e.getRight().accept(serializer);
        }
    };

    /**
     * Renders the given domain operator for the given expression.
     *
     * @param expression The expression
     * @param serializer The serializer
     */
    void render(ChainingArithmeticExpression expression, PersistenceExpressionSerializer serializer);

}
