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
import com.blazebit.expression.persistence.DocumentationMetadataDefinition;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.spi.FunctionInvoker;
import com.blazebit.expression.persistence.FunctionRenderer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.blazebit.expression.persistence.PersistenceDomainContributor.NUMERIC;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public abstract class NumericFunction implements FunctionRenderer, FunctionInvoker, Serializable {

    private static final List<NumericFunction> FUNCTIONS = Arrays.asList(
            new NumericFunction("SQRT") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.sqrt(value));
                }
            },
            new NumericFunction("SIN") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.sin(value));
                }
            },
            new NumericFunction("COS") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.cos(value));
                }
            },
            new NumericFunction("TAN") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.tan(value));
                }
            },
            new NumericFunction("ASIN") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.asin(value));
                }
            },
            new NumericFunction("ACOS") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.acos(value));
                }
            },
            new NumericFunction("ATAN") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.atan(value));
                }
            },
            new NumericFunction("LOG") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.log(value));
                }
            },
            new NumericFunction("EXP") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.exp(value));
                }
            },
            new NumericFunction("RADIANS") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.toRadians(value));
                }
            },
            new NumericFunction("DEGREES") {
                @Override
                protected Object invoke(double value) {
                    return new BigDecimal(Math.toDegrees(value));
                }
            }
    );

    private final String name;

    private NumericFunction(String name) {
        this.name = name;
    }

    /**
     * Adds the numeric functions SQRT, SIN, COS, TAN, LOG, EXP, RADIANS and DEGREES to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        for (NumericFunction f : FUNCTIONS) {
            domainBuilder.createFunction(f.name)
                    .withMetadata(new FunctionRendererMetadataDefinition(f))
                    .withMetadata(new FunctionInvokerMetadataDefinition(f))
                    .withMetadata(DocumentationMetadataDefinition.localized(f.name))
                    .withExactArgumentCount(1)
                    .withArgument("number", DocumentationMetadataDefinition.localized(f.name + "_ARG"))
                    .build();
            domainBuilder.withFunctionTypeResolver(f.name, StaticDomainFunctionTypeResolvers.returning(NUMERIC));
        }
    }

    @Override
    public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
        Object argument = arguments.get(function.getArgument(0));
        if (argument == null) {
            return null;
        }

        if (argument instanceof Number) {
            return invoke(((Number) argument).doubleValue());
        } else {
            throw new IllegalArgumentException("Illegal argument for " + name + " function: " + argument);
        }
    }

    /**
     * Invoke the numeric function with the given double value.
     *
     * @param value The double value
     * @return the result
     */
    protected abstract Object invoke(double value);

    @Override
    public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append(name).append("(");
        argumentRenderers.values().iterator().next().accept(sb);
        sb.append(')');
    }
}
