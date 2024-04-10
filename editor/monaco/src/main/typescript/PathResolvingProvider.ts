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

import {PathResult} from "./PathResult";
import * as monaco from "monaco-editor/esm/vs/editor/editor.api";
import {Symbol} from "./Symbol";
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

    templateMode: boolean;
    identifierStart: RegExp;
    identifier: RegExp;
    pathOperators: RegExp;

    constructor(templateMode: boolean, identifierStart: RegExp = /[a-zA-Z_$\u0080-\ufffe]/, identifier: RegExp = /[a-zA-Z_$0-9\u0080-\ufffe]/, pathOperators: string[] = ['.']) {
        this.templateMode = templateMode;
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
        let text = textBeforeCursor;
        let i = textBeforeCursor.length;
        let pathParts: string[] = [];
        let lastPathOperatorEnd = i;
        let lastPathPartDotIndex = -1;
        let offendingChar = ' ';
        OUTER: while (i--) {
            let c = textBeforeCursor.charAt(i);
            // Consume the whole quote if we encounter one
            QUOTE: if (c == '`') {
                let quoteCharIndex = i;
                if (i == 0) {
                    offendingChar = c;
                    break;
                }
                while (i--) {
                    c = textBeforeCursor.charAt(i);
                    if (c == '`') {
                        if (i == 0) {
                            offendingChar = ' ';
                            break OUTER;
                        }
                        c = textBeforeCursor.charAt(--i);
                        break QUOTE;
                    }
                }
                // If we get here, the quote is not closed on the lhs, so we treat the quote chars as offending and stop
                offendingChar = '`';
                i = quoteCharIndex;
                textBeforeCursor = textBeforeCursor.substring(i + 1);
                break;
            }
            if (this.pathOperators.test(c)) {
                if (lastPathPartDotIndex == -1) {
                    lastPathPartDotIndex = i;
                }
                if (textBeforeCursor.charAt(i + 1) == '`') {
                    pathParts.unshift(textBeforeCursor.substring(i + 2, lastPathOperatorEnd - 1));
                } else {
                    pathParts.unshift(textBeforeCursor.substring(i + 1, lastPathOperatorEnd).trim());
                }
                lastPathOperatorEnd = i;
            }
            else if (!this.identifier.test(c)) {
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
                                offendingChar = ' ';
                                continue OUTER;
                            }
                            i = oldPos;
                        } else if (this.pathOperators.test(c)) {
                            // To support suggestion in multi-line scenarios, we allow whitespace after a path operator character
                            textBeforeCursor = text;
                            offendingChar = ' ';
                            i++;
                            continue OUTER;
                        } else if (this.identifier.test(c) && textBeforeCursor.length != 0 && this.pathOperators.test(textBeforeCursor.charAt(0))) {
                            // To support suggestion in multi-line scenarios, we allow whitespace before a path operator character
                            textBeforeCursor = text;
                            offendingChar = ' ';
                            continue OUTER;
                        }
                        break;
                    }
                    i--;
                } while (i >= 0);
                break;
            }
        }
        if (text.charAt(i + 1) == '`') {
            pathParts.unshift(text.substring(i + 2, lastPathOperatorEnd - 1));
        } else {
            pathParts.unshift(text.substring(i + 1, lastPathOperatorEnd).trim());
        }
        return {
            text: textBeforeCursor,
            pathParts: pathParts,
            lastPathPartDotIndex: lastPathPartDotIndex - (text.length - textBeforeCursor.length),
            offendingChar: offendingChar,
            offendingCharIndex: i
        };
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

        let i = 0;
        let offendingChar = " ";
        for (; i < textAfterCursor.length; i++) {
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
        pathResult.pathParts[pathResult.pathParts.length - 1] = pathResult.pathParts[pathResult.pathParts.length - 1] + textAfterCursor;
        return {
            text: pathResult.text + textAfterCursor,
            pathParts: pathResult.pathParts,
            lastPathPartDotIndex: pathResult.lastPathPartDotIndex,
            offendingChar: offendingChar,
            offendingCharIndex: i
        };
    }

    // NOTE: that we use
    //   filterText: identifier.toUpperCase()
    // Because apparently otherwise, monaco won't suggest certain quote identifiers

    varSuggestion(s: Symbol, range: monaco.IRange = null): monaco.languages.CompletionItem {
        let doc: string | monaco.IMarkdownString = "";
        if (s.documentation != null) {
            doc = { value: s.documentation, isTrusted: true };
        }
        let identifier = s.identifier;
        return {
            label: s.name,
            kind: monaco.languages.CompletionItemKind.Variable,
            detail: s.type.name,
            documentation: doc,
            insertText: identifier,
            filterText: identifier.toUpperCase(),
            range: this.rangeForIdentifier(range, identifier)
        }
    }

    attrSuggestion(a: EntityAttribute, range: monaco.IRange = null): monaco.languages.CompletionItem {
        let doc: string | monaco.IMarkdownString = "";
        if (a.documentation != null) {
            doc = { value: a.documentation, isTrusted: true };
        }
        let identifier = (a as any).identifier;
        return {
            label: a.name,
            kind: monaco.languages.CompletionItemKind.Field,
            detail: a.type.name,
            documentation: doc,
            insertText: identifier,
            filterText: identifier.toUpperCase(),
            range: this.rangeForIdentifier(range, identifier)
        }
    }

    enumSuggestion(t: EnumDomainType, e: EnumDomainTypeValue, range: monaco.IRange = null): monaco.languages.CompletionItem {
        let doc: string | monaco.IMarkdownString = "";
        if (e.documentation != null) {
            doc = { value: e.documentation, isTrusted: true };
        }
        let identifier = (e as any).identifier;
        return {
            label: e.value,
            kind: monaco.languages.CompletionItemKind.EnumMember,
            detail: t.name,
            documentation: doc,
            insertText: identifier,
            filterText: identifier.toUpperCase(),
            range: this.rangeForIdentifier(range, identifier)
        }
    }

    typeSuggestion(t: DomainType, range: monaco.IRange = null): monaco.languages.CompletionItem {
        if (t instanceof EnumDomainType) {
            let doc: string | monaco.IMarkdownString = "";
            // if (t.documentation != null) {
            //     doc = { value: t.documentation, isTrusted: true };
            // }
            let identifier = (t as any).identifier;
            return {
                label: t.name,
                kind: monaco.languages.CompletionItemKind.Enum,
                detail: t.name,
                documentation: doc,
                insertText: identifier,
                filterText: identifier.toUpperCase(),
                range: this.rangeForIdentifier(range, identifier)
            }
        }
        return null;
    }

    functionSuggestion(f: DomainFunction, range: monaco.IRange = null): monaco.languages.CompletionItem {
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
        if (f.arguments.length != 0) {
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
            documentation += "---\n\n" + paramInfo + " |";
        }
        label += ")";
        let identifier = (f as any).identifier;
        return {
            label: f.name,
            detail: label,
            kind: monaco.languages.CompletionItemKind.Function,
            documentation: {value: documentation, isTrusted: true},
            insertText: f.arguments.length == 0 && f.minArgumentCount == 0 ? identifier + "()" : identifier + "($0)",
            insertTextRules: monaco.languages.CompletionItemInsertTextRule.InsertAsSnippet,
            filterText: identifier.toUpperCase(),
            range: this.rangeForIdentifier(range, identifier)
        }
    }

    private rangeForIdentifier(range: monaco.IRange, identifier: string): { insert: monaco.IRange, replace: monaco.IRange } {
        if (range == null) {
            return null;
        }
        return {
            insert: range,
            replace: range
        };
    }
}