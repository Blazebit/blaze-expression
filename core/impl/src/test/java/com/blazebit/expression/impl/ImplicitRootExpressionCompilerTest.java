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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.expression.Predicate;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ImplicitRootExpressionCompilerTest extends AbstractExpressionCompilerTest {

    @Test
    public void testAddition() {
        setImplicitRootProvider((pathParts, context) -> {
            for (Map.Entry<String, DomainType> entry : context.getRootDomainTypes().entrySet()) {
                if (entry.getValue() instanceof EntityDomainType) {
                    EntityDomainType entityDomainType = (EntityDomainType) entry.getValue();
                    if (entityDomainType.getAttribute(pathParts.get(0)) != null) {
                        return entry.getKey();
                    }
                }
            }

            return null;
        });
        Predicate predicate = parsePredicateOnly("age > 1");
        assertEquals(
                gt(attr("user", "age"), number(1L)),
                predicate
        );
    }
}
