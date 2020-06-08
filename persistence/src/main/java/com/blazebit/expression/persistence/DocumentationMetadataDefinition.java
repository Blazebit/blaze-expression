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

package com.blazebit.expression.persistence;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.spi.DomainSerializer;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * A metadata definition for a language element documentation.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DocumentationMetadataDefinition implements MetadataDefinition<DocumentationMetadataDefinition>, DomainSerializer<DocumentationMetadataDefinition>, Serializable {

    /**
     * The property name that can be used in the properties map of {@link com.blazebit.domain.runtime.model.DomainModel#serialize(Class, String, Map)}
     * that represent the locale in which to render the documentation.
     */
    public static final String LOCALE_PROPERTY = "locale";

    private final String documentation;
    private final Locale defaultLocale;

    /**
     * Creates a new documentation metadata definition.
     *
     * @param documentation The documentation
     */
    public DocumentationMetadataDefinition(String documentation) {
        this.documentation = serialize(documentation);
        this.defaultLocale = null;
    }

    private DocumentationMetadataDefinition(String documentation, Locale defaultLocale) {
        this.documentation = documentation;
        this.defaultLocale = defaultLocale;
    }

    /**
     * Creates a new documentation metadata definition.
     *
     * @param documentationKey The documentation resource bundle key
     * @return The documentation metadata definition
     */
    public static DocumentationMetadataDefinition localized(String documentationKey) {
        return new DocumentationMetadataDefinition(documentationKey, Locale.ENGLISH);
    }

    /**
     * Creates a new documentation metadata definition.
     *
     * @param documentationKey The documentation resource bundle key
     * @param defaultLocale The default locale to use
     * @return The documentation metadata definition
     */
    public static DocumentationMetadataDefinition localized(String documentationKey, Locale defaultLocale) {
        return new DocumentationMetadataDefinition(documentationKey, defaultLocale);
    }

    private static String serialize(String documentation) {
        StringBuilder sb = new StringBuilder(documentation.length() + 9);
        sb.append("{\"doc\":\"");
        for (int i = 0; i < documentation.length(); i++) {
            final char c = documentation.charAt(i);
            if (c == '"' || c == '\\') {
                sb.append('\\');
            }
            sb.append(c);
        }
        sb.append("\"}");
        return sb.toString();
    }

    @Override
    public <T> T serialize(DomainModel domainModel, DocumentationMetadataDefinition element, Class<T> targetType, String format, Map<String, Object> properties) {
        if (targetType != String.class || !"json".equals(format)) {
            return null;
        }

        if (defaultLocale == null) {
            return (T) documentation;
        }
        Object o = properties.get(LOCALE_PROPERTY);
        Locale locale;
        if (o instanceof Locale) {
            locale = (Locale) o;
        } else {
            locale = defaultLocale;
        }
        return (T) serialize(ResourceBundle.getBundle("resource-bundles/persistence", locale).getString(documentation));
    }

    @Override
    public Class<DocumentationMetadataDefinition> getJavaType() {
        return DocumentationMetadataDefinition.class;
    }

    @Override
    public DocumentationMetadataDefinition build(MetadataDefinitionHolder<?> definitionHolder) {
        return this;
    }
}
