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

import {PathResult} from "./PathResult";
import * as monaco from "monaco-editor";
import {Symbol} from "./Symbol";
import {IMarkdownString} from "monaco-editor";
import {
    DomainFunction,
    DomainType,
    EntityAttribute,
    EnumDomainType,
    EnumDomainTypeValue
} from "blaze-domain";
import {QuoteMode} from "./QuoteMode";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PathResolvingProvider {

    identifierStart: RegExp;
    identifier: RegExp;
    pathOperators: RegExp;

    constructor(identifierStart: RegExp = /[a-zA-Z_$\u0080-\ufffe]/, identifier: RegExp = /[a-zA-Z_$0-9\u0080-\ufffe]/, pathOperators: string[] = ['.']) {
        this.identifierStart = identifierStart;
        this.identifier = identifier;
        var pathOperatorsRegex = "";

        for (var i = 0; i < pathOperators.length; i++) {
            let pathOperator = pathOperators[i];
            if (i != 0) {
                pathOperatorsRegex += "|";
            }
            pathOperatorsRegex += pathOperator.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
        }
        this.pathOperators = new RegExp(pathOperatorsRegex);
    }

    analyzePathBeforeCursor(textBeforeCursor: string): PathResult {
        let i = textBeforeCursor.length;
        var offendingChar = " ";
        OUTER: while (i--) {
            let c = textBeforeCursor.charAt(i);
            if (!this.pathOperators.test(c) && !this.identifier.test(c)) {
                let text = textBeforeCursor;
                textBeforeCursor = textBeforeCursor.substring(i + 1);
                // Find the first non-whitespace offending char
                do {
                    if (!/\s/.test((c = text.charAt(i)))) {
                        offendingChar = c;
                        if (c == ')') {
                            // If we find a parenthesis, we try to find a matching open parenthesis
                            let oldPos = i;
                            let parenthesis = 1;
                            let mode = QuoteMode.NONE;
                            while (i-- && parenthesis != 0) {
                                let c2 = text.charAt(i);
                                mode = mode.onCharBackwards(c2);
                                if (mode == QuoteMode.NONE) {
                                    if (c2 == '(') {
                                        parenthesis--;
                                    } else if (c2 == ')') {
                                        parenthesis++;
                                    }
                                }
                            }

                            if (parenthesis == 0) {
                                textBeforeCursor = text;
                                offendingChar = " ";
                                continue OUTER;
                            }
                            i = oldPos;
                        } else if (this.identifier.test(c) && this.pathOperators.test(textBeforeCursor.charAt(0))) {
                            // To support suggestion in multi-line scenarios, we allow whitespace before a path operator character
                            textBeforeCursor = text;
                            offendingChar = " ";
                            continue OUTER;
                        }
                        break;
                    }
                    i--;
                } while (i != 0);
                break;
            }
        }
        return { text: textBeforeCursor, offendingChar: offendingChar };
    }

    analyzePathAroundCursor(model: monaco.editor.ITextModel, position: monaco.Position): PathResult {
        let textBeforeCursor = model.getValueInRange({
            startLineNumber: 1,
            startColumn: 1,
            endLineNumber: position.lineNumber,
            endColumn: position.column
        });
        let pathResult = this.analyzePathBeforeCursor(textBeforeCursor);
        let textAfterCursor = model.getValueInRange({
            startLineNumber: position.lineNumber,
            startColumn: position.column,
            endLineNumber: position.lineNumber,
            endColumn: model.getLineMaxColumn(position.lineNumber)
        });

        let offendingChar = " ";
        for (let i = 0; i < textAfterCursor.length; i++) {
            let c = textAfterCursor.charAt(i);
            if (this.pathOperators.test(c)) {
                textAfterCursor = textAfterCursor.substring(0, i);
                offendingChar = c;
                break;
            }
            if (!this.identifier.test(c)) {
                let text = textAfterCursor;
                textAfterCursor = textAfterCursor.substring(0, i);
                // Find the first non-whitespace offending char
                do {
                    if (!/\s/.test((c = text.charAt(i)))) {
                        offendingChar = c;
                        break;
                    }
                    i++;
                } while (i != text.length);
                break;
            }
        }
        return { text: pathResult.text + textAfterCursor, offendingChar: offendingChar };
    }

    varSuggestion(v: string, s: Symbol): monaco.languages.CompletionItem {
        let doc: string | IMarkdownString = "";
        if (s.documentation != null) {
            doc = { value: s.documentation, isTrusted: true };
        }
        return {
            label: v,
            kind: monaco.languages.CompletionItemKind.Variable,
            detail: s.type.name,
            documentation: doc,
            insertText: v,
            range: null
        }
    }

    attrSuggestion(v: string, a: EntityAttribute): monaco.languages.CompletionItem {
        let doc: string | IMarkdownString = "";
        if (a.documentation != null) {
            doc = { value: a.documentation, isTrusted: true };
        }
        return {
            label: v,
            kind: monaco.languages.CompletionItemKind.Field,
            detail: a.type.name,
            documentation: doc,
            insertText: v,
            range: null
        }
    }

    enumSuggestion(v: string, t: EnumDomainType, e: EnumDomainTypeValue): monaco.languages.CompletionItem {
        let doc: string | IMarkdownString = "";
        if (e.documentation != null) {
            doc = { value: e.documentation, isTrusted: true };
        }
        return {
            label: v,
            kind: monaco.languages.CompletionItemKind.EnumMember,
            detail: t.name,
            documentation: doc,
            insertText: v,
            range: null
        }
    }

    typeSuggestion(v: string, t: DomainType): monaco.languages.CompletionItem {
        if (t instanceof EnumDomainType) {
            let doc: string | IMarkdownString = "";
            // if (t.documentation != null) {
            //     doc = { value: t.documentation, isTrusted: true };
            // }
            return {
                label: v,
                kind: monaco.languages.CompletionItemKind.Enum,
                detail: t.name,
                documentation: doc,
                insertText: v,
                range: null
            }
        }
        return null;
    }

    functionSuggestion(f: DomainFunction): monaco.languages.CompletionItem {
        let label: string;
        if (f.resultType == null) {
            label = "any";
        } else {
            label = f.resultType.name;
        }
        label += " " + f.name + "(";

        var paramInfo = "| Params | |\n" +
            "|------------:|-------------|";
        let documentation: string = "";
        if (f.documentation != null) {
            documentation += f.documentation + "\n\n";
        }
        for (var i = 0; i < f.arguments.length; i++) {
            if (i != 0) {
                label += ", ";
            }
            let p = f.arguments[i];
            paramInfo += "\n| " + p.name + " | ";
            if (p.documentation != null) {
                paramInfo += p.documentation;
            }
            if (p.type == null) {
                label += "any";
            } else {
                label += p.type.name;
            }
            label += " " + p.name;
            if (f.minArgumentCount != -1 && i >= f.minArgumentCount) {
                label += "?";
            }
        }
        label += ")";
        documentation += "---\n\n" + paramInfo + " |";
        return {
            label: f.name,
            detail: label,
            kind: monaco.languages.CompletionItemKind.Function,
            documentation: {value: documentation, isTrusted: true},
            insertText: f.arguments.length == 0 && f.minArgumentCount == 0 ? f.name + "()" : f.name + "($0)",
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
            range: null
        }
    }
}