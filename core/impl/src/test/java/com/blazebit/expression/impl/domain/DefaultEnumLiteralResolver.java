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

import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.spi.EnumLiteralResolver;
import com.blazebit.expression.spi.ResolvedLiteral;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class DefaultEnumLiteralResolver implements EnumLiteralResolver {
    @Override
    public ResolvedLiteral resolveLiteral(ExpressionCompiler.Context context, EnumDomainTypeValue value) {
        Class<?> javaEnumType = value.getOwner().getJavaType();
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Enum<?> javaEnumValue = Enum.valueOf((Class<Enum>) javaEnumType, value.getValue());
        return new DefaultResolvedLiteral(value.getOwner(), javaEnumValue);
    }
}
