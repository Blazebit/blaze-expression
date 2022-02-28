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

import {PathResolvingProvider} from "./PathResolvingProvider";
import * as monaco from "monaco-editor/esm/vs/editor/editor.api";
import {symbolTables} from "./EditorFactory";
import {TypeResolvingBlazeExpressionParserVisitor} from "./PredicateInlayHintsBlazeExpressionParserVisitor";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PredicateInlayHintsProvider extends PathResolvingProvider implements monaco.languages.InlayHintsProvider {

    constructor(templateMode: boolean, identifierStart: RegExp = /[a-zA-Z_$\u0080-\ufffe]/, identifier: RegExp = /[a-zA-Z_$0-9\u0080-\ufffe]/, pathOperators: string[] = ['.']) {
        super(templateMode, identifierStart, identifier, pathOperators);
    }

    provideInlayHints(model: monaco.editor.ITextModel, range: monaco.Range, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.InlayHintList> {
        let symbolTable = symbolTables[model.id];
        let value = model.getValueInRange(range);
        let visitor = new TypeResolvingBlazeExpressionParserVisitor(symbolTable);
        symbolTable.model.parseTree(value, this.templateMode).accept(visitor);
        return {
            hints: visitor.hints,
            dispose() {}
        };
    }

}