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

import {PathResolvingProvider} from "./PathResolvingProvider";
import * as monaco from "monaco-editor/esm/vs/editor/editor.api";
import {symbolTables} from "./EditorFactory";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PredicateSignatureHelpProvider extends PathResolvingProvider implements monaco.languages.SignatureHelpProvider {

    readonly signatureHelpRetriggerCharacters: ReadonlyArray<string>;
    readonly signatureHelpTriggerCharacters: ReadonlyArray<string>;

    constructor(templateMode: boolean, identifierStart: RegExp = /[a-zA-Z_$\u0080-\ufffe]/, identifier: RegExp = /[a-zA-Z_$0-9\u0080-\ufffe]/, pathOperators: string[] = ['.']) {
        super(templateMode, identifierStart, identifier, pathOperators);
        this.signatureHelpTriggerCharacters = [',', '('];
    }

    provideSignatureHelp(model: monaco.editor.ITextModel, position: monaco.Position, token: monaco.CancellationToken, context: monaco.languages.SignatureHelpContext): monaco.languages.ProviderResult<monaco.languages.SignatureHelpResult> {
        let originalPosition = position;
        let ranges: monaco.Range[] = null;
        do {
            ranges = (model as any).bracketPairs.findEnclosingBrackets(position);
            if (ranges == null || ranges.length == 0) {
                break;
            }
            let bracketType = model.getValueInRange({
                startLineNumber: ranges[0].startLineNumber,
                startColumn: ranges[0].startColumn,
                endLineNumber: ranges[0].endLineNumber,
                endColumn: ranges[0].endColumn
            });
            if (bracketType == '(' || bracketType == ')') {
                break;
            }
            let startLineNumber = Number.MAX_VALUE;
            let startColumn = Number.MAX_VALUE;
            for (let i = 0; i < ranges.length; i++) {
                if (ranges[i].startLineNumber < startLineNumber || ranges[i].startLineNumber == startLineNumber && ranges[i].startColumn < startColumn) {
                    startLineNumber = ranges[i].startLineNumber;
                    startColumn = ranges[i].startColumn;
                }
            }
            if (startColumn == 1) {
                if (startLineNumber == 1) {
                    ranges = null;
                    break;
                } else {
                    position = new monaco.Position(startLineNumber - 1, model.getLineMaxColumn(startLineNumber - 1));
                }
            } else {
                position = new monaco.Position(startLineNumber, startColumn - 1);
            }
        } while (true);
        if (ranges != null && ranges.length != 0) {
            let startLineNumber = Number.MAX_VALUE;
            let startColumn = Number.MAX_VALUE;
            let endLineNumber = Number.MIN_VALUE;
            let endColumn = Number.MIN_VALUE;
            for (let i = 0; i < ranges.length; i++) {
                if (ranges[i].startLineNumber < startLineNumber || ranges[i].startLineNumber == startLineNumber && ranges[i].startColumn < startColumn) {
                    startLineNumber = ranges[i].startLineNumber;
                    startColumn = ranges[i].startColumn;
                }
                if (ranges[i].startLineNumber > endLineNumber || ranges[i].endLineNumber == endLineNumber && ranges[i].endColumn > endColumn) {
                    endLineNumber = ranges[i].endLineNumber;
                    endColumn = ranges[i].endColumn;
                }
            }
            let textBeforeCursor = model.getValueInRange({
                startLineNumber: 1,
                startColumn: 1,
                endLineNumber: startLineNumber,
                endColumn: startColumn
            });
            let pathResult = this.analyzePathBeforeCursor(textBeforeCursor);
            let path = pathResult.text;

            let symbolTable = symbolTables[model.id];
            let completionItem = null;
            if (path.length != 0 && path.charAt(0).match(this.identifierStart)) {
                let parts = path.split(this.pathOperators);
                if (parts.length == 1) {
                    let domainFunction = symbolTable.model.domainModel.getFunction(path);
                    if (domainFunction != null) {
                        let maxArgumentCount = Math.max(domainFunction.argumentCount, domainFunction.arguments.length);
                        let activeParameter = 0;
                        // Try to find out the active parameter
                        let cursorOffset = model.getOffsetAt(originalPosition);
                        let bracketOffset = model.getOffsetAt({ lineNumber: startLineNumber, column: startColumn });
                        let cursorIndex = cursorOffset - bracketOffset;
                        let bracketExpression = model.getValueInRange({
                            startLineNumber: startLineNumber,
                            startColumn: startColumn,
                            endLineNumber: endLineNumber,
                            endColumn: endColumn
                        });

                        // try {
                        // let subExpression = path + bracketExpression;
                        //     // TODO: Maybe try to somehow gracefully recover from syntax errors
                        //     let tree = parseTree(subExpression);
                        //     console.log(tree);
                        //     // TODO: we have to collect the function argument parse trees and match them against the cursor
                        // } catch (e) {

                        let paramsString = bracketExpression.substring(1, bracketExpression.length - 1);
                        cursorIndex -= 2;
                        let startIdx = 0;
                        let commaIndex = -1;
                        do {
                            let idx = paramsString.indexOf(",", commaIndex + 1);
                            if (idx == -1 || idx > cursorIndex || activeParameter == maxArgumentCount) {
                                break;
                            }

                            let expr = paramsString.substring(startIdx, idx);
                            try {
                                symbolTable.model.parseTree(expr);
                                startIdx = idx + 1;
                                activeParameter++;
                            } catch (e) {}
                            commaIndex = idx;
                        } while (commaIndex != -1);
                        // }

                        completionItem = this.functionSuggestion(domainFunction);
                        let params: monaco.languages.ParameterInformation[] = [];
                        for (var i = 0; i < domainFunction.arguments.length; i++) {
                            let p = domainFunction.arguments[i];
                            let label;
                            if (p.type == null) {
                                label = "any";
                            } else {
                                label = p.type.name;
                            }
                            label += " " + p.name;
                            if (domainFunction.minArgumentCount != -1 && i >= domainFunction.minArgumentCount) {
                                label += "?";
                            }
                            let doc: string | monaco.IMarkdownString = "";
                            if (p.documentation != null) {
                                doc = { value: p.documentation, isTrusted: true };
                            }
                            params.push({ label: label, documentation: doc });
                        }
                        let doc: string | monaco.IMarkdownString = "";
                        if (domainFunction.documentation != null) {
                            doc = { value: domainFunction.documentation, isTrusted: true };
                        }
                        return {
                            value: {
                                activeSignature: 0,
                                activeParameter: activeParameter,
                                signatures: [
                                    { label: completionItem.detail, documentation: doc, parameters: params }
                                ]
                            },
                            dispose() {}
                        };
                    }
                }
            }
        }
        return null;
    }
}
