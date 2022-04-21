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

parser grammar PredicateParser;

options { tokenVocab=PredicateLexer; }

parsePredicate
    : predicate EOF;

parseExpression
    : expression EOF;

parseExpressionOrPredicate
    : predicateOrExpression EOF;

parseTemplate
    : template? EOF;

template
    : (TEXT | (EXPRESSION_START expression EXPRESSION_END))+
    ;

expression
    : LP expression RP                                                                          # GroupedExpression
    | literal                                                                                   # LiteralExpression
    | path                                                                                      # PathExpression
    | functionInvocation                                                                        # FunctionExpression
    | MINUS expression                                                                          # UnaryMinusExpression
    | PLUS expression                                                                           # UnaryPlusExpression
    | lhs=expression op=(ASTERISK|SLASH|PERCENT) rhs=expression                                 # MultiplicativeExpression
    | lhs=expression op=(PLUS|MINUS) rhs=expression                                             # AdditiveExpression
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
    | lhs=expression NOT? BETWEEN begin=expression AND end=expression                           # BetweenPredicate
    | functionInvocation                                                                        # BooleanFunction
    | path                                                                                      # PathPredicate
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
    : identifier pathAttributes?
    | functionInvocation pathAttributes
    ;

pathAttributes
    : (DOT identifier)+
    ;

literal
    : NUMERIC_LITERAL
    | INTEGER_LITERAL
    | stringLiteral
    | TRUE
    | FALSE
    | timestampLiteral
    | temporalIntervalLiteral
    | collectionLiteral
    | entityLiteral
    ;

stringLiteral
    : START_QUOTE TEXT_IN_QUOTE* END_QUOTE
    | START_DOUBLE_QUOTE TEXT_IN_DOUBLE_QUOTE* END_DOUBLE_QUOTE
    ;

collectionLiteral
    : LB (literal (COMMA literal)*)? RB
    ;

entityLiteral
    : name=identifier LP (identifier EQUAL predicateOrExpression COMMA)* identifier EQUAL predicateOrExpression RP
    ;

functionInvocation
    : name=identifier LP ((identifier EQUAL predicateOrExpression COMMA)* identifier EQUAL predicateOrExpression)? RP    #NamedInvocation
    | name=identifier LP ((predicateOrExpression COMMA)* predicateOrExpression)? RP                                      #IndexedFunctionInvocation
    ;

timestampLiteral
    : TIMESTAMP LP datePart (timePart (DOT fraction=(INTEGER_LITERAL | LEADING_ZERO_INTEGER_LITERAL))? )? RP
    ;

datePart
    : INTEGER_LITERAL MINUS (INTEGER_LITERAL | LEADING_ZERO_INTEGER_LITERAL) MINUS (INTEGER_LITERAL | LEADING_ZERO_INTEGER_LITERAL)
    ;

timePart
    : (INTEGER_LITERAL | LEADING_ZERO_INTEGER_LITERAL) COLON (INTEGER_LITERAL | LEADING_ZERO_INTEGER_LITERAL) COLON (INTEGER_LITERAL | LEADING_ZERO_INTEGER_LITERAL)
    ;

temporalIntervalLiteral
    : INTERVAL (
      (years=INTEGER_LITERAL YEARS (months=INTEGER_LITERAL MONTHS)? (days=INTEGER_LITERAL DAYS)? (hours=INTEGER_LITERAL HOURS)? (minutes=INTEGER_LITERAL MINUTES)? (seconds=INTEGER_LITERAL SECONDS)?)
    | (months=INTEGER_LITERAL MONTHS (days=INTEGER_LITERAL DAYS)? (hours=INTEGER_LITERAL HOURS)? (minutes=INTEGER_LITERAL MINUTES)? (seconds=INTEGER_LITERAL SECONDS)?)
    | (days=INTEGER_LITERAL DAYS (hours=INTEGER_LITERAL HOURS)? (minutes=INTEGER_LITERAL MINUTES)? (seconds=INTEGER_LITERAL SECONDS)?)
    | (hours=INTEGER_LITERAL HOURS (minutes=INTEGER_LITERAL MINUTES)? (seconds=INTEGER_LITERAL SECONDS)?)
    | (minutes=INTEGER_LITERAL MINUTES (seconds=INTEGER_LITERAL SECONDS)?)
    | (seconds=INTEGER_LITERAL SECONDS)
    )
    ;

identifier
    : IDENTIFIER
    | QUOTED_IDENTIFIER
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