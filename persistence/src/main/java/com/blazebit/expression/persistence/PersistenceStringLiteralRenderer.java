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

package com.blazebit.expression.persistence;

import com.blazebit.domain.runtime.model.DomainType;

import java.io.Serializable;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceStringLiteralRenderer implements PersistenceLiteralRenderer, Serializable {

    public static final PersistenceStringLiteralRenderer INSTANCE = new PersistenceStringLiteralRenderer();

    private PersistenceStringLiteralRenderer() {
    }

    @Override
    public void render(Object value, DomainType domainType, PersistenceExpressionSerializer serializer) {
        CharSequence charSequence = (CharSequence) value;
        StringBuilder sb = serializer.getStringBuilder();
        sb.append('\'');
        for (int i = 0; i < charSequence.length(); i++) {
            final char c = charSequence.charAt(i);
            if (c == '\'') {
                sb.append('\'');
            }
            sb.append(c);
        }
        sb.append('\'');
    }
}
