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

package com.blazebit.expression.declarative.impl;

import java.lang.annotation.Annotation;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.declarative.spi.DeclarativeMetadataProcessor;
import com.blazebit.domain.spi.ServiceProvider;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@com.blazebit.apt.service.ServiceProvider(DeclarativeMetadataProcessor.class)
public class EnumDeclarativeMetadataProcessor implements DeclarativeMetadataProcessor {
    @Override
    public Class getProcessingAnnotation() {
        return null;
    }

    @Override
    public MetadataDefinition<?> process(Class clazz, Annotation annotation, ServiceProvider serviceProvider) {
        if (clazz.isEnum()) {
            return EnumComparisonOperatorInterpreter.INSTANCE;
        }
        return null;
    }
}
