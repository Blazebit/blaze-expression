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
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.base.BaseContributor;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class EndsWithFunction implements FunctionInvoker, Serializable {

    private static final EndsWithFunction INSTANCE = new EndsWithFunction();

    private EndsWithFunction() {
    }

    /**
     * Adds the ENDS_WITH function to the domain builder.
     *
     * @param domainBuilder The domain builder
     * @param classLoader The class loader for resource bundle resolving
     */
    public static void addFunction(DomainBuilder domainBuilder, ClassLoader classLoader) {
        domainBuilder.createFunction("ENDS_WITH")
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("ENDS_WITH", classLoader))
                .withMinArgumentCount(2)
                .withResultType(BaseContributor.BOOLEAN_TYPE_NAME)
                .withArgument("string", BaseContributor.STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("ENDS_WITH_STRING", classLoader))
                .withArgument("substring", BaseContributor.STRING_TYPE_NAME, DocumentationMetadataDefinition.localized("ENDS_WITH_SUBSTRING", classLoader))
                .build();
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
        String string = (String) arguments.getValue(1);
        if (string == null) {
            return null;
        }
        String substring = (String) arguments.getValue(0);
        if (substring == null) {
            return null;
        }
        return string.endsWith(substring);
    }

}
