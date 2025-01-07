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

import com.blazebit.domain.Domain;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceConformanceTest {

    @Test
    public void testConformance() {
        DomainModel domainModel = Domain.getDefaultProvider().createDefaultBuilder().build();
        List<String> failed = new ArrayList<>();
        for (DomainFunction domainFunction : domainModel.getFunctions().values()) {
            PersistenceFunctionRenderer persistenceFunctionRenderer = domainFunction.getMetadata(PersistenceFunctionRenderer.class);
            if (persistenceFunctionRenderer == null) {
                failed.add(domainFunction.getName());
            }
        }
        if (!failed.isEmpty()) {
            Assert.fail("Not all functions have function renderers: " + failed);
        }
    }
}
