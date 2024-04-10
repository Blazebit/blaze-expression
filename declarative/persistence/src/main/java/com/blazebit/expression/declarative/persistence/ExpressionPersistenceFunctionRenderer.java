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

package com.blazebit.expression.declarative.persistence;

import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.expression.persistence.PersistenceFunctionRenderer;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.persistence.PersistenceDomainFunctionArgumentRenderers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A function renderer that renders function arguments into 1-based parameter placeholders.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionPersistenceFunctionRenderer implements MetadataDefinition<PersistenceFunctionRenderer>, PersistenceFunctionRenderer, Serializable {

    private final String[] chunks;
    private final int[] parameterIndices;
    private final boolean rendersPredicate;

    /**
     * Creates a function renderer that renders function arguments into 1-based parameter placeholders.
     *
     * @param template The rendering template
     * @param rendersPredicate Whether a predicate is rendered
     */
    public ExpressionPersistenceFunctionRenderer(String template, boolean rendersPredicate) {
        this.rendersPredicate = rendersPredicate;
        List<String> chunkList = new ArrayList<String>();
        List<Integer> parameterIndexList = new ArrayList<Integer>();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);

            if (c == '?') {
                chunkList.add(sb.toString());
                sb.setLength(0);

                while (++i < template.length()) {
                    c = template.charAt(i);
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    } else {
                        parameterIndexList.add(Integer.parseInt(sb.toString()) - 1);
                        sb.setLength(0);
                        sb.append(c);
                        break;
                    }
                }

                if (i == template.length()) {
                    parameterIndexList.add(Integer.parseInt(sb.toString()) - 1);
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            chunkList.add(sb.toString());
        }

        int[] parameterIndices = new int[parameterIndexList.size()];
        for (int i = 0; i < parameterIndexList.size(); i++) {
            parameterIndices[i] = parameterIndexList.get(i);
        }

        this.chunks = chunkList.toArray(new String[chunkList.size()]);
        this.parameterIndices = parameterIndices;
    }

    @Override
    public boolean rendersPredicate() {
        return rendersPredicate;
    }

    @Override
    public void render(DomainFunction function, DomainType returnType, PersistenceDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
        for (int i = 0; i < chunks.length; i++) {
            sb.append(chunks[i]);

            if (i < parameterIndices.length) {
                argumentRenderers.renderArgument(sb, parameterIndices[i]);
            }
        }
    }

    @Override
    public Class<PersistenceFunctionRenderer> getJavaType() {
        return PersistenceFunctionRenderer.class;
    }

    @Override
    public PersistenceFunctionRenderer build(MetadataDefinitionHolder definitionHolder) {
        return this;
    }
}
