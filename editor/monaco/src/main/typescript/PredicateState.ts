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

import * as monaco from "monaco-editor/esm/vs/editor/editor.api";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PredicateState implements monaco.languages.IState {

    public readonly mode: number;

    constructor(mode: number) {
        this.mode = mode;
    }

    clone(): monaco.languages.IState {
        return new PredicateState(this.mode);
    }

    equals(other: monaco.languages.IState): boolean {
        return this.mode == (other as PredicateState).mode;
    }

}