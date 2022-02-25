/*
 * Copyright 2019 - 2022 Blazebit.
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
public class ExpressionSerializerTest extends AbstractExpressionCompilerTest {

    @Test
    public void arithmeticOperatorPrecedenceTest1() {
        Expression expr = parseArithmeticExpressionOnly("(1 + 2) * 2");
        String serializedExpression = serializeExpression(expr);
        assertEquals("(1 + 2) * 2", serializedExpression);
    }

    @Test
    public void arithmeticOperatorPrecedenceTest2() {
        Expression expr = parseArithmeticExpressionOnly("(1 + 2) * (2 + 2)");
        String serializedExpression = serializeExpression(expr);
        assertEquals("(1 + 2) * (2 + 2)", serializedExpression);
    }

    @Test
    public void arithmeticOperatorPrecedenceTest3() {
        Expression expr = parseArithmeticExpressionOnly("1 + (2 * (3 / 4))");
        String serializedExpression = serializeExpression(expr);
        assertEquals("1 + 2 * (3 / 4)", serializedExpression);
    }

    @Test
    public void booleanOperatorPrecedenceTest1() {
        Expression expr = parsePredicateOnly("(1 = 1 AND 1 = 1) OR 1 = 1");
        String serializedExpression = serializeExpression(expr);
        assertEquals("1 = 1 AND 1 = 1 OR 1 = 1", serializedExpression);
    }

    @Test
    public void booleanOperatorPrecedenceTest2() {
        Expression expr = parsePredicateOnly("(1 = 1 OR 1 = 1) OR 1 = 1");
        String serializedExpression = serializeExpression(expr);
        assertEquals("1 = 1 OR 1 = 1 OR 1 = 1", serializedExpression);
    }

    @Test
    public void booleanOperatorPrecedenceTest3() {
        Expression expr = parsePredicateOnly("(1 = 1 OR 1 = 1) OR (1 = 1 AND 1 = 1)");
        String serializedExpression = serializeExpression(expr);
        assertEquals("1 = 1 OR 1 = 1 OR 1 = 1 AND 1 = 1", serializedExpression);
    }

    @Test
    public void booleanOperatorPrecedenceTest4() {
        Expression expr = parsePredicateOnly("1 = 1 AND 1 = 1 OR 1 = 1");
        String serializedExpression = serializeExpression(expr);
        assertEquals("1 = 1 AND 1 = 1 OR 1 = 1", serializedExpression);
    }
}
