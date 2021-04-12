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

import {ParserRuleContext} from "antlr4ts";
import {ErrorEntry} from "./ErrorEntry";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class ExpressionException extends Error {
    error: ErrorEntry;

    constructor(message: string, ctx: ParserRuleContext, startLine: number, endLine: number, startCol: number, endCol: number) {
        super(message);
        Object.setPrototypeOf(this, new.target.prototype);
        if (ctx == null) {
            this.error = new ErrorEntry(startLine, endLine, startCol, endCol, message);
        } else {
            this.error = new ErrorEntry(ctx.start.line, ctx.stop.line, ctx.start.startIndex + 1, ctx.stop.stopIndex + 2, message);
        }
    }

}