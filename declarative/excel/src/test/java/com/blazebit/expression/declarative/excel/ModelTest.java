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

package com.blazebit.expression.declarative.excel;

import com.blazebit.domain.declarative.DeclarativeDomain;
import com.blazebit.domain.declarative.DomainFunction;
import com.blazebit.domain.declarative.DomainFunctionParam;
import com.blazebit.domain.declarative.DomainFunctions;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.excel.ExcelExpressionSerializer;
import com.blazebit.expression.excel.ExcelExpressionSerializerContext;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collections;

public class ModelTest {

    private final DomainType domainType;
    private final ExpressionService expressionService;

    public ModelTest() {
        DomainModel domainModel = DeclarativeDomain.getDefaultProvider()
                .createDefaultConfiguration()
                .addDomainType(User.class)
                .addDomainFunctions(Functions.class)
                .createDomainModel();
        domainType = domainModel.getType(User.class.getSimpleName());
        this.expressionService = Expressions.forModel(domainModel);
    }

    public Object testExpression(String expr, User user) {
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionInterpreter interpreter = expressionService.createInterpreter();
        ExpressionCompiler.Context compilerContext = compiler.createContext(Collections.singletonMap("user", domainType));
        Expression expression = compiler.createExpression(expr, compilerContext);
        ExpressionInterpreter.Context context = ExpressionInterpreterContext.create(expressionService)
            .withRoot("user", user);
        return interpreter.evaluate(expression, context);
    }

    @Test
    public void test1() {
        Assert.assertEquals(BigInteger.valueOf(4), testExpression("length(user.name)", new UserImpl("Hugo", 20)));
    }

    @Test
    public void test2() {
        Assert.assertEquals(true, testExpression("IS_OLD(user)", new UserImpl("Hugo", 20)));
        Assert.assertEquals(false, testExpression("IS_OLD(user)", new UserImpl("Hugo", 18)));
        Assert.assertEquals(false, testExpression("IS_OLD(user, 'abc')", new UserImpl("Hugo", 18)));
        Assert.assertEquals(false, testExpression("IS_OLD(user, 'abc', 'asd')", new UserImpl("Hugo", 18)));
    }

    @Test
    public void test3() {
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionCompiler.Context compilerContext = compiler.createContext(Collections.singletonMap("user", domainType));
        Expression expression = compiler.createExpression("IS_OLD(user)", compilerContext);
        ExpressionSerializer<StringBuilder> serializer = expressionService.createSerializer(StringBuilder.class, "excel");
        ExpressionSerializer.Context serializerContext = new ExcelExpressionSerializerContext(expressionService, 5)
            .withContextParameter("user", 5);
        StringBuilder sb = new StringBuilder();
        serializer.serializeTo(serializerContext, expression, sb);
        Assert.assertEquals("A5 > 18", sb.toString());
    }

    @DomainFunctions
    static class Functions {
        @DomainFunction("IS_OLD")
        @ExcelFunction("A?1 > 18")
        static Boolean isOld(ExpressionInterpreter.Context context, @DomainFunctionParam("person") User user, String... args) {
            return user.getAge() > 18;
        }
    }

    @com.blazebit.domain.declarative.DomainType
    public static interface User {
        String getName();
        long getAge();
    }

    static class UserImpl implements User {
        private final String name;
        private final long age;

        public UserImpl(String name, long age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getAge() {
            return age;
        }
    }
}
