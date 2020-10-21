package com.blazebit.expression.persistence;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.EnumDomainTypeBuilder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionServiceFactory;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.persistence.function.CurrentTimestampFunction;
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

    private final ExpressionServiceFactory expressionServiceFactory;
    private Instant instant;

    public PredicateInterpreterTest() {
        DomainBuilder domainBuilder = Domain.getDefaultProvider().createDefaultBuilder();
        TypeUtils.registerStringlyType(domainBuilder, "Language", string -> new Locale(string));
        EnumDomainTypeBuilder currencyEnumBuilder = domainBuilder.createEnumType("Currency");
        TypeUtils.registerStringlyEnumType(domainBuilder, currencyEnumBuilder, Currency::getInstance);
        currencyEnumBuilder.withValue("EUR")
            .withValue("USD")
            .build();
        DomainModel domainModel = domainBuilder.build();
        this.expressionServiceFactory = Expressions.forModel(domainModel);
    }

    private Boolean testPredicate(String expr) {
        ExpressionCompiler compiler = expressionServiceFactory.createCompiler();
        ExpressionInterpreter interpreter = expressionServiceFactory.createInterpreter();
        Predicate expression = compiler.createPredicate(expr);
        ExpressionInterpreter.Context context = interpreter.createContext(Collections.emptyMap(), Collections.emptyMap());
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
