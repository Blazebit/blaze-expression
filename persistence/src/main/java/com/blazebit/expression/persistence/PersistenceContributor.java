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

package com.blazebit.expression.persistence;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.DomainFunctionDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.spi.DomainContributor;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.base.GlobalStringlyTypeDestructorFunctionInvoker;
import com.blazebit.expression.base.StringlyTypeConstructorFunctionInvoker;
import com.blazebit.expression.base.StringlyTypeDestructorFunctionInvoker;
import com.blazebit.expression.persistence.function.PersistenceAbsFunction;
import com.blazebit.expression.persistence.function.PersistenceAtan2Function;
import com.blazebit.expression.persistence.function.PersistenceCeilFunction;
import com.blazebit.expression.persistence.function.PersistenceCurrentDateFunction;
import com.blazebit.expression.persistence.function.PersistenceCurrentTimeFunction;
import com.blazebit.expression.persistence.function.PersistenceCurrentTimestampFunction;
import com.blazebit.expression.persistence.function.PersistenceEndsWithFunction;
import com.blazebit.expression.persistence.function.PersistenceFloorFunction;
import com.blazebit.expression.persistence.function.PersistenceFunctionRendererMetadataDefinition;
import com.blazebit.expression.persistence.function.PersistenceGreatestFunction;
import com.blazebit.expression.persistence.function.PersistenceLTrimFunction;
import com.blazebit.expression.persistence.function.PersistenceLeastFunction;
import com.blazebit.expression.persistence.function.PersistenceLengthFunction;
import com.blazebit.expression.persistence.function.PersistenceLocateFunction;
import com.blazebit.expression.persistence.function.PersistenceLocateLastFunction;
import com.blazebit.expression.persistence.function.PersistenceLowerFunction;
import com.blazebit.expression.persistence.function.PersistenceNumericFunction;
import com.blazebit.expression.persistence.function.PersistencePowFunction;
import com.blazebit.expression.persistence.function.PersistenceRTrimFunction;
import com.blazebit.expression.persistence.function.PersistenceRandomFunction;
import com.blazebit.expression.persistence.function.PersistenceReplaceFunction;
import com.blazebit.expression.persistence.function.PersistenceRoundFunction;
import com.blazebit.expression.persistence.function.PersistenceSizeFunction;
import com.blazebit.expression.persistence.function.PersistenceStartsWithFunction;
import com.blazebit.expression.persistence.function.PersistenceSubstringFunction;
import com.blazebit.expression.persistence.function.PersistenceTrimFunction;
import com.blazebit.expression.persistence.function.PersistenceUpperFunction;
import com.blazebit.expression.spi.FunctionInvoker;

import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceContributor implements DomainContributor {

    @Override
    public void contribute(DomainBuilder domainBuilder) {
        domainBuilder.extendBasicType(BaseContributor.INTEGER_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceDomainOperatorRenderer.SIMPLE), new PersistenceLiteralRendererMetadataDefinition(PersistenceLiteralRenderer.SIMPLE));
        domainBuilder.extendBasicType(BaseContributor.NUMERIC_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceDomainOperatorRenderer.SIMPLE), new PersistenceLiteralRendererMetadataDefinition(PersistenceLiteralRenderer.SIMPLE));
        domainBuilder.extendBasicType(BaseContributor.STRING_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceStringOperatorRenderer.INSTANCE), new PersistenceLiteralRendererMetadataDefinition(PersistenceStringLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.TIMESTAMP_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceTimestampOperatorRenderer.INSTANCE), new PersistenceLiteralRendererMetadataDefinition(PersistenceTimestampLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.TIME_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceTimeOperatorRenderer.INSTANCE), new PersistenceLiteralRendererMetadataDefinition(PersistenceTimeLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.DATE_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceDateOperatorRenderer.INSTANCE), new PersistenceLiteralRendererMetadataDefinition(PersistenceDateLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.INTERVAL_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceIntervalOperatorRenderer.INSTANCE), new PersistenceLiteralRendererMetadataDefinition(PersistenceIntervalLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.BOOLEAN_TYPE_NAME, new PersistenceDomainOperatorRendererMetadataDefinition(PersistenceDomainOperatorRenderer.SIMPLE), new PersistenceLiteralRendererMetadataDefinition(PersistenceLiteralRenderer.SIMPLE));

        for (DomainFunctionDefinition functionDefinition : domainBuilder.getFunctions().values()) {
            Map<Class<?>, MetadataDefinition<?>> metadataDefinitions = functionDefinition.getMetadataDefinitions();
            if (!metadataDefinitions.containsKey(PersistenceFunctionRenderer.class)) {
                // For functions that do not contain a persistence function renderer,
                // we check if they are stringly typed, and if so, by default assume we can render-through
                MetadataDefinition<FunctionInvoker> metadataDefinition = (MetadataDefinition<FunctionInvoker>) metadataDefinitions.get(FunctionInvoker.class);
                FunctionInvoker functionInvoker = metadataDefinition.build(null);
                if (functionInvoker instanceof StringlyTypeDestructorFunctionInvoker) {
                    domainBuilder.extendFunction(functionDefinition.getName(), new PersistenceFunctionRendererMetadataDefinition(new PersistenceStringlyTypeDestructorFunctionRenderer(PersistenceStringlyTypeHandler.INSTANCE)));
                } else if (functionInvoker instanceof StringlyTypeConstructorFunctionInvoker) {
                    domainBuilder.extendFunction(functionDefinition.getName(), new PersistenceFunctionRendererMetadataDefinition(new PersistenceStringlyTypeConstructorFunctionRenderer(PersistenceStringlyTypeHandler.INSTANCE)));
                } else if (functionInvoker instanceof GlobalStringlyTypeDestructorFunctionInvoker) {
                    GlobalStringlyTypeDestructorFunctionInvoker functionHandler = (GlobalStringlyTypeDestructorFunctionInvoker) functionInvoker;
                    domainBuilder.extendFunction(functionDefinition.getName(), new PersistenceFunctionRendererMetadataDefinition(new PersistenceGlobalStringlyTypeDestructorFunctionRenderer(functionHandler, functionDefinition.getName())));
                }
            }
        }

        PersistenceCurrentTimestampFunction.addFunction(domainBuilder);
        PersistenceCurrentDateFunction.addFunction(domainBuilder);
        PersistenceCurrentTimeFunction.addFunction(domainBuilder);
        PersistenceSubstringFunction.addFunction(domainBuilder);
        PersistenceReplaceFunction.addFunction(domainBuilder);
        PersistenceTrimFunction.addFunction(domainBuilder);
        PersistenceLTrimFunction.addFunction(domainBuilder);
        PersistenceRTrimFunction.addFunction(domainBuilder);
        PersistenceUpperFunction.addFunction(domainBuilder);
        PersistenceLowerFunction.addFunction(domainBuilder);
        PersistenceLengthFunction.addFunction(domainBuilder);
        PersistenceLocateFunction.addFunction(domainBuilder);
        PersistenceLocateLastFunction.addFunction(domainBuilder);
        PersistenceStartsWithFunction.addFunction(domainBuilder);
        PersistenceEndsWithFunction.addFunction(domainBuilder);
        PersistenceAbsFunction.addFunction(domainBuilder);
        PersistenceCeilFunction.addFunction(domainBuilder);
        PersistenceFloorFunction.addFunction(domainBuilder);
        PersistenceNumericFunction.addFunction(domainBuilder);
        PersistenceAtan2Function.addFunction(domainBuilder);
        PersistenceRoundFunction.addFunction(domainBuilder);
        PersistenceRandomFunction.addFunction(domainBuilder);
        PersistencePowFunction.addFunction(domainBuilder);
        PersistenceGreatestFunction.addFunction(domainBuilder);
        PersistenceLeastFunction.addFunction(domainBuilder);
        PersistenceSizeFunction.addFunction(domainBuilder);
    }

    @Override
    public int priority() {
        // We use a priority of 600 so that user contributors, which usually don't configure a priority, are guaranteed to run afterwards
        return 600;
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    static class PersistenceDomainOperatorRendererMetadataDefinition implements MetadataDefinition<PersistenceDomainOperatorRenderer> {

        private final PersistenceDomainOperatorRenderer persistenceDomainOperatorRenderer;

        /**
         * Creates a metadata definition for the given {@link PersistenceDomainOperatorRenderer}.
         *
         * @param persistenceDomainOperatorRenderer The domain operator renderer
         */
        public PersistenceDomainOperatorRendererMetadataDefinition(PersistenceDomainOperatorRenderer persistenceDomainOperatorRenderer) {
            this.persistenceDomainOperatorRenderer = persistenceDomainOperatorRenderer;
        }

        @Override
        public Class<PersistenceDomainOperatorRenderer> getJavaType() {
            return PersistenceDomainOperatorRenderer.class;
        }

        @Override
        public PersistenceDomainOperatorRenderer build(MetadataDefinitionHolder definitionHolder) {
            return persistenceDomainOperatorRenderer;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    static class PersistenceLiteralRendererMetadataDefinition implements MetadataDefinition<PersistenceLiteralRenderer> {

        private final PersistenceLiteralRenderer persistenceLiteralRenderer;

        /**
         * Creates a metadata definition for the given {@link PersistenceLiteralRenderer}.
         *
         * @param persistenceLiteralRenderer The literal renderer
         */
        public PersistenceLiteralRendererMetadataDefinition(PersistenceLiteralRenderer persistenceLiteralRenderer) {
            this.persistenceLiteralRenderer = persistenceLiteralRenderer;
        }

        @Override
        public Class<PersistenceLiteralRenderer> getJavaType() {
            return PersistenceLiteralRenderer.class;
        }

        @Override
        public PersistenceLiteralRenderer build(MetadataDefinitionHolder definitionHolder) {
            return persistenceLiteralRenderer;
        }
    }

}
