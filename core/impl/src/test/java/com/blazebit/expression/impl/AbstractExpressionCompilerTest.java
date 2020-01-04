/*
 * Copyright 2019 - 2020 Blazebit.
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
import com.blazebit.domain.runtime.model.DomainPredicateType;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.StaticDomainOperationTypeResolvers;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.ArithmeticOperatorType;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.impl.domain.DefaultEnumLiteralResolver;
import com.blazebit.expression.impl.domain.DefaultNumericLiteralResolver;
import com.blazebit.expression.impl.domain.DefaultStringLiteralResolver;
import com.blazebit.expression.impl.domain.DefaultTemporalLiteralResolver;
import com.blazebit.expression.impl.model.Gender;
import org.junit.Before;
import org.junit.BeforeClass;

import java.math.BigDecimal;
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

    private ExpressionCompilerImpl expressionCompiler;
    private static DomainModel defaultDomainModel;
    private DomainModel domainModel;

    @BeforeClass
    public static void createDefaultTestDomainModel() {
        DomainBuilder builder = new DomainBuilderImpl()
                .createBasicType("boolean", Boolean.class)
                .withOperator("boolean", new DomainOperator[]{ DomainOperator.NOT })
                .withPredicate("boolean", DomainPredicateType.distinguishable())
                .createBasicType("long", Long.class)
                .withOperator("long", DomainOperator.arithmetic())
                .withPredicate("long", DomainPredicateType.comparable())
                .createBasicType("integer", Integer.class)
                .withOperator("integer", DomainOperator.arithmetic())
                .withPredicate("integer", DomainPredicateType.comparable())
                .createBasicType("bigdecimal", BigDecimal.class)
                .withOperator("bigdecimal", DomainOperator.arithmetic())
                .withPredicate("bigdecimal", DomainPredicateType.comparable())
                .createBasicType("string", String.class)
                .withOperator("string", new DomainOperator[]{ DomainOperator.PLUS })
                .withPredicate("string", DomainPredicateType.distinguishable())
                .createBasicType("timestamp", Instant.class)
                .withPredicate("timestamp", DomainPredicateType.comparable())
                .createEnumType("gender", Gender.class)
                    .withValue(Gender.FEMALE.name())
                    .withValue(Gender.MALE.name())
                .build()
                .withPredicate("gender", DomainPredicateType.distinguishable())
                .withNumericLiteralResolver(new DefaultNumericLiteralResolver())
                .withStringLiteralResolver(new DefaultStringLiteralResolver())
                .withTemporalLiteralResolver(new DefaultTemporalLiteralResolver())
                .withEnumLiteralResolver(new DefaultEnumLiteralResolver())
                .createEntityType("user")
                    .addAttribute("id", Long.class)
                    .addAttribute("email", String.class)
                    .addAttribute("age", Integer.class)
                    .addAttribute("birthday", Instant.class)
                    .addAttribute("gender", Gender.class)
                .build();

        for (final Class<?> type : Arrays.asList(Integer.class, Long.class, BigDecimal.class)) {
            builder.withOperationTypeResolver(type, DomainOperator.MODULO, StaticDomainOperationTypeResolvers.returning(Integer.class));
            builder.withOperationTypeResolver(type, DomainOperator.UNARY_MINUS, StaticDomainOperationTypeResolvers.returning(type));
            builder.withOperationTypeResolver(type, DomainOperator.UNARY_PLUS, StaticDomainOperationTypeResolvers.returning(type));
            builder.withOperationTypeResolver(type, DomainOperator.DIVISION, StaticDomainOperationTypeResolvers.returning(BigDecimal.class));
            for (DomainOperator domainOperator : Arrays.asList(DomainOperator.PLUS, DomainOperator.MINUS, DomainOperator.MULTIPLICATION)) {
                builder.withOperationTypeResolver(type, domainOperator, StaticDomainOperationTypeResolvers.widest(BigDecimal.class, Integer.class));
            }
        }

        defaultDomainModel = builder.build();
    }

    @Before
    public void setup() {
        domainModel = createDomainModel();
        expressionCompiler = new ExpressionCompilerImpl(domainModel, new LiteralFactory(domainModel));
    }

    protected DomainModel createDomainModel() {
        return defaultDomainModel;
    }

    protected ExpressionCompiler.Context getCompileContext() {
        return expressionCompiler.createContext(Collections.singletonMap("user", domainModel.getType("user")));
    }

    protected Predicate parsePredicate(String input) {
        return expressionCompiler.createPredicate(input, getCompileContext());
    }

    protected Expression parseArithmeticExpression(String input) {
        return expressionCompiler.createExpression(input, getCompileContext());
    }

    protected CompoundPredicate or(Predicate... disjuncts) {
        return new CompoundPredicate(booleanDomainType(), Arrays.asList(disjuncts), false);
    }

    protected CompoundPredicate and(Predicate... conjuncts) {
        return new CompoundPredicate(booleanDomainType(), Arrays.asList(conjuncts), true);
    }

    protected Literal time(Instant value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofInstant(value));
    }

    protected Literal time(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofDateTimeString(value));
    }

    protected Literal interval(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofTemporalIntervalString(value));
    }

    protected static String wrapTimestamp(String dateTimeStr) {
        return "TIMESTAMP('" + dateTimeStr + "')";
    }

    protected Literal string(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofString(value));
    }

    protected Literal number(long value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofBigDecimal(new BigDecimal(value)));
    }

    protected Literal number(BigDecimal value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofBigDecimal(value));
    }

    protected Literal number(String value) {
        return new Literal(expressionCompiler.getLiteralFactory().ofNumericString(value));
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
        return new Path(entity, pathAttributes, type);
    }

    protected Literal enumValue(String enumName, String enumKey) {
        return new Literal(expressionCompiler.getLiteralFactory().ofEnumValue((EnumDomainType) domainModel.getType(enumName), enumKey));
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
        return domainModel.getType(Boolean.class);
    }

    interface ExpectedExpressionProducer<T extends AbstractExpressionCompilerTest> {
        Expression getExpectedExpression(T testInstance);
    }
}
