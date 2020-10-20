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

package com.blazebit.expression.persistence.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.StaticDomainFunctionTypeResolvers;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.persistence.FunctionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.NUMERIC;
import static com.blazebit.expression.persistence.PersistenceDomainContributor.INTEGER;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class RoundFunction implements FunctionRenderer, FunctionInvoker, Serializable {

    private static final RoundFunction INSTANCE = new RoundFunction();

    private RoundFunction() {
    }

    /**
     * Adds the ROUND function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.createFunction("ROUND")
                .withMetadata(new FunctionRendererMetadataDefinition(INSTANCE))
                .withMetadata(new FunctionInvokerMetadataDefinition(INSTANCE))
                .withMetadata(DocumentationMetadataDefinition.localized("ROUND"))
                .withArgument("value", NUMERIC, DocumentationMetadataDefinition.localized("ROUND_VALUE"))
                .withArgument("precision", INTEGER, DocumentationMetadataDefinition.localized("ROUND_PRECISION"))
                .withMinArgumentCount(1)
                .build();
        domainBuilder.withFunctionTypeResolver("ROUND", StaticDomainFunctionTypeResolvers.returning(NUMERIC));
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        Object value = arguments.get(function.getArgument(0));
        if (value == null) {
            return null;
        }
        int prec = 0;
        if (arguments.size() > 1) {
            Object precision = arguments.get(function.getArgument(1));
            if (precision == null) {
                return null;
            }
            prec = ((Number) precision).intValue();
        }

        return new BigDecimal(((Number) value).doubleValue()).round(new MathContext(prec, RoundingMode.HALF_UP));
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("ROUND(");
        argumentRenderers.get(function.getArgument(0)).accept(sb);
        if (argumentRenderers.size() > 1) {
            sb.append(", ");
            argumentRenderers.get(function.getArgument(1)).accept(sb);
        }
        sb.append(')');
    }
}
