/*
 * Copyright 2019 - 2022 Blazebit.
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

package com.blazebit.expression.excel;

import com.blazebit.domain.runtime.model.DomainType;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelIntervalLiteralRenderer implements ExcelLiteralRenderer, Serializable {

    public static final ExcelIntervalLiteralRenderer INSTANCE = new ExcelIntervalLiteralRenderer();

    private ExcelIntervalLiteralRenderer() {
    }

    @Override
    public void render(Object value, DomainType domainType, ExcelExpressionSerializer serializer) {
        throw new UnsupportedOperationException("Not possible to render interval literals. Should be handled by operator renderers!");
    }
}
