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

import com.blazebit.domain.impl.boot.model.DomainBuilderImpl;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.Expression;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class FunctionExpressionCompilerTest extends AbstractExpressionCompilerTest {

    private static DomainModel testDomainModel;

    @BeforeClass
    public static void defineTestDomainModel() {
        testDomainModel = new DomainBuilderImpl()
                .createBasicType(TIMESTAMP, Instant.class)
                .createFunction("CURRENT_TIMESTAMP")
                    .withResultType(TIMESTAMP)
                    .build()
                .build();
    }

    @Override
    protected DomainModel createDomainModel() {
        return testDomainModel;
    }

    @Test
    public void testCurrentTimestampFunction() {
        Expression expression = parseArithmeticExpression("CURRENT_TIMESTAMP()");
        assertEquals(
                pos(functionInvocation("CURRENT_TIMESTAMP")),
                expression
        );
    }
}
