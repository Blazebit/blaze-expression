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

package com.blazebit.expression.persistence;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceStringlyTypeHandlerMetadataDefinition implements MetadataDefinition<PersistenceStringlyTypeHandler>, Serializable {

    private final PersistenceStringlyTypeHandler persistenceStringlyTypeHandler;

    /**
     * Create a new metadata definition with the given stringly type handler.
     *
     * @param persistenceStringlyTypeHandler The stringly type handler
     */
    public PersistenceStringlyTypeHandlerMetadataDefinition(PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        this.persistenceStringlyTypeHandler = persistenceStringlyTypeHandler;
    }

    @Override
    public Class<PersistenceStringlyTypeHandler> getJavaType() {
        return PersistenceStringlyTypeHandler.class;
    }

    @Override
    public PersistenceStringlyTypeHandler build(MetadataDefinitionHolder definitionHolder) {
        return persistenceStringlyTypeHandler;
    }
}
