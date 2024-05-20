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

lexer grammar PredicateLexer;

WS : ( ' ' | '\t' | '\f' | EOL ) -> channel(HIDDEN);
fragment EOL                : '\r\n' | '\r' | '\n';

fragment DIGIT              : '0'..'9';
fragment DIGITS             : DIGIT+;
fragment DIGIT_NOT_ZERO     : '1'..'9';
fragment INTEGER            : '0' | (DIGIT_NOT_ZERO DIGITS?);
fragment EXPONENT_PART      : [eE] SIGNED_INTEGER;
fragment SIGNED_INTEGER     : [+-]? DIGITS;
fragment ESCAPE_SEQUENCE    : ('\\' ('b'|'t'|'n'|'f'|'r'|'\\"'|'\''|'\\')) | UNICODE_ESCAPE;
fragment HEX_DIGIT          : ('0'..'9'|'a'..'f'|'A'..'F') ;
fragment UNICODE_ESCAPE     : '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT;

START_QUOTE: '\'' -> pushMode(QUOTE_STRING);
START_DOUBLE_QUOTE: '"' -> pushMode(DOUBLE_QUOTE_STRING);

INTEGER_LITERAL
    : INTEGER
    ;

NUMERIC_LITERAL
    : INTEGER '.' DIGITS EXPONENT_PART?
    | INTEGER EXPONENT_PART
    ;

LEADING_ZERO_INTEGER_LITERAL
    : '0' DIGITS
    ;

AND             : [aA] [nN] [dD];
BETWEEN         : [bB] [eE] [tT] [wW] [eE] [eE] [nN];
DATE            : [dD] [aA] [tT] [eE];
DAYS            : [dD] [aA] [yY] [sS];
DISTINCT        : [dD] [iI] [sS] [tT] [iI] [nN] [cC] [tT];
EMPTY           : [eE] [mM] [pP] [tT] [yY];
FALSE           : [fF] [aA] [lL] [sS] [eE];
FROM            : [fF] [rR] [oO] [mM];
FULL            : [fF] [uU] [lL] [lL];
HOURS           : [hH] [oO] [uU] [rR] [sS];
IN              : [iI] [nN];
INTERVAL        : [iI] [nN] [tT] [eE] [rR] [vV] [aA] [lL];
IS              : [iI] [sS];
JOIN            : [jJ] [oO] [iI] [nN];
LEFT            : [lL] [eE] [fF] [tT];
MINUTES         : [mM] [iI] [nN] [uU] [tT] [eE] [sS];
MONTHS          : [mM] [oO] [nN] [tT] [hH] [sS];
NOT             : [nN] [oO] [tT];
NULL            : [nN] [uU] [lL] [lL];
ON              : [oO] [nN];
OR              : [oO] [rR];
RIGHT           : [rR] [iI] [gG] [hH] [tT];
SECONDS         : [sS] [eE] [cC] [oO] [nN] [dD] [sS];
SELECT          : [sS] [eE] [lL] [eE] [cC] [tT];
TIME            : [tT] [iI] [mM] [eE];
TIMESTAMP       : [tT] [iI] [mM] [eE] [sS] [tT] [aA] [mM] [pP];
TRUE            : [tT] [rR] [uU] [eE];
WHERE           : [wW] [hH] [eE] [rR] [eE];
YEARS           : [yY] [eE] [aA] [rR] [sS];

LESS            : '<';
LESS_EQUAL      : '<=';
GREATER         : '>';
GREATER_EQUAL   : '>=';
EQUAL           : '=';
NOT_EQUAL       : '!=' | '<>';
PLUS            : '+';
MINUS           : '-';
ASTERISK        : '*';
SLASH           : '/';
PERCENT         : '%';

LP              : '(';
RP              : ')';
LB              : '[';
RB              : ']';
COMMA           : ',';
DOT             : '.';
COLON           : ':';
EXCLAMATION_MARK: '!';

IDENTIFIER
    : ('a'..'z'|'A'..'Z'|'_'|'$'|'\u0080'..'\ufffe')('a'..'z'|'A'..'Z'|'_'|'$'|'0'..'9'|'\u0080'..'\ufffe')*
    ;

QUOTED_IDENTIFIER
    : '`' ( ESCAPE_SEQUENCE | ~('\\'|'`'|'\r'|'\n') )* '`' { this.setText(LiteralFactory.unescapeString(this.getText())); }
    ;

// The following is to support template expressions
EXPRESSION_END: '}' -> pushMode(TEMPLATE);

mode TEMPLATE;

EXPRESSION_START: '#{' -> popMode;

TEXT: (
    '\\#{'
    | ~('#')
    | '#' { this._input.LA(1) != LiteralFactory.OPEN_BRACKET }?
    )+ { this.setText(LiteralFactory.unescapeTemplateText(this.getText())); }
    ;

mode QUOTE_STRING;

TEXT_IN_QUOTE
    : EOL
    | ( ESCAPE_SEQUENCE | ~('\\'|'\''|'\r'|'\n') )+ EOL?
    ;

END_QUOTE: '\'' -> popMode;

mode DOUBLE_QUOTE_STRING;

TEXT_IN_DOUBLE_QUOTE
    : EOL
    | ( ESCAPE_SEQUENCE | ~('\\'|'"'|'\r'|'\n') )+ EOL?
    ;

END_DOUBLE_QUOTE: '"' -> popMode;