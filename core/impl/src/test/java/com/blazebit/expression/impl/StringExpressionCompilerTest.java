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

package com.blazebit.expression.impl;

import com.blazebit.expression.Expression;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@RunWith(Parameterized.class)
public class StringExpressionCompilerTest extends AbstractExpressionCompilerTest {

    private final String expr;
    private final ExpectedExpressionProducer<StringExpressionCompilerTest> expectedExpressionProducer;

    public StringExpressionCompilerTest(String expr, ExpectedExpressionProducer<StringExpressionCompilerTest> expectedExpressionProducer) {
        this.expr = expr;
        this.expectedExpressionProducer = expectedExpressionProducer;
    }

    @Parameters(name = "{1} {2}")
    public static Collection<Object[]> getTestData() {
        Object[][] testCases = {
                {
                        "''",
                        new ExpectedExpressionProducer<StringExpressionCompilerTest>() {
                            @Override
                            public Expression getExpectedExpression(StringExpressionCompilerTest testInstance) {
                                return pos(testInstance.string(""));
                            }
                        }

                },
                {
                        "'abc'",
                        new ExpectedExpressionProducer<StringExpressionCompilerTest>() {
                            @Override
                            public Expression getExpectedExpression(StringExpressionCompilerTest testInstance) {
                                return pos(testInstance.string("abc"));
                            }
                        }
                }
        };

        List<Object[]> parameters = new ArrayList<>(testCases.length);
        for (Object[] literal : testCases) {
            parameters.add(new Object[]{
                    literal[0],
                    literal[1]
            });
        }

        return parameters;
    }

    @Test
    public void comparisonWithLiteralTest() {
        assertEquals(expectedExpressionProducer.getExpectedExpression(this), parseArithmeticExpression(expr));
    }
}
