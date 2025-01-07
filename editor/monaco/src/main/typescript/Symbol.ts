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

import {DomainType} from "blaze-domain";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class Symbol {
    /**
     * The symbol name.
     */
    name: string;
    /**
     * The symbol name rendered as identifier.
     */
    identifier: string;
    /**
     * The type of the symbol.
     */
    type: DomainType;
    /**
     * The symbol documentation.
     */
    documentation: string;

    constructor(name: string, identifier: string, type: DomainType, documentation: string) {
        this.name = name;
        this.identifier = identifier;
        this.type = type;
        this.documentation = documentation;
    }
}