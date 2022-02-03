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

package com.blazebit.expression.base;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.TypeAdapter;

import java.io.Serializable;

/**
 * A TypeAdapter for enum values that are stringly types.
 *
 * @param <T> The model type of the stringly type
 * @author Christian Beikov
 * @since 2.0.3
 */
public class EnumValueStringlyTypeAdapter<T> implements TypeAdapter<T, EnumDomainTypeValue>, MetadataDefinition<TypeAdapter<T, EnumDomainTypeValue>>, Serializable {

    private final StringlyTypeHandler<T> stringlyTypeHandler;

    /**
     * Creates a new type adapter for enum value stringly type handlers.
     *
     * @param stringlyTypeHandler The stringly type handler
     */
    public EnumValueStringlyTypeAdapter(StringlyTypeHandler<T> stringlyTypeHandler) {
        this.stringlyTypeHandler = stringlyTypeHandler;
    }

    @Override
    public EnumDomainTypeValue toInternalType(ExpressionInterpreter.Context context, T value, DomainType domainType) {
        return ((EnumDomainType) domainType).getEnumValues().get(stringlyTypeHandler.destruct(value));
    }

    @Override
    public T toModelType(ExpressionInterpreter.Context context, EnumDomainTypeValue value, DomainType domainType) {
        return stringlyTypeHandler.construct(value.getValue());
    }

    @Override
    public Class<TypeAdapter<T, EnumDomainTypeValue>> getJavaType() {
        return (Class) TypeAdapter.class;
    }

    @Override
    public TypeAdapter<T, EnumDomainTypeValue> build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }
}
