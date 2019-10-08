/*
 * Copyright 2019 Blazebit.
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

package com.blazebit.expression.persistence.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainFunctionTypeResolver;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.FunctionInvoker;
import com.blazebit.expression.persistence.FunctionRenderer;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.INTEGER;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class SizeFunction implements FunctionRenderer, FunctionInvoker {

    private static final SizeFunction INSTANCE = new SizeFunction();

    private SizeFunction() {
    }

    /**
     * Adds the SIZE function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.createFunction("SIZE")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withExactArgumentCount(1)
                .withCollectionArgument("collection")
                .withResultType(INTEGER)
                .build();
        domainBuilder.withFunctionTypeResolver("SIZE", new DomainFunctionTypeResolver() {
            @Override
            public DomainType resolveType(DomainModel domainModel, DomainFunction function, Map<DomainFunctionArgument, DomainType> argumentTypes) {
                DomainType argumentType = argumentTypes.values().iterator().next();
                if (!(argumentType instanceof CollectionDomainType)) {
                    throw new IllegalArgumentException("SIZE only accepts a collection argument! Invalid type given: " + argumentType);
                }
                return domainModel.getType(INTEGER);
            }
        });
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        Object argument = arguments.get(function.getArgument(0));
        if (argument == null) {
            return null;
        }

        if (argument instanceof Collection<?>) {
            return ((Collection) argument).size();
        } else {
            throw new IllegalArgumentException("Illegal argument for SIZE function: " + argument);
        }
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb) {
        sb.append("SIZE(");
        argumentRenderers.values().iterator().next().accept(sb);
        sb.append(')');
    }
}
