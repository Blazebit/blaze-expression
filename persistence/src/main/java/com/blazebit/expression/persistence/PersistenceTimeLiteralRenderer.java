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
import java.time.LocalTime;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceTimeLiteralRenderer implements PersistenceLiteralRenderer, Serializable {

    public static final PersistenceTimeLiteralRenderer INSTANCE = new PersistenceTimeLiteralRenderer();

    private PersistenceTimeLiteralRenderer() {
    }

    @Override
    public void render(Object value, DomainType domainType, PersistenceExpressionSerializer serializer) {
        LocalTime localTime = (LocalTime) value;
        StringBuilder sb = serializer.getStringBuilder();
        sb.append("{t '");
        if (localTime.getHour() < 10) {
            sb.append('0');
        }
        sb.append(localTime.getHour());
        sb.append(':');
        if (localTime.getMinute() < 10) {
            sb.append('0');
        }
        sb.append(localTime.getMinute());
        sb.append(':');
        if (localTime.getSecond() < 10) {
            sb.append('0');
        }
        sb.append(localTime.getSecond());
        int nano = localTime.getNano();
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
