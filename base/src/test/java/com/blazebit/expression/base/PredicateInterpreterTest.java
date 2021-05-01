package com.blazebit.expression.base;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.base.function.CurrentTimestampFunction;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PredicateInterpreterTest {

    private final ExpressionService expressionService;
    private Instant instant;

    public PredicateInterpreterTest() {
        DomainBuilder domainBuilder = Domain.getDefaultProvider().createDefaultBuilder();
        domainBuilder.createBasicType("Language");
        domainBuilder.createEnumType("Currency")
            .withValue("EUR")
            .withValue("USD")
            .build();
        StringlyTypeUtils.registerStringlyType(domainBuilder, "Language", Locale::new);
        StringlyTypeUtils.registerStringlyType(domainBuilder, "Currency", Currency::getInstance);
        DomainModel domainModel = domainBuilder.build();
        this.expressionService = Expressions.forModel(domainModel);
    }

    private Boolean testPredicate(String expr) {
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionInterpreter interpreter = expressionService.createInterpreter();
        Predicate expression = compiler.createPredicate(expr);
        ExpressionInterpreter.Context context = ExpressionInterpreterContext.create(expressionService);
        if (instant != null) {
            context.setProperty(CurrentTimestampFunction.INSTANT_PROPERTY, instant);
            instant = null;
        }
        return interpreter.evaluate(expression, context);
    }

    @Test
    public void testBasic() {
        Assert.assertEquals(false, testPredicate("1 > 2"));
    }

    @Test
    public void testBasic2() {
        Assert.assertEquals(true, testPredicate("1 + 2.0 = 3"));
    }

    @Test
    public void testBasic3() {
        Assert.assertEquals(true, testPredicate("CURRENT_TIMESTAMP() = CURRENT_TIMESTAMP()"));
    }

    @Test
    public void testStringly1() {
        Assert.assertEquals(true, testPredicate("LANGUAGE('de') = LANGUAGE(TO_STRING(LANGUAGE('de')))"));
    }

    @Test
    public void testStringly2() {
        Assert.assertEquals(true, testPredicate("LANGUAGE('de') = 'de'"));
    }

    @Test
    public void testStringly3() {
        Assert.assertEquals(true, testPredicate("'de' = LANGUAGE('de')"));
    }

    @Test
    public void testStringly4() {
        Assert.assertEquals(true, testPredicate("Currency.EUR = 'EUR'"));
    }

    @Test
    public void testStringly5() {
        Assert.assertEquals(true, testPredicate("'EUR' = Currency.EUR"));
    }

}
