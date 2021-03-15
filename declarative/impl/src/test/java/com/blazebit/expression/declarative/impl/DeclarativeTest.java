package com.blazebit.expression.declarative.impl;

import com.blazebit.domain.declarative.DeclarativeDomain;
import com.blazebit.domain.declarative.DeclarativeDomainConfiguration;
import com.blazebit.domain.declarative.DomainType;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class DeclarativeTest {

    @Test
    public void testInterpretationWithoutPersistence() {
        DeclarativeDomainConfiguration configuration = DeclarativeDomain.getDefaultProvider().createDefaultConfiguration();
        configuration.addDomainType(TestEntity.class);
        DomainModel domainModel = configuration.createDomainModel();
        Map<String, com.blazebit.domain.runtime.model.DomainType> rootDomainTypes = Collections.singletonMap("entity", domainModel.getType("TestEntity"));
        ExpressionService expressionService = Expressions.forModel(domainModel);
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionInterpreter interpreter = expressionService.createInterpreter();
        ExpressionInterpreter.Context interpreterContext = interpreter.createContext(rootDomainTypes, Collections.singletonMap("entity", new TestEntity("abc")));
        Object name = interpreter.evaluate(compiler.createExpression("entity.name", compiler.createContext(rootDomainTypes)), interpreterContext);
        Object size = interpreter.evaluate(compiler.createExpression("SIZE(entity.names)", compiler.createContext(rootDomainTypes)), interpreterContext);
        Assert.assertEquals("abc", name);
        Assert.assertEquals(BigInteger.ONE, size);
    }

    @DomainType(value = "TestEntity")
    public static class TestEntity {

        private final String name;

        public TestEntity(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Collection<String> getNames() {
            return Collections.singleton(name);
        }
    }
}
