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

package com.blazebit.expression.persistence.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.persistence.PersistenceDomainFunctionArgumentRenderers;
import com.blazebit.expression.persistence.PersistenceFunctionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceNumericFunction implements PersistenceFunctionRenderer, Serializable {

    private static final List<PersistenceNumericFunction> FUNCTIONS = Arrays.asList(
            new PersistenceNumericFunction("SQRT"),
            new PersistenceNumericFunction("SIN"),
            new PersistenceNumericFunction("COS"),
            new PersistenceNumericFunction("TAN"),
            new PersistenceNumericFunction("ASIN"),
            new PersistenceNumericFunction("ACOS"),
            new PersistenceNumericFunction("ATAN"),
            new PersistenceNumericFunction("LOG"),
            new PersistenceNumericFunction("EXP"),
            new PersistenceNumericFunction("RADIANS"),
            new PersistenceNumericFunction("DEGREES")
    );

    private final String name;

    private PersistenceNumericFunction(String name) {
        this.name = name;
    }

    /**
     * Adds the numeric functions SQRT, SIN, COS, TAN, LOG, EXP, RADIANS and DEGREES to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        for (PersistenceNumericFunction f : FUNCTIONS) {
            domainBuilder.extendFunction(f.name, new PersistenceFunctionRendererMetadataDefinition(f));
        }
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, PersistenceDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append(name).append("(");
        argumentRenderers.renderArguments(sb);
        sb.append(')');
    }
}
