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
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.AttributeAccessor;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class MethodAttributeAccessor implements MetadataDefinition<AttributeAccessor>, AttributeAccessor, Serializable {

    private static final Field GETTER;

    static {
        try {
            Field field = MethodAttributeAccessor.class.getDeclaredField("getter");
            field.setAccessible(true);
            GETTER = field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final transient Method getter;

    public MethodAttributeAccessor(Method getter) {
        this.getter = getter;
    }

    @Override
    public Object getAttribute(ExpressionInterpreter.Context context, Object value, EntityDomainTypeAttribute attribute) {
        try {
            return getter.invoke(value);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't access attribute " + attribute + " on object: " + value, e);
        }
    }

    @Override
    public Class<AttributeAccessor> getJavaType() {
        return AttributeAccessor.class;
    }

    @Override
    public AttributeAccessor build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeUTF(getter.getDeclaringClass().getName());
        out.writeUTF(getter.getName());
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        String className = in.readUTF();
        String methodName = in.readUTF();
        try {
            Method method = Class.forName(className).getDeclaredMethod(methodName);
            GETTER.set(this, method);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

}
