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

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class Atan2Function implements FunctionInvoker, Serializable {

    private final boolean exact;

    private Atan2Function(boolean exact) {
        this.exact = exact;
    }

    /**
     * Adds the ATAN2 function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("ATAN2")
                .withMetadata(new FunctionInvokerMetadataDefinition(new Atan2Function(domainBuilder.getType(BaseContributor.NUMERIC_TYPE_NAME).getJavaType() == BigDecimal.class)))
                .withMetadata(DocumentationMetadataDefinition.localized("ATAN2", classLoader))
                .withArgument("y", BaseContributor.INTEGER_OR_NUMERIC_TYPE_NAME, DocumentationMetadataDefinition.localized("ATAN2_Y", classLoader))
                .withArgument("x", BaseContributor.INTEGER_OR_NUMERIC_TYPE_NAME, DocumentationMetadataDefinition.localized("ATAN2_X", classLoader))
                .build();
        domainBuilder.withFunctionTypeResolver("ATAN2", StaticDomainFunctionTypeResolvers.returning(BaseContributor.NUMERIC_TYPE_NAME));
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Object y = arguments.getValue(0);
        if (y == null) {
            return null;
        }
        Object x = arguments.getValue(1);
        if (x == null) {
            return null;
        }

        double result = Math.atan2(((Number) y).doubleValue(), ((Number) x).doubleValue());
        if (exact) {
            return BigDecimal.valueOf(result);
        } else {
            return result;
        }
    }
}
