/*
 * Copyright 2014 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may [nN]ot use this file except in compliance with the License.
 * You may obtain [aA] copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
lexer grammar BlazeExpressionLexer;
import PredicateLexer;

STRING_LITERAL
    : '"' ( ESCAPE_SEQUENCE | ~('\\'|'"') )* '"' {this.text = this.text.substring(1, this.text.length - 1);}
    | ('\'' ( ESCAPE_SEQUENCE | ~('\\'|'\'') )* '\'')+ {this.text = this.text.substring(1, this.text.length - 1).replace(/''/g, "'");}
    ;