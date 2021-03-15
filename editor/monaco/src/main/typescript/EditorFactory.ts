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

import {BlazeExpressionConstructOptions} from "./BlazeExpressionConstructOptions";
import {PredicateTokensProvider} from "./PredicateTokensProvider";
import {PredicateHoverProvider} from "./PredicateHoverProvider";
import {PredicateSignatureHelpProvider} from "./PredicateSignatureHelpProvider";
import {PredicateCompletionProvider} from "./PredicateCompletionProvider";
import {SymbolTable} from "./SymbolTable";
import * as monaco from "monaco-editor";
import {ErrorEntry} from "./ErrorEntry";
import {CharStreams, CommonTokenStream, ConsoleErrorListener} from "antlr4ts";
import {CollectorErrorListener} from "./CollectorErrorListener";
import {BlazeExpressionErrorStrategy} from "./BlazeExpressionErrorStrategy";
import {DomainType} from "blaze-domain";
import {MyBlazeExpressionParserVisitor} from "./MyBlazeExpressionParserVisitor";
import {ExpressionException} from "./ExpressionException";
import {BlazeExpressionLexer} from "./blaze-expression-predicate/BlazeExpressionLexer";
import {BlazeExpressionParser} from "./blaze-expression-predicate/BlazeExpressionParser";

export let symbolTables: StringMap<SymbolTable> = {};

export function createEditor(monaco, opts: BlazeExpressionConstructOptions, options?: monaco.editor.IStandaloneEditorConstructionOptions): monaco.editor.IStandaloneCodeEditor {
    let domElement = opts.domElement;
    let input = opts.jsonContext;
    let singleLineMode = typeof opts.singleLineMode === 'undefined' ? false : opts.singleLineMode;
    let extensions = opts.extensions;
    let expectedResultTypes = typeof opts.expectedResultTypes === 'undefined' ? [] : opts.expectedResultTypes;
    let wrongResultTypeErrorMessage;
    if (typeof opts.wrongResultTypeErrorMessage === 'undefined') {
        wrongResultTypeErrorMessage = 'Expected one of the following the following types ['
        wrongResultTypeErrorMessage += expectedResultTypes[0];
        for (let i = 1; i < expectedResultTypes.length; i++) {
            wrongResultTypeErrorMessage += ", " + expectedResultTypes[i];
        }
        wrongResultTypeErrorMessage += "] but got: ";
    } else {
        wrongResultTypeErrorMessage = opts.wrongResultTypeErrorMessage;
    }
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
                { open: '[', close: ']', notIn: ['string'] },
                { open: '(', close: ')', notIn: ['string'] },
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

        monaco.languages.registerHoverProvider('predicate', new PredicateHoverProvider());
        monaco.languages.registerSignatureHelpProvider('predicate', new PredicateSignatureHelpProvider());
        monaco.languages.registerCompletionItemProvider('predicate', new PredicateCompletionProvider());
    }

    let symbolTable;
    if (opts.expressionModel != null) {
        symbolTable = SymbolTable.from(opts.expressionModel, input);
    } else {
        symbolTable = SymbolTable.parse(input, extensions);
    }
    let textArea = domElement.getElementsByTagName('textarea')[0];
    let textModel = monaco.editor.createModel(textArea.innerText, 'predicate');
    symbolTables[textModel.id] = symbolTable;
    textModel.onWillDispose(function() {
        symbolTables[textModel.id] = null;
    });
    let singleLineOptions: monaco.editor.IStandaloneEditorConstructionOptions = {
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
        suggest: { showWords: false }
    };
    let multiLineOptions: monaco.editor.IStandaloneEditorConstructionOptions = {
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
    if (typeof options !== 'object') {
        if (singleLineMode) {
            options = singleLineOptions;
        } else {
            options = multiLineOptions;
        }
    } else {
        if (singleLineMode) {
            options = {
                ...singleLineOptions,
                ...options
            };
        } else {
            options = {
                ...multiLineOptions,
                ...options
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
                let newContent = textModel.getValueInRange({
                    startLineNumber: 1,
                    startColumn: e.range.startColumn,
                    endLineNumber: 1,
                    endColumn: e.range.endLineNumber > 1 ? Number.MAX_VALUE : e.range.endColumn
                });
                let lineCount = textModel.getLineCount();
                for (let i = 1; i < lineCount; i++) {
                    newContent += " " + textModel.getLineContent(i + 1);
                }
                // Undo the last paste operation which contains newlines
                textModel['_commandManager'].undo();
                // Instead push this new paste without newlines
                editor.executeEdits('paste', [{
                    range: {
                        startLineNumber: 1,
                        startColumn: e.range.startColumn,
                        endLineNumber: 1,
                        endColumn: e.range.startColumn + newContent.length
                    },
                    text: newContent
                }]);
            }
        });
    }
    editor.onDidChangeModelContent(function (event) {
        var code = editor.getValue();
        textArea.innerText = code;
        var model = editor.getModel();
        let symbolTable = symbolTables[model.id];
        var syntaxErrors = validate(code, symbolTable, expectedResultTypes, wrongResultTypeErrorMessage);
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
        }
        monaco.editor.setModelMarkers(model, "owner", monacoErrors);
    });
    return editor;
}

function validate(input: string, symbolTable: SymbolTable, expectedResultTypes: string[], errorMessage: string) : ErrorEntry[] {
    let errors : ErrorEntry[] = [];

    const lexer = new BlazeExpressionLexer(CharStreams.fromString(input));
    lexer.removeErrorListeners();
    lexer.addErrorListener(new ConsoleErrorListener());

    const parser = new BlazeExpressionParser(new CommonTokenStream(lexer));
    parser.removeErrorListeners();
    parser.addErrorListener(new CollectorErrorListener(errors));
    parser.errorHandler = new BlazeExpressionErrorStrategy();

    const tree = parser.parseExpressionOrPredicate();
    try {
        let resultType: DomainType = tree.accept(new MyBlazeExpressionParserVisitor(symbolTable));
        if (resultType != null && expectedResultTypes && expectedResultTypes.length != 0) {
            for (let expectedResultType of expectedResultTypes) {
                if (resultType.name == expectedResultType) {
                    return errors;
                }
            }
            errors.push(new ErrorEntry(0, 0, 0, 0, errorMessage + resultType.name));
        }
    } catch (e) {
        if (e instanceof ExpressionException) {
            errors.push(e.error);
        } else {
            throw e;
        }
    }
    return errors;
}