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
package com.blazebit.expression.impl.domain;

import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.impl.AbstractExpressionCompilerTest;
import com.blazebit.expression.spi.ResolvedLiteral;
import com.blazebit.expression.spi.TemporalLiteralResolver;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DefaultTemporalLiteralResolver implements TemporalLiteralResolver {
    @Override
    public ResolvedLiteral resolveDateLiteral(ExpressionCompiler.Context context, LocalDate value) {
        return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(AbstractExpressionCompilerTest.TIMESTAMP), value.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    @Override
    public ResolvedLiteral resolveTimeLiteral(ExpressionCompiler.Context context, LocalTime value) {
        return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(AbstractExpressionCompilerTest.TIMESTAMP), value.atDate(LocalDate.of(1970, 1, 1)).toInstant(ZoneOffset.UTC));
    }

    @Override
    public ResolvedLiteral resolveTimestampLiteral(ExpressionCompiler.Context context, Instant value) {
        return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(AbstractExpressionCompilerTest.TIMESTAMP), value);
    }

    @Override
    public ResolvedLiteral resolveIntervalLiteral(ExpressionCompiler.Context context, TemporalInterval value) {
        return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(AbstractExpressionCompilerTest.INTERVAL), value);
    }
}
