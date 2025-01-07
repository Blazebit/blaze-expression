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

package com.blazebit.expression.spi;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.ExpressionServiceBuilder;

/**
 * Interface implemented by the expression implementation provider.
 *
 * Implementations are instantiated via {@link java.util.ServiceLoader}.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ExpressionServiceBuilderProvider {

    /**
     * Creates an empty expression service builder.
     *
     * @param domainModel The domain model to use for expression services
     * @return the expression service builder
     */
    public ExpressionServiceBuilder createEmptyBuilder(DomainModel domainModel);

    /**
     * Creates an empty expression service builder and returns it after running {@link ExpressionServiceBuilder#withDefaults()} on it.
     *
     * @param domainModel The domain model to use for expression services
     * @return the expression service builder
     */
    public ExpressionServiceBuilder createDefaultBuilder(DomainModel domainModel);

    /**
     * Creates a expression service builder based on an existing expression service.
     *
     * @param expressionService The expression service to extend
     * @return the expression service builder
     */
    public ExpressionServiceBuilder createBuilder(ExpressionService expressionService);

    /**
     * Creates a expression service builder based on an existing expression service and extended domain model.
     *
     * @param expressionService The expression service to extend
     * @param domainModel The extended domain model to use for expression services
     * @return the expression service builder
     */
    public ExpressionServiceBuilder createBuilder(ExpressionService expressionService, DomainModel domainModel);


}
