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
import * as monaco from "monaco-editor";
import {CollectionDomainType, EntityDomainType, EnumDomainType} from "blaze-domain";
import {symbolTables} from "./EditorFactory";
import {SymbolTable} from "./SymbolTable";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PredicateCompletionProvider extends PathResolvingProvider implements monaco.languages.CompletionItemProvider {

    literalCssClass: string;
    // If these chars are offending for the text before the cursor, we do not provide suggestions
    disallowedOffendingChars: RegExp = /['")]/
    triggerCharacters: string[] = [];

    constructor(templateMode: boolean, literalCssClass: string = null, identifierStart: RegExp = /[a-zA-Z_$\u0080-\ufffe]/, identifier: RegExp = /[a-zA-Z_$0-9\u0080-\ufffe]/, pathOperators: string[] = ['.']) {
        super(templateMode, identifierStart, identifier, pathOperators);
        this.literalCssClass = literalCssClass;
        for (var i = 0; i < pathOperators.length; i++) {
            let pathOperator = pathOperators[i];
            if (pathOperator.length == 1) {
                this.triggerCharacters.push(pathOperator);
            }
        }
    }

    provideCompletionItems(model: monaco.editor.ITextModel, position: monaco.Position, context: monaco.languages.CompletionContext, token: monaco.CancellationToken): monaco.languages.CompletionList {
        let textBeforeCursor = model.getValueInRange({
            startLineNumber: 1,
            startColumn: 1,
            endLineNumber: position.lineNumber,
            endColumn: position.column
        });
        let symbolTable = symbolTables[model.id];
        // This is a "hack" to be able to avoid suggestions in string literals
        // The line token API is not public and the matching against the class name is also not nice
        // But this is the only way without tokenizing again
        let lineTokens = (model as any).getLineTokens(position.lineNumber);
        let tokenIndex = lineTokens.findTokenIndexAtOffset(position.column);
        if (this.literalCssClass == lineTokens.getClassName(tokenIndex)) {
            return {
                suggestions: [],
                incomplete: false
            };
        }
        let wordPosition = model.getWordUntilPosition(position);
        let range: monaco.IRange = {
            startLineNumber: position.lineNumber,
            endLineNumber: position.lineNumber,
            startColumn: wordPosition.startColumn,
            endColumn: position.column
        };
        return this.computeCompletionItems(textBeforeCursor, symbolTable, range);
    }

    computeCompletionItems(textBeforeCursor: string, symbolTable: SymbolTable, range?: monaco.IRange): monaco.languages.CompletionList {
        let suggestions: monaco.languages.CompletionItem[] = [];
        let incomplete = false;
        let pathResult = this.analyzePathBeforeCursor(textBeforeCursor);
        if (typeof range === 'undefined') {
            range = { startLineNumber: 1, endLineNumber: 1, startColumn: pathResult.offendingCharIndex + 1, endColumn: textBeforeCursor.length };
        }
        // If the offending character is a quote char, we try to replace it as well
        if (pathResult.offendingChar == '`') {
            range = {
                startLineNumber: range.startLineNumber,
                endLineNumber: range.endLineNumber,
                startColumn: range.startColumn - 1,
                endColumn: range.endColumn + 1
            };
        }
        let originalText = textBeforeCursor;
        textBeforeCursor = pathResult.text;
        let offendingChar = pathResult.offendingChar;

        SUGGEST: if (pathResult.pathParts.length != 0 && pathResult.pathParts[0].length != 0 && pathResult.pathParts[0].charAt(0).match(this.identifierStart)) {
            if (this.templateMode) {
                // In the template mode we only provide suggestions if we encounter the expression start token
                if (!symbolTable.model.inTemplateExpressionContext(originalText.substring(0, pathResult.offendingCharIndex + 1))) {
                    break SUGGEST;
                }
            }
            let parts = pathResult.pathParts;
            if (parts.length == 1) {
                for (let v in symbolTable.variables) {
                    suggestions.push(this.varSuggestion(symbolTable.variables[v], range));
                }
                let types = symbolTable.model.domainModel.types;
                for (let t in types) {
                    let item = this.typeSuggestion(types[t], range);
                    if (item != null) {
                        suggestions.push(item);
                    }
                }
                let funcs = symbolTable.model.domainModel.functions;
                for (let f in funcs) {
                    suggestions.push(this.functionSuggestion(funcs[f], range));
                }
            } else {
                if (parts.length == 2) {
                    let type = symbolTable.model.domainModel.types[parts[0].trim()];
                    if (type instanceof EnumDomainType) {
                        for (let enumValue in type.enumValues) {
                            suggestions.push(this.enumSuggestion(type, type.enumValues[enumValue], range));
                        }
                    }
                }
                if (suggestions.length == 0) {
                    let basePath = textBeforeCursor.substring(0, pathResult.lastPathPartDotIndex);
                    let domainType = symbolTable.model.resolveType(basePath, symbolTable);
                    if (domainType instanceof CollectionDomainType) {
                        domainType = domainType.elementType;
                    }
                    if (domainType instanceof EntityDomainType) {
                        for (let a in domainType.attributes) {
                            suggestions.push(this.attrSuggestion(domainType.attributes[a], range));
                        }
                    }
                }
            }
            incomplete = suggestions.length == 0;
        }

        SUGGEST: if (textBeforeCursor.length == 0) {
            if (this.disallowedOffendingChars.test(offendingChar)) {
                return {
                    suggestions: suggestions,
                    incomplete: false
                };
            }
            if (this.templateMode) {
                // In the template mode we only provide suggestions if we encounter the expression start token
                if (!symbolTable.model.inTemplateExpressionContext(originalText.substring(0, pathResult.offendingCharIndex + 1))) {
                    break SUGGEST;
                }
            }
            for (let v in symbolTable.variables) {
                suggestions.push(this.varSuggestion(symbolTable.variables[v], range));
            }
            let types = symbolTable.model.domainModel.types;
            for (let t in types) {
                let item = this.typeSuggestion(types[t], range);
                if (item != null) {
                    suggestions.push(item);
                }
            }
            let funcs = symbolTable.model.domainModel.functions;
            for (let f in funcs) {
                suggestions.push(this.functionSuggestion(funcs[f], range));
            }
            incomplete = suggestions.length == 0;
        }
        return {
            suggestions: suggestions,
            incomplete: incomplete
        };
    }

    resolveCompletionItem(item: monaco.languages.CompletionItem, token: monaco.CancellationToken): monaco.languages.ProviderResult<monaco.languages.CompletionItem> {
        return item;
    }

}