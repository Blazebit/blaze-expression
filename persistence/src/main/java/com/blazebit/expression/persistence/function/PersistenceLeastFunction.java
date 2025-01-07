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

package com.blazebit.expression.persistence.function;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.persistence.PersistenceDomainFunctionArgumentRenderers;
import com.blazebit.expression.persistence.PersistenceFunctionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceLeastFunction implements PersistenceFunctionRenderer, Serializable {

    private static final PersistenceLeastFunction INSTANCE = new PersistenceLeastFunction();

    private PersistenceLeastFunction() {
    }

    /**
     * Adds the LEAST function to the domain builder.
     *
     * @param domainBuilder The domain builder
     */
    public static void addFunction(DomainBuilder domainBuilder) {
        domainBuilder.extendFunction("LEAST", new PersistenceFunctionRendererMetadataDefinition(INSTANCE));
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, PersistenceDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        sb.append("LEAST(");
        argumentRenderers.renderArguments(sb);
        sb.append(')');
    }
}
