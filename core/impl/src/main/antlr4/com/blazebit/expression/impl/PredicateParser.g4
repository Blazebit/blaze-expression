/*
 * Copyright 2014 Blazebit.
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
parser grammar PredicateParser;

options { tokenVocab=PredicateLexer; }

parsePredicate
    : predicate EOF;

parseExpression
    : expression EOF;

expression
    : LP expression RP                                                                          # GroupedExpression
    | lhs=expression ASTERISK rhs=expression                                                    # MultiplicationExpression
    | lhs=expression SLASH rhs=expression                                                       # DivisionExpression
    | lhs=expression PERCENT rhs=expression                                                     # ModuloExpression
    | lhs=expression PLUS rhs=expression                                                        # AdditionExpression
    | lhs=expression MINUS rhs=expression                                                       # SubtractionExpression
    | MINUS expression                                                                          # UnaryMinusExpression
    | PLUS expression                                                                           # UnaryPlusExpression
    | literal                                                                                   # LiteralExpression
    | path                                                                                      # PathExpression
    | function                                                                                  # FunctionExpression
    ;

predicate
    : LP predicate RP                                                                           # GroupedPredicate
    | (NOT | EXCLAMATION_MARK) predicate                                                        # NegatedPredicate
    | predicate AND predicate                                                                   # AndPredicate
    | predicate OR predicate                                                                    # OrPredicate
    | expression IS NOT? NULL                                                                   # IsNullPredicate
    | expression IS NOT? EMPTY                                                                  # IsEmptyPredicate
    | lhs=expression EQUAL rhs=expression                                                       # EqualityPredicate
    | lhs=expression NOT_EQUAL rhs=expression                                                   # InequalityPredicate
    | lhs=expression GREATER rhs=expression                                                     # GreaterThanPredicate
    | lhs=expression GREATER_EQUAL rhs=expression                                               # GreaterThanOrEqualPredicate
    | lhs=expression LESS rhs=expression                                                        # LessThanPredicate
    | lhs=expression LESS_EQUAL rhs=expression                                                  # LessThanOrEqualPredicate
    | expression NOT? IN inList                                                                 # InPredicate
    | lhs=expression NOT? BETWEEN start=expression AND end=expression                           # BetweenPredicate
    | function                                                                                  # BooleanFunction
    ;

predicateOrExpression
    : expression
    | predicate
    ;

inList
    : LP expression (COMMA expression)* RP
    | expression
    ;

path
    : identifier (DOT identifier)*
    ;

literal
    : NUMERIC_LITERAL
    | STRING_LITERAL
    | TRUE
    | FALSE
    | timestampLiteral
    | temporalIntervalLiteral
    | collectionLiteral
    ;

collectionLiteral
    : LB (literal (COMMA literal)*)? RB
    ;

function
    : name=identifier LP ((predicateOrExpression COMMA)* predicateOrExpression)? RP                                      #IndexedFunctionInvocation
    | name=identifier LP ((identifier EQUAL predicateOrExpression COMMA)* identifier EQUAL predicateOrExpression)? RP    #NamedInvocation
    ;

timestampLiteral
    : TIMESTAMP LP datePart (timePart (DOT fraction=(NUMERIC_LITERAL | LEADING_ZERO_NUMERIC_LITERAL))? )? RP
    ;

datePart
    : NUMERIC_LITERAL MINUS (NUMERIC_LITERAL | LEADING_ZERO_NUMERIC_LITERAL) MINUS (NUMERIC_LITERAL | LEADING_ZERO_NUMERIC_LITERAL)
    ;

timePart
    : (NUMERIC_LITERAL | LEADING_ZERO_NUMERIC_LITERAL) COLON (NUMERIC_LITERAL | LEADING_ZERO_NUMERIC_LITERAL) COLON (NUMERIC_LITERAL | LEADING_ZERO_NUMERIC_LITERAL)
    ;

temporalIntervalLiteral
    : INTERVAL (
      (years=NUMERIC_LITERAL YEARS (months=NUMERIC_LITERAL MONTHS)? (days=NUMERIC_LITERAL DAYS)? (hours=NUMERIC_LITERAL HOURS)? (minutes=NUMERIC_LITERAL MINUTES)? (seconds=NUMERIC_LITERAL SECONDS)?)
    | (months=NUMERIC_LITERAL MONTHS (days=NUMERIC_LITERAL DAYS)? (hours=NUMERIC_LITERAL HOURS)? (minutes=NUMERIC_LITERAL MINUTES)? (seconds=NUMERIC_LITERAL SECONDS)?)
    | (days=NUMERIC_LITERAL DAYS (hours=NUMERIC_LITERAL HOURS)? (minutes=NUMERIC_LITERAL MINUTES)? (seconds=NUMERIC_LITERAL SECONDS)?)
    | (hours=NUMERIC_LITERAL HOURS (minutes=NUMERIC_LITERAL MINUTES)? (seconds=NUMERIC_LITERAL SECONDS)?)
    | (minutes=NUMERIC_LITERAL MINUTES (seconds=NUMERIC_LITERAL SECONDS)?)
    | (seconds=NUMERIC_LITERAL SECONDS)
    )
    ;

identifier
    : IDENTIFIER
    | AND
    | BETWEEN
    | DAYS
    | HOURS
    | IN
    | IS
    | MINUTES
    | MONTHS
    | NOT
    | OR
    | SECONDS
    | TIMESTAMP
    | YEARS
    ;