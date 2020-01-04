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
package com.blazebit.expression.impl.domain;

import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.ResolvedLiteral;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.domain.runtime.model.TemporalLiteralResolver;

import java.time.Instant;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DefaultTemporalLiteralResolver implements TemporalLiteralResolver {
    @Override
    public ResolvedLiteral resolveTimestampLiteral(DomainModel domainModel, Instant value) {
        return new DefaultResolvedLiteral(domainModel.getType(Instant.class), value);
    }

    @Override
    public ResolvedLiteral resolveIntervalLiteral(DomainModel domainModel, TemporalInterval value) {
        return new DefaultResolvedLiteral(domainModel.getType(TemporalInterval.class), value);
    }
}
