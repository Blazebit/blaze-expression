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

import {DomainModel, DomainType} from "blaze-domain";
import {EntityLiteral} from "./EntityLiteral";
import {LiteralKind} from "./LiteralKind";
import {EnumLiteral} from "./EnumLiteral";
import {CollectionLiteral} from "./CollectionLiteral";

/**
 * A resolver for literal values.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export interface LiteralResolver {
    /**
     * Resolves the domain type of the given literal value.
     *
     * @param domainModel The domain model
     * @param kind The kind of literal
     * @param value The boolean value
     * @return the resolved literal
     */
    resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType;
}