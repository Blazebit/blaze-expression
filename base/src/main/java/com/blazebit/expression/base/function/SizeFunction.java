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
import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainFunctionTypeResolver;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class SizeFunction implements FunctionInvoker, DomainFunctionTypeResolver, Serializable {

    private final boolean exact;

    private SizeFunction(boolean exact) {
        this.exact = exact;
    }

    /**
     * Adds the SIZE function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        SizeFunction sizeFunction = new SizeFunction(domainBuilder.getType(BaseContributor.NUMERIC_TYPE_NAME).getJavaType() == BigDecimal.class);
        domainBuilder.createFunction("SIZE")
                .withMetadata(new FunctionInvokerMetadataDefinition(sizeFunction))
                .withMetadata(DocumentationMetadataDefinition.localized("SIZE", classLoader))
                .withExactArgumentCount(1)
                .withCollectionArgument("collection", DocumentationMetadataDefinition.localized("SIZE_ARG", classLoader))
                .withResultType(BaseContributor.INTEGER_TYPE_NAME)
                .build();
        domainBuilder.withFunctionTypeResolver("SIZE", sizeFunction);
    }

    @Override
    public DomainType resolveType(DomainModel domainModel, DomainFunction function, Map<DomainFunctionArgument, DomainType> argumentTypes) {
        DomainType argumentType = argumentTypes.values().iterator().next();
        if (!(argumentType instanceof CollectionDomainType)) {
            throw new DomainModelException("SIZE only accepts a collection argument! Invalid type given: " + argumentType);
        }
        return domainModel.getType(BaseContributor.INTEGER_TYPE_NAME);
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Object argument = arguments.getValue(0);
        if (argument == null) {
            return null;
        }

        if (argument instanceof Collection<?>) {
            int size = ((Collection<?>) argument).size();
            if (exact) {
                return BigInteger.valueOf(size);
            } else {
                return (long) size;
            }
        } else {
            throw new DomainModelException("Illegal argument for SIZE function: " + argument);
        }
    }

}
