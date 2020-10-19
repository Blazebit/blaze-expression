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

package com.blazebit.expression.declarative.view;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.declarative.spi.DeclarativeAttributeMetadataProcessor;
import com.blazebit.domain.declarative.spi.ServiceProvider;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.metamodel.CorrelatedAttribute;
import com.blazebit.persistence.view.metamodel.ManagedViewType;
import com.blazebit.persistence.view.metamodel.MappingAttribute;
import com.blazebit.persistence.view.metamodel.MethodAttribute;
import com.blazebit.persistence.view.metamodel.SubqueryAttribute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@com.blazebit.apt.service.ServiceProvider(DeclarativeAttributeMetadataProcessor.class)
public class EntityViewAttributeDeclarativeMetadataProcessor implements DeclarativeAttributeMetadataProcessor<Annotation> {

    @Override
    public Class<Annotation> getProcessingAnnotation() {
        return null;
    }

    @Override
    public MetadataDefinition<?> process(Class<?> annotatedClass, Method method, Annotation annotation, ServiceProvider<?> serviceProvider) {
        EntityViewManager entityViewManager = serviceProvider.getService(EntityViewManager.class);
        if (entityViewManager == null) {
            throw new IllegalStateException("Missing EntityViewManager! Please provide the EntityViewManager as service via DeclarativeDomainConfiguration.withService(EntityViewManager.class, evm)");
        }
        if (Modifier.isAbstract(method.getModifiers())) {
            ManagedViewType<?> managedViewType = entityViewManager.getMetamodel().managedView(annotatedClass);
            if (managedViewType != null) {
                MethodAttribute<?, ?> attribute = managedViewType.getAttribute(getAttributeName(method));
                if (attribute instanceof MappingAttribute<?, ?>) {
                    if (attribute.getLimitExpression() == null) {
                        return new MappingExpressionRendererImpl((MappingAttribute<?, ?>) attribute);
                    } else {
                        return new MappingExpressionCorrelationRendererImpl((MappingAttribute<?, ?>) attribute);
                    }
                } else if (attribute instanceof SubqueryAttribute<?, ?>) {
                    SubqueryAttribute<?, ?> subqueryAttribute = (SubqueryAttribute<?, ?>) attribute;
                    return new SubqueryCorrelationRendererImpl(subqueryAttribute);
                } else if (attribute instanceof CorrelatedAttribute<?, ?>) {
                    CorrelatedAttribute<?, ?> correlatedAttribute = (CorrelatedAttribute<?, ?>) attribute;
                    return new CorrelationProviderCorrelationRendererImpl(correlatedAttribute);
                } else {
                    throw new IllegalStateException("Unknown attribute type: " + attribute);
                }
            }
        }
        return null;
    }

    private static String getAttributeName(Method getterOrSetter) {
        String name = getterOrSetter.getName();
        StringBuilder sb = new StringBuilder(name.length());
        int index = name.startsWith("is") ? 2 : 3;
        char firstAttributeNameChar = name.charAt(index);
        return sb.append(Character.toLowerCase(firstAttributeNameChar))
            .append(name, index + 1, name.length())
            .toString();
    }

}
