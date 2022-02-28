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

import { expect } from 'chai';
import 'mocha';
import {PredicateCompletionProvider} from "./../../main/typescript/PredicateCompletionProvider";
import {UserModel} from "./UserModel";
import {SymbolTable} from "../../main/typescript";

let assertSuggestsLabels = (completionProvider: PredicateCompletionProvider, symbolTable: SymbolTable, text: string, ...labels: string[]) => {
    let completionItems = completionProvider.computeCompletionItems(text, symbolTable);
    let filteredCompletionItems;
    if (labels.length == 0) {
        filteredCompletionItems = completionItems.suggestions;
    } else {
        filteredCompletionItems = completionItems.suggestions.filter(item => {
            if (typeof item.label === 'string') {
                return labels.indexOf(item.label) != -1;
            }
            return labels.indexOf(item.label.label) != -1;
        });
    }
    expect(filteredCompletionItems).to.length(labels.length);
}

describe('Test completion', function() {
    const symbolTable = UserModel.getSymbolTable();
    const expressionCompleter = new PredicateCompletionProvider(false);
    const templateCompleter = new PredicateCompletionProvider(true);
    it('Expression attribute completion', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, "u.", "name");
    });
    it('Expression attribute completion with space prefix', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, " u.", "name");
    });
    it('Expression nested attribute completion with newline after dot', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, " u.\nparent.", "name");
    });
    it('Expression nested attribute completion with newline before dot', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, " u\n.parent.", "name");
    });
    it('Expression nested attribute completion with newline before dot on newline', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, " u\n.parent\n.", "name");
    });
    it('Expression nested attribute completion with newline after dot on newline', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, " u\n.parent.\n", "name");
    });
    it('Quoted expression completion', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, "`the", "the user");
    });
    it('Quoted expression completion within expression', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, "1 + `the", "the user");
    });
    it('Quoted expression attribute completion', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, "`the user`.", "name");
    });
    // it('Quoted expression in predicate attribute completion', function() {
    //     assertSuggestsLabels(expressionCompleter, symbolTable, "1=1 and `the user`.", "name");
    // });
    it('Expression empty completion', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, "", "u");
    });
    it('Expression attribute completion in parenthesis', function() {
        assertSuggestsLabels(expressionCompleter, symbolTable, "(u.", "name");
    });
    it('Template expression completion', function() {
        assertSuggestsLabels(templateCompleter, symbolTable, "Hello #{", "u");
    });
    it('Template expression sub-path completion', function() {
        assertSuggestsLabels(templateCompleter, symbolTable, "Hello #{u.", "name");
    });
    it('Template empty no-completion', function() {
        assertSuggestsLabels(templateCompleter, symbolTable, "");
    });
    it('Template text no-completion', function() {
        assertSuggestsLabels(templateCompleter, symbolTable, "u.");
    });
    it('Template text escaped expression start no-completion', function() {
        assertSuggestsLabels(templateCompleter, symbolTable, "Hello \\#{u.");
    });
});
