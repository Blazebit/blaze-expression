/*
 * Copyright 2019 - 2020 Blazebit.
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

import * as monaco from 'monaco-editor';
import {DomainModel, DomainType, EntityDomainType, DomainFunction} from 'blaze-domain';
import {CommonTokenStream, InputStream, Token, error, Parser} from 'antlr4/index.js'
import ILineTokens = monaco.languages.ILineTokens;
import {DefaultErrorStrategy} from 'antlr4/error/ErrorStrategy.js'
import {BlazeExpressionLexer} from "blaze-expression-predicate/BlazeExpressionLexer.js"
import {BlazeExpressionParser} from "blaze-expression-predicate/BlazeExpressionParser.js"
import IToken = monaco.languages.IToken;

let symbolTables: StringMap<SymbolTable> = {};

export function createEditor(monaco, domElement: HTMLElement, input: string, extensions?: any, options?: monaco.editor.IStandaloneEditorConstructionOptions) {
    if (monaco.languages.getEncodedLanguageId('predicate') == 0) {
        monaco.languages.register({id: 'predicate'});
        monaco.languages.setTokensProvider('predicate', new PredicateTokensProvider());

        var literalFg = '3b8737';
        var idFg = '344482';
        var symbolsFg = '000000';
        var keywordFg = '7132a8';
        var errorFg = 'ff0000';

        monaco.editor.defineTheme('blazeTheme', {
            base: 'vs',
            inherit: false,
            rules: [
                {token: 'numeric_literal.blaze', foreground: literalFg},
                {token: 'string_literal.blaze', foreground: literalFg},

                {token: 'identifier.blaze', foreground: idFg, fontStyle: 'italic'},
                {token: 'quoted_identifier.blaze', foreground: idFg, fontStyle: 'italic'},

                {token: 'less.blaze', foreground: symbolsFg},
                {token: 'less_equal.blaze', foreground: symbolsFg},
                {token: 'greater.blaze', foreground: symbolsFg},
                {token: 'greater_equal.blaze', foreground: symbolsFg},
                {token: 'equal.blaze', foreground: symbolsFg},
                {token: 'not_equal.blaze', foreground: symbolsFg},
                {token: 'plus.blaze', foreground: symbolsFg},
                {token: 'minus.blaze', foreground: symbolsFg},
                {token: 'asterisk.blaze', foreground: symbolsFg},
                {token: 'slash.blaze', foreground: symbolsFg},
                {token: 'percent.blaze', foreground: symbolsFg},

                {token: 'lp.blaze', foreground: symbolsFg},
                {token: 'rp.blaze', foreground: symbolsFg},
                {token: 'lb.blaze', foreground: symbolsFg},
                {token: 'rb.blaze', foreground: symbolsFg},
                {token: 'comma.blaze', foreground: symbolsFg},
                {token: 'dot.blaze', foreground: symbolsFg},
                {token: 'colon.blaze', foreground: symbolsFg},
                {token: 'exclamation_mark.blaze', foreground: symbolsFg},


                {token: 'and.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'between.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'days.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'empty.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'false.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'hours.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'in.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'interval.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'is.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'minutes.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'months.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'not.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'null.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'or.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'seconds.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'timestamp.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'true.blaze', foreground: keywordFg, fontStyle: 'bold'},
                {token: 'years.blaze', foreground: keywordFg, fontStyle: 'bold'},

                {token: 'error.blaze', foreground: errorFg}
            ]
        });

        // TODO: HoverProvider
        // monaco.languages.registerHoverProvider('predicate', {provideHover: function (model, position, token) {
        //
        // }});
        // TODO: SignatureHelpProvider
        // monaco.languages.registerSignatureHelpProvider('predicate', {provideSignatureHelp: function(model, position, token, context) {
        //
        // }});
        monaco.languages.registerCompletionItemProvider('predicate', new PredicateCompletionProvider());
    }

    let symbolTable = SymbolTable.parse(input, extensions);
    let textArea = domElement.getElementsByTagName('textarea')[0];
    let textModel = monaco.editor.createModel(textArea.innerText, 'predicate');
    symbolTables[textModel.id] = symbolTable;
    textModel.onWillDispose(function() {
        symbolTables[textModel.id] = null;
    });
    if (typeof options !== 'object') {
        options = {
            theme: 'blazeTheme',
            contextmenu: false,
            autoClosingBrackets: 'always',
            autoClosingQuotes: 'always',
            wordWrap: 'off',
            lineNumbers: 'off',
            scrollbar: { horizontal: 'hidden', vertical: 'hidden' },
            find: { addExtraSpaceOnTop: false, autoFindInSelection: 'never', seedSearchStringFromSelection: false },
            minimap: { enabled: false },
            suggest: { showWords: false }
        };
    }
    options.language = 'predicate';
    options.model = textModel;
    let editor = monaco.editor.create(domElement, options);
    // TODO: HoverProvider
    // monaco.languages.registerHoverProvider('predicate', {provideHover: function (model, position, token) {
    //
    // }});
    // TODO: SignatureHelpProvider
    // monaco.languages.registerSignatureHelpProvider('predicate', {provideSignatureHelp: function(model, position, token, context) {
    //
    // }});
    editor.onDidChangeModelContent(function (event) {
        var code = editor.getValue();
        textArea.innerText = code;
        var syntaxErrors = validate(code);
        var monacoErrors = [];
        for (var e of syntaxErrors) {
            monacoErrors.push({
                startLineNumber: e.startLine,
                startColumn: e.startCol,
                endLineNumber: e.endLine,
                endColumn: e.endCol,
                message: e.message,
                severity: monaco.MarkerSeverity.Error
            });
        };
        var model = monaco.editor.getModels()[0];
        monaco.editor.setModelMarkers(model, "owner", monacoErrors);
    });
    return editor;
}

export class PredicateState implements monaco.languages.IState {
    clone(): monaco.languages.IState {
        return new PredicateState();
    }

    equals(other: monaco.languages.IState): boolean {
        return true;
    }

}

export class PredicateTokensProvider implements monaco.languages.TokensProvider {
    getInitialState(): monaco.languages.IState {
        return new PredicateState();
    }

    tokenize(line: string, state: monaco.languages.IState): monaco.languages.ILineTokens {
        // So far we ignore the state, which is not great for performance reasons
        return tokensForLine(line);
    }

}

interface StringMap<T> {
    [key: string]: T;
}

export class SymbolTable {
    variables: StringMap<DomainType>;
    model: DomainModel;

    static parse(input: string, extension: StringMap<Function>): SymbolTable {
        let model = DomainModel.parse(input, extension);
        let json = JSON.parse(input), symbols = json['symbols'];
        let vars: StringMap<DomainType> = {};
        for (let name in symbols) {
            vars[name] = model.types[symbols[name]];
        }
        return { variables: vars, model: model };
    }
}

export class PredicateCompletionProvider implements monaco.languages.CompletionItemProvider {

    identifierStart: RegExp;
    identifier: RegExp;
    pathOperators: RegExp;
    disallowedOffendingChars: RegExp = /[")]/
    triggerCharacters: string[] = [];

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

            if (pathOperator.length == 1) {
                this.triggerCharacters.push(pathOperator);
            }
        }
        this.pathOperators = new RegExp(pathOperatorsRegex);
    }

    provideCompletionItems(model: monaco.editor.ITextModel, position: monaco.Position, context: monaco.languages.CompletionContext, token: monaco.CancellationToken): monaco.languages.CompletionList {
        let suggestions: monaco.languages.CompletionItem[] = [];
        let textBeforeCursor = model.getValueInRange({
            startLineNumber: 1,
            startColumn: 1,
            endLineNumber: position.lineNumber,
            endColumn: position.column
        });
        let functionSuggestion = function(f: DomainFunction): monaco.languages.CompletionItem {
            let documentation: string;
            if (f.resultType == null) {
                documentation = "any";
            } else {
                documentation = f.resultType.name;
            }
            documentation += " " + f.name + "(\n";
            // TODO: Try to use markdown for params because html is not supported
            var paramInfo = "<table><tr><td>Params</td><td>";
            var label = f.name + "(";
            for (var i = 0; i < f.arguments.length; i++) {
                if (i != 0) {
                    label += ", ";
                    documentation += ",";
                }
                documentation += "\n\t";
                let p = f.arguments[i];
                if (p.type == null) {
                    label += "any";
                    documentation += "any";
                } else {
                    label += p.type.name;
                    documentation += p.type.name;
                }
                label += " " + p.name;
                documentation += " " + p.name;
                paramInfo += "\n" + p.name + " - " + p.documentation;
            }
            label += ")";
            documentation += ")\n---\n" + paramInfo + "</td></tr></table>";
            return {
                // TODO: not sure how to get the function name rendered nicer. Documentation shouldn't repeat detail..
                // label: label,
                // detail: f.type.name,
                label: f.name,
                detail: label,
                kind: monaco.languages.CompletionItemKind.Function,
                documentation: {value: documentation, isTrusted: true},
                insertText: f.name + "()",
                range: null
            }
        };
        let i = textBeforeCursor.length;
        var offendingChar = " ";
        // Only consider identifiers and paths
        while (i--) {
            let c = textBeforeCursor.charAt(i);
            if (!this.pathOperators.test(c) && !this.identifier.test(c)) {
                let text = textBeforeCursor;
                textBeforeCursor = textBeforeCursor.substring(i + 1);
                // Find the first non-whitespace offending char
                do {
                    if (!/\s/.test((c = text.charAt(i)))) {
                        offendingChar = c;
                        break;
                    }
                    i--;
                } while (i != 0);
                break;
            }
        }
        let symbolTable = symbolTables[model.id];
        if (textBeforeCursor.length != 0 && textBeforeCursor.charAt(0).match(this.identifierStart)) {
            let parts = textBeforeCursor.split(this.pathOperators);
            if (parts.length == 1) {
                for (let v in symbolTable.variables) {
                    if (v.indexOf(parts[0]) === 0) {
                        suggestions.push({
                            label: v,
                            kind: monaco.languages.CompletionItemKind.Variable,
                            detail: symbolTable.variables[v].name,
                            insertText: v,
                            range: null
                        });
                    }
                }
                let funcs = symbolTable.model.functions;
                for (let f in funcs) {
                    if (f.indexOf(parts[0]) === 0) {
                        suggestions.push(functionSuggestion(funcs[f]));
                    }
                }
            } else {
                let domainType = symbolTable.variables[parts[0]];
                let lastIdx = parts.length - 1;
                for (i = 1; i < lastIdx; i++) {
                    if (domainType instanceof EntityDomainType) {
                        let attribute = domainType.attributes[parts[i]];
                        if (attribute == null) {
                            domainType = null;
                            break;
                        }
                        domainType = attribute.type;
                    } else {
                        domainType = null;
                        break;
                    }
                }
                if (domainType instanceof EntityDomainType) {
                    for (let a in domainType.attributes) {
                        if (a.indexOf(parts[lastIdx]) === 0) {
                            suggestions.push({
                                label: a,
                                kind: monaco.languages.CompletionItemKind.Variable,
                                detail: domainType.attributes[a].type.name,
                                insertText: a,
                                range: null
                            });
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
                suggestions.push({
                    label: v,
                    kind: monaco.languages.CompletionItemKind.Variable,
                    detail: symbolTable.variables[v].name,
                    insertText: v,
                    range: null
                });
            }
            let funcs = symbolTable.model.functions;
            for (let f in funcs) {
                suggestions.push(functionSuggestion(funcs[f]));
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

const EOF = -1;

class PredicateToken implements IToken {
    scopes: string;
    startIndex: number;

    constructor(ruleName: String, startIndex: number) {
        this.scopes = ruleName.toLowerCase() + ".blaze";
        this.startIndex = startIndex;
    }
}

class PredicateLineTokens implements ILineTokens {
    endState: monaco.languages.IState;
    tokens: monaco.languages.IToken[];

    constructor(tokens: monaco.languages.IToken[]) {
        this.endState = new PredicateState();
        this.tokens = tokens;
    }
}

export function tokensForLine(input: string): monaco.languages.ILineTokens {
    let errorStartingPoints: number[] = [];

    class ErrorCollectorListener extends error.ErrorListener {
        syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
            errorStartingPoints.push(column)
        }
    }

    const lexer = createLexer(input);
    lexer.removeErrorListeners();
    let errorListener = new ErrorCollectorListener();
    lexer.addErrorListener(errorListener);
    let done = false;
    let myTokens: monaco.languages.IToken[] = [];
    do {
        let token = lexer.nextToken();
        if (token == null) {
            done = true
        } else {
            // We exclude EOF
            if (token.type == EOF) {
                done = true;
            } else {
                let tokenTypeName = lexer.symbolicNames[token.type];
                let myToken = new PredicateToken(tokenTypeName, token.column);
                myTokens.push(myToken);
            }
        }
    } while (!done);

    // Add all errors
    for (let e of errorStartingPoints) {
        myTokens.push(new PredicateToken("error.blaze", e));
    }
    myTokens.sort((a, b) => (a.startIndex > b.startIndex) ? 1 : -1)

    return new PredicateLineTokens(myTokens);
}


class ConsoleErrorListener extends error.ErrorListener {
    syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
        console.log("ERROR " + msg);
    }
}

export class Error {
    startLine: number;
    endLine: number;
    startCol: number;
    endCol: number;
    message: string;

    constructor(startLine: number, endLine: number, startCol: number, endCol: number, message: string) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.startCol = startCol;
        this.endCol = endCol;
        this.message = message;
    }

}

class CollectorErrorListener extends error.ErrorListener {

    private errors : Error[] = []

    constructor(errors: Error[]) {
        super()
        this.errors = errors
    }

    syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
        var endColumn = column + 1;
        if (offendingSymbol._text !== null) {
            endColumn = column + offendingSymbol._text.length;
        }
        this.errors.push(new Error(line, line, column, endColumn, msg));
    }

}

export function createLexer(input: String) {
    const chars = new InputStream(input);
    const lexer = new BlazeExpressionLexer(chars);

    lexer.strictMode = false;

    return lexer;
}

export function getTokens(input: String) : Token[] {
    return createLexer(input).getAllTokens()
}

function createParser(input) {
    const lexer = createLexer(input);

    return createParserFromLexer(lexer);
}

function createParserFromLexer(lexer) {
    const tokens = new CommonTokenStream(lexer);
    return new BlazeExpressionParser(tokens);
}

function parseTree(input) {
    const parser = createParser(input);

    return parser.parseExpression();
}

export function parseTreeStr(input) {
    const lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new ConsoleErrorListener());

    const parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new ConsoleErrorListener());

    const tree = parser.parseExpression();

    return tree.toStringTree(parser.ruleNames);
}

class SuggestionErrorListener extends error.ErrorListener {

    private errors : Error[] = []

    constructor(errors: Error[]) {
        super()
        this.errors = errors
    }

    syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
        console.log("ERROR " + msg);
    }
}

class BlazeExpressionErrorStrategy extends DefaultErrorStrategy {

    reportUnwantedToken(recognizer: Parser) {
        return super.reportUnwantedToken(recognizer);
    }

    singleTokenDeletion(recognizer: Parser) {
        var nextTokenType = recognizer.getTokenStream().LA(2);
        if (recognizer.getTokenStream().LA(1) == BlazeExpressionParser.NL) {
            return null;
        }
        var expecting = this.getExpectedTokens(recognizer);
        if (expecting.contains(nextTokenType)) {
            this.reportUnwantedToken(recognizer);
            recognizer.consume(); // simply delete extra token
            // we want to return the token we're actually matching
            var matchedSymbol = recognizer.getCurrentToken();
            this.reportMatch(recognizer); // we know current token is correct
            return matchedSymbol;
        } else {
            return null;
        }
    }
    getExpectedTokens = function(recognizer) {
        return recognizer.getExpectedTokens();
    };

    reportMatch = function(recognizer) {
        this.endErrorCondition(recognizer);
    };

}

export function validate(input: string) : Error[] {
    let errors : Error[] = [];

    const lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new ConsoleErrorListener());

    const parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new CollectorErrorListener(errors));
    parser._errHandler = new BlazeExpressionErrorStrategy();

    const tree = parser.parseExpression();
    return errors;
}