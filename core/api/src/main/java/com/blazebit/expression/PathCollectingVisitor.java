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

package com.blazebit.expression;

import java.util.Collection;

/**
 * A visitor that collects paths.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PathCollectingVisitor extends VisitorAdapter {

    private final Collection<Path> paths;

    /**
     * Creates a new path collecting visitor.
     *
     * @param paths The collection to add the paths to
     */
    public PathCollectingVisitor(Collection<Path> paths) {
        this.paths = paths;
    }

    @Override
    public void visit(Path e) {
        paths.add(e);
        if (e.getBase() != null) {
            e.getBase().accept(this);
        }
    }
}
