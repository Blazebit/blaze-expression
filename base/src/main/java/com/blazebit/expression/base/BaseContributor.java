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

package com.blazebit.expression.base;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainOperationTypeResolver;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.domain.runtime.model.DomainPredicateTypeResolver;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.DomainTypeResolverException;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.domain.runtime.model.StaticDomainOperationTypeResolvers;
import com.blazebit.domain.runtime.model.StaticDomainPredicateTypeResolvers;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.domain.spi.DomainContributor;
import com.blazebit.domain.spi.DomainSerializer;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.ExpressionServiceBuilder;
import com.blazebit.expression.base.function.AbsFunction;
import com.blazebit.expression.base.function.Atan2Function;
import com.blazebit.expression.base.function.CeilFunction;
import com.blazebit.expression.base.function.CurrentDateFunction;
import com.blazebit.expression.base.function.CurrentTimeFunction;
import com.blazebit.expression.base.function.CurrentTimestampFunction;
import com.blazebit.expression.base.function.EndsWithFunction;
import com.blazebit.expression.base.function.FloorFunction;
import com.blazebit.expression.base.function.GreatestFunction;
import com.blazebit.expression.base.function.LTrimFunction;
import com.blazebit.expression.base.function.LeastFunction;
import com.blazebit.expression.base.function.LengthFunction;
import com.blazebit.expression.base.function.LocateFunction;
import com.blazebit.expression.base.function.LocateLastFunction;
import com.blazebit.expression.base.function.LowerFunction;
import com.blazebit.expression.base.function.NumericFunction;
import com.blazebit.expression.base.function.PowFunction;
import com.blazebit.expression.base.function.RTrimFunction;
import com.blazebit.expression.base.function.RandomFunction;
import com.blazebit.expression.base.function.ReplaceFunction;
import com.blazebit.expression.base.function.RoundFunction;
import com.blazebit.expression.base.function.SizeFunction;
import com.blazebit.expression.base.function.StartsWithFunction;
import com.blazebit.expression.base.function.SubstringFunction;
import com.blazebit.expression.base.function.TrimFunction;
import com.blazebit.expression.base.function.UpperFunction;
import com.blazebit.expression.spi.BooleanLiteralResolver;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DefaultResolvedLiteral;
import com.blazebit.expression.spi.DomainOperatorInterpreter;
import com.blazebit.expression.spi.EnumLiteralResolver;
import com.blazebit.expression.spi.ExpressionServiceContributor;
import com.blazebit.expression.spi.ExpressionServiceSerializer;
import com.blazebit.expression.spi.NumericLiteralResolver;
import com.blazebit.expression.spi.ResolvedLiteral;
import com.blazebit.expression.spi.StringLiteralResolver;
import com.blazebit.expression.spi.TemporalLiteralResolver;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class BaseContributor implements DomainContributor, ExpressionServiceContributor {

    public static final String CONFIGURATION_NUMERIC_EXACT = "numeric.exact";
    public static final String CONFIGURATION_NUMERIC_SCALE = "numeric.scale";
    public static final int DEFAULT_NUMERIC_SCALE = 5;
    public static final String CONFIGURATION_NUMERIC_ROUNDING = "numeric.rounding";
    public static final RoundingMode DEFAULT_NUMERIC_ROUNDING = RoundingMode.HALF_UP;

    // NOTE: Copied to TypeAdapterRegistry. Keep in sync
    public static final String BOOLEAN_TYPE_NAME = "Boolean";
    public static final String INTEGER_TYPE_NAME = "Integer";
    public static final String NUMERIC_TYPE_NAME = "Numeric";
    public static final String TIMESTAMP_TYPE_NAME = "Timestamp";
    public static final Class<?> TIME = LocalTime.class;
    public static final String TIME_TYPE_NAME = "Time";
    public static final String INTERVAL_TYPE_NAME = "Interval";
    public static final String STRING_TYPE_NAME = "String";
    public static final String DATE_TYPE_NAME = "Date";

    public static final String INTEGER_OR_NUMERIC_TYPE_NAME = INTEGER_TYPE_NAME + "|" + NUMERIC_TYPE_NAME;

    public static final BooleanLiteralResolver BOOLEAN_LITERAL_TYPE_RESOLVER = new BooleanLiteralResolverImpl();
    public static final NumericLiteralResolver NUMERIC_LITERAL_TYPE_RESOLVER = new NumericLiteralResolverImpl();
    public static final TemporalLiteralResolver TEMPORAL_LITERAL_TYPE_RESOLVER = new TemporalLiteralResolverImpl();
    public static final StringLiteralResolver STRING_LITERAL_TYPE_RESOLVER = new StringLiteralResolverImpl();
    public static final EnumLiteralResolver ENUM_LITERAL_RESOLVER = new EnumLiteralResolverImpl();

    @Override
    public void contribute(ExpressionServiceBuilder expressionServiceBuilder) {
        expressionServiceBuilder.withNumericLiteralResolver(NUMERIC_LITERAL_TYPE_RESOLVER);
        expressionServiceBuilder.withStringLiteralResolver(STRING_LITERAL_TYPE_RESOLVER);
        expressionServiceBuilder.withTemporalLiteralResolver(TEMPORAL_LITERAL_TYPE_RESOLVER);
        expressionServiceBuilder.withBooleanLiteralResolver(BOOLEAN_LITERAL_TYPE_RESOLVER);
        expressionServiceBuilder.withEnumLiteralResolver(ENUM_LITERAL_RESOLVER);
    }

    @Override
    public void contribute(DomainBuilder domainBuilder) {
        Object numericExact = domainBuilder.getProperty(CONFIGURATION_NUMERIC_EXACT);
        if (numericExact == null || (boolean) numericExact) {
            createBasicType(domainBuilder, BigInteger.class, INTEGER_TYPE_NAME, DomainOperator.arithmetic(), DomainPredicate.comparable(), handlersFor(ExactNumericOperatorInterpreter.INSTANCE, "INTEGER"));
            createBasicType(domainBuilder, BigDecimal.class, NUMERIC_TYPE_NAME, DomainOperator.arithmetic(), DomainPredicate.comparable(), handlersFor(ExactNumericOperatorInterpreter.INSTANCE, "NUMERIC"));
        } else {
            createBasicType(domainBuilder, Long.class, INTEGER_TYPE_NAME, DomainOperator.arithmetic(), DomainPredicate.comparable(), handlersFor(ApproximateNumericOperatorInterpreter.INSTANCE, "INTEGER"));
            createBasicType(domainBuilder, Double.class, NUMERIC_TYPE_NAME, DomainOperator.arithmetic(), DomainPredicate.comparable(), handlersFor(ApproximateNumericOperatorInterpreter.INSTANCE, "NUMERIC"));
        }
        createBasicType(domainBuilder, String.class, STRING_TYPE_NAME, new DomainOperator[]{ DomainOperator.PLUS }, DomainPredicate.comparable(), handlersFor(StringOperatorInterpreter.INSTANCE, "STRING"));
        createBasicType(domainBuilder, Instant.class, TIMESTAMP_TYPE_NAME, new DomainOperator[]{ DomainOperator.PLUS, DomainOperator.MINUS }, DomainPredicate.comparable(), handlersFor(TimestampOperatorInterpreter.INSTANCE, "TIMESTAMP"));
        createBasicType(domainBuilder, LocalTime.class, TIME_TYPE_NAME, new DomainOperator[]{ DomainOperator.PLUS, DomainOperator.MINUS }, DomainPredicate.comparable(), handlersFor(TimeOperatorInterpreter.INSTANCE, "TIME"));
        createBasicType(domainBuilder, LocalDate.class, DATE_TYPE_NAME, new DomainOperator[]{ DomainOperator.PLUS, DomainOperator.MINUS }, DomainPredicate.comparable(), handlersFor(DateOperatorInterpreter.INSTANCE, "DATE"));
        createBasicType(domainBuilder, TemporalInterval.class, INTERVAL_TYPE_NAME, new DomainOperator[]{ DomainOperator.PLUS, DomainOperator.MINUS }, DomainPredicate.comparable(), handlersFor(IntervalOperatorInterpreter.INSTANCE, "INTERVAL"));
        createBasicType(domainBuilder, Boolean.class, BOOLEAN_TYPE_NAME, new DomainOperator[]{ DomainOperator.NOT }, DomainPredicate.distinguishable(), handlersFor(BooleanOperatorInterpreter.INSTANCE, "BOOLEAN"));

        for (String type : Arrays.asList(INTEGER_TYPE_NAME, NUMERIC_TYPE_NAME)) {
            domainBuilder.withOperationTypeResolver(type, DomainOperator.MODULO, StaticDomainOperationTypeResolvers.widest(NUMERIC_TYPE_NAME, INTEGER_TYPE_NAME));
            domainBuilder.withOperationTypeResolver(type, DomainOperator.UNARY_MINUS, StaticDomainOperationTypeResolvers.returning(type));
            domainBuilder.withOperationTypeResolver(type, DomainOperator.UNARY_PLUS, StaticDomainOperationTypeResolvers.returning(type));
            domainBuilder.withOperationTypeResolver(type, DomainOperator.DIVISION, StaticDomainOperationTypeResolvers.returning(NUMERIC_TYPE_NAME, INTEGER_TYPE_NAME, NUMERIC_TYPE_NAME));
            for (DomainOperator domainOperator : Arrays.asList(DomainOperator.MINUS, DomainOperator.MULTIPLICATION)) {
                domainBuilder.withOperationTypeResolver(type, domainOperator, StaticDomainOperationTypeResolvers.widest(NUMERIC_TYPE_NAME, INTEGER_TYPE_NAME));
            }
            domainBuilder.withOperationTypeResolver(type, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.widest(STRING_TYPE_NAME, NUMERIC_TYPE_NAME, INTEGER_TYPE_NAME));

            withPredicateTypeResolvers(domainBuilder, type, INTEGER_TYPE_NAME, NUMERIC_TYPE_NAME);
        }

        domainBuilder.withOperationTypeResolver(STRING_TYPE_NAME, DomainOperator.PLUS, new StringlyDomainOperationTypeResolver(STRING_TYPE_NAME, STRING_TYPE_NAME, INTEGER_TYPE_NAME, NUMERIC_TYPE_NAME));
        StringlyDomainPredicateTypeResolver stringlyDomainPredicateTypeResolver = new StringlyDomainPredicateTypeResolver(BOOLEAN_TYPE_NAME, STRING_TYPE_NAME);
        for (DomainPredicate domainPredicate : domainBuilder.getEnabledPredicates(domainBuilder.getType(STRING_TYPE_NAME).getName())) {
            domainBuilder.withPredicateTypeResolver(STRING_TYPE_NAME, domainPredicate, stringlyDomainPredicateTypeResolver);
        }

        domainBuilder.withDefaultPredicateResultType(BOOLEAN_TYPE_NAME);
        domainBuilder.withOperationTypeResolver(BOOLEAN_TYPE_NAME, DomainOperator.NOT, StaticDomainOperationTypeResolvers.returning(BOOLEAN_TYPE_NAME));
        withPredicateTypeResolvers(domainBuilder, BOOLEAN_TYPE_NAME, BOOLEAN_TYPE_NAME);

        domainBuilder.withOperationTypeResolver(TIMESTAMP_TYPE_NAME, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.returning(TIMESTAMP_TYPE_NAME, new String[][]{ { TIMESTAMP_TYPE_NAME }, { INTERVAL_TYPE_NAME }}));
        domainBuilder.withOperationTypeResolver(TIMESTAMP_TYPE_NAME, DomainOperator.MINUS, StaticDomainOperationTypeResolvers.returning(TIMESTAMP_TYPE_NAME, new String[][]{ { TIMESTAMP_TYPE_NAME }, { INTERVAL_TYPE_NAME }}));
        withPredicateTypeResolvers(domainBuilder, TIMESTAMP_TYPE_NAME, TIMESTAMP_TYPE_NAME);

        domainBuilder.withOperationTypeResolver(TIME_TYPE_NAME, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.returning(TIME_TYPE_NAME, new String[][]{ { TIME_TYPE_NAME }, { INTERVAL_TYPE_NAME }}));
        domainBuilder.withOperationTypeResolver(TIME_TYPE_NAME, DomainOperator.MINUS, StaticDomainOperationTypeResolvers.returning(TIME_TYPE_NAME, new String[][]{ { TIME_TYPE_NAME }, { INTERVAL_TYPE_NAME }}));
        withPredicateTypeResolvers(domainBuilder, TIME_TYPE_NAME, TIME_TYPE_NAME);

        domainBuilder.withOperationTypeResolver(DATE_TYPE_NAME, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.returning(DATE_TYPE_NAME, new String[][]{ { DATE_TYPE_NAME }, { INTERVAL_TYPE_NAME }}));
        domainBuilder.withOperationTypeResolver(DATE_TYPE_NAME, DomainOperator.MINUS, StaticDomainOperationTypeResolvers.returning(DATE_TYPE_NAME, new String[][]{ { DATE_TYPE_NAME }, { INTERVAL_TYPE_NAME }}));
        withPredicateTypeResolvers(domainBuilder, DATE_TYPE_NAME, DATE_TYPE_NAME);

        domainBuilder.withOperationTypeResolver(INTERVAL_TYPE_NAME, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.widest(TIMESTAMP_TYPE_NAME, TIME_TYPE_NAME, INTERVAL_TYPE_NAME));
        domainBuilder.withOperationTypeResolver(INTERVAL_TYPE_NAME, DomainOperator.MINUS, StaticDomainOperationTypeResolvers.widest(TIMESTAMP_TYPE_NAME, TIME_TYPE_NAME, INTERVAL_TYPE_NAME));
        withPredicateTypeResolvers(domainBuilder, INTERVAL_TYPE_NAME, INTERVAL_TYPE_NAME);

        CurrentTimestampFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        CurrentDateFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        CurrentTimeFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        SubstringFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        ReplaceFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        TrimFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        LTrimFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        RTrimFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        UpperFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        LowerFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        LengthFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        LocateFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        LocateLastFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        StartsWithFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        EndsWithFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        AbsFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        CeilFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        FloorFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        NumericFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        Atan2Function.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        RoundFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        RandomFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        PowFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        GreatestFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        LeastFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
        SizeFunction.addFunction(domainBuilder, BaseContributor.class.getClassLoader());
    }

    @Override
    public int priority() {
        // We use a priority of 500 so that user contributors, which usually don't configure a priority, are guaranteed to run afterwards
        return 500;
    }

    private static void withPredicateTypeResolvers(DomainBuilder domainBuilder, String type, String... supportedTypes) {
        Set<DomainPredicate> enabledPredicates = domainBuilder.getEnabledPredicates(domainBuilder.getType(type).getName());
        if (!enabledPredicates.isEmpty()) {
            DomainPredicateTypeResolver predicateTypeResolver = StaticDomainPredicateTypeResolvers.returning(BOOLEAN_TYPE_NAME, supportedTypes);
            for (DomainPredicate domainPredicate : enabledPredicates) {
                domainBuilder.withPredicateTypeResolver(type, domainPredicate, predicateTypeResolver);
            }
        }
    }

    private <T extends ComparisonOperatorInterpreter & DomainOperatorInterpreter> MetadataDefinition<?>[] handlersFor(T instance, String documentationKey) {
        return new MetadataDefinition[] {
            new ComparisonOperatorInterpreterMetadataDefinition(instance),
            new DomainOperatorInterpreterMetadataDefinition(instance),
            DocumentationMetadataDefinition.localized(documentationKey, BaseContributor.class.getClassLoader())
        };
    }

    private static void createBasicType(DomainBuilder domainBuilder, Class<?> type, String name, DomainOperator[] operators, DomainPredicate[] predicates, MetadataDefinition<?>... metadataDefinitions) {
        domainBuilder.createBasicType(name, type, metadataDefinitions);
        domainBuilder.withOperator(name, operators);
        domainBuilder.withPredicate(name, predicates);
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class BooleanLiteralResolverImpl implements ExpressionServiceSerializer<BooleanLiteralResolver>, BooleanLiteralResolver, Serializable {
        @Override
        public <T> T serialize(ExpressionService expressionService, BooleanLiteralResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            return (T) "\"BooleanLiteralResolver\"";
        }

        @Override
        public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, boolean value) {
            return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(BOOLEAN_TYPE_NAME), value);
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class NumericLiteralResolverImpl implements ExpressionServiceSerializer<NumericLiteralResolver>, NumericLiteralResolver, Serializable {
        @Override
        public <T> T serialize(ExpressionService expressionService, NumericLiteralResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            return (T) "\"NumericLiteralResolver\"";
        }

        @Override
        public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, Number value) {
            if (value instanceof BigDecimal || value instanceof Double) {
                return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(NUMERIC_TYPE_NAME), value);
            }
            return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(INTEGER_TYPE_NAME), value);
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class TemporalLiteralResolverImpl implements ExpressionServiceSerializer<TemporalLiteralResolver>, TemporalLiteralResolver, Serializable {
        @Override
        public <T> T serialize(ExpressionService expressionService, TemporalLiteralResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            return (T) "\"TemporalLiteralResolver\"";
        }

        @Override
        public ResolvedLiteral resolveTimestampLiteral(ExpressionCompiler.Context context, Instant value) {
            return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(TIMESTAMP_TYPE_NAME), value);
        }

        @Override
        public ResolvedLiteral resolveIntervalLiteral(ExpressionCompiler.Context context, TemporalInterval value) {
            return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(INTERVAL_TYPE_NAME), value);
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class StringLiteralResolverImpl implements ExpressionServiceSerializer<StringLiteralResolver>, StringLiteralResolver, Serializable {

        @Override
        public <T> T serialize(ExpressionService expressionService, StringLiteralResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            return (T) "\"StringLiteralResolver\"";
        }

        @Override
        public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, String value) {
            return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(STRING_TYPE_NAME), value);
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class EnumLiteralResolverImpl implements ExpressionServiceSerializer<EnumLiteralResolver>, EnumLiteralResolver, Serializable {

        @Override
        public <T> T serialize(ExpressionService expressionService, EnumLiteralResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            return (T) "\"SimpleEnumLiteralResolver\"";
        }

        @Override
        public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, EnumDomainTypeValue value) {
            return new DefaultResolvedLiteral(value.getOwner(), value);
        }

    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    static class ComparisonOperatorInterpreterMetadataDefinition implements MetadataDefinition<ComparisonOperatorInterpreter>, Serializable {

        private final ComparisonOperatorInterpreter comparisonOperatorInterpreter;

        /**
         * Creates a metadata definition for the given {@link ComparisonOperatorInterpreter}.
         *
         * @param comparisonOperatorInterpreter The comparison operator interpreter
         */
        public ComparisonOperatorInterpreterMetadataDefinition(ComparisonOperatorInterpreter comparisonOperatorInterpreter) {
            this.comparisonOperatorInterpreter = comparisonOperatorInterpreter;
        }

        @Override
        public Class<ComparisonOperatorInterpreter> getJavaType() {
            return ComparisonOperatorInterpreter.class;
        }

        @Override
        public ComparisonOperatorInterpreter build(MetadataDefinitionHolder definitionHolder) {
            return comparisonOperatorInterpreter;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    static class DomainOperatorInterpreterMetadataDefinition implements MetadataDefinition<DomainOperatorInterpreter> {

        private final DomainOperatorInterpreter domainOperatorInterpreter;

        /**
         * Creates a metadata definition for the given {@link DomainOperatorInterpreter}.
         *
         * @param domainOperatorInterpreter The domain operator interpreter
         */
        public DomainOperatorInterpreterMetadataDefinition(DomainOperatorInterpreter domainOperatorInterpreter) {
            this.domainOperatorInterpreter = domainOperatorInterpreter;
        }

        @Override
        public Class<DomainOperatorInterpreter> getJavaType() {
            return DomainOperatorInterpreter.class;
        }

        @Override
        public DomainOperatorInterpreter build(MetadataDefinitionHolder definitionHolder) {
            return domainOperatorInterpreter;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class StringlyDomainOperationTypeResolver implements DomainOperationTypeResolver, DomainSerializer<DomainOperationTypeResolver>, Serializable {

        private final String returningType;
        private final Set<String> supportedTypeNames;

        public StringlyDomainOperationTypeResolver(String returningType, String... supportedTypeNames) {
            this.returningType = returningType;
            this.supportedTypeNames = new HashSet<>(Arrays.asList(supportedTypeNames));
        }

        @Override
        public DomainType resolveType(DomainModel domainModel, List<DomainType> domainTypes) {
            for (int i = 0; i < domainTypes.size(); i++) {
                DomainType domainType = domainTypes.get(i);
                if (!supportedTypeNames.contains(domainType.getName())) {
                    List<DomainType> types = new ArrayList<>(supportedTypeNames.size());
                    for (String typeName : supportedTypeNames) {
                        types.add(domainModel.getType(typeName));
                    }
                    if (domainType.getMetadata(StringlyTypeHandler.class) == null) {
                        throw new DomainTypeResolverException("The operation operand at index " + i + " with the domain type '" + domainType + "' is unsupported! Expected one of the following: " + types);
                    }
                }
            }
            return domainModel.getType(returningType);
        }

        @Override
        public <T> T serialize(DomainModel domainModel, DomainOperationTypeResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("{\"RestrictedDomainOperationTypeResolver\":[");
            sb.append('"').append(returningType).append("\",[");
            for (DomainType domainType : domainModel.getTypes().values()) {
                String typeName = domainType.getName();
                if (supportedTypeNames.contains(typeName) || domainType.getMetadata(StringlyTypeHandler.class) != null) {
                    sb.append('"').append(typeName).append("\",");
                }
            }
            sb.setCharAt(sb.length() - 1, ']');
            sb.append(']').append('}');
            return (T) sb.toString();
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class StringlyDomainPredicateTypeResolver implements DomainPredicateTypeResolver, DomainSerializer<DomainPredicateTypeResolver>, Serializable {

        private final String returningType;
        private final Set<String> supportedTypeNames;

        public StringlyDomainPredicateTypeResolver(String returningType, String... supportedTypeNames) {
            this.returningType = returningType;
            this.supportedTypeNames = new HashSet<>(Arrays.asList(supportedTypeNames));
        }

        @Override
        public DomainType resolveType(DomainModel domainModel, List<DomainType> domainTypes) {
            for (int i = 0; i < domainTypes.size(); i++) {
                DomainType domainType = domainTypes.get(i);
                if (!supportedTypeNames.contains(domainType.getName())) {
                    List<DomainType> types = new ArrayList<>(supportedTypeNames.size());
                    for (String typeName : supportedTypeNames) {
                        types.add(domainModel.getType(typeName));
                    }
                    if (domainType.getMetadata(StringlyTypeHandler.class) == null) {
                        throw new DomainTypeResolverException("The predicate operand at index " + i + " with the domain type '" + domainType + "' is unsupported! Expected one of the following types: " + types);
                    }
                }
            }
            return domainModel.getType(returningType);
        }

        @Override
        public <T> T serialize(DomainModel domainModel, DomainPredicateTypeResolver element, Class<T> targetType, String format, Map<String, Object> properties) {
            if (targetType != String.class || !"json".equals(format)) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("{\"RestrictedDomainPredicateTypeResolver\":[");
            sb.append('"').append(domainModel.getType(returningType).getName()).append("\",[");
            for (DomainType domainType : domainModel.getTypes().values()) {
                String typeName = domainType.getName();
                if (supportedTypeNames.contains(typeName) || domainType.getMetadata(StringlyTypeHandler.class) != null) {
                    sb.append('"').append(typeName).append("\",");
                }
            }
            sb.setCharAt(sb.length() - 1, ']');
            sb.append(']').append('}');
            return (T) sb.toString();
        }
    }

}
