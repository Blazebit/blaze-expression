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
import java.util.Collection;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class LeastFunction implements FunctionInvoker, Serializable {

    private static final LeastFunction INSTANCE = new LeastFunction();

    private LeastFunction() {
    }

    /**
     * Adds the LEAST function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("LEAST")
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("LEAST", classLoader))
                .withMinArgumentCount(2)
                .withArgument("first", DocumentationMetadataDefinition.localized("LEAST_FIRST", classLoader))
                .withArgument("second", DocumentationMetadataDefinition.localized("LEAST_SECOND", classLoader))
                .withArgument("other", DocumentationMetadataDefinition.localized("LEAST_OTHER", classLoader))
                .build();
        domainBuilder.withFunctionTypeResolver("LEAST", StaticDomainFunctionTypeResolvers.widest(BaseContributor.NUMERIC_TYPE_NAME));
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        Comparable least = (Comparable) arguments.getValue(0);
        Object second = arguments.getValue(1);
        Collection<Object> other = (Collection<Object>) arguments.getValue(2);
        if (second != null && (least == null || least.compareTo(second) > 0)) {
            least = (Comparable) second;
        }
        if (other != null && !other.isEmpty()) {
            for (Object value : other) {
                // TODO: automatic widening of arguments?
                if (value != null && (least == null || least.compareTo(value) > 0)) {
                    least = (Comparable) value;
                }
            }
        }
        return least;
    }
}
