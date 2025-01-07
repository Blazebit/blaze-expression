/*
 * Copyright 2019 - 2025 Blazebit.
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

import {ANTLRErrorListener} from "antlr4ts";
import {ErrorEntry} from "./ErrorEntry";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class CollectorErrorListener implements ANTLRErrorListener<any> {

    private errors : ErrorEntry[] = []

    constructor(errors: ErrorEntry[]) {
        this.errors = errors
    }

    syntaxError(recognizer, offendingSymbol, line, column, msg, e) {
        if (typeof offendingSymbol === 'undefined') {
            // TODO: read token text manually and internationalize error
            //  maybe even provide special errors for certain scenarios
            //  if the first char is \ this might be a wrong escape sequence
            this.errors.push(new ErrorEntry(line, line, column + 1, column + 1, msg));
        } else {
            // TODO: if the offending symbol is EOF, maybe a closing quote is missing
            //  Maybe check recognizer.getExpectedTokens()
            //  Or move this into BlazeExpressionErrorStrategy#reportNoViableAlternative
            let endColumn = offendingSymbol.stop + 1;
            this.errors.push(new ErrorEntry(line, line, column + 1, endColumn, msg));
        }
    }

}
