/*
 * Copyright 2019 - 2025 Blazebit.
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
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.SyntaxErrorException;
import com.blazebit.expression.TypeErrorException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class TemplateExpressionCompilerTest extends AbstractExpressionCompilerTest {

    @Test
    public void testSomeText() {
        Expression templateExpression = parseTemplateExpression("Hello 123");
        assertEquals(
                string("Hello 123"),
                templateExpression
        );
    }

    @Test
    public void testVariableCharacterAtStart() {
        Expression templateExpression = parseTemplateExpression("Hello ##{123}");
        assertEquals(
            plus(string("Hello #"), number(123)),
            templateExpression
        );
    }

    @Test
    public void testVariableCharacterAtEnd() {
        Expression templateExpression = parseTemplateExpression("Hello 123#");
        assertEquals(
            string("Hello 123#"),
            templateExpression
        );
    }

    @Test
    public void testVariableCharacterAtEnd2() {
        Expression templateExpression = parseTemplateExpression("Hello #{123}#");
        assertEquals(
            plus(plus(string("Hello "), number(123)), string("#")),
            templateExpression
        );
    }

    @Test
    public void testVariableCharacters1() {
        Expression templateExpression = parseTemplateExpression("Hello #123");
        assertEquals(
            string("Hello #123"),
            templateExpression
        );
    }

    @Test
    public void testVariableCharacters2() {
        Expression templateExpression = parseTemplateExpression("Hello {123");
        assertEquals(
            string("Hello {123"),
            templateExpression
        );
    }

    @Test(expected = SyntaxErrorException.class)
    public void testVariableCharacters3() {
        parseTemplateExpression("Hello #{123");
    }

    @Test
    public void testEscaping() {
        Expression templateExpression = parseTemplateExpression("Hello \\#{123");
        assertEquals(
            string("Hello #{123"),
            templateExpression
        );
    }

    @Test
    public void testTemplate() {
        Expression templateExpression = parseTemplateExpression("Hello #{123}");
        assertEquals(
            plus(string("Hello "), number(123)),
            templateExpression
        );
    }

    @Test
    public void testTemplate1() {
        Expression templateExpression = parseTemplateExpression("#{123} Hello");
        assertEquals(
            plus(plus(string(""), number(123)), string(" Hello")),
            templateExpression
        );
    }

    @Test
    public void testTemplate2() {
        Expression templateExpression = parseTemplateExpression("#{1}#{2}#{3} Hello, my name is\nWhat?");
        assertEquals(
            plus(plus(plus(plus(string(""), number(1)), number(2)), number(3)), string(" Hello, my name is\nWhat?")),
            templateExpression
        );
    }
}
