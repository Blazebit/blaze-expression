/*
 * Copyright 2019 - 2024 Blazebit.
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

package com.blazebit.expression.declarative.impl;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.expression.spi.TypeAdapter;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class TypeAdapterMetadataDefinition<X, Y> implements MetadataDefinition<TypeAdapter<X, Y>>, Serializable {

    private static final Field INTERNAL_TYPE;

    static {
        try {
            Field field = TypeAdapterMetadataDefinition.class.getDeclaredField("internalType");
            field.setAccessible(true);
            INTERNAL_TYPE = field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final TypeAdapter<X, Y> typeAdapter;
    private final transient Class<Y> internalType;

    public TypeAdapterMetadataDefinition(TypeAdapter<X, Y> typeAdapter, Class<Y> internalType) {
        this.typeAdapter = typeAdapter;
        this.internalType = internalType;
    }

    public Class<Y> getInternalType() {
        return internalType;
    }

    @Override
    public Class<TypeAdapter<X, Y>> getJavaType() {
        return (Class) TypeAdapter.class;
    }

    @Override
    public TypeAdapter<X, Y> build(MetadataDefinitionHolder definitionHolder) {
        return typeAdapter;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeUTF(internalType.getName());
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String className = in.readUTF();
        try {
            INTERNAL_TYPE.set(this, Class.forName(className));
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
