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

package com.blazebit.expression;

import java.util.List;

/**
 * A provider for an implicit root variable.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface ImplicitRootProvider {
    /**
     * Returns the implicit root alias to use for the given path parts or <code>null</code>.
     *
     * @param pathParts The path parts
     * @param context The compile context
     * @return the implicit root alias to use for the given path parts or <code>null</code>
     */
    public String determineImplicitRoot(List<String> pathParts, ExpressionCompiler.Context context);
}
