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

package com.blazebit.expression.base.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.StaticDomainFunctionTypeResolvers;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class RoundFunction implements FunctionInvoker, Serializable {

    private static final RoundFunction INSTANCE = new RoundFunction();

    private RoundFunction() {
    }

    /**
     * Adds the ROUND function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("ROUND")
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("ROUND", classLoader))
                .withArgument("value", BaseContributor.NUMERIC_TYPE_NAME, DocumentationMetadataDefinition.localized("ROUND_VALUE", classLoader))
                .withArgument("precision", BaseContributor.INTEGER_TYPE_NAME, DocumentationMetadataDefinition.localized("ROUND_PRECISION", classLoader))
                .withMinArgumentCount(1)
                .build();
        domainBuilder.withFunctionTypeResolver("ROUND", StaticDomainFunctionTypeResolvers.returning(BaseContributor.NUMERIC_TYPE_NAME));
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Object value = arguments.getValue(0);
        if (value == null) {
            return null;
        }
        int prec = 0;
        if (arguments.assignedArguments() > 1) {
            Object precision = arguments.getValue(1);
            if (precision == null) {
                return null;
            }
            prec = ((Number) precision).intValue();
        }

        return new BigDecimal(((Number) value).doubleValue()).round(new MathContext(prec, RoundingMode.HALF_UP));
    }
}
