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

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
class FunctionInvokerMetadataDefinition implements MetadataDefinition<FunctionInvoker>, Serializable {

    private final FunctionInvoker functionInvoker;

    /**
     * Creates a new function invoker metadata definition.
     *
     * @param functionInvoker The function invoker
     */
    public FunctionInvokerMetadataDefinition(FunctionInvoker functionInvoker) {
        this.functionInvoker = functionInvoker;
    }

    @Override
    public Class<FunctionInvoker> getJavaType() {
        return FunctionInvoker.class;
    }

    @Override
    public FunctionInvoker build(MetadataDefinitionHolder<?> definitionHolder) {
        return functionInvoker;
    }
}
