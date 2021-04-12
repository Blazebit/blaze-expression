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

import * as monaco from "monaco-editor";
import {PredicateState} from "./PredicateState";
import {PredicateLineTokens} from "./PredicateLineTokens";
import {PredicateToken} from "./PredicateToken";
import {ANTLRErrorListener, CharStreams} from "antlr4ts";
import {BlazeExpressionLexer} from "./blaze-expression-predicate/BlazeExpressionLexer";
import {Lexer} from "antlr4ts/Lexer";

const EOF = -1;

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class PredicateTokensProvider implements monaco.languages.TokensProvider {

    private readonly templateMode: boolean;

    constructor(templateMode: boolean) {
        this.templateMode = templateMode;
    }

    getInitialState(): monaco.languages.IState {
        return new PredicateState(this.templateMode ? BlazeExpressionLexer.TEMPLATE : Lexer.DEFAULT_MODE);
    }

    tokenize(input: string, state: monaco.languages.IState): monaco.languages.ILineTokens {
        // So far we ignore the state, which is not great for performance reasons
        let errorStartingPoints: number[] = [];

        class ErrorCollectorListener implements ANTLRErrorListener<any> {
            syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
                errorStartingPoints.push(column)
            }
        }

        const lexer = new BlazeExpressionLexer(CharStreams.fromString(input));
        let mode = (state as PredicateState).mode;
        if (mode != Lexer.DEFAULT_MODE) {
            lexer.pushMode(mode);
        }
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
                    let tokenTypeName = lexer.vocabulary.getSymbolicName(token.type);
                    let myToken = new PredicateToken(tokenTypeName, token.charPositionInLine);
                    myTokens.push(myToken);
                }
            }
        } while (!done);

        // Add all errors
        for (let e of errorStartingPoints) {
            myTokens.push(new PredicateToken("error.blaze", e));
        }
        myTokens.sort((a, b) => (a.startIndex > b.startIndex) ? 1 : -1)

        return new PredicateLineTokens(lexer._mode, myTokens);
    }

}