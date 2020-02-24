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

package com.blazebit.expression.declarative;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.spi.TypeAdapter;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class TypeAdapterMetadataDefinition<X, Y> implements MetadataDefinition<TypeAdapter> {

    private final TypeAdapter<X, Y> typeAdapter;
    private final Class<Y> internalType;

    public TypeAdapterMetadataDefinition(TypeAdapter<X, Y> typeAdapter, Class<Y> internalType) {
        this.typeAdapter = typeAdapter;
        this.internalType = internalType;
    }

    public Class<Y> getInternalType() {
        return internalType;
    }

    @Override
    public Class<TypeAdapter> getJavaType() {
        return TypeAdapter.class;
    }

    @Override
    public TypeAdapter build(MetadataDefinitionHolder<?> definitionHolder) {
        return typeAdapter;
    }
}
