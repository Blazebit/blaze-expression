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

package com.blazebit.expression.excel;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.spi.AttributeAccessor;
import org.junit.Assert;
import org.junit.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelExpressionSerializerTest {

    private final DomainModel domainModel;
    private final ExpressionService expressionService;

    public ExcelExpressionSerializerTest() throws Exception {
        DomainBuilder domainBuilder = Domain.getDefaultProvider().createDefaultBuilder();
        domainBuilder.createEnumType("Gender")
            .withValue("Female")
            .withValue("Male")
            .build()
            .withPredicate("Gender", DomainPredicate.distinguishable());
        domainBuilder.createEntityType("User")
            .addAttribute("id", BaseContributor.INTEGER_TYPE_NAME)
            .addAttribute("name", BaseContributor.STRING_TYPE_NAME)
            .addAttribute("email", BaseContributor.STRING_TYPE_NAME)
            .addAttribute("age", BaseContributor.INTEGER_TYPE_NAME)
            .addAttribute("birthday", BaseContributor.TIMESTAMP_TYPE_NAME)
            .addAttribute("gender", "Gender")
            .addAttribute("active", BaseContributor.BOOLEAN_TYPE_NAME)
            .build();
        domainBuilder.createEntityType("GlobalState")
            .addAttribute("rootName", BaseContributor.STRING_TYPE_NAME, new MethodAttributeAccessor(GlobalState.class.getMethod("getRootName")))
            .build();
        domainModel = domainBuilder.build();
        expressionService = Expressions.forModel(domainModel);
    }

    private static class GlobalState {
        private final String rootName;

        public GlobalState(String rootName) {
            this.rootName = rootName;
        }

        public String getRootName() {
            return rootName;
        }
    }

    public static class MethodAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor, Serializable {

        private final Method getter;

        public MethodAttributeAccessor(Method getter) {
            this.getter = getter;
        }

        @Override
        public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute) {
            try {
                return getter.invoke(value);
            } catch (Exception e) {
                throw new RuntimeException("Couldn't access attribute " + attribute + " on object: " + value, e);
            }
        }

        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }

        @Override
        public AttributeAccessor build(MetadataDefinitionHolder definitionHolder) {
            return this;
        }

    }

    private String serialize(String expressionString) {
        ExpressionCompiler compiler = expressionService.createCompiler();
        Map<String, DomainType> rootDomainTypes = new HashMap<>();
        rootDomainTypes.put("u", domainModel.getType("User"));
        rootDomainTypes.put("g", domainModel.getType("GlobalState"));
        ExpressionCompiler.Context compilerContext = compiler.createContext(rootDomainTypes);
        Expression expression = compiler.createExpressionOrPredicate(expressionString, compilerContext);
        ExpressionSerializer<StringBuilder> excelSerializer = expressionService.createSerializer(StringBuilder.class, "excel");
        ExpressionInterpreter.Context interpreterContext = ExpressionInterpreterContext.create(expressionService)
            .withRoot("g", new GlobalState("The root"));
        ExcelExpressionSerializerContext serializerContext = new ExcelExpressionSerializerContext(expressionService, 1)
            .withInterpreterContextForInlining(interpreterContext)
            .withExcelColumn("u.name", 1)
            .withExcelColumn("u.birthday", 2)
            .withExcelColumn("u.age", 3)
            .withExcelColumn("u.gender", 4)
            .withExcelColumn("u.active", 5)
            .withExcelColumn("u.id", 6)
            .withContextParameter("g.rootName", "The root");
        StringBuilder sb = new StringBuilder();
        excelSerializer.serializeTo(serializerContext, expression, sb);
        return sb.toString();
    }

    @Test
    public void testColumn() {
        Assert.assertEquals("A1", serialize("u.name"));
    }

    @Test
    public void testInlineGlobalSimple() {
        Assert.assertEquals("A1 & \"The root\"", serialize("u.name + g.rootName"));
    }

    @Test
    public void testInlineGlobalExpression() {
        Assert.assertEquals("A1 & \"The\"", serialize("u.name + SUBSTRING(g.rootName, 1, 3)"));
    }

    @Test
    public void testBooleanLiteral() {
        Assert.assertEquals("E1 = TRUE()", serialize("u.active = true"));
    }
}
