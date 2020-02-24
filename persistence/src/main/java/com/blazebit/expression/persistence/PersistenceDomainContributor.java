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

package com.blazebit.expression.persistence;

import com.blazebit.apt.service.ServiceProvider;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.BooleanLiteralResolver;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicateType;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.NumericLiteralResolver;
import com.blazebit.domain.runtime.model.ResolvedLiteral;
import com.blazebit.domain.runtime.model.StaticDomainOperationTypeResolvers;
import com.blazebit.domain.runtime.model.StringLiteralResolver;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.domain.runtime.model.TemporalLiteralResolver;
import com.blazebit.domain.spi.DomainContributor;
import com.blazebit.expression.persistence.function.AbsFunction;
import com.blazebit.expression.persistence.function.Atan2Function;
import com.blazebit.expression.persistence.function.CeilFunction;
import com.blazebit.expression.persistence.function.CurrentDateFunction;
import com.blazebit.expression.persistence.function.CurrentTimeFunction;
import com.blazebit.expression.persistence.function.CurrentTimestampFunction;
import com.blazebit.expression.persistence.function.EndsWithFunction;
import com.blazebit.expression.persistence.function.FloorFunction;
import com.blazebit.expression.persistence.function.GreatestFunction;
import com.blazebit.expression.persistence.function.LTrimFunction;
import com.blazebit.expression.persistence.function.LeastFunction;
import com.blazebit.expression.persistence.function.LengthFunction;
import com.blazebit.expression.persistence.function.LocateFunction;
import com.blazebit.expression.persistence.function.LocateLastFunction;
import com.blazebit.expression.persistence.function.LowerFunction;
import com.blazebit.expression.persistence.function.NumericFunction;
import com.blazebit.expression.persistence.function.PowFunction;
import com.blazebit.expression.persistence.function.RTrimFunction;
import com.blazebit.expression.persistence.function.RandomFunction;
import com.blazebit.expression.persistence.function.ReplaceFunction;
import com.blazebit.expression.persistence.function.RoundFunction;
import com.blazebit.expression.persistence.function.SizeFunction;
import com.blazebit.expression.persistence.function.StartsWithFunction;
import com.blazebit.expression.persistence.function.SubstringFunction;
import com.blazebit.expression.persistence.function.TrimFunction;
import com.blazebit.expression.persistence.function.UpperFunction;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainOperatorInterpreter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@ServiceProvider(DomainContributor.class)
public class PersistenceDomainContributor implements DomainContributor {

    // NOTE: Copied to TypeAdapterRegistry. Keep in sync
    public static final Class<?> BOOLEAN = Boolean.class;
    public static final Class<?> INTEGER = BigInteger.class;
    public static final Class<?> NUMERIC = BigDecimal.class;
    public static final Class<?> TIMESTAMP = Instant.class;
    public static final Class<?> TIME = LocalTime.class;
    public static final Class<?> INTERVAL = TemporalInterval.class;
    public static final Class<?> STRING = String.class;

    public static final BooleanLiteralResolver BOOLEAN_LITERAL_TYPE_RESOLVER = new BooleanLiteralResolver() {
        @Override
        public ResolvedLiteral resolveLiteral(DomainModel domainModel, boolean value) {
            return new DefaultResolvedLiteral(domainModel.getType(BOOLEAN), value);
        }
    };
    public static final NumericLiteralResolver NUMERIC_LITERAL_TYPE_RESOLVER = new NumericLiteralResolver() {
        @Override
        public ResolvedLiteral resolveLiteral(DomainModel domainModel, Number value) {
            if (value instanceof BigDecimal && ((BigDecimal) value).scale() > 0) {
                return new DefaultResolvedLiteral(domainModel.getType(NUMERIC), value);
            } else if (value instanceof BigInteger) {
                return new DefaultResolvedLiteral(domainModel.getType(INTEGER), value);
            }
            return new DefaultResolvedLiteral(domainModel.getType(INTEGER), BigInteger.valueOf(value.longValue()));
        }
    };
    public static final TemporalLiteralResolver TEMPORAL_LITERAL_TYPE_RESOLVER = new TemporalLiteralResolver() {
        @Override
        public ResolvedLiteral resolveTimestampLiteral(DomainModel domainModel, Instant value) {
            return new DefaultResolvedLiteral(domainModel.getType(TIMESTAMP), value);
        }

        @Override
        public ResolvedLiteral resolveIntervalLiteral(DomainModel domainModel, TemporalInterval value) {
            return new DefaultResolvedLiteral(domainModel.getType(INTERVAL), value);
        }
    };
    public static final StringLiteralResolver STRING_LITERAL_TYPE_RESOLVER = new StringLiteralResolver() {
        @Override
        public ResolvedLiteral resolveLiteral(DomainModel domainModel, String value) {
            return new DefaultResolvedLiteral(domainModel.getType(STRING), value);
        }
    };

    @Override
    public void contribute(DomainBuilder domainBuilder) {
        createBasicType(domainBuilder, INTEGER, DomainOperator.arithmetic(), DomainPredicateType.comparable(), handlersFor(NumericOperatorHandler.INSTANCE));
        createBasicType(domainBuilder, NUMERIC, DomainOperator.arithmetic(), DomainPredicateType.comparable(), handlersFor(NumericOperatorHandler.INSTANCE));
        createBasicType(domainBuilder, STRING, new DomainOperator[]{ DomainOperator.PLUS }, DomainPredicateType.comparable(), handlersFor(StringOperatorHandler.INSTANCE));
        createBasicType(domainBuilder, TIMESTAMP, new DomainOperator[]{ DomainOperator.PLUS, DomainOperator.MINUS }, DomainPredicateType.comparable(), handlersFor(TimestampOperatorHandler.INSTANCE));
        createBasicType(domainBuilder, TIME, new DomainOperator[]{ DomainOperator.PLUS, DomainOperator.MINUS }, DomainPredicateType.comparable(), handlersFor(TimeOperatorHandler.INSTANCE));
        createBasicType(domainBuilder, BOOLEAN, new DomainOperator[]{ DomainOperator.NOT }, DomainPredicateType.distinguishable(), handlersFor(BooleanOperatorHandler.INSTANCE));
        domainBuilder.withNumericLiteralResolver(NUMERIC_LITERAL_TYPE_RESOLVER);
        domainBuilder.withStringLiteralResolver(STRING_LITERAL_TYPE_RESOLVER);
        domainBuilder.withTemporalLiteralResolver(TEMPORAL_LITERAL_TYPE_RESOLVER);
        domainBuilder.withBooleanLiteralResolver(BOOLEAN_LITERAL_TYPE_RESOLVER);

        for (Class<?> type : Arrays.asList(INTEGER, NUMERIC)) {
            domainBuilder.withOperationTypeResolver(type, DomainOperator.MODULO, StaticDomainOperationTypeResolvers.returning(INTEGER));
            domainBuilder.withOperationTypeResolver(type, DomainOperator.UNARY_MINUS, StaticDomainOperationTypeResolvers.returning(type));
            domainBuilder.withOperationTypeResolver(type, DomainOperator.UNARY_PLUS, StaticDomainOperationTypeResolvers.returning(type));
            domainBuilder.withOperationTypeResolver(type, DomainOperator.DIVISION, StaticDomainOperationTypeResolvers.returning(NUMERIC));
            for (DomainOperator domainOperator : Arrays.asList(DomainOperator.PLUS, DomainOperator.MINUS, DomainOperator.MULTIPLICATION)) {
                domainBuilder.withOperationTypeResolver(type, domainOperator, StaticDomainOperationTypeResolvers.widest(NUMERIC, INTEGER));
            }
        }

        domainBuilder.withOperationTypeResolver(STRING, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.returning(STRING));
        domainBuilder.withOperationTypeResolver(BOOLEAN, DomainOperator.NOT, StaticDomainOperationTypeResolvers.returning(BOOLEAN));

        domainBuilder.withOperationTypeResolver(TIMESTAMP, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.returning(TIMESTAMP));
        domainBuilder.withOperationTypeResolver(TIMESTAMP, DomainOperator.MINUS, StaticDomainOperationTypeResolvers.returning(TIMESTAMP));

        domainBuilder.withOperationTypeResolver(TIME, DomainOperator.PLUS, StaticDomainOperationTypeResolvers.returning(TIME));
        domainBuilder.withOperationTypeResolver(TIME, DomainOperator.MINUS, StaticDomainOperationTypeResolvers.returning(TIME));

        CurrentTimestampFunction.addFunction(domainBuilder);
        CurrentDateFunction.addFunction(domainBuilder);
        CurrentTimeFunction.addFunction(domainBuilder);
        SubstringFunction.addFunction(domainBuilder);
        ReplaceFunction.addFunction(domainBuilder);
        TrimFunction.addFunction(domainBuilder);
        LTrimFunction.addFunction(domainBuilder);
        RTrimFunction.addFunction(domainBuilder);
        UpperFunction.addFunction(domainBuilder);
        LowerFunction.addFunction(domainBuilder);
        LengthFunction.addFunction(domainBuilder);
        LocateFunction.addFunction(domainBuilder);
        LocateLastFunction.addFunction(domainBuilder);
        StartsWithFunction.addFunction(domainBuilder);
        EndsWithFunction.addFunction(domainBuilder);
        AbsFunction.addFunction(domainBuilder);
        CeilFunction.addFunction(domainBuilder);
        FloorFunction.addFunction(domainBuilder);
        NumericFunction.addFunction(domainBuilder);
        Atan2Function.addFunction(domainBuilder);
        RoundFunction.addFunction(domainBuilder);
        RandomFunction.addFunction(domainBuilder);
        PowFunction.addFunction(domainBuilder);
        GreatestFunction.addFunction(domainBuilder);
        LeastFunction.addFunction(domainBuilder);
        SizeFunction.addFunction(domainBuilder);
    }

    private <T extends ComparisonOperatorInterpreter & DomainOperatorInterpreter> MetadataDefinition<?>[] handlersFor(T instance) {
        return new MetadataDefinition[] {
            new ComparisonOperatorInterpreterMetadataDefinition(instance),
            new DomainOperatorInterpreterMetadataDefinition(instance),
            new DomainOperatorRendererMetadataDefinition(DomainOperatorRenderer.SIMPLE)
        };
    }

    private <T extends DomainOperatorRenderer & ComparisonOperatorInterpreter & DomainOperatorInterpreter> MetadataDefinition<?>[] handlersFor(T instance) {
        return new MetadataDefinition[] {
            new ComparisonOperatorInterpreterMetadataDefinition(instance),
            new DomainOperatorInterpreterMetadataDefinition(instance),
            new DomainOperatorRendererMetadataDefinition(instance)
        };
    }

    private static void createBasicType(DomainBuilder domainBuilder, Class<?> type, DomainOperator[] operators, DomainPredicateType[] predicates, MetadataDefinition<?>... metadataDefinitions) {
        String typeName = type.getSimpleName();
        domainBuilder.createBasicType(typeName, type, metadataDefinitions);
        domainBuilder.withOperator(typeName, operators);
        domainBuilder.withPredicate(typeName, predicates);
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class ComparisonOperatorInterpreterMetadataDefinition implements MetadataDefinition<ComparisonOperatorInterpreter> {

        private final ComparisonOperatorInterpreter comparisonOperatorInterpreter;

        public ComparisonOperatorInterpreterMetadataDefinition(ComparisonOperatorInterpreter comparisonOperatorInterpreter) {
            this.comparisonOperatorInterpreter = comparisonOperatorInterpreter;
        }

        @Override
        public Class<ComparisonOperatorInterpreter> getJavaType() {
            return ComparisonOperatorInterpreter.class;
        }

        @Override
        public ComparisonOperatorInterpreter build(MetadataDefinitionHolder<?> definitionHolder) {
            return comparisonOperatorInterpreter;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class DomainOperatorInterpreterMetadataDefinition implements MetadataDefinition<DomainOperatorInterpreter> {

        private final DomainOperatorInterpreter domainOperatorInterpreter;

        public DomainOperatorInterpreterMetadataDefinition(DomainOperatorInterpreter domainOperatorInterpreter) {
            this.domainOperatorInterpreter = domainOperatorInterpreter;
        }

        @Override
        public Class<DomainOperatorInterpreter> getJavaType() {
            return DomainOperatorInterpreter.class;
        }

        @Override
        public DomainOperatorInterpreter build(MetadataDefinitionHolder<?> definitionHolder) {
            return domainOperatorInterpreter;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class DomainOperatorRendererMetadataDefinition implements MetadataDefinition<DomainOperatorRenderer> {

        private final DomainOperatorRenderer domainOperatorRenderer;

        public DomainOperatorRendererMetadataDefinition(DomainOperatorRenderer domainOperatorRenderer) {
            this.domainOperatorRenderer = domainOperatorRenderer;
        }

        @Override
        public Class<DomainOperatorRenderer> getJavaType() {
            return DomainOperatorRenderer.class;
        }

        @Override
        public DomainOperatorRenderer build(MetadataDefinitionHolder<?> definitionHolder) {
            return domainOperatorRenderer;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class DefaultResolvedLiteral implements ResolvedLiteral {

        private final DomainType type;
        private final Object value;

        public DefaultResolvedLiteral(DomainType type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public DomainType getType() {
            return type;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DefaultResolvedLiteral that = (DefaultResolvedLiteral) o;
            return Objects.equals(type, that.type) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }
    }

}
