package com.blazebit.expression.declarative.impl;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.declarative.DeclarativeDomain;
import com.blazebit.domain.declarative.DeclarativeDomainConfiguration;
import com.blazebit.domain.declarative.DomainType;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.base.StringlyTypeUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;

public class DeclarativeTest {

    @Test
    public void testInterpretationWithoutPersistence() {
        DomainBuilder domainBuilder = Domain.getDefaultProvider().createDefaultBuilder();
        domainBuilder.createEnumType("Currency")
            .withValue("EUR")
            .withValue("USD")
            .build();
        StringlyTypeUtils.registerStringlyType(domainBuilder, "Currency", Currency::getInstance);
        DeclarativeDomainConfiguration configuration = DeclarativeDomain.getDefaultProvider().createDefaultConfiguration(domainBuilder);
        configuration.withTypeResolverDecorator(typeResolver -> {
            return (contextClass, type, builder) -> {
                if (Currency.class == type) {
                    return "Currency";
                }
                return typeResolver.resolve(contextClass, type, builder);
            };
        });
        configuration.addDomainType(TestEntity.class);
        DomainModel domainModel = configuration.createDomainModel();
        Map<String, com.blazebit.domain.runtime.model.DomainType> rootDomainTypes = Collections.singletonMap("entity", domainModel.getType("TestEntity"));
        ExpressionService expressionService = Expressions.forModel(domainModel);
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionInterpreter interpreter = expressionService.createInterpreter();
        ExpressionInterpreter.Context interpreterContext = ExpressionInterpreterContext.create(expressionService)
            .withRoot("entity", new TestEntity("abc", Currency.getInstance("EUR")));
        ExpressionCompiler.Context compilerContext = compiler.createContext(rootDomainTypes);
        Object name = interpreter.evaluate(compiler.createExpression("entity.name", compilerContext), interpreterContext);
        Object size = interpreter.evaluate(compiler.createExpression("SIZE(entity.names)", compilerContext), interpreterContext);
        Object currency = interpreter.evaluate(compiler.createExpression("entity.currency", compilerContext), interpreterContext);
        Object currencyModel = interpreter.evaluateAsModelType(compiler.createExpression("entity.currency", compilerContext), interpreterContext);
        Assert.assertEquals("abc", name);
        Assert.assertEquals(BigInteger.ONE, size);
        Assert.assertEquals(domainModel.getEnumType("Currency").getEnumValues().get("EUR"), currency);
        Assert.assertEquals(Currency.getInstance("EUR"), currencyModel);
    }

    @DomainType(value = "TestEntity")
    public static class TestEntity {

        private final String name;
        private final Currency currency;

        public TestEntity(String name, Currency currency) {
            this.name = name;
            this.currency = currency;
        }

        public String getName() {
            return name;
        }

        public Currency getCurrency() {
            return currency;
        }

        public Collection<String> getNames() {
            return Collections.singleton(name);
        }
    }
}
