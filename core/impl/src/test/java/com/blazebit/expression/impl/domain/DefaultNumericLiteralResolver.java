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
package com.blazebit.expression.impl.domain;

import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.impl.AbstractExpressionCompilerTest;
import com.blazebit.expression.spi.NumericLiteralResolver;
import com.blazebit.expression.spi.ResolvedLiteral;

import java.math.BigDecimal;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DefaultNumericLiteralResolver implements NumericLiteralResolver {
    @Override
    public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, Number value) {
        if (value instanceof BigDecimal || value instanceof Double) {
            return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(AbstractExpressionCompilerTest.BIGDECIMAL), value);
        }
        return new DefaultResolvedLiteral(context.getExpressionService().getDomainModel().getType(AbstractExpressionCompilerTest.INTEGER), value.intValue());
    }
}
