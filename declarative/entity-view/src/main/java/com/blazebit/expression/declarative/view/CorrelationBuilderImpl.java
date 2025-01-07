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

package com.blazebit.expression.declarative.view;

import com.blazebit.persistence.BaseFromQueryBuilder;
import com.blazebit.persistence.BaseQueryBuilder;
import com.blazebit.persistence.CorrelationQueryBuilder;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.FromProvider;
import com.blazebit.persistence.FullSelectCTECriteriaBuilder;
import com.blazebit.persistence.JoinOnBuilder;
import com.blazebit.persistence.JoinType;
import com.blazebit.persistence.SubqueryBuilder;
import com.blazebit.persistence.view.CorrelationBuilder;
import com.blazebit.persistence.view.metamodel.Attribute;

import javax.persistence.metamodel.EntityType;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class CorrelationBuilderImpl implements CorrelationBuilder {

    private final BaseQueryBuilder<?, ?> criteriaBuilder;
    private final Map<String, Object> optionalParameters;
    private final String joinBase;
    private final String correlationAlias;
    private final String correlationExternalAlias;
    private final JoinType joinType;
    private final Attribute<?, ?> attribute;
    private boolean correlated;
    private Object correlationBuilder;

    /**
     * Creates a new correlation builder.
     * @param criteriaBuilder           The criteria builder
     * @param optionalParameters        The optional parameters
     * @param joinBase                  The join base
     * @param correlationAlias          The correlation alias
     * @param correlationExternalAlias  The correlation external alias
     * @param joinType                  The join type
     * @param attribute                 The attribute
     */
    public CorrelationBuilderImpl(CriteriaBuilder<?> criteriaBuilder, Map<String, Object> optionalParameters, String joinBase, String correlationAlias, String correlationExternalAlias, JoinType joinType, Attribute<?, ?> attribute) {
        this.criteriaBuilder = criteriaBuilder;
        this.optionalParameters = optionalParameters;
        this.joinBase = joinBase;
        this.correlationAlias = correlationAlias;
        this.correlationExternalAlias = correlationExternalAlias;
        this.joinType = joinType;
        this.attribute = attribute;
    }

    @Override
    public <T> T getService(Class<T> serviceClass) {
        return criteriaBuilder.getService(serviceClass);
    }

    @Override
    public FromProvider getCorrelationFromProvider() {
        return criteriaBuilder;
    }

    @Override
    public String getCorrelationAlias() {
        return correlationExternalAlias;
    }

    /**
     * Finishes the correlation builder.
     */
    public void finish() {
        if (correlationBuilder instanceof SubqueryBuilder<?>) {
            ((SubqueryBuilder<?>) correlationBuilder).end();
        } else  if (correlationBuilder instanceof FullSelectCTECriteriaBuilder<?>) {
            ((FullSelectCTECriteriaBuilder<?>) correlationBuilder).end();
        }
    }

    @Override
    public JoinOnBuilder<CorrelationQueryBuilder> correlate(Class<?> entityClass) {
        if (correlated) {
            throw new IllegalArgumentException("Can not correlate with multiple entity classes!");
        }

        correlated = true;
        if (attribute.getLimitExpression() == null) {
            return (JoinOnBuilder<CorrelationQueryBuilder>) (JoinOnBuilder<?>) criteriaBuilder.joinOn(joinBase, entityClass, correlationAlias, joinType);
        } else {
            BaseFromQueryBuilder<?, ?> lateralBuilder = criteriaBuilder.joinLateralEntitySubquery(joinBase, entityClass, correlationExternalAlias, correlationAlias, joinType);
            attribute.renderLimit(correlationAlias, criteriaBuilder, optionalParameters, lateralBuilder);
            this.correlationBuilder = lateralBuilder;
            return lateralBuilder.getService(JoinOnBuilder.class);
        }
    }

    @Override
    public JoinOnBuilder<CorrelationQueryBuilder> correlate(EntityType<?> entityType) {
        if (correlated) {
            throw new IllegalArgumentException("Can not correlate with multiple entity classes!");
        }

        correlated = true;
        if (attribute.getLimitExpression() == null) {
            return (JoinOnBuilder<CorrelationQueryBuilder>) (JoinOnBuilder<?>) criteriaBuilder.joinOn(joinBase, entityType, correlationAlias, joinType);
        } else {
            BaseFromQueryBuilder<?, ?> lateralBuilder = criteriaBuilder.joinLateralEntitySubquery(joinBase, entityType, correlationExternalAlias, correlationAlias, joinType);
            attribute.renderLimit(correlationAlias, criteriaBuilder, optionalParameters, lateralBuilder);
            this.correlationBuilder = lateralBuilder;
            return lateralBuilder.getService(JoinOnBuilder.class);
        }
    }

    @Override
    public JoinOnBuilder<CorrelationQueryBuilder> correlate(String correlationPath) {
        if (correlated) {
            throw new IllegalArgumentException("Can not correlate with multiple entity classes!");
        }

        correlated = true;
        if (attribute.getLimitExpression() == null) {
            return (JoinOnBuilder<CorrelationQueryBuilder>) (JoinOnBuilder<?>) criteriaBuilder.joinOn(correlationPath, correlationAlias, joinType);
        } else {
            BaseFromQueryBuilder<?, ?> lateralBuilder = criteriaBuilder.joinLateralEntitySubquery(correlationPath, correlationExternalAlias, correlationAlias, joinType);
            attribute.renderLimit(correlationAlias, criteriaBuilder, optionalParameters, lateralBuilder);
            this.correlationBuilder = lateralBuilder;
            return lateralBuilder.getService(JoinOnBuilder.class);
        }
    }
}
