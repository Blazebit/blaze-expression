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
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class FloorFunction implements FunctionInvoker, Serializable {

    private static final FloorFunction INSTANCE = new FloorFunction();

    private FloorFunction() {
    }

    /**
     * Adds the FLOOR function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("FLOOR")
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("FLOOR", classLoader))
                .withExactArgumentCount(1)
                .withArgument("number", BaseContributor.INTEGER_OR_NUMERIC_TYPE_NAME, DocumentationMetadataDefinition.localized("FLOOR_ARG", classLoader))
                .build();
        domainBuilder.withFunctionTypeResolver("FLOOR", StaticDomainFunctionTypeResolvers.returning(BaseContributor.INTEGER_TYPE_NAME));
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Object argument = arguments.getValue(0);
        if (argument == null) {
            return null;
        }

        if (argument instanceof BigInteger || argument instanceof Long) {
            return argument;
        } else if (argument instanceof BigDecimal) {
            return ((BigDecimal) argument).setScale(0, RoundingMode.FLOOR).toBigInteger();
        } else if (argument instanceof Double) {
            return Math.floor((Double) argument);
        } else {
            throw new DomainModelException("Illegal argument for CEIL function: " + argument);
        }
    }
}
