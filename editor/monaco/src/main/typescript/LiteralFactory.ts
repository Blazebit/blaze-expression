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

/**
 * A factory for literals.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class LiteralFactory {

    static readonly OPEN_BRACKET: number = '{'.charCodeAt(0);

    /**
     * Unescapes the given string literal expression.
     *
     * @param s the string literal expression
     * @return The unescaped content of the string literal expression
     */
    public static unescapeString(s: string): string {
        let end = s.length - 1;
        let sb = [];
        for (let i = 1; i < end; i++) {
        let c = s.charAt(i);
        if (c == '\\' && (i + 1) < end) {
            let nextChar = s.charAt(++i);
            switch (nextChar) {
                case 'b':
                    c = '\b';
                    break;
                case 't':
                    c = '\t';
                    break;
                case 'n':
                    c = '\n';
                    break;
                case 'f':
                    c = '\f';
                    break;
                case 'r':
                    c = '\r';
                    break;
                case '\\':
                    c = '\\';
                    break;
                case '\'':
                    c = '\'';
                    break;
                case '"':
                    c = '"';
                    break;
                case '`':
                    c = '`';
                    break;
                case 'u':
                    c = String.fromCharCode(parseInt(s.substring(i + 1, i + 5), 16));
                    i += 4;
                    break;
                default:
                    break;
                }
            }
            sb.push(c);
        }
        return sb.join('');
    }

    /**
     * Unescapes the given template text expression.
     *
     * @param s the template text expression
     * @return The unescaped content of the template text expression
     */
    public static unescapeTemplateText(s: string): string {
        let sb = [];
        let end = s.length;
        for (let i = 0; i < end; i++) {
            let c = s.charAt(i);
            if (c == '\\' && (i + 1) < end && s.charAt(i + 1) == '#') {
                continue;
            }
            sb.push(c);
        }
        return end == sb.length ? s : sb.toString();
    }
}