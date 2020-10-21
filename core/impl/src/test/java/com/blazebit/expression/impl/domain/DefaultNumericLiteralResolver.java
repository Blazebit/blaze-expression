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
import com.blazebit.domain.runtime.model.NumericLiteralResolver;
import com.blazebit.domain.runtime.model.ResolvedLiteral;
import com.blazebit.expression.impl.AbstractExpressionCompilerTest;

import java.math.BigDecimal;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DefaultNumericLiteralResolver implements NumericLiteralResolver {
    @Override
    public ResolvedLiteral resolveLiteral(DomainModel domainModel, Number value) {
        if (value instanceof BigDecimal && ((BigDecimal) value).scale() > 0) {
            return new DefaultResolvedLiteral(domainModel.getType(AbstractExpressionCompilerTest.BIGDECIMAL), value);
        }
        return new DefaultResolvedLiteral(domainModel.getType(AbstractExpressionCompilerTest.INTEGER), value.intValue());
    }
}
