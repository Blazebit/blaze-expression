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

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.persistence.PersistenceFunctionRenderer;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceFunctionRendererMetadataDefinition implements MetadataDefinition<PersistenceFunctionRenderer>, Serializable {

    private final PersistenceFunctionRenderer persistenceFunctionRenderer;

    /**
     * Creates a new function renderer metadata definition.
     *
     * @param persistenceFunctionRenderer The function renderer
     */
    public PersistenceFunctionRendererMetadataDefinition(PersistenceFunctionRenderer persistenceFunctionRenderer) {
        this.persistenceFunctionRenderer = persistenceFunctionRenderer;
    }

    @Override
    public Class<PersistenceFunctionRenderer> getJavaType() {
        return PersistenceFunctionRenderer.class;
    }

    @Override
    public PersistenceFunctionRenderer build(MetadataDefinitionHolder definitionHolder) {
        return persistenceFunctionRenderer;
    }
}
