/*
 * Copyright 2019 - 2021 Blazebit.
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

package com.blazebit.expression.base;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class StringlyTypeHandlerMetadataDefinition implements MetadataDefinition<StringlyTypeHandler>, Serializable {

    private final StringlyTypeHandler<?> stringlyTypeHandler;

    /**
     * Create a new metadata definition with the given stringly type handler.
     *
     * @param stringlyTypeHandler The stringly type handler
     */
    public StringlyTypeHandlerMetadataDefinition(StringlyTypeHandler<?> stringlyTypeHandler) {
        this.stringlyTypeHandler = stringlyTypeHandler;
    }

    @Override
    public Class<StringlyTypeHandler> getJavaType() {
        return StringlyTypeHandler.class;
    }

    @Override
    public StringlyTypeHandler<?> build(MetadataDefinitionHolder definitionHolder) {
        return stringlyTypeHandler;
    }
}
