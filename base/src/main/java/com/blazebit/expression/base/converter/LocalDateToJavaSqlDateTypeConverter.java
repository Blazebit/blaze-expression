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

package com.blazebit.expression.base.converter;

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.spi.TypeConverter;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class LocalDateToJavaSqlDateTypeConverter implements TypeConverter<LocalDate, Date>, Serializable {
    @Override
    public Date convert(ExpressionInterpreter.Context context, LocalDate value, DomainType domainType) {
        return Date.valueOf(value);
    }
}
