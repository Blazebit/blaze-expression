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

package com.blazebit.expression.persistence;

import com.blazebit.domain.runtime.model.DomainType;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceTimestampLiteralRenderer implements PersistenceLiteralRenderer, Serializable {

    public static final PersistenceTimestampLiteralRenderer INSTANCE = new PersistenceTimestampLiteralRenderer();

    private PersistenceTimestampLiteralRenderer() {
    }

    @Override
    public void render(Object value, DomainType domainType, PersistenceExpressionSerializer serializer) {
        Instant instant = (Instant) value;
        StringBuilder sb = serializer.getStringBuilder();
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.UTC);
        sb.append("{ts '");
        sb.append(offsetDateTime.getYear());
        sb.append('-');
        if (offsetDateTime.getMonthValue() < 10) {
            sb.append('0');
        }
        sb.append(offsetDateTime.getMonthValue());
        sb.append('-');
        if (offsetDateTime.getDayOfMonth() < 10) {
            sb.append('0');
        }
        sb.append(offsetDateTime.getDayOfMonth());
        sb.append(' ');
        if (offsetDateTime.getHour() < 10) {
            sb.append('0');
        }
        sb.append(offsetDateTime.getHour());
        sb.append(':');
        if (offsetDateTime.getMinute() < 10) {
            sb.append('0');
        }
        sb.append(offsetDateTime.getMinute());
        sb.append(':');
        if (offsetDateTime.getSecond() < 10) {
            sb.append('0');
        }
        sb.append(offsetDateTime.getSecond());
        int nano = offsetDateTime.getNano();
        if (nano != 0L) {
            sb.append('.');
            if (nano < 100_000_000) {
                sb.append('0');
                if (nano < 10_000_000) {
                    sb.append('0');
                    if (nano < 1_000_000) {
                        sb.append('0');
                        if (nano < 100_000) {
                            sb.append('0');
                            if (nano < 10_000) {
                                sb.append('0');
                                if (nano < 1_000) {
                                    sb.append('0');
                                    if (nano < 100) {
                                        sb.append('0');
                                        if (nano < 10) {
                                            sb.append('0');
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            sb.append(nano);
        }
        sb.append("'}");
    }
}
