package com.blazebit.expression.persistence;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionInterpreterTest {

    private final DomainModel domainModel;
    private final ExpressionServiceFactory expressionServiceFactory;
    private Instant instant;
    private final ExpressionCompiler compiler;
    private final ExpressionInterpreter interpreter;
    private final Map<String, DomainType> testTypes = new HashMap<>();
    private final Map<String, Object> testData = new HashMap<>();

    public class User {
        String status;
        public User(Boolean status) {
            this.status = status.toString();
        }
        public String getStatus() {
            return status;
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
        MetadataDefinition[] statusAttributeMetadata = new MetadataDefinition[2];
        statusAttributeMetadata[0] = new UserStatusAttributeAccessor();
        statusAttributeMetadata[1] = new TypeAdapterMetadataDefinition(BooleanTypeAdapter.INSTANCE);

        DomainBuilder domainBuilder = Domain.getDefaultProvider().createDefaultBuilder()
                .createEntityType("user")
                .addAttribute("status", Boolean.class, statusAttributeMetadata)
                .build();
        this.domainModel = domainBuilder.build();
        this.expressionServiceFactory = Expressions.forModel(domainModel);
        this.compiler = expressionServiceFactory.createCompiler();
        this.interpreter = expressionServiceFactory.createInterpreter();
        this.testTypes.put("user", domainModel.getType("user"));
        this.testData.put("user", new User(true));
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

}
