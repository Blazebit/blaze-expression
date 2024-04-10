/*
 * Copyright 2019 - 2024 Blazebit.
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

package com.blazebit.expression.impl;

import com.blazebit.expression.Expression;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ArithmeticExpressionCompilerTest extends AbstractExpressionCompilerTest {

    @Test
    public void arithmeticWithLiteralTest() {
        Expression expr = parseArithmeticExpression("1 + 2");
        assertEquals(plus(pos(number(1)), pos(number(2))), expr);
    }

    @Test
    public void arithmeticWithLiteralWideningTest() {
        Expression expr = parseArithmeticExpression("1.1 + 2");
        assertEquals(plus(pos(number("1.1")), pos(number(2))), expr);
    }

    @Test
    public void comparisonWithLiteralWideningTest() {
        Expression expr = parsePredicate("1.1 = 2");
        assertEquals(eq(pos(number("1.1")), pos(number(2))), expr);
    }

    @Test
    public void arithmeticOperatorPrecedence() {
        Expression expr = parseArithmeticExpressionOnly("1 / 1 * 1");
        assertEquals(mul(div(pos(number(1)), pos(number(1))), pos(number(1))), expr);
    }
}
