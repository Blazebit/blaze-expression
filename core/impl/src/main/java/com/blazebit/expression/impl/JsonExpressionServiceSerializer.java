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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.spi.ExpressionServiceSerializer;

import java.io.Serializable;
import java.util.Map;

/**
 * A JSON expression service serializer.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class JsonExpressionServiceSerializer implements ExpressionServiceSerializer<ExpressionService>, Serializable {

    @Override
    public boolean canSerialize(Object element) {
        return element instanceof ExpressionService;
    }

    @Override
    public <T> T serialize(ExpressionService expressionService, ExpressionService model, Class<T> targetType, String format, Map<String, Object> properties) {
        return serialize(expressionService, null, model, targetType, format, properties);
    }

    @Override
    public <T> T serialize(ExpressionService expressionService, ExpressionService baseModel, ExpressionService model, Class<T> targetType, String format, Map<String, Object> properties) {
        if (targetType != String.class || !"json".equals(format)) {
            return null;
        }
        DomainModel domainModel = expressionService.getDomainModel();
        String domainSerialization;
        if (baseModel == null) {
            domainSerialization = domainModel.serialize(String.class, format, properties);
        } else if (domainModel != baseModel.getDomainModel()) {
            domainSerialization = domainModel.serialize(baseModel.getDomainModel(), String.class, format, properties);
        } else {
            domainSerialization = null;
        }

        StringBuilder sb;
        if (domainSerialization == null) {
            sb = new StringBuilder(200);
            sb.append('{');
        } else {
            sb = new StringBuilder(domainSerialization.length() + 200);
            sb.append('{');
            sb.append("\"domain\":").append(domainSerialization).append(',');
        }

        serializerResolver("booleanLiteralResolver", model, model.getBooleanLiteralResolver(), baseModel == null ? null : baseModel.getBooleanLiteralResolver(), properties, sb);
        serializerResolver("numericLiteralResolver", model, model.getNumericLiteralResolver(), baseModel == null ? null : baseModel.getNumericLiteralResolver(), properties, sb);
        serializerResolver("stringLiteralResolver", model, model.getStringLiteralResolver(), baseModel == null ? null : baseModel.getStringLiteralResolver(), properties, sb);
        serializerResolver("temporalLiteralResolver", model, model.getTemporalLiteralResolver(), baseModel == null ? null : baseModel.getTemporalLiteralResolver(), properties, sb);
        serializerResolver("entityLiteralResolver", model, model.getEntityLiteralResolver(), baseModel == null ? null : baseModel.getEntityLiteralResolver(), properties, sb);
        serializerResolver("enumLiteralResolver", model, model.getEnumLiteralResolver(), baseModel == null ? null : baseModel.getEnumLiteralResolver(), properties, sb);
        serializerResolver("collectionLiteralResolver", model, model.getCollectionLiteralResolver(), baseModel == null ? null : baseModel.getCollectionLiteralResolver(), properties, sb);

        if (sb.length() == 1) {
            sb.append('}');
        } else {
            sb.setCharAt(sb.length() - 1, '}');
        }
        return (T) sb.toString();
    }

    private void serializerResolver(String key, ExpressionService domainModel, Object resolver, Object baseResolver, Map<String, Object> properties, StringBuilder sb) {
        if (resolver != baseResolver && resolver instanceof ExpressionServiceSerializer<?>) {
            String json = ((ExpressionServiceSerializer<Object>) resolver).serialize(domainModel, null, String.class, "json", properties);
            if (json != null) {
                sb.append('"').append(key).append("\":").append(json).append(',');
            }
        }
    }

}
