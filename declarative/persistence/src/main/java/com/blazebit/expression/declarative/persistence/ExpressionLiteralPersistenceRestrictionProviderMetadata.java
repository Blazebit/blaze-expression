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

package com.blazebit.expression.declarative.persistence;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;

import java.io.Serializable;

/**
 * Metadata for a {@link EntityLiteralPersistenceRestrictionProvider}.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionLiteralPersistenceRestrictionProviderMetadata implements MetadataDefinition<EntityLiteralPersistenceRestrictionProvider>, Serializable {

    private final EntityLiteralPersistenceRestrictionProvider entityLiteralRestrictionProvider;

    /**
     * Creates a metadata object for the given {@link EntityLiteralPersistenceRestrictionProvider} class.
     *
     * @param clazz The class
     */
    public ExpressionLiteralPersistenceRestrictionProviderMetadata(Class<? extends EntityLiteralPersistenceRestrictionProvider> clazz) {
        try {
            this.entityLiteralRestrictionProvider = clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Class<EntityLiteralPersistenceRestrictionProvider> getJavaType() {
        return EntityLiteralPersistenceRestrictionProvider.class;
    }

    @Override
    public EntityLiteralPersistenceRestrictionProvider build(MetadataDefinitionHolder definitionHolder) {
        return entityLiteralRestrictionProvider;
    }
}
