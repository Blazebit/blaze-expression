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

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export abstract class QuoteMode {

    abstract onChar(c: string): QuoteMode;

    abstract onCharBackwards(c: string): QuoteMode;

    static NONE: QuoteMode = new class extends QuoteMode {

        onChar(c: string): QuoteMode {
            if (c == '\'') {
                return QuoteMode.SINGLE;
            } else if (c == '\"') {
                return QuoteMode.DOUBLE;
            }

            return QuoteMode.NONE;
        }

        onCharBackwards(c: string): QuoteMode {
            if (c == '\'') {
                return QuoteMode.SINGLE;
            } else if (c == '\"') {
                return QuoteMode.DOUBLE;
            }

            return QuoteMode.NONE;
        }
    };

    static SINGLE: QuoteMode = new class extends QuoteMode {

        onChar(c: string): QuoteMode {
            if (c == '\'') {
                return QuoteMode.NONE;
            }

            return QuoteMode.SINGLE;
        }

        onCharBackwards(c: string): QuoteMode {
            return this.onChar(c);
        }
    };

    static DOUBLE: QuoteMode = new class extends QuoteMode {

        onChar(c: string): QuoteMode {
            if (c == '"') {
                return QuoteMode.NONE;
            }

            return QuoteMode.DOUBLE;
        }

        onCharBackwards(c: string): QuoteMode {
            return this.onChar(c);
        }
    };
}