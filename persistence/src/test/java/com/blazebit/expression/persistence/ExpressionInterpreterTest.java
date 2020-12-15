package com.blazebit.expression.persistence;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.EnumDomainTypeBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.DomainTypeResolverException;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionServiceFactory;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.SyntaxErrorException;
import com.blazebit.expression.persistence.function.CurrentTimestampFunction;
import com.blazebit.expression.spi.AttributeAccessor;
import com.blazebit.expression.spi.TypeAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionInterpreterTest {

    private static final int SECONDS_PER_DAY = 86400;
    private final DomainModel domainModel;
    private final ExpressionServiceFactory expressionServiceFactory;
    private Instant instant;
    private final ExpressionCompiler compiler;
    private final ExpressionInterpreter interpreter;
    private final Map<String, DomainType> testTypes = new HashMap<>();
    private final Map<String, Object> testData = new HashMap<>();

    public class User {
        String status;
        Locale language;
        Currency currency;
        public User(Boolean status, Locale language, Currency currency) {
            this.status = status.toString();
            this.language = language;
            this.currency = currency;
        }
        public String getStatus() {
            return status;
        }
        public Locale getLanguage() {
            return language;
        }
        public Currency getCurrency() {
            return currency;
        }
    }
    public static class UserStatusAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public UserStatusAttributeAccessor() {
        }
        @Override
        public Object getAttribute(Object value, EntityDomainTypeAttribute attribute) {
            return ((User) value).getStatus();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder<?> definitionHolder) {
            return this;
        }
    }
    public static class UserLanguageAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public UserLanguageAttributeAccessor() {
        }
        @Override
        public Object getAttribute(Object value, EntityDomainTypeAttribute attribute) {
            return ((User) value).getLanguage();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder<?> definitionHolder) {
            return this;
        }
    }
    public static class UserCurrencyAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor {
        public UserCurrencyAttributeAccessor() {
        }
        @Override
        public Object getAttribute(Object value, EntityDomainTypeAttribute attribute) {
            return ((User) value).getCurrency();
        }
        @Override
        public Class<AttributeAccessor> getJavaType() {
            return AttributeAccessor.class;
        }
        @Override
        public AttributeAccessor build(MetadataDefinitionHolder<?> definitionHolder) {
            return this;
        }
    }
    private static class BooleanTypeAdapter implements TypeAdapter<String, Boolean> {
        public static final BooleanTypeAdapter INSTANCE = new BooleanTypeAdapter();

        @Override
        public Boolean toInternalType(ExpressionInterpreter.Context context, String value, DomainType domainType) {
            return value == null || value.isEmpty() ? null : Boolean.valueOf(value);
        }

        @Override
        public String toModelType(ExpressionInterpreter.Context context, Boolean value, DomainType domainType) {
            return value == null ? null : value.toString();
        }
    }
    private static class TypeAdapterMetadataDefinition implements MetadataDefinition<TypeAdapter<?, ?>> {
        private final TypeAdapter<?, ?> typeAdapter;

        public TypeAdapterMetadataDefinition(TypeAdapter<?, ?> typeAdapter) {
            this.typeAdapter = typeAdapter;
        }

        @Override
        public Class<TypeAdapter<?, ?>> getJavaType() {
            return (Class<TypeAdapter<?, ?>>) (Class<?>) TypeAdapter.class;
        }

        @Override
        public TypeAdapter<?, ?> build(MetadataDefinitionHolder<?> definitionHolder) {
            return typeAdapter;
        }
    }

    public ExpressionInterpreterTest() {
        MetadataDefinition[] languageAttributeMetadata = new MetadataDefinition[1];
        languageAttributeMetadata[0] = new UserLanguageAttributeAccessor();
        MetadataDefinition[] currencyAttributeMetadata = new MetadataDefinition[1];
        currencyAttributeMetadata[0] = new UserCurrencyAttributeAccessor();

        MetadataDefinition[] statusAttributeMetadata = new MetadataDefinition[2];
        statusAttributeMetadata[0] = new UserStatusAttributeAccessor();
        statusAttributeMetadata[1] = new TypeAdapterMetadataDefinition(BooleanTypeAdapter.INSTANCE);

        DomainBuilder domainBuilder = Domain.getDefaultProvider().createDefaultBuilder();
        TypeUtils.registerStringlyType(domainBuilder, "Language", string -> new Locale(string));
        EnumDomainTypeBuilder currencyEnumBuilder = domainBuilder.createEnumType("Currency");
        TypeUtils.registerStringlyEnumType(domainBuilder, currencyEnumBuilder, Currency::getInstance);
        currencyEnumBuilder.withValue("EUR")
            .withValue("USD")
            .build();
        domainBuilder.createEntityType("user")
                .addAttribute("status", PersistenceDomainContributor.BOOLEAN_TYPE_NAME, statusAttributeMetadata)
                .addAttribute("language", "Language", languageAttributeMetadata)
                .addAttribute("currency", "Currency", currencyAttributeMetadata)
                .build();
        this.domainModel = domainBuilder.build();
        this.expressionServiceFactory = Expressions.forModel(domainModel);
        this.compiler = expressionServiceFactory.createCompiler();
        this.interpreter = expressionServiceFactory.createInterpreter();
        this.testTypes.put("user", domainModel.getType("user"));
        this.testData.put("user", new User(true, new Locale("de"), Currency.getInstance("EUR")));
    }

    private ExpressionInterpreter.Context createInterpreterContext(Map<String, DomainType> rootDomainTypes, Map<String, Object> rootObjects) {
        ExpressionInterpreter.Context context = interpreter.createContext(rootDomainTypes, rootObjects);
        if (instant != null) {
            context.setProperty(CurrentTimestampFunction.INSTANT_PROPERTY, instant);
            instant = null;
        }
        return context;
    }
    private Object testExpression(String expr) {
        return interpreter.evaluate(
                compiler.createExpression(expr, compiler.createContext(testTypes)),
                createInterpreterContext(testTypes, testData));
    }
    private Object testPredicate(String expr) {
        return interpreter.evaluate(
                compiler.createPredicate(expr, compiler.createContext(testTypes)),
                createInterpreterContext(testTypes, testData));
    }

    @Test
    public void testBasic() {
        Assert.assertEquals(BigInteger.valueOf(3), testExpression("1 + 2"));
    }

    @Test
    public void testBasic2() {
        Assert.assertEquals(new BigDecimal("3.0"), testExpression("1 + 2.0"));
    }

    @Test
    public void testBasic3() {
        this.instant = Instant.ofEpochMilli(0);
        Assert.assertEquals(instant, testExpression("CURRENT_TIMESTAMP()"));
    }

    @Test
    public void testBasic4() {
        this.instant = Instant.ofEpochMilli(0);
        Assert.assertEquals(instant.plus(10, ChronoUnit.SECONDS), testExpression("CURRENT_TIMESTAMP() + INTERVAL 10 SECONDS"));
        this.instant = Instant.ofEpochMilli(0);
        Assert.assertEquals(instant.plus(10, ChronoUnit.SECONDS), testExpression("INTERVAL 10 SECONDS + CURRENT_TIMESTAMP()"));
    }

    @Test
    public void testBasic5() {
        this.instant = Instant.ofEpochMilli(0);
        Assert.assertEquals(LocalTime.ofSecondOfDay(Math.floorMod(instant.getEpochSecond() + 10, SECONDS_PER_DAY)), testExpression("CURRENT_TIME() + INTERVAL 10 SECONDS"));
        this.instant = Instant.ofEpochMilli(0);
        Assert.assertEquals(LocalTime.ofSecondOfDay(Math.floorMod(instant.getEpochSecond() + 10, SECONDS_PER_DAY)), testExpression("INTERVAL 10 SECONDS + CURRENT_TIME()"));
    }

    @Test
    public void testBasic6() {
        Assert.assertEquals(new TemporalInterval(0, 0, 0, 0, 0, 20), testExpression("INTERVAL 10 SECONDS + INTERVAL 10 SECONDS"));
    }

    @Test
    public void testBasic7() {
        try {
            testExpression("CURRENT_TIMESTAMP() + CURRENT_TIMESTAMP()");
        } catch (DomainTypeResolverException ex) {
            Assert.assertTrue(ex.getMessage().contains("[Interval]"));
        }
    }

    @Test
    public void testBooleanLiteralTrue() {
        Assert.assertEquals(Boolean.TRUE, testExpression("true"));
    }
    @Test
    public void testBooleanLiteralFalse() {
        Assert.assertEquals(Boolean.FALSE, testExpression("false"));
    }
    @Test(expected = SyntaxErrorException.class)
    public void testBooleanTrueNegated() {
        // The following will fail since "literal" currently is no viable alternative to predicate - maybe it should be?
        Assert.assertEquals(Boolean.FALSE, testPredicate("!true"));
    }

    @Test
    public void testBooleanVariableTrue() {
        Assert.assertEquals(Boolean.TRUE, testPredicate("user.status"));
    }
    @Test
    public void testBooleanVariableTrueNegated() {
        Assert.assertEquals(Boolean.FALSE, testPredicate("!user.status"));
    }

    @Test
    public void testBooleanStringVariableTrue() {
        Assert.assertEquals("true", interpreter.evaluateAsModelType(
                compiler.createExpression("user.status", compiler.createContext(testTypes)),
                createInterpreterContext(testTypes, testData)));
    }
    @Test
    public void testBooleanStringVariableTrueNegated() {
        // Currently it is allowed to use "evaluateAsModelType(Expression, ..)" for predicates as well - even tough this does not make much sense right now.
        Assert.assertEquals(Boolean.FALSE, interpreter.evaluateAsModelType(
                compiler.createPredicate("!user.status", compiler.createContext(testTypes)),
                createInterpreterContext(testTypes, testData)));
    }

    @Test
    public void testBooleanStringVariablesCombined() {
        Assert.assertEquals(Boolean.TRUE, testPredicate("user.status and user.status"));
    }

    @Test
    public void testStringly1() {
        Assert.assertEquals(new Locale("de"), testExpression("user.language"));
    }

    @Test
    public void testStringly2() {
        Assert.assertEquals("de", testExpression("TO_STRING(user.language)"));
    }

    @Test
    public void testStringly3() {
        Assert.assertEquals(true, testPredicate("user.language = 'de'"));
    }

    @Test
    public void testStringly4() {
        Assert.assertEquals(true, testPredicate("'de' = user.language"));
    }

    @Test
    public void testStringly5() {
        Assert.assertEquals("de_DE", testExpression("user.language + '_DE'"));
    }

    @Test
    public void testStringly6() {
        Assert.assertEquals("DE_de", testExpression("'DE_' + user.language"));
    }

    @Test
    public void testStringly7() {
        Assert.assertEquals(true, testPredicate("user.currency = 'EUR'"));
    }

    @Test
    public void testStringly8() {
        Assert.assertEquals(true, testPredicate("'EUR' = user.currency"));
    }

    @Test
    public void testStringly9() {
        Assert.assertEquals("EUR-USD", testExpression("user.currency + '-USD'"));
    }

    @Test
    public void testStringly10() {
        Assert.assertEquals("USD-EUR", testExpression("'USD-' + user.currency"));
    }
}
