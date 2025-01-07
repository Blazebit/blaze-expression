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

package com.blazebit.expression.declarative.persistence;

import com.blazebit.domain.declarative.DeclarativeDomain;
import com.blazebit.domain.declarative.DomainFunction;
import com.blazebit.domain.declarative.DomainFunctionParam;
import com.blazebit.domain.declarative.DomainFunctions;
import com.blazebit.domain.declarative.Metadata;
import com.blazebit.domain.declarative.MetadataType;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Expressions;
import com.blazebit.expression.Literal;
import com.blazebit.expression.persistence.PersistenceDomainFunctionArgumentRenderers;
import com.blazebit.expression.persistence.PersistenceExpressionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializerContext;
import com.blazebit.expression.persistence.PersistenceFunctionRenderer;
import com.blazebit.persistence.BaseWhereBuilder;
import com.blazebit.persistence.BetweenBuilder;
import com.blazebit.persistence.CaseWhenStarterBuilder;
import com.blazebit.persistence.CommonQueryBuilder;
import com.blazebit.persistence.FullQueryBuilder;
import com.blazebit.persistence.LikeBuilder;
import com.blazebit.persistence.MultipleSubqueryInitiator;
import com.blazebit.persistence.PredicateBuilder;
import com.blazebit.persistence.QuantifiableBinaryPredicateBuilder;
import com.blazebit.persistence.RestrictionBuilder;
import com.blazebit.persistence.SimpleCaseWhenStarterBuilder;
import com.blazebit.persistence.SubqueryBuilder;
import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.WhereBuilder;
import com.blazebit.persistence.WhereOrBuilder;
import com.blazebit.persistence.internal.RestrictionBuilderExperimental;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertTrue;

public class ModelTest {

    private final DomainType domainType;
    private final ExpressionService expressionService;

    public ModelTest() {
        DomainModel domainModel = DeclarativeDomain.getDefaultProvider()
                .createDefaultConfiguration()
                .addDomainType(User.class)
                .addDomainFunctions(Functions.class)
                .addDomainFunctions(DateFunctions.class)
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
        ExpressionSerializer<WhereBuilder> serializer = expressionService.createSerializer(WhereBuilder.class);
        ExpressionSerializer.Context serializerContext = new PersistenceExpressionSerializerContext<>(expressionService, null)
            .withAlias("user", "u");
        WhereBuilderMock whereBuilderMock = new WhereBuilderMock();
        serializer.serializeTo(serializerContext, expression, whereBuilderMock);
        Assert.assertEquals("u.age > 18", whereBuilderMock.predicate);
    }

    @Test
    public void test4() {
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionCompiler.Context compilerContext = compiler.createContext(Collections.singletonMap("user", domainType));
        Expression expression = compiler.createPredicate("user.age > 18", compilerContext);
        ExpressionSerializer<WhereBuilder> serializer = expressionService.createSerializer(WhereBuilder.class);
        ExpressionSerializer.Context serializerContext = new PersistenceExpressionSerializerContext<>(expressionService, null)
            .withAlias("user", "u");
        WhereBuilderMock whereBuilderMock = new WhereBuilderMock();
        try {
            serializer.serializeTo(serializerContext, expression, whereBuilderMock);
        } catch (IllegalStateException ex) {
            assertTrue(ex.getMessage().contains("'User.age'"));
        }
    }

    @Test
    public void test5() {
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionCompiler.Context compilerContext = compiler.createContext(Collections.singletonMap("user", domainType));
        Expression expression = compiler.createPredicate("user.registrationDate < TIMESTAMP(2020-01-01)", compilerContext);
        ExpressionSerializer<WhereBuilder> serializer = expressionService.createSerializer(WhereBuilder.class);
        ExpressionSerializer.Context serializerContext = new PersistenceExpressionSerializerContext<>(expressionService, null)
            .withAlias("user", "u");
        WhereBuilderMock whereBuilderMock = new WhereBuilderMock();
        serializer.serializeTo(serializerContext, expression, whereBuilderMock);
        Assert.assertEquals("u.registrationDate < {ts '2020-01-01 00:00:00'}", whereBuilderMock.predicate);
    }

    @Test
    public void test6() {
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionCompiler.Context compilerContext = compiler.createContext(
                Collections.singletonMap("user", domainType));
        Expression expression = compiler.createPredicate(
                "user.registrationDate < ZONED_DATE_TIME('2021-12-08T14:12:13.585294800+08:00[Asia/Kuala_Lumpur]')",
                compilerContext);
        ExpressionSerializer<WhereBuilder> serializer = expressionService.createSerializer(WhereBuilder.class);
        ExpressionSerializer.Context serializerContext = new PersistenceExpressionSerializerContext<>(expressionService,
                null)
                .withAlias("user", "u");
        WhereBuilderMock whereBuilderMock = new WhereBuilderMock();
        serializer.serializeTo(serializerContext, expression, whereBuilderMock);
        Assert.assertEquals("u.registrationDate < {ts '2021-12-08 14:12:13.5852948'}", whereBuilderMock.predicate);
    }

    @Test
    public void test7() {
        ExpressionCompiler compiler = expressionService.createCompiler();
        ExpressionCompiler.Context compilerContext = compiler.createContext(
                Collections.singletonMap("user", domainType));
        Expression expression = compiler.createPredicate(
                "user.registrationDate < ZONED_DATE_TIME('2021-12-08T14:12:13.585294800+08:00')",
                compilerContext);
        ExpressionSerializer<WhereBuilder> serializer = expressionService.createSerializer(WhereBuilder.class);
        ExpressionSerializer.Context serializerContext = new PersistenceExpressionSerializerContext<>(expressionService,
                null)
                .withAlias("user", "u");
        WhereBuilderMock whereBuilderMock = new WhereBuilderMock();
        serializer.serializeTo(serializerContext, expression, whereBuilderMock);
        Assert.assertEquals("u.registrationDate < {ts '2021-12-08 14:12:13.5852948'}", whereBuilderMock.predicate);
    }

    @DomainFunctions
    static class Functions {
        @DomainFunction("IS_OLD")
        @PersistenceFunction("?1.age > 18")
        static Boolean isOld(ExpressionInterpreter.Context context, @DomainFunctionParam("person") User user, String... args) {
            return user.getAge() > 18;
        }
    }

    /**
     * Function that accepts a String representation of {@link ZonedDateTime} ISO-8601 in and returns a {@link Timestamp} representation of the same date.
     * Usage example: ZONED_DATE_TIME('2021-12-08T14:12:13.585294800+08:00') -> {ts '2021-12-08 14:12:13.5852948'}
     */
    @DomainFunctions
    static class DateFunctions {
        @DomainFunction("ZONED_DATE_TIME")
        @Metadata(ZonedDateTimePersistenceFunctionRenderer.class)
        Timestamp zonedDateTime(String zonedDateTime) {
            return Timestamp.valueOf(zonedDateTime);
        }
    }

    @com.blazebit.domain.declarative.DomainType
    public static interface User {
        String getName();
        long getAge();
        @Metadata(MyExpressionRenderer.class)
        LocalDate getRegistrationDate();
    }

    static class UserImpl implements User {
        private final String name;
        private final long age;
        private final LocalDate registrationDate;

        public UserImpl(String name, long age) {
            this.name = name;
            this.age = age;
            this.registrationDate = LocalDate.of(1970, 1, 1);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public long getAge() {
            return age;
        }

        @Override
        public LocalDate getRegistrationDate() {
            return registrationDate;
        }
    }

    static class WhereBuilderMock implements WhereBuilder, MultipleSubqueryInitiator<RestrictionBuilder>, RestrictionBuilder {

        private String predicate;

        @Override
        public WhereOrBuilder whereOr() {
            return null;
        }

        @Override
        public WhereBuilder setWhereExpression(String expression) {
            return null;
        }

        @Override
        public BaseWhereBuilder whereExpression(String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator whereExpressionSubqueries(String expression) {
            predicate = expression;
            return this;
        }

        @Override
        public MultipleSubqueryInitiator setWhereExpressionSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryInitiator<RestrictionBuilder> whereSubquery() {
            return null;
        }

        @Override
        public SubqueryInitiator<RestrictionBuilder> whereSubquery(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator<RestrictionBuilder> whereSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder<RestrictionBuilder> whereSubquery(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder<RestrictionBuilder> whereSubquery(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public RestrictionBuilder where(String expression) {
            return null;
        }

        @Override
        public CaseWhenStarterBuilder<RestrictionBuilder> whereCase() {
            return null;
        }

        @Override
        public SimpleCaseWhenStarterBuilder<RestrictionBuilder> whereSimpleCase(String expression) {
            return null;
        }

        @Override
        public SubqueryInitiator whereExists() {
            return null;
        }

        @Override
        public SubqueryInitiator whereNotExists() {
            return null;
        }

        @Override
        public SubqueryBuilder whereExists(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder whereNotExists(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public CommonQueryBuilder<?> getParentQueryBuilder() {
            return null;
        }

        @Override
        public SubqueryInitiator<MultipleSubqueryInitiator<RestrictionBuilder>> with(String subqueryAlias) {
            return null;
        }

        @Override
        public SubqueryBuilder<MultipleSubqueryInitiator> with(String subqueryAlias, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public RestrictionBuilder end() {
            return this;
        }

        @Override
        public BetweenBuilder betweenExpression(String start) {
            return null;
        }

        @Override
        public BetweenBuilder between(Object start) {
            return null;
        }

        @Override
        public SubqueryInitiator<BetweenBuilder> betweenSubquery() {
            return null;
        }

        @Override
        public SubqueryInitiator<BetweenBuilder> betweenSubquery(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator<BetweenBuilder> betweenSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder<BetweenBuilder> betweenSubquery(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder<BetweenBuilder> betweenSubquery(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public BetweenBuilder notBetweenExpression(String start) {
            return null;
        }

        @Override
        public BetweenBuilder notBetween(Object start) {
            return null;
        }

        @Override
        public SubqueryInitiator<BetweenBuilder> notBetweenSubquery() {
            return null;
        }

        @Override
        public SubqueryInitiator<BetweenBuilder> notBetweenSubquery(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator<BetweenBuilder> notBetweenSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder<BetweenBuilder> notBetweenSubquery(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder<BetweenBuilder> notBetweenSubquery(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public QuantifiableBinaryPredicateBuilder eq() {
            return null;
        }

        @Override
        public SubqueryInitiator eq(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator eqSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder eq(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder eq(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public Object eq(Object value) {
            return null;
        }

        @Override
        public Object eqExpression(String expression) {
            return null;
        }

        @Override
        public QuantifiableBinaryPredicateBuilder notEq() {
            return null;
        }

        @Override
        public SubqueryInitiator notEq(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator notEqSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder notEq(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder notEq(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public Object notEq(Object value) {
            return null;
        }

        @Override
        public Object notEqExpression(String expression) {
            return null;
        }

        @Override
        public QuantifiableBinaryPredicateBuilder gt() {
            return null;
        }

        @Override
        public SubqueryInitiator gt(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator gtSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder gt(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder gt(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public Object gt(Object value) {
            return null;
        }

        @Override
        public Object gtExpression(String expression) {
            return null;
        }

        @Override
        public QuantifiableBinaryPredicateBuilder ge() {
            return null;
        }

        @Override
        public SubqueryInitiator ge(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator geSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder ge(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder ge(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public Object ge(Object value) {
            return null;
        }

        @Override
        public Object geExpression(String expression) {
            return null;
        }

        @Override
        public QuantifiableBinaryPredicateBuilder lt() {
            return null;
        }

        @Override
        public SubqueryInitiator lt(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator ltSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder lt(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder lt(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public Object lt(Object value) {
            return null;
        }

        @Override
        public Object ltExpression(String expression) {
            return null;
        }

        @Override
        public QuantifiableBinaryPredicateBuilder le() {
            return null;
        }

        @Override
        public SubqueryInitiator le(String subqueryAlias, String expression) {
            return null;
        }

        @Override
        public MultipleSubqueryInitiator leSubqueries(String expression) {
            return null;
        }

        @Override
        public SubqueryBuilder le(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder le(String subqueryAlias, String expression, FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public Object le(Object value) {
            return null;
        }

        @Override
        public Object leExpression(String expression) {
            return null;
        }

        @Override
        public SubqueryInitiator in() {
            return null;
        }

        @Override
        public SubqueryInitiator notIn() {
            return null;
        }

        @Override
        public SubqueryBuilder in(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public SubqueryBuilder notIn(FullQueryBuilder criteriaBuilder) {
            return null;
        }

        @Override
        public Object inExpressions(String... parameterOrLiteralExpressions) {
            return null;
        }

        @Override
        public Object inCollectionExpression(String collectionParameterExpression) {
            return null;
        }

        @Override
        public Object in(Collection values) {
            return null;
        }

        @Override
        public Object in(Object... values) {
            return null;
        }

        @Override
        public Object notInExpressions(String... parameterOrLiteralExpressions) {
            return null;
        }

        @Override
        public Object notInCollectionExpression(String collectionParameterExpression) {
            return null;
        }

        @Override
        public Object notIn(Collection values) {
            return null;
        }

        @Override
        public Object notIn(Object... values) {
            return null;
        }

        @Override
        public BetweenBuilder betweenLiteral(Object start) {
            return null;
        }

        @Override
        public BetweenBuilder notBetweenLiteral(Object start) {
            return null;
        }

        @Override
        public Object eqLiteral(Object value) {
            return null;
        }

        @Override
        public Object notEqLiteral(Object value) {
            return null;
        }

        @Override
        public Object gtLiteral(Object value) {
            return null;
        }

        @Override
        public Object geLiteral(Object value) {
            return null;
        }

        @Override
        public Object ltLiteral(Object value) {
            return null;
        }

        @Override
        public Object leLiteral(Object value) {
            return null;
        }

        @Override
        public Object inLiterals(Collection values) {
            return null;
        }

        @Override
        public Object inLiterals(Object... values) {
            return null;
        }

        @Override
        public Object notInLiterals(Collection values) {
            return null;
        }

        @Override
        public Object notInLiterals(Object... values) {
            return null;
        }

        @Override
        public Object isNull() {
            return null;
        }

        @Override
        public Object isNotNull() {
            return null;
        }

        @Override
        public Object isEmpty() {
            return null;
        }

        @Override
        public Object isNotEmpty() {
            return null;
        }

        @Override
        public Object isMemberOf(String expression) {
            return null;
        }

        @Override
        public Object isNotMemberOf(String expression) {
            return null;
        }

        @Override
        public LikeBuilder like(boolean caseSensitive) {
            return null;
        }

        @Override
        public LikeBuilder like() {
            return null;
        }

        @Override
        public LikeBuilder notLike(boolean caseSensitive) {
            return null;
        }

        @Override
        public LikeBuilder notLike() {
            return null;
        }

        @Override
        public RestrictionBuilderExperimental nonPortable() {
            return null;
        }

        @Override
        public PredicateBuilder where() {
            return null;
        }
    }

    @MetadataType(PersistenceExpressionRenderer.class)
    public static class MyExpressionRenderer implements PersistenceExpressionRenderer {
        @Override
        public void render(StringBuilder sb, PersistenceExpressionSerializer serializer) {
            sb.append(".registrationDate");
        }
    }
}
