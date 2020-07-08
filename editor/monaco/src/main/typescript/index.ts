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
import {IMarkdownString} from 'monaco-editor';
import {
    BasicDomainType,
    CollectionDomainType,
    CollectionLiteral,
    DomainFunction,
    DomainModel,
    DomainOperator,
    DomainPredicate,
    DomainType,
    EntityAttribute,
    EntityDomainType,
    EntityLiteral,
    EnumLiteral,
    EnumDomainType,
    LiteralKind,
    LiteralResolver
} from 'blaze-domain';
import {CommonTokenStream, error, InputStream, Parser, ParserRuleContext, Token} from 'antlr4/index.js'
import {TerminalNode} from 'antlr4/tree/Tree.js'
import {DefaultErrorStrategy} from 'antlr4/error/ErrorStrategy.js'
import {BlazeExpressionLexer} from "blaze-expression-predicate/BlazeExpressionLexer.js"
import {BlazeExpressionParser} from "blaze-expression-predicate/BlazeExpressionParser.js"
import {BlazeExpressionParserVisitor} from "blaze-expression-predicate/BlazeExpressionParserVisitor.js"
import ILineTokens = monaco.languages.ILineTokens;
import IToken = monaco.languages.IToken;
import type = Mocha.utils.type;

let symbolTables: StringMap<SymbolTable> = {};

export function createEditor(monaco, domElement: HTMLElement, input: string, extensions?: any, options?: monaco.editor.IStandaloneEditorConstructionOptions) {
    if (monaco.languages.getEncodedLanguageId('predicate') == 0) {
        monaco.languages.register({id: 'predicate'});
        monaco.languages.setTokensProvider('predicate', new PredicateTokensProvider());
        monaco.languages.setLanguageConfiguration('predicate', {
            brackets: [['[', ']'], ['(', ')']],
            surroundingPairs: [
                { open: '[', close: ']' },
                { open: '(', close: ')' },
                { open: "'", close: "'" },
                { open: '"', close: '"' },
            ],
            autoClosingPairs: [
                { open: '[', close: ']' },
                { open: '(', close: ')' },
                { open: "'", close: "'", notIn: ['string'] },
                { open: '"', close: '"', notIn: ['string'] },
            ]
        });

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
                {token: 'leading_zero_numeric_literal.blaze', foreground: literalFg},
                {token: 'String', foreground: literalFg},

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

    let singleLineMode = true;
    if (typeof extensions === "undefined") {
        extensions = {};
    }
    let registerIfAbsent = function(k: string, f: Function) {
        if (!(extensions[k] instanceof Function)) {
            extensions[k] = f;
        }
    };

    registerIfAbsent("BooleanLiteralResolver", function(): LiteralResolver {
        return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
            return domainModel.types['Boolean'];
        }};
    });
    registerIfAbsent("NumericLiteralResolver", function(): LiteralResolver {
        return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
            if (isNaN(parseInt(value as string))) {
                return domainModel.types['Numeric'];
            } else {
                return domainModel.types['Integer'];
            }
        }};
    });
    registerIfAbsent("StringLiteralResolver", function(): LiteralResolver {
        return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
            return domainModel.types['String'];
        }};
    });
    registerIfAbsent("TemporalLiteralResolver", function(type: string): LiteralResolver {
        return { resolveLiteral(domainModel: DomainModel, kind: LiteralKind, value: boolean | string | EntityLiteral | EnumLiteral | CollectionLiteral): DomainType {
            if (kind == LiteralKind.INTERVAL) {
                return domainModel.types['Interval'];
            } else {
                return domainModel.types['Timestamp'];
            }
        }};
    });

    let symbolTable = SymbolTable.parse(input, extensions);
    let textArea = domElement.getElementsByTagName('textarea')[0];
    let textModel = monaco.editor.createModel(textArea.innerText, 'predicate');
    symbolTables[textModel.id] = symbolTable;
    textModel.onWillDispose(function() {
        symbolTables[textModel.id] = null;
    });
    if (typeof options !== 'object') {
        if (singleLineMode) {
            options = {
                theme: 'blazeTheme',
                contextmenu: false,
                wordWrap: 'off',
                lineNumbers: 'off',
                lineNumbersMinChars: 0,
                overviewRulerLanes: 0,
                overviewRulerBorder: false,
                lineDecorationsWidth: 0,
                hideCursorInOverviewRuler: true,
                glyphMargin: false,
                folding: false,
                scrollBeyondLastColumn: 0,
                scrollbar: { horizontal: 'hidden', vertical: 'hidden' },
                find: { addExtraSpaceOnTop: false, autoFindInSelection: 'never', seedSearchStringFromSelection: false },
                minimap: { enabled: false },
                suggest: { showWords: false },
            };
        } else {
            options = {
                theme: 'blazeTheme',
                contextmenu: false,
                wordWrap: 'off',
                lineNumbers: 'off',
                lineNumbersMinChars: 0,
                overviewRulerLanes: 0,
                overviewRulerBorder: false,
                lineDecorationsWidth: 0,
                hideCursorInOverviewRuler: true,
                glyphMargin: false,
                folding: false,
                scrollBeyondLastColumn: 0,
                scrollbar: {horizontal: 'hidden', vertical: 'hidden'},
                find: {addExtraSpaceOnTop: false, autoFindInSelection: 'never', seedSearchStringFromSelection: false},
                minimap: {enabled: false},
                suggest: {showWords: false},
            };
        }
    }
    options.language = 'predicate';
    options.model = textModel;
    let editor = monaco.editor.create(domElement, options);
    if (singleLineMode) {
        editor.onKeyDown(e => {
            if (e.keyCode == monaco.KeyCode.Enter) {
                // We only prevent enter when the suggest model is not active
                if (editor.getContribution('editor.contrib.suggestController').model.state == 0) {
                    e.preventDefault();
                }
            }
        });
        editor.onDidPaste(e => {
           if (e.range.endLineNumber > 1) {
               let newContent = "";
               let lineCount = textModel.getLineCount();
               for (let i = 0; i < lineCount; i++) {
                   newContent += textModel.getLineContent(i + 1);
               }
               textModel.setValue(newContent);
           }
        });
    }
    editor.onDidChangeModelContent(function (event) {
        var code = editor.getValue();
        textArea.innerText = code;
        var model = editor.getModel();
        let symbolTable = symbolTables[model.id];
        var syntaxErrors = validate(code, symbolTable);
        var monacoErrors: monaco.editor.IMarkerData[] = [];
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

export class Symbol {
    /**
     * The symbol name.
     */
    name: string;
    /**
     * The type of the symbol.
     */
    type: DomainType;
    /**
     * The symbol documentation.
     */
    documentation: string;

    constructor(name: string, type: DomainType, documentation: string) {
        this.name = name;
        this.type = type;
        this.documentation = documentation;
    }
}

export class SymbolTable {
    variables: StringMap<Symbol>;
    model: DomainModel;

    static parse(input: string, extension: StringMap<Function>): SymbolTable {
        let model = DomainModel.parse(input, extension);
        let json = JSON.parse(input), symbols = json['symbols'];
        let vars: StringMap<Symbol> = {};
        for (let name in symbols) {
            let s = symbols[name];
            vars[name] = new Symbol(name, model.types[s['type']], s['doc']);
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
        let varSuggestion = function(v: string, s: Symbol): monaco.languages.CompletionItem {
            return {
                label: v,
                kind: monaco.languages.CompletionItemKind.Variable,
                detail: s.type.name,
                documentation: s.documentation,
                insertText: v,
                range: null
            }
        };
        let attrSuggestion = function(v: string, a: EntityAttribute): monaco.languages.CompletionItem {
            let doc: IMarkdownString = null;
            if (a.documentation != null) {
                doc = {value: a.documentation, isTrusted: true};
            }
            return {
                label: v,
                kind: monaco.languages.CompletionItemKind.Field,
                detail: a.type.name,
                documentation: doc,
                insertText: v,
                range: null
            }
        };
        let functionSuggestion = function(f: DomainFunction): monaco.languages.CompletionItem {
            let label: string;
            if (f.resultType == null) {
                label = "any";
            } else {
                label = f.resultType.name;
            }
            label += " " + f.name + "(";

            var paramInfo = "| | |\n" +
                "|-------------|-------------|\n" +
                "| Params         | |\n| |";
            let documentation: string = "";
            if (f.documentation != null) {
                documentation += f.documentation + "\n\n";
            }
            for (var i = 0; i < f.arguments.length; i++) {
                if (i != 0) {
                    label += ", ";
                    paramInfo += "\n| |";
                }
                let p = f.arguments[i];
                if (p.type == null) {
                    label += "any";
                } else {
                    label += p.type.name;
                }
                label += " " + p.name;
                if (f.minArgumentCount != -1 && i >= f.minArgumentCount) {
                    label += "?";
                }
                paramInfo += p.name + " - " + p.documentation;
            }
            label += ")";
            documentation += "---\n\n" + paramInfo + " |";
            return {
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
                    suggestions.push(varSuggestion(v, symbolTable.variables[v]));
                }
                let funcs = symbolTable.model.functions;
                for (let f in funcs) {
                    suggestions.push(functionSuggestion(funcs[f]));
                }
            } else {
                let domainType = symbolTable.variables[parts[0]].type;
                let lastIdx = parts.length - 1;
                for (i = 1; i < lastIdx; i++) {
                    if (domainType instanceof CollectionDomainType) {
                        domainType = domainType.elementType;
                    }
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
                if (domainType instanceof CollectionDomainType) {
                    domainType = domainType.elementType;
                }
                if (domainType instanceof EntityDomainType) {
                    for (let a in domainType.attributes) {
                        suggestions.push(attrSuggestion(a, domainType.attributes[a]));
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
                suggestions.push(varSuggestion(v, symbolTable.variables[v]));
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
        let lower = ruleName.toLowerCase();
        // TODO: parameterize rule?
        if ('string_literal' == lower) {
            this.scopes = "String";
        } else {
            this.scopes = lower + ".blaze";
        }
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

export class ErrorEntry {
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

    private errors : ErrorEntry[] = []

    constructor(errors: ErrorEntry[]) {
        super()
        this.errors = errors
    }

    syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
        let endColumn = offendingSymbol.stop + 1;
        this.errors.push(new ErrorEntry(line, line, column + 1, endColumn, msg));
    }

}

export function createLexer(input: String) {
    const chars = new InputStream(input);
    const lexer = new BlazeExpressionLexer(chars);
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
    return parser.parsePredicateOrExpression();
}

export function parseTreeStr(input) {
    const lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new ConsoleErrorListener());

    const parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new ConsoleErrorListener());

    const tree = parser.parsePredicateOrExpression();

    return tree.toStringTree(parser.ruleNames);
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

export function validate(input: string, symbolTable: SymbolTable) : ErrorEntry[] {
    let errors : ErrorEntry[] = [];

    const lexer = createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new ConsoleErrorListener());

    const parser = createParserFromLexer(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new CollectorErrorListener(errors));
    parser._errHandler = new BlazeExpressionErrorStrategy();

    const tree = parser.parsePredicateOrExpression();
    try {
        tree.accept(new MyBlazeExpressionParserVisitor(symbolTable));
    } catch (e) {
        if (e instanceof ExpressionException) {
            errors.push(e.error);
        } else {
            throw e;
        }
    }
    return errors;
}

export class MyBlazeExpressionParserVisitor extends BlazeExpressionParserVisitor {
    symbolTable: SymbolTable;
    private booleanDomainType: BasicDomainType;

    constructor(symbolTable: SymbolTable) {
        super();
        this.symbolTable = symbolTable;
        this.booleanDomainType = this.symbolTable.model.booleanLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.BOOLEAN, 'true');
    }

    visitChildren(ctx) {
        if (ctx.children) {
            let result = null;
            for (let i = 0; i < ctx.children.length; i++) {
                result = ctx.children[i].accept(this);
            }
            return result;
        } else {
            return null;
        }
    }

    visitParsePredicate(ctx: BlazeExpressionParser.ParsePredicateContext) {
        return ctx.predicate().accept(this);
    }
    
    visitParseExpression(ctx: BlazeExpressionParser.ParseExpressionContext) {
        return ctx.expression().accept(this);
    }
    
    visitGroupedPredicate(ctx: BlazeExpressionParser.GroupedPredicateContext) {
        return ctx.predicate().accept(this);
    }
    
    visitNegatedPredicate(ctx: BlazeExpressionParser.NegatedPredicateContext) {
        return ctx.predicate().accept(this);
    }
    
    visitOrPredicate(ctx: BlazeExpressionParser.OrPredicateContext) {
        let preds = ctx.predicate();
        preds[0].accept(this);
        preds[1].accept(this);
        return this.booleanDomainType;
    }
    
    visitAndPredicate(ctx: BlazeExpressionParser.AndPredicateContext) {
        let preds = ctx.predicate();
        preds[0].accept(this);
        preds[1].accept(this);
        return this.booleanDomainType;
    }
    
    visitIsNullPredicate(ctx: BlazeExpressionParser.IsNullPredicateContext) {
        let left = ctx.expression().accept(this);
        let predicateTypeResolvers = this.symbolTable.model.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.NULLNESS]];
    
        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left.getType(), [1, 2, 3]);
        } else {
            let domainType = predicateTypeResolver.resolveType(this.symbolTable.model, [left]);
            if (domainType == null) {
                throw this.cannotResolvePredicateType(ctx, DomainPredicate.NULLNESS, [left]);
            } else {
                return this.booleanDomainType;
            }
        }
    }
    
    visitIsEmptyPredicate(ctx: BlazeExpressionParser.IsEmptyPredicateContext) {
        let left = ctx.expression().accept(this);
        let predicateTypeResolvers = this.symbolTable.model.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.COLLECTION]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1, 2, 3]);
        } else {
            let domainType = predicateTypeResolver.resolveType(this.symbolTable.model, [left]);
            if (domainType == null) {
                throw this.cannotResolvePredicateType(ctx, DomainPredicate.COLLECTION, [left]);
            } else {
                return this.booleanDomainType;
            }
        }
    }
    
    visitInequalityPredicate(ctx: BlazeExpressionParser.InequalityPredicateContext) {
        return this.createComparisonPredicate(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainPredicate.EQUALITY
    );
    }
    
    visitLessThanOrEqualPredicate(ctx: BlazeExpressionParser.LessThanOrEqualPredicateContext) {
        return this.createComparisonPredicate(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainPredicate.RELATIONAL
    );
    }
    
    visitEqualityPredicate(ctx: BlazeExpressionParser.EqualityPredicateContext) {
        return this.createComparisonPredicate(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainPredicate.EQUALITY
    );
    }
    
    visitGreaterThanPredicate(ctx: BlazeExpressionParser.GreaterThanPredicateContext) {
        return this.createComparisonPredicate(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainPredicate.RELATIONAL
    );
    }
    
    visitLessThanPredicate(ctx: BlazeExpressionParser.LessThanPredicateContext) {
        return this.createComparisonPredicate(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainPredicate.RELATIONAL
    );
    }
    
    visitGreaterThanOrEqualPredicate(ctx: BlazeExpressionParser.GreaterThanOrEqualPredicateContext) {
        return this.createComparisonPredicate(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainPredicate.RELATIONAL
    );
    }
    
    createComparisonPredicate(ctx: ParserRuleContext, left: DomainType, right: DomainType, domainPredicate: DomainPredicate) {
        let operandTypes = [left, right];
        let predicateTypeResolvers = this.symbolTable.model.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[domainPredicate]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1]);
        } else {
            let domainType = predicateTypeResolver.resolveType(this.symbolTable.model, operandTypes);
            if (domainType == null) {
                throw this.cannotResolvePredicateType(ctx, domainPredicate, operandTypes);
            } else {
                return this.booleanDomainType;
            }
        }
    }
    
    visitInPredicate(ctx: BlazeExpressionParser.InPredicateContext) {
        let left: DomainType = ctx.expression().accept(this);
        let operandTypes: DomainType[] = this.getExpressionList(ctx.inList().expression());
        let predicateTypeResolvers = this.symbolTable.model.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.EQUALITY]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1]);
        } else {
            operandTypes.unshift(left);
            let domainType = predicateTypeResolver.resolveType(this.symbolTable.model, operandTypes);
            if (domainType == null) {
                throw this.cannotResolvePredicateType(ctx, DomainPredicate.EQUALITY, operandTypes);
            } else {
                return this.booleanDomainType;
            }
        }
    }
    
    visitBetweenPredicate(ctx: BlazeExpressionParser.BetweenPredicateContext) {
        let left: DomainType = ctx.lhs.accept(this);
        let lower: DomainType = ctx.start.accept(this);
        let upper: DomainType = ctx.end.accept(this);

        let predicateTypeResolvers = this.symbolTable.model.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.RELATIONAL]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1]);
        } else {
            let operandTypes = [left, lower, upper];
            let domainType = predicateTypeResolver.resolveType(this.symbolTable.model, operandTypes);
            if (domainType == null) {
                throw this.cannotResolvePredicateType(ctx, DomainPredicate.RELATIONAL, operandTypes);
            } else {
                return this.booleanDomainType;
            }
        }
    }
    
    visitBooleanFunction(ctx: BlazeExpressionParser.BooleanFunctionContext) {
        let resultType = super.visitBooleanFunction(ctx);
        if (resultType == this.booleanDomainType) {
            return resultType;
        }
    
        throw new TypeErrorException("Invalid use of non-boolean returning function: " + ctx.getText(), ctx, -1, -1, -1, -1);
    }
    
    visitGroupedExpression(ctx: BlazeExpressionParser.GroupedExpressionContext) {
        return ctx.expression().accept(this);
    }
    
    visitAdditionExpression(ctx: BlazeExpressionParser.AdditionExpressionContext) {
        return this.createArithmeticExpression(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainOperator.PLUS
    );
    }
    
    visitSubtractionExpression(ctx: BlazeExpressionParser.SubtractionExpressionContext) {
        return this.createArithmeticExpression(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainOperator.MINUS
    );
    }
    
    visitDivisionExpression(ctx: BlazeExpressionParser.DivisionExpressionContext) {
        return this.createArithmeticExpression(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainOperator.DIVISION
    );
    }
    
    visitMultiplicationExpression(ctx: BlazeExpressionParser.MultiplicationExpressionContext) {
        return this.createArithmeticExpression(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainOperator.MULTIPLICATION
    );
    }
    
    visitModuloExpression(ctx: BlazeExpressionParser.ModuloExpressionContext) {
        return this.createArithmeticExpression(
            ctx,
            ctx.lhs.accept(this),
            ctx.rhs.accept(this),
            DomainOperator.MODULO
    );
    }
    
    createArithmeticExpression(ctx: ParserRuleContext, left: DomainType, right: DomainType, operator: DomainOperator) {
        let operandTypes = [left, right];
        let operationTypeResolvers = this.symbolTable.model.operationTypeResolvers[left.name];
        let operationTypeResolver = operationTypeResolvers == null ? null : operationTypeResolvers[DomainOperator[operator]];
        if (operationTypeResolver == null) {
            throw this.missingOperationTypeResolver(ctx, left, 1);
        } else {
            let domainType = operationTypeResolver.resolveType(this.symbolTable.model, operandTypes);
            if (domainType == null) {
                throw this.cannotResolveOperationType(ctx, operator, operandTypes);
            } else {
                return domainType;
            }
        }
    }
    
    visitUnaryMinusExpression(ctx: BlazeExpressionParser.UnaryMinusExpressionContext) {
        let left = ctx.expression().accept(this);
        let operandTypes = [left];
        let operationTypeResolvers = this.symbolTable.model.operationTypeResolvers[left.name];
        let operationTypeResolver = operationTypeResolvers == null ? null : operationTypeResolvers[DomainOperator[DomainOperator.UNARY_MINUS]];
        if (operationTypeResolver == null) {
            throw this.missingOperationTypeResolver(ctx, left, 0);
        } else {
            let domainType = operationTypeResolver.resolveType(this.symbolTable.model, operandTypes);
            if (domainType == null) {
                throw this.cannotResolveOperationType(ctx, DomainOperator.UNARY_MINUS, operandTypes);
            } else {
                return domainType;
            }
        }
    }
    
    visitUnaryPlusExpression(ctx: BlazeExpressionParser.UnaryPlusExpressionContext) {
        let left = ctx.expression().accept(this);
        let operandTypes = [left];
        let operationTypeResolvers = this.symbolTable.model.operationTypeResolvers[left.name];
        let operationTypeResolver = operationTypeResolvers == null ? null : operationTypeResolvers[DomainOperator[DomainOperator.UNARY_PLUS]];
        if (operationTypeResolver == null) {
            throw this.missingOperationTypeResolver(ctx, left, 0);
        } else {
            let domainType = operationTypeResolver.resolveType(this.symbolTable.model, operandTypes);
            if (domainType == null) {
                throw this.cannotResolveOperationType(ctx, DomainOperator.UNARY_PLUS, operandTypes);
            } else {
                return domainType;
            }
        }
    }

    visitTimestampLiteral(ctx: BlazeExpressionParser.TimestampLiteralContext) {
        return this.symbolTable.model.temporalLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.TIMESTAMP, ctx.getText());
    }
    
    visitTemporalIntervalLiteral(ctx: BlazeExpressionParser.TemporalIntervalLiteralContext) {
        this.parseTemporalAmount(ctx, ctx.years, "years");
        this.parseTemporalAmount(ctx, ctx.months, "months");
        this.parseTemporalAmount(ctx, ctx.days, "days");
        this.parseTemporalAmount(ctx, ctx.hours, "hours");
        this.parseTemporalAmount(ctx, ctx.minutes, "minutes");
        this.parseTemporalAmount(ctx, ctx.seconds, "seconds");
        return this.symbolTable.model.temporalLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.INTERVAL, ctx.getText());
    }
    
    parseTemporalAmount(ctx: BlazeExpressionParser.ParserRuleContext, token: Token, field: string) {
        if (token == null) {
            return 0;
        }
        let amountString = token.text;
        let amount = parseInt(amountString);
        if (isNaN(amount) || amount < 0) {
            throw new SyntaxErrorException("Illegal value given for temporal field '" + field + "': " + amountString, ctx, -1, -1, -1, -1);
        }
        return amount;
    }
    
    visitCollectionLiteral(ctx: BlazeExpressionParser.CollectionLiteralContext) {
        let literalList = this.getExpressionList(ctx.literal());
        let literal: CollectionLiteral = {
            type: this.symbolTable.model.types['Collection[' + literalList[0].name + ']'] as CollectionDomainType,
            values: []
        };
        return this.symbolTable.model.collectionLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.COLLECTION, literal);
    }
    
    visitPath(ctx: BlazeExpressionParser.PathContext) {
        let identifiers = ctx.identifier();
        let alias = identifiers[0].getText();
        let symbol = this.symbolTable.variables[alias];
        if (symbol == null) {
            if (identifiers.length == 2) {
                let type = this.symbolTable.model.types[alias];
                if (type instanceof EnumDomainType) {
                    let value = type.enumValues[identifiers[1].getText()];
                    if (value == null) {
                        throw new DomainModelException(this.format("The value '{0}' on the enum domain type '{1}' does not exist!", value, type.name), identifiers[1], -1, -1, -1, -1);
                    }
                    if (this.symbolTable.model.enumLiteralResolver == null) {
                        return type;
                    } else {
                        return this.symbolTable.model.enumLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.ENUM, {
                            enumType: type,
                            value: value
                        });
                    }
                }
            }
            throw this.unknownType(identifiers[0], alias);
        } else {
            let type = symbol.type;
            for (let i = 1; i < identifiers.length; i++) {
                let pathElement = identifiers[i].getText();
                if (type instanceof CollectionDomainType) {
                    type = type.elementType;
                }
                if (type instanceof EntityDomainType) {
                    let attribute = type.attributes[pathElement];
                    if (attribute == null) {
                        throw this.unknownEntityAttribute(identifiers[i], type, pathElement);
                    } else {
                        type = attribute.type;
                    }
                } else {
                    throw this.unsupportedType(identifiers[i], type.name);
                }
            }
    
            return type;
        }
    }
    
    visitIndexedFunctionInvocation(ctx: BlazeExpressionParser.IndexedFunctionInvocationContext) {
        let functionName = ctx.name.getText();
        let func = this.symbolTable.model.functions[functionName];
        if (func == null) {
            throw this.unknownFunction(ctx, functionName);
        } else {
            let predicateOrExpressions = ctx.predicateOrExpression();
            let literalList = this.getExpressionList(predicateOrExpressions);
            if (func.argumentCount != -1 && literalList.length > func.argumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at most " + func.argumentCount + " arguments but found " + literalList.length, ctx, -1, -1, -1, -1);
            }
            if (literalList.length < func.minArgumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at least " + func.minArgumentCount + " arguments but found " + literalList.length, ctx, -1, -1, -1, -1);
            }
            let argumentTypes: DomainType[] = [];
            let i = 0;
            let lastIdx = func.arguments.length - 1;
            let end = Math.min(lastIdx, literalList.length);
            for (; i < end; i++) {
                argumentTypes.push(literalList[i]);
            }
            if (lastIdx != -1) {
                let domainFunctionArgument = func.arguments[lastIdx];
                if (func.argumentCount == -1) {
                    // Varargs
                    let elementType: DomainType = domainFunctionArgument.type;
                    if (elementType instanceof CollectionDomainType) {
                        elementType = elementType.elementType;
                    }
                    for (; i < literalList.length; i++) {
                        if (elementType != literalList[i]) {
                            // invalid heterogeneous use for var-args
                            let offending = predicateOrExpressions[i];
                            throw new DomainModelException("Function '" + func.name + "' expects the " + (i + 1) + "th argument to be of type " + elementType.name + " but was " + literalList[i].name, null, offending.start.line, offending.stop.line, offending.start.start + 1, ctx.stop.stop + 1);
                        }
                    }
                    argumentTypes.push(domainFunctionArgument.type);
                } else if (i < literalList.length) {
                    argumentTypes.push(literalList[i]);
                }
            }
            return func.resultTypeResolver.resolveType(this.symbolTable.model, func, argumentTypes);
        }
    }
    
    visitNamedInvocation(ctx: BlazeExpressionParser.NamedInvocationContext) {
        let entityOrFunctionName = ctx.name.getText();
        let func = this.symbolTable.model.functions[entityOrFunctionName];
        if (func == null) {
            let type = this.symbolTable.model.types[entityOrFunctionName];
            if (type instanceof EntityDomainType) {
                let argNames: BlazeExpressionParser.IdentifierContext[] = ctx.identifier();
                let literalList = this.getExpressionList(ctx.predicateOrExpression());
                let args: StringMap<any> = {};
                for (let i = 0; i < literalList.length; i++) {
                    let attribute = type.attributes[argNames[i].getText()];
                    args[attribute.name] = literalList[i];
                }
                return this.symbolTable.model.entityLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.ENTITY, {
                    entityType: type,
                    attributeValues: args
                });
            } else {
                throw this.unknownFunction(ctx, entityOrFunctionName);
            }
        } else {
            let argNames: BlazeExpressionParser.IdentifierContext[] = ctx.identifier();
            let literalList = this.getExpressionList(ctx.predicateOrExpression());
            if (func.argumentCount != -1 && literalList.length > func.argumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at most " + func.argumentCount + " arguments but found " + literalList.length, ctx, -1, -1, -1, -1);
            }
            if (literalList.length < func.minArgumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at least " + func.minArgumentCount + " arguments but found " + literalList.length, ctx, -1, -1, -1, -1);
            }
            let argumentTypes: DomainType[] = new Array(func.arguments.length);
            for (let i = 0; i < literalList.length; i++) {
                let domainFunctionArgument = func.arguments[argNames[i].getText()];
                argumentTypes[domainFunctionArgument.position] = literalList[i];
            }
            return func.resultTypeResolver.resolveType(this.symbolTable.model, func, argumentTypes);
        }
    }

    visitTerminal(node: TerminalNode) {
        if (node.getSymbol().type == BlazeExpressionLexer.EOF) {
            return null;
        }
        switch (node.getSymbol().type) {
            case BlazeExpressionLexer.STRING_LITERAL:
                return this.symbolTable.model.stringLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.STRING, node.getText());
            case BlazeExpressionLexer.TRUE:
                return this.symbolTable.model.booleanLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.BOOLEAN, node.getText());
            case BlazeExpressionLexer.NUMERIC_LITERAL:
                return this.symbolTable.model.numericLiteralResolver.resolveLiteral(this.symbolTable.model, LiteralKind.NUMERIC, node.getText());
            default:
                throw new Error("Terminal node '" + node.getText() + "' not handled");
        }
    }
    
    getExpressionList(items: ParserRuleContext[]) {
        let expressions = [];
        for (let item of items) {
            expressions.push(item.accept(this));
        }
        return expressions;
    }
    
    private missingPredicateTypeResolver(ctx: ParserRuleContext, type: DomainType, tokenIndexes: number[]) {
        let symbols = "";
        let startLine;
        let endLine;
        let startCol;
        let endCol;
        for (let i = 0; i < tokenIndexes.length; i++) {
            let j = tokenIndexes[i];
            if (j < ctx.children.length) {
                let sym = ctx.children[j].symbol;
                if (i == 0) {
                    startLine = endLine = sym.line;
                    startCol = sym.start;
                    endCol = sym.stop;
                } else {
                    endLine = sym.line;
                    endCol = sym.stop;
                    symbols += " ";
                }
                symbols += sym.text;
            }
        }

        return new DomainModelException(this.format("Missing predicate type resolver for type {0} and predicate {1}", type.name, symbols), null, startLine, endLine, startCol + 1, endCol + 1);
    }

    private missingOperationTypeResolver(ctx: ParserRuleContext, type: DomainType, tokenIndex: number) {
        let sym = ctx.children[tokenIndex].symbol;
        return new DomainModelException(this.format("Missing operation type resolver for type {0} and operator {1}", type.name, sym.text), null, sym.line, sym.line, sym.start + 1, sym.stop + 1);
    }

    private unknownType(ctx: ParserRuleContext, typeName: string) {
        return new DomainModelException(this.format("Undefined type '{0}'", typeName), ctx, -1, -1, -1, -1);
    }

    private unknownEntityAttribute(ctx: ParserRuleContext, entityDomainType: EntityDomainType, attributeName: string) {
        return new DomainModelException(this.format("Attribute {0} undefined for entity {1}", attributeName, entityDomainType.name), ctx, -1, -1, -1, -1);
    }

    private unknownFunction(ctx: ParserRuleContext, identifier: string) {
        return new DomainModelException(this.format("Undefined function '{0}'", identifier), ctx, -1, -1, -1, -1);
    }

    private unsupportedType(ctx: ParserRuleContext, typeName: string) {
        return new TypeErrorException(this.format("Resolved type for identifier {0} is not supported", typeName), ctx, -1, -1, -1, -1);
    }

    private cannotResolvePredicateType(ctx: ParserRuleContext, predicateType: DomainPredicate, operandTypes: DomainType[]) {
        return new TypeErrorException(this.format("Cannot resolve predicate type for predicate {0} and operand types {1}", DomainPredicate[predicateType], this.typeNames(operandTypes)), ctx, -1, -1, -1, -1);
    }
    
    private cannotResolveOperationType(ctx: ParserRuleContext, operator: DomainOperator, operandTypes: DomainType[]) {
        return new TypeErrorException(this.format("Cannot resolve operation type for operator {0} and operand types {1}", DomainOperator[operator], this.typeNames(operandTypes)), ctx, -1, -1, -1, -1);
    }

    private typeNames(operandTypes: DomainType[]): string {
        let typeNames = "[";
        for (let i = 0; i < operandTypes.length; i++) {
            if (i != 0) {
                typeNames += ", ";
            }
            typeNames += operandTypes[i].name;
        }
        return typeNames + "]";
    }

    private format(format: string, ...args): string {
        return format.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] != 'undefined'
                ? args[number]
                : match
                ;
        });
    }
}

export class ExpressionException extends Error {
    error: ErrorEntry;

    constructor(message: string, ctx: ParserRuleContext, startLine: number, endLine: number, startCol: number, endCol: number) {
        super(message);
        Object.setPrototypeOf(this, ExpressionException.prototype);
        if (ctx == null) {
            this.error = new ErrorEntry(startLine, endLine, startCol, endCol, message);
        } else {
            this.error = new ErrorEntry(ctx.start.line, ctx.stop.line, ctx.start.start + 1, ctx.stop.stop + 1, message);
        }
    }

}

export class TypeErrorException extends ExpressionException {
    constructor(message: string, ctx: ParserRuleContext, startLine: number, endLine: number, startCol: number, endCol: number) {
        super(message, ctx, startLine, endLine, startCol, endCol);
    }
}

export class DomainModelException extends ExpressionException {
    constructor(message: string, ctx: ParserRuleContext, startLine: number, endLine: number, startCol: number, endCol: number) {
        super(message, ctx, startLine, endLine, startCol, endCol);
    }
}

export class SyntaxErrorException extends ExpressionException {
    constructor(message: string, ctx: ParserRuleContext, startLine: number, endLine: number, startCol: number, endCol: number) {
        super(message, ctx, startLine, endLine, startCol, endCol);
    }
}