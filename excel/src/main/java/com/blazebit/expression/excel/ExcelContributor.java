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

package com.blazebit.expression.excel;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.DomainFunctionDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.spi.DomainContributor;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.base.GlobalStringlyTypeDestructorFunctionInvoker;
import com.blazebit.expression.base.StringlyTypeConstructorFunctionInvoker;
import com.blazebit.expression.base.StringlyTypeDestructorFunctionInvoker;
import com.blazebit.expression.excel.function.ExcelAbsFunction;
import com.blazebit.expression.excel.function.ExcelAtan2Function;
import com.blazebit.expression.excel.function.ExcelCeilFunction;
import com.blazebit.expression.excel.function.ExcelCurrentDateFunction;
import com.blazebit.expression.excel.function.ExcelCurrentTimeFunction;
import com.blazebit.expression.excel.function.ExcelCurrentTimestampFunction;
import com.blazebit.expression.excel.function.ExcelEndsWithFunction;
import com.blazebit.expression.excel.function.ExcelFloorFunction;
import com.blazebit.expression.excel.function.ExcelFunctionRendererMetadataDefinition;
import com.blazebit.expression.excel.function.ExcelGreatestFunction;
import com.blazebit.expression.excel.function.ExcelLTrimFunction;
import com.blazebit.expression.excel.function.ExcelLeastFunction;
import com.blazebit.expression.excel.function.ExcelLengthFunction;
import com.blazebit.expression.excel.function.ExcelLocateFunction;
import com.blazebit.expression.excel.function.ExcelLocateLastFunction;
import com.blazebit.expression.excel.function.ExcelLowerFunction;
import com.blazebit.expression.excel.function.ExcelNumericFunction;
import com.blazebit.expression.excel.function.ExcelPowFunction;
import com.blazebit.expression.excel.function.ExcelRTrimFunction;
import com.blazebit.expression.excel.function.ExcelRandomFunction;
import com.blazebit.expression.excel.function.ExcelReplaceFunction;
import com.blazebit.expression.excel.function.ExcelRoundFunction;
import com.blazebit.expression.excel.function.ExcelSizeFunction;
import com.blazebit.expression.excel.function.ExcelStartsWithFunction;
import com.blazebit.expression.excel.function.ExcelSubstringFunction;
import com.blazebit.expression.excel.function.ExcelTrimFunction;
import com.blazebit.expression.excel.function.ExcelUpperFunction;
import com.blazebit.expression.spi.FunctionInvoker;

import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelContributor implements DomainContributor {

    @Override
    public void contribute(DomainBuilder domainBuilder) {
        domainBuilder.extendBasicType(BaseContributor.INTEGER_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelNumericOperatorRenderer.INSTANCE), new ExcelLiteralRendererMetadataDefinition(ExcelLiteralRenderer.SIMPLE));
        domainBuilder.extendBasicType(BaseContributor.NUMERIC_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelNumericOperatorRenderer.INSTANCE), new ExcelLiteralRendererMetadataDefinition(ExcelLiteralRenderer.SIMPLE));
        domainBuilder.extendBasicType(BaseContributor.STRING_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelStringOperatorRenderer.INSTANCE), new ExcelLiteralRendererMetadataDefinition(ExcelStringLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.TIMESTAMP_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelTimestampOperatorRenderer.INSTANCE), new ExcelLiteralRendererMetadataDefinition(ExcelTimestampLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.TIME_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelTimeOperatorRenderer.INSTANCE), new ExcelLiteralRendererMetadataDefinition(ExcelTimeLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.DATE_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelDateOperatorRenderer.INSTANCE), new ExcelLiteralRendererMetadataDefinition(ExcelDateLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.INTERVAL_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelIntervalOperatorRenderer.INSTANCE), new ExcelLiteralRendererMetadataDefinition(ExcelIntervalLiteralRenderer.INSTANCE));
        domainBuilder.extendBasicType(BaseContributor.BOOLEAN_TYPE_NAME, new ExcelDomainOperatorRendererMetadataDefinition(ExcelDomainOperatorRenderer.SIMPLE), new ExcelLiteralRendererMetadataDefinition(ExcelBooleanLiteralRenderer.INSTANCE));

        for (DomainFunctionDefinition functionDefinition : domainBuilder.getFunctions().values()) {
            Map<Class<?>, MetadataDefinition<?>> metadataDefinitions = functionDefinition.getMetadataDefinitions();
            if (!metadataDefinitions.containsKey(ExcelFunctionRenderer.class)) {
                // For functions that do not contain an excel function renderer,
                // we check if they are stringly typed, and if so, by default assume we can render-through
                MetadataDefinition<FunctionInvoker> metadataDefinition = (MetadataDefinition<FunctionInvoker>) functionDefinition.getMetadataDefinitions().get(FunctionInvoker.class);
                FunctionInvoker functionInvoker = metadataDefinition.build(null);
                if (functionInvoker instanceof StringlyTypeDestructorFunctionInvoker) {
                    domainBuilder.extendFunction(functionDefinition.getName(), new ExcelFunctionRendererMetadataDefinition(new ExcelStringlyTypeDestructorFunctionRenderer(ExcelStringlyTypeHandler.INSTANCE)));
                } else if (functionInvoker instanceof StringlyTypeConstructorFunctionInvoker) {
                    domainBuilder.extendFunction(functionDefinition.getName(), new ExcelFunctionRendererMetadataDefinition(new ExcelStringlyTypeConstructorFunctionRenderer(ExcelStringlyTypeHandler.INSTANCE)));
                } else if (functionInvoker instanceof GlobalStringlyTypeDestructorFunctionInvoker) {
                    GlobalStringlyTypeDestructorFunctionInvoker functionHandler = (GlobalStringlyTypeDestructorFunctionInvoker) functionInvoker;
                    domainBuilder.extendFunction(functionDefinition.getName(), new ExcelFunctionRendererMetadataDefinition(new ExcelGlobalStringlyTypeDestructorFunctionRenderer(functionHandler, functionDefinition.getName())));
                }
            }
        }

        ExcelCurrentTimestampFunction.addFunction(domainBuilder);
        ExcelCurrentDateFunction.addFunction(domainBuilder);
        ExcelCurrentTimeFunction.addFunction(domainBuilder);
        ExcelSubstringFunction.addFunction(domainBuilder);
        ExcelReplaceFunction.addFunction(domainBuilder);
        ExcelTrimFunction.addFunction(domainBuilder);
        ExcelLTrimFunction.addFunction(domainBuilder);
        ExcelRTrimFunction.addFunction(domainBuilder);
        ExcelUpperFunction.addFunction(domainBuilder);
        ExcelLowerFunction.addFunction(domainBuilder);
        ExcelLengthFunction.addFunction(domainBuilder);
        ExcelLocateFunction.addFunction(domainBuilder);
        ExcelLocateLastFunction.addFunction(domainBuilder);
        ExcelStartsWithFunction.addFunction(domainBuilder);
        ExcelEndsWithFunction.addFunction(domainBuilder);
        ExcelAbsFunction.addFunction(domainBuilder);
        ExcelCeilFunction.addFunction(domainBuilder);
        ExcelFloorFunction.addFunction(domainBuilder);
        ExcelNumericFunction.addFunction(domainBuilder);
        ExcelAtan2Function.addFunction(domainBuilder);
        ExcelRoundFunction.addFunction(domainBuilder);
        ExcelRandomFunction.addFunction(domainBuilder);
        ExcelPowFunction.addFunction(domainBuilder);
        ExcelGreatestFunction.addFunction(domainBuilder);
        ExcelLeastFunction.addFunction(domainBuilder);
        ExcelSizeFunction.addFunction(domainBuilder);
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
    static class ExcelDomainOperatorRendererMetadataDefinition implements MetadataDefinition<ExcelDomainOperatorRenderer> {

        private final ExcelDomainOperatorRenderer excelDomainOperatorRenderer;

        /**
         * Creates a metadata definition for the given {@link ExcelDomainOperatorRenderer}.
         *
         * @param excelDomainOperatorRenderer The domain operator renderer
         */
        public ExcelDomainOperatorRendererMetadataDefinition(ExcelDomainOperatorRenderer excelDomainOperatorRenderer) {
            this.excelDomainOperatorRenderer = excelDomainOperatorRenderer;
        }

        @Override
        public Class<ExcelDomainOperatorRenderer> getJavaType() {
            return ExcelDomainOperatorRenderer.class;
        }

        @Override
        public ExcelDomainOperatorRenderer build(MetadataDefinitionHolder definitionHolder) {
            return excelDomainOperatorRenderer;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    static class ExcelLiteralRendererMetadataDefinition implements MetadataDefinition<ExcelLiteralRenderer> {

        private final ExcelLiteralRenderer excelLiteralRenderer;

        /**
         * Creates a metadata definition for the given {@link ExcelLiteralRenderer}.
         *
         * @param excelLiteralRenderer The literal renderer
         */
        public ExcelLiteralRendererMetadataDefinition(ExcelLiteralRenderer excelLiteralRenderer) {
            this.excelLiteralRenderer = excelLiteralRenderer;
        }

        @Override
        public Class<ExcelLiteralRenderer> getJavaType() {
            return ExcelLiteralRenderer.class;
        }

        @Override
        public ExcelLiteralRenderer build(MetadataDefinitionHolder definitionHolder) {
            return excelLiteralRenderer;
        }
    }

}
