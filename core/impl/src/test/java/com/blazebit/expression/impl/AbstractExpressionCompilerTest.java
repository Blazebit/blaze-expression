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

package com.blazebit.expression.impl;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.impl.boot.model.DomainBuilderImpl;
import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.StaticDomainFunctionTypeResolvers;
import com.blazebit.domain.runtime.model.StaticDomainOperationTypeResolvers;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.ArithmeticOperatorType;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.EnumLiteral;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.ImplicitRootProvider;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.impl.domain.DefaultBooleanLiteralResolver;
import com.blazebit.expression.impl.domain.DefaultEnumLiteralResolver;
import com.blazebit.expression.impl.domain.DefaultNumericLiteralResolver;
import com.blazebit.expression.impl.domain.DefaultStringLiteralResolver;
import com.blazebit.expression.impl.domain.DefaultTemporalLiteralResolver;
import com.blazebit.expression.impl.model.Gender;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public abstract class AbstractExpressionCompilerTest {

    public static final String BOOLEAN = "boolean";
    public static final String LONG = "long";
    public static final String INTEGER = "integer";
    public static final String BIGDECIMAL = "bigdecimal";
    public static final String STRING = "string";
    public static final String TIMESTAMP = "timestamp";
    public static final String INTERVAL = "interval";
    public static final String GENDER = "gender";

    private static DomainModel defaultDomainModel;
    private DomainModel domainModel;
    private ExpressionCompilerImpl expressionCompiler;
    private ExpressionCompiler.Context context;
    private ExpressionSerializer<StringBuilder> expressionSerializer;
    private ExpressionSerializer<StringBuilder> expressionTemplateSerializer;

    @BeforeClass
    public static void createDefaultTestDomainModel() {
        DomainBuilder builder = new DomainBuilderImpl()
                .createBasicType(BOOLEAN, Boolean.class)
                .withOperator(BOOLEAN, new DomainOperator[]{ DomainOperator.NOT })
                .withPredicate(BOOLEAN, DomainPredicate.distinguishable())
                .withDefaultPredicateResultType(BOOLEAN)
                .createBasicType(LONG, Long.class)
                .withOperator(LONG, DomainOperator.arithmetic())
                .withPredicate(LONG, DomainPredicate.comparable())
                .createBasicType(INTEGER, Integer.class)
                .withOperator(INTEGER, DomainOperator.arithmetic())
                .withPredicate(INTEGER, DomainPredicate.comparable())
                .createBasicType(BIGDECIMAL, BigDecimal.class)
                .withOperator(BIGDECIMAL, DomainOperator.arithmetic())
                .withPredicate(BIGDECIMAL, DomainPredicate.comparable())
                .createBasicType(STRING, String.class)
                .withOperator(STRING, new DomainOperator[]{ DomainOperator.PLUS })
                .withPredicate(STRING, DomainPredicate.distinguishable())
                .createBasicType(TIMESTAMP, Instant.class)
                .withPredicate(TIMESTAMP, DomainPredicate.comparable())
                .createBasicType(INTERVAL, TemporalInterval.class)
                .withPredicate(INTERVAL, DomainPredicate.comparable())
                .createEnumType(GENDER, Gender.class)
                    .withValue(Gender.FEMALE.name())
                    .withValue(Gender.MALE.name())
                .build()
                .withPredicate(GENDER, DomainPredicate.distinguishable())
                .createEntityType("user")
                    .addAttribute("id", LONG)
                    .addAttribute("email", STRING)
                    .addAttribute("age", INTEGER)
                    .addAttribute("birthday", TIMESTAMP)
                    .addAttribute("gender", GENDER)
                    .addAttribute("active", BOOLEAN)
                .build()
                .createFunction("self")
                    .withArgument("object")
                .build()
                .withFunctionTypeResolver("self", StaticDomainFunctionTypeResolvers.FIRST_ARGUMENT_TYPE);

        for (final String type : Arrays.asList(INTEGER, LONG, BIGDECIMAL)) {
            builder.withOperationTypeResolver(type, DomainOperator.MODULO, StaticDomainOperationTypeResolvers.returning(INTEGER));
            builder.withOperationTypeResolver(type, DomainOperator.UNARY_MINUS, StaticDomainOperationTypeResolvers.returning(type));
            builder.withOperationTypeResolver(type, DomainOperator.UNARY_PLUS, StaticDomainOperationTypeResolvers.returning(type));
            builder.withOperationTypeResolver(type, DomainOperator.DIVISION, StaticDomainOperationTypeResolvers.returning(BIGDECIMAL));
            for (DomainOperator domainOperator : Arrays.asList(DomainOperator.PLUS, DomainOperator.MINUS, DomainOperator.MULTIPLICATION)) {
                builder.withOperationTypeResolver(type, domainOperator, StaticDomainOperationTypeResolvers.widest(BIGDECIMAL, INTEGER));
            }
        }

        defaultDomainModel = builder.build();
    }

    @Before
    public void setup() {
        domainModel = createDomainModel();
        ExpressionService expressionService = Expressions.getDefaultProvider().createDefaultBuilder(domainModel)
            .withNumericLiteralResolver(new DefaultNumericLiteralResolver())
            .withStringLiteralResolver(new DefaultStringLiteralResolver())
            .withTemporalLiteralResolver(new DefaultTemporalLiteralResolver())
            .withEnumLiteralResolver(new DefaultEnumLiteralResolver())
            .withBooleanLiteralResolver(new DefaultBooleanLiteralResolver())
            .build();
        expressionCompiler = (ExpressionCompilerImpl) expressionService.createCompiler();
        setImplicitRootProvider(null);
        expressionSerializer = expressionService.createSerializer();
        expressionTemplateSerializer = expressionService.createTemplateSerializer();
    }

    protected void setImplicitRootProvider(ImplicitRootProvider implicitRootProvider) {
        context = expressionCompiler.createContext(Collections.singletonMap("user", domainModel.getType("user")), implicitRootProvider);
    }

    protected DomainModel createDomainModel() {
        return defaultDomainModel;
    }

    protected Predicate parsePredicateOnly(String input) {
        return expressionCompiler.createPredicate(input, context);
    }

    protected Predicate parsePredicate(String input) {
        Predicate predicate = expressionCompiler.createPredicate(input, context);
        StringBuilder sb = new StringBuilder();
        expressionSerializer.serializeTo(predicate, sb);
        Assert.assertEquals(input, sb.toString());
        return predicate;
    }

    protected Expression parseArithmeticExpressionOnly(String input) {
        return expressionCompiler.createExpression(input, context);
    }

    protected Expression parseArithmeticExpression(String input) {
        Expression expression = expressionCompiler.createExpression(input, context);
        StringBuilder sb = new StringBuilder();
        expressionSerializer.serializeTo(expression, sb);
        Assert.assertEquals(input, sb.toString());
        return expression;
    }

    protected String serializeExpression(Expression expression) {
        StringBuilder sb = new StringBuilder();
        expressionSerializer.serializeTo(expression, sb);
        return sb.toString();
    }

    protected Expression parseTemplateExpression(String input) {
        Expression templateExpression = expressionCompiler.createTemplateExpression(input, context);
        StringBuilder sb = new StringBuilder();
        expressionTemplateSerializer.serializeTo(templateExpression, sb);
        Assert.assertEquals(input, sb.toString());
        return templateExpression;
    }

    protected CompoundPredicate or(Predicate... disjuncts) {
        return new CompoundPredicate(booleanDomainType(), Arrays.asList(disjuncts), false);
    }

    protected CompoundPredicate and(Predicate... conjuncts) {
        return new CompoundPredicate(booleanDomainType(), Arrays.asList(conjuncts), true);
    }

    protected Literal time(Instant value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofInstant(context, value));
    }

    protected Literal time(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofDateTimeString(context, value));
    }

    protected Literal interval(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofTemporalIntervalString(context, value));
    }

    protected static String wrapTimestamp(String dateTimeStr) {
        return "TIMESTAMP('" + dateTimeStr + "')";
    }

    protected Literal string(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofString(context, value));
    }

    protected Literal number(long value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofBigInteger(context, BigInteger.valueOf(value)));
    }

    protected Literal number(BigDecimal value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofBigDecimal(context, value));
    }

    protected Literal number(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofNumericString(context, value));
    }

    protected Path attr(String entity, String... attributes) {
        EntityDomainType entityDomainType = (EntityDomainType) domainModel.getType(entity);
        DomainType type = entityDomainType;
        List<EntityDomainTypeAttribute> pathAttributes = new ArrayList<>(attributes.length);
        if (attributes.length != 0) {
            for (int i = 0; ; ) {
                EntityDomainTypeAttribute attribute = entityDomainType.getAttribute(attributes[i]);
                type = attribute.getType();
                pathAttributes.add(attribute);
                i++;
                if (i == attributes.length) {
                    break;
                } else {
                    if (type instanceof EntityDomainType) {
                        entityDomainType = (EntityDomainType) type;
                    } else if (type instanceof CollectionDomainType) {
                        entityDomainType = (EntityDomainType) ((CollectionDomainType) type).getElementType();
                    } else {
                        throw new IllegalArgumentException("De-referencing non-entity type attribute: " + attribute);
                    }
                }
            }
        }
        return new Path(entity, Collections.unmodifiableList(pathAttributes), type);
    }

    protected Path attr(ArithmeticExpression base, String... attributes) {
        EntityDomainType entityDomainType = (EntityDomainType) base.getType();
        DomainType type = entityDomainType;
        List<EntityDomainTypeAttribute> pathAttributes = new ArrayList<>(attributes.length);
        if (attributes.length != 0) {
            for (int i = 0; ; ) {
                EntityDomainTypeAttribute attribute = entityDomainType.getAttribute(attributes[i]);
                type = attribute.getType();
                pathAttributes.add(attribute);
                i++;
                if (i == attributes.length) {
                    break;
                } else {
                    if (type instanceof EntityDomainType) {
                        entityDomainType = (EntityDomainType) type;
                    } else if (type instanceof CollectionDomainType) {
                        entityDomainType = (EntityDomainType) ((CollectionDomainType) type).getElementType();
                    } else {
                        throw new IllegalArgumentException("De-referencing non-entity type attribute: " + attribute);
                    }
                }
            }
        }
        return new Path(base, Collections.unmodifiableList(pathAttributes), type);
    }

    protected Literal enumValue(String enumName, String enumKey) {
        EnumDomainType enumDomainType = (EnumDomainType) domainModel.getType(enumName);
        return new EnumLiteral(enumDomainType.getEnumValues().get(enumKey), expressionCompiler.getLiteralFactory().ofEnumValue(context, enumDomainType, enumKey));
    }

//	protected static CollectionAtom collectionAttr(String identifier) {
//		return new CollectionAtom(new Path(identifier, TermType.COLLECTION));
//	}

    protected ComparisonPredicate neq(ArithmeticExpression left, ArithmeticExpression right) {
        return new ComparisonPredicate(booleanDomainType(), left, right, ComparisonOperator.NOT_EQUAL);
    }

    protected ComparisonPredicate eq(ArithmeticExpression left, ArithmeticExpression right) {
        return new ComparisonPredicate(booleanDomainType(), left, right, ComparisonOperator.EQUAL);
    }

    protected ComparisonPredicate gt(ArithmeticExpression left, ArithmeticExpression right) {
        return new ComparisonPredicate(booleanDomainType(), left, right, ComparisonOperator.GREATER);
    }

    protected ComparisonPredicate ge(ArithmeticExpression left, ArithmeticExpression right) {
        return new ComparisonPredicate(booleanDomainType(), left, right, ComparisonOperator.GREATER_OR_EQUAL);
    }

    protected ComparisonPredicate lt(ArithmeticExpression left, ArithmeticExpression right) {
        return new ComparisonPredicate(booleanDomainType(), left, right, ComparisonOperator.LOWER);
    }

    protected ComparisonPredicate le(ArithmeticExpression left, ArithmeticExpression right) {
        return new ComparisonPredicate(booleanDomainType(), left, right, ComparisonOperator.LOWER_OR_EQUAL);
    }

    protected static ArithmeticExpression pos(ArithmeticExpression expression) {
        return expression;//new ArithmeticFactor(expression, false);
    }

    protected static ArithmeticFactor neg(ArithmeticExpression expression) {
        return new ArithmeticFactor(expression.getType(), expression, true);
    }

    protected static ArithmeticExpression plus(ArithmeticExpression left, ArithmeticExpression right) {
        return new ChainingArithmeticExpression(left.getType(), left, right, ArithmeticOperatorType.PLUS);
    }

    protected static ArithmeticExpression minus(ArithmeticExpression left, ArithmeticExpression right) {
        return new ChainingArithmeticExpression(left.getType(), left, right, ArithmeticOperatorType.MINUS);
    }

    protected BetweenPredicate between(ArithmeticExpression left, ArithmeticExpression lower, ArithmeticExpression upper) {
        return new BetweenPredicate(booleanDomainType(), left, upper, lower);
    }

//	protected static StringInCollectionPredicate inCollection(StringAtom value, CollectionAtom collection) {
//		return new StringInCollectionPredicate(value, collection, false);
//	}

    protected InPredicate in(ArithmeticExpression value, ArithmeticExpression... items) {
        return new InPredicate(booleanDomainType(), value, Arrays.asList(items), false);
    }

    protected FunctionInvocation functionInvocation(String functionName, Expression... argumentArray) {
        DomainFunction domainFunction = domainModel.getFunction(functionName);
        Map<DomainFunctionArgument, DomainType> argumentTypes = new HashMap<>();
        Map<DomainFunctionArgument, Expression> arguments = new LinkedHashMap<>();
        for (int i = 0; i < argumentArray.length; i++) {
            argumentTypes.put(domainFunction.getArguments().get(i), argumentArray[i].getType());
            arguments.put(domainFunction.getArguments().get(i), argumentArray[i]);
        }
        DomainType functionType = domainModel.getFunctionTypeResolver(functionName).resolveType(domainModel, domainFunction, argumentTypes);
        return new FunctionInvocation(domainFunction, arguments, functionType);
    }

    private DomainType booleanDomainType() {
        return domainModel.getType(BOOLEAN);
    }

    interface ExpectedExpressionProducer<T extends AbstractExpressionCompilerTest> {
        Expression getExpectedExpression(T testInstance);
    }
}
