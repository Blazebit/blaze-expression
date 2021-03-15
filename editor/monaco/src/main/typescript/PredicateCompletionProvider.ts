/*
 * Copyright 2019 - 2021 Blazebit.
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
import * as monaco from "monaco-editor";
import {CollectionDomainType, EntityDomainType, EnumDomainType} from "blaze-domain";
import {symbolTables} from "./EditorFactory";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PredicateCompletionProvider extends PathResolvingProvider implements monaco.languages.CompletionItemProvider {

    disallowedOffendingChars: RegExp = /[")]/
    triggerCharacters: string[] = [];

    constructor(identifierStart: RegExp = /[a-zA-Z_$\u0080-\ufffe]/, identifier: RegExp = /[a-zA-Z_$0-9\u0080-\ufffe]/, pathOperators: string[] = ['.']) {
        super(identifierStart, identifier, pathOperators);
        for (var i = 0; i < pathOperators.length; i++) {
            let pathOperator = pathOperators[i];
            if (pathOperator.length == 1) {
                this.triggerCharacters.push(pathOperator);
            }
        }
    }

    provideCompletionItems(model: monaco.editor.ITextModel, position: monaco.Position, context: monaco.languages.CompletionContext, token: monaco.CancellationToken): monaco.languages.CompletionList {
        let suggestions: monaco.languages.CompletionItem[] = [];
        let textBeforeCursor = model.getValueInRange({
            startLineNumber: 1,
            startColumn: 1,
            endLineNumber: position.lineNumber,
            endColumn: position.column
        });
        let pathResult = this.analyzePathBeforeCursor(textBeforeCursor);
        textBeforeCursor = pathResult.text;
        let offendingChar = pathResult.offendingChar;

        let symbolTable = symbolTables[model.id];
        if (textBeforeCursor.length != 0 && textBeforeCursor.charAt(0).match(this.identifierStart)) {
            let parts = textBeforeCursor.split(this.pathOperators);
            if (parts.length == 1) {
                for (let v in symbolTable.variables) {
                    suggestions.push(this.varSuggestion(v, symbolTable.variables[v]));
                }
                let types = symbolTable.model.domainModel.types;
                for (let t in types) {
                    let item = this.typeSuggestion(t, types[t])
                    if (item != null) {
                        suggestions.push(item);
                    }
                }
                let funcs = symbolTable.model.domainModel.functions;
                for (let f in funcs) {
                    suggestions.push(this.functionSuggestion(funcs[f]));
                }
            } else {
                let basePath = textBeforeCursor.substring(0, textBeforeCursor.length - parts[parts.length - 1].length - 1);
                if (parts.length == 2) {
                    let type = symbolTable.model.domainModel.types[basePath.trim()];
                    if (type instanceof EnumDomainType) {
                        for (let enumValue in type.enumValues) {
                            suggestions.push(this.enumSuggestion(enumValue, type, type.enumValues[enumValue]));
                        }
                    }
                }
                if (suggestions.length == 0) {
                    let domainType = symbolTable.model.resolveType(basePath, symbolTable);
                    if (domainType instanceof CollectionDomainType) {
                        domainType = domainType.elementType;
                    }
                    if (domainType instanceof EntityDomainType) {
                        for (let a in domainType.attributes) {
                            suggestions.push(this.attrSuggestion(a, domainType.attributes[a]));
                        }
                    }
                }
            }
        }

        if (textBeforeCursor.length == 0) {
            if (this.disallowedOffendingChars.test(offendingChar)) {
                return {
                    suggestions: suggestions,
                    incomplete: false
                };
            }
            for (let v in symbolTable.variables) {
                suggestions.push(this.varSuggestion(v, symbolTable.variables[v]));
            }
            let types = symbolTable.model.domainModel.types;
            for (let t in types) {
                let item = this.typeSuggestion(t, types[t])
                if (item != null) {
                    suggestions.push(item);
                }
            }
            let funcs = symbolTable.model.domainModel.functions;
            for (let f in funcs) {
                suggestions.push(this.functionSuggestion(funcs[f]));
            }
        }
        return {
            suggestions: suggestions,
            incomplete: suggestions.length == 0
        };
    }

    resolveCompletionItem(model: monaco.editor.ITextModel, position: monaco.Position, item: monaco.languages.CompletionItem, token: monaco.CancellationToken): PromiseLike<monaco.languages.CompletionItem | undefined | null> | monaco.languages.CompletionItem | undefined | null {
        return item;
    }

}