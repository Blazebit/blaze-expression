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

import {BlazeExpressionConstructOptions} from "./BlazeExpressionConstructOptions";
import {PredicateTokensProvider} from "./PredicateTokensProvider";
import {PredicateHoverProvider} from "./PredicateHoverProvider";
import {PredicateSignatureHelpProvider} from "./PredicateSignatureHelpProvider";
import {PredicateCompletionProvider} from "./PredicateCompletionProvider";
import {SymbolTable} from "./SymbolTable";
import * as monaco from "monaco-editor/esm/vs/editor/editor.api";
import {ErrorEntry} from "./ErrorEntry";
import {CollectorErrorListener} from "./CollectorErrorListener";
import {BlazeExpressionErrorStrategy} from "./BlazeExpressionErrorStrategy";
import {DomainType} from "blaze-domain";
import {ExpressionException} from "./ExpressionException";
import {PredicateInlayHintsProvider} from "./PredicateInlayHintsProvider";

export let symbolTables: StringMap<SymbolTable> = {};

export function createTemplateEditor(monaco, opts: BlazeExpressionConstructOptions, options?: monaco.editor.IStandaloneEditorConstructionOptions): monaco.editor.IStandaloneCodeEditor {
    return _createEditor(monaco, true, opts, options);
}

export function createEditor(monaco, opts: BlazeExpressionConstructOptions, options?: monaco.editor.IStandaloneEditorConstructionOptions): monaco.editor.IStandaloneCodeEditor {
    return _createEditor(monaco, false, opts, options);
}

function _createEditor(monaco, templateMode: boolean, opts: BlazeExpressionConstructOptions, options?: monaco.editor.IStandaloneEditorConstructionOptions): monaco.editor.IStandaloneCodeEditor {
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
    let required = opts.required === true;
    let requiredMessage;
    if (required) {
        if (typeof opts.requiredMessage === 'undefined') {
            requiredMessage = "Input is required";
        } else {
            requiredMessage = opts.requiredMessage;
        }
    } else {
        requiredMessage = null;
    }
    if (monaco.languages.getEncodedLanguageId('predicate') == 0) {
        monaco.languages.register({id: 'predicate'});
        monaco.languages.register({id: 'template'});
        monaco.languages.setTokensProvider('predicate', new PredicateTokensProvider(false));
        monaco.languages.setTokensProvider('template', new PredicateTokensProvider(true));
        let languageConfig: monaco.languages.LanguageConfiguration = {
            brackets: [['[', ']'], ['(', ')'], ['{', '}']],
            surroundingPairs: [
                { open: '[', close: ']' },
                { open: '(', close: ')' },
                { open: '{', close: '}' },
                { open: "'", close: "'" },
                { open: '"', close: '"' },
            ],
            autoClosingPairs: [
                { open: '[', close: ']', notIn: ['string'] },
                { open: '(', close: ')', notIn: ['string'] },
                { open: '{', close: '}', notIn: ['string'] },
                { open: "'", close: "'", notIn: ['string'] },
                { open: '"', close: '"', notIn: ['string'] },
            ]
        };
        monaco.languages.setLanguageConfiguration('predicate', languageConfig);
        monaco.languages.setLanguageConfiguration('template', languageConfig);

        var literalFg = '3B8737';
        var idFg = '344482';
        var symbolsFg = '000000';
        var textFg = '000001';
        var keywordFg = '7132A8';
        var errorFg = 'FF0000';

        let blazeTheme: monaco.editor.IStandaloneThemeData = {
            base: 'vs',
            inherit: false,
            rules: [
                {token: 'numeric_literal.blaze', foreground: literalFg},
                {token: 'leading_zero_numeric_literal.blaze', foreground: literalFg},

                {token: 'start_quote.blaze', foreground: literalFg},
                {token: 'start_double_quote.blaze', foreground: literalFg},
                {token: 'end_quote.blaze', foreground: literalFg},
                {token: 'end_double_quote.blaze', foreground: literalFg},
                {token: 'text_in_quote.blaze', foreground: literalFg},
                {token: 'text_in_double_quote.blaze', foreground: literalFg},

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
            ], colors: {}
        };
        monaco.editor.defineTheme('blazeTheme', blazeTheme);
        // For templates we try to use a special background to highlight expressions
        // NOTE: This kind of styling currently does not work and requires manual CSS work
        // Also see https://github.com/microsoft/monaco-editor/issues/586
        let expressionBg = 'C6BF5540';
        let blazeTemplateTheme: monaco.editor.IStandaloneThemeData = JSON.parse(JSON.stringify(blazeTheme));
        for (const rule of blazeTemplateTheme.rules) {
            rule.background = expressionBg;
        }
        blazeTemplateTheme.rules.push( {token: 'text.blaze', foreground: textFg, background: 'ffffff'});
        blazeTemplateTheme.rules.push( {token: 'expression_start.blaze', foreground: literalFg, fontStyle: 'italic', background: expressionBg});
        blazeTemplateTheme.rules.push( {token: 'expression_end.blaze', foreground: literalFg, fontStyle: 'italic', background: expressionBg});
        monaco.editor.defineTheme('blazeTemplateTheme', blazeTemplateTheme);

        // This is a "hack" to be able to avoid suggestions in string literals
        // The line token API is not public and the matching against the class name is also not nice
        // But this is the only way without tokenizing again
        let predicateLiteralClass = 'mtk4';
        let templateLiteralClass = 'mtk5';

        try {
            monaco.editor.setTheme('blazeTheme');
            let tempModel1 = monaco.editor.createModel('""', 'predicate', null);
            predicateLiteralClass = 'mtk' + tempModel1._tokenization._tokenizationSupport._standaloneThemeService._theme._tokenTheme._colorMap._color2id.get(literalFg);
            tempModel1.dispose();
            monaco.editor.setTheme('blazeTemplateTheme');
            let tempModel2 = monaco.editor.createModel('#{""}', 'template', null);
            templateLiteralClass = 'mtk' + tempModel2._tokenization._tokenizationSupport._standaloneThemeService._theme._tokenTheme._colorMap._color2id.get(literalFg);
            tempModel2.dispose();
        } catch (e) {
            // Ignore
        }

        monaco.languages.registerHoverProvider('predicate', new PredicateHoverProvider(false));
        monaco.languages.registerSignatureHelpProvider('predicate', new PredicateSignatureHelpProvider(false));
        monaco.languages.registerCompletionItemProvider('predicate', new PredicateCompletionProvider(false, predicateLiteralClass));
        monaco.languages.registerInlayHintsProvider('predicate', new PredicateInlayHintsProvider(false));
        monaco.languages.registerHoverProvider('template', new PredicateHoverProvider(true));
        monaco.languages.registerSignatureHelpProvider('template', new PredicateSignatureHelpProvider(true));
        monaco.languages.registerCompletionItemProvider('template', new PredicateCompletionProvider(true, templateLiteralClass));
        monaco.languages.registerInlayHintsProvider('template', new PredicateInlayHintsProvider(true));
    }

    let symbolTable;
    if (opts.expressionModel != null) {
        symbolTable = SymbolTable.from(opts.expressionModel, input);
    } else {
        symbolTable = SymbolTable.parse(input, extensions);
    }
    let textArea = domElement.getElementsByTagName('textarea')[0];
    let textModel = monaco.editor.createModel(textArea.value, templateMode ? 'template' : 'predicate');
    symbolTables[textModel.id] = symbolTable;
    textModel.onWillDispose(function() {
        symbolTables[textModel.id] = null;
    });
    let singleLineOptions: monaco.editor.IStandaloneEditorConstructionOptions = {
        theme: templateMode ? 'blazeTemplateTheme' : 'blazeTheme',
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
        find: { addExtraSpaceOnTop: false, autoFindInSelection: 'never', seedSearchStringFromSelection: 'never' },
        inlayHints: { enabled: true },
        minimap: { enabled: false },
        suggest: { showWords: false }
    };
    let multiLineOptions: monaco.editor.IStandaloneEditorConstructionOptions = {
        theme: templateMode ? 'blazeTemplateTheme' : 'blazeTheme',
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
        find: {addExtraSpaceOnTop: false, autoFindInSelection: 'never', seedSearchStringFromSelection: 'never'},
        inlayHints: { enabled: true },
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
    options.language = templateMode ? 'template' : 'predicate';
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
        textArea.value = code;
        var model = editor.getModel();
        let symbolTable = symbolTables[model.id];
        var syntaxErrors = validate(code, symbolTable, templateMode, expectedResultTypes, wrongResultTypeErrorMessage);
        var monacoErrors: monaco.editor.IMarkerData[] = [];
        if (code.length == 0) {
            if (required) {
                monacoErrors.push({
                    startLineNumber: 1,
                    startColumn: 1,
                    endLineNumber: 1,
                    endColumn: 1,
                    message: requiredMessage,
                    severity: monaco.MarkerSeverity.Error
                });
            }
        } else {
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
        }
        monaco.editor.setModelMarkers(model, "owner", monacoErrors);
    });
    return editor;
}

function validate(input: string, symbolTable: SymbolTable, templateMode: boolean, expectedResultTypes: string[], errorMessage: string) : ErrorEntry[] {
    let errors : ErrorEntry[] = [];

    const lexer = templateMode ? symbolTable.model.createTemplateLexer(input) : symbolTable.model.createLexer(input);
    lexer.removeErrorListeners();
    lexer.addErrorListener(new CollectorErrorListener(errors));

    const parser = symbolTable.model.parserFactory(lexer);
    parser.removeErrorListeners();
    parser.addErrorListener(new CollectorErrorListener(errors));
    parser.errorHandler = new BlazeExpressionErrorStrategy();

    let tree;
    if (templateMode) {
        tree = symbolTable.model.parseTemplateRuleInvoker(parser);
    } else {
        tree = symbolTable.model.parseRuleInvoker(parser);
    }
    try {
        let resultType: DomainType = symbolTable.model.typeResolver(tree, symbolTable);
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