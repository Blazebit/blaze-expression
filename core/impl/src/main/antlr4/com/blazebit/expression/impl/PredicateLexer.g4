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
lexer grammar PredicateLexer;

WS : ( ' ' | '\t' | '\f' | EOL ) -> skip;
fragment EOL                : [\r\n]+;

fragment DIGIT              : '0'..'9';
fragment DIGITS             : DIGIT+;
fragment DIGIT_NOT_ZERO     : '1'..'9';
fragment INTEGER            : '0' | (DIGIT_NOT_ZERO DIGITS?);
fragment EXPONENT_PART      : [eE] SIGNED_INTEGER;
fragment SIGNED_INTEGER     : [+-]? DIGITS;
fragment ESCAPE_SEQUENCE    : ('\\' ('b'|'t'|'n'|'f'|'r'|'\\"'|'\''|'\\')) | UNICODE_ESCAPE;
fragment HEX_DIGIT          : ('0'..'9'|'a'..'f'|'A'..'F') ;
fragment UNICODE_ESCAPE     : '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT;

STRING_LITERAL
    : '"' ( ESCAPE_SEQUENCE | ~('\\'|'"') )* '"' {setText(getText().substring(1, getText().length() - 1));}
    | ('\'' ( ESCAPE_SEQUENCE | ~('\\'|'\'') )* '\'')+ {setText(getText().substring(1, getText().length() - 1).replace("''", "'"));}
    ;

NUMERIC_LITERAL
    : INTEGER ('.' DIGITS)? EXPONENT_PART?
    ;

LEADING_ZERO_NUMERIC_LITERAL
    : '0' DIGITS
    ;

AND             : [aA] [nN] [dD];
BETWEEN         : [bB] [eE] [tT] [wW] [eE] [eE] [nN];
DAYS            : [dD] [aA] [yY] [sS];
EMPTY           : [eE] [mM] [pP] [tT] [yY];
FALSE           : [fF] [aA] [lL] [sS] [eE];
HOURS           : [hH] [oO] [uU] [rR] [sS];
IN              : [iI] [nN];
INTERVAL        : [iI] [nN] [tT] [eE] [rR] [vV] [aA] [lL];
IS              : [iI] [sS];
MINUTES         : [mM] [iI] [nN] [uU] [tT] [eE] [sS];
MONTHS          : [mM] [oO] [nN] [tT] [hH] [sS];
NOT             : [nN] [oO] [tT];
NULL            : [nN] [uU] [lL] [lL];
OR              : [oO] [rR];
SECONDS         : [sS] [eE] [cC] [oO] [nN] [dD] [sS];
TIMESTAMP       : [tT] [iI] [mM] [eE] [sS] [tT] [aA] [mM] [pP];
TRUE            : [tT] [rR] [uU] [eE];
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
    : '`' ( ESCAPE_SEQUENCE | ~('\\'|'`') )* '`'
    ;