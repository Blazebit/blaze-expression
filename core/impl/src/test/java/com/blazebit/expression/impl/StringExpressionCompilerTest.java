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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class StringExpressionCompilerTest extends AbstractExpressionCompilerTest {

    @Test
    public void test() {
        assertEquals(string("abc"), parseArithmeticExpression("'abc'"));
        assertEquals(string(""), parseArithmeticExpression("''"));
    }

    @Test
    public void testMultiLine() {
        assertEquals(string("abc\ndef"), parseArithmeticExpressionOnly("'abc\ndef'"));
        assertEquals(string("\r\n"), parseArithmeticExpressionOnly("'\r\n'"));
    }

    @Test
    public void testEscaping() {
        assertEquals(string("a\nb"), parseArithmeticExpression("'a\\nb'"));
        assertEquals(string("a\u1234"), parseArithmeticExpressionOnly("'a\\u1234'"));
        assertEquals(string("a'b"), parseArithmeticExpression("'a\\'b'"));
    }
}
