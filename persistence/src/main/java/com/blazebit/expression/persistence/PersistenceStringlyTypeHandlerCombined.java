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

import com.blazebit.expression.base.StringlyTypeHandler;

/**
 * A combined contract for persistence and interpretation to allow uses of lambdas in {@link PersistenceStringlyTypeUtils}.
 *
 * @param <T> The stringly type
 * @author Christian Beikov
 * @since 1.0.0
 */
public interface PersistenceStringlyTypeHandlerCombined<T> extends PersistenceStringlyTypeHandler, StringlyTypeHandler<T> {

}
