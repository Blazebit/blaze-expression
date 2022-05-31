// Generated from target/antlr4/BlazeExpressionParser.g4 by ANTLR 4.9.0-SNAPSHOT


import { ParseTreeVisitor } from "antlr4ts/tree/ParseTreeVisitor";

import { GroupedExpressionContext } from "./BlazeExpressionParser";
import { LiteralExpressionContext } from "./BlazeExpressionParser";
import { PathExpressionContext } from "./BlazeExpressionParser";
import { FunctionExpressionContext } from "./BlazeExpressionParser";
import { UnaryMinusExpressionContext } from "./BlazeExpressionParser";
import { UnaryPlusExpressionContext } from "./BlazeExpressionParser";
import { MultiplicativeExpressionContext } from "./BlazeExpressionParser";
import { AdditiveExpressionContext } from "./BlazeExpressionParser";
import { NamedInvocationContext } from "./BlazeExpressionParser";
import { IndexedFunctionInvocationContext } from "./BlazeExpressionParser";
import { GroupedPredicateContext } from "./BlazeExpressionParser";
import { NegatedPredicateContext } from "./BlazeExpressionParser";
import { AndPredicateContext } from "./BlazeExpressionParser";
import { OrPredicateContext } from "./BlazeExpressionParser";
import { IsNullPredicateContext } from "./BlazeExpressionParser";
import { IsEmptyPredicateContext } from "./BlazeExpressionParser";
import { EqualityPredicateContext } from "./BlazeExpressionParser";
import { InequalityPredicateContext } from "./BlazeExpressionParser";
import { GreaterThanPredicateContext } from "./BlazeExpressionParser";
import { GreaterThanOrEqualPredicateContext } from "./BlazeExpressionParser";
import { LessThanPredicateContext } from "./BlazeExpressionParser";
import { LessThanOrEqualPredicateContext } from "./BlazeExpressionParser";
import { InPredicateContext } from "./BlazeExpressionParser";
import { BetweenPredicateContext } from "./BlazeExpressionParser";
import { BooleanFunctionContext } from "./BlazeExpressionParser";
import { PathPredicateContext } from "./BlazeExpressionParser";
import { ParsePredicateContext } from "./BlazeExpressionParser";
import { ParseExpressionContext } from "./BlazeExpressionParser";
import { ParseExpressionOrPredicateContext } from "./BlazeExpressionParser";
import { ParseTemplateContext } from "./BlazeExpressionParser";
import { TemplateContext } from "./BlazeExpressionParser";
import { ExpressionContext } from "./BlazeExpressionParser";
import { PredicateContext } from "./BlazeExpressionParser";
import { PredicateOrExpressionContext } from "./BlazeExpressionParser";
import { InListContext } from "./BlazeExpressionParser";
import { PathContext } from "./BlazeExpressionParser";
import { PathAttributesContext } from "./BlazeExpressionParser";
import { LiteralContext } from "./BlazeExpressionParser";
import { StringLiteralContext } from "./BlazeExpressionParser";
import { CollectionLiteralContext } from "./BlazeExpressionParser";
import { EntityLiteralContext } from "./BlazeExpressionParser";
import { FunctionInvocationContext } from "./BlazeExpressionParser";
import { DateLiteralContext } from "./BlazeExpressionParser";
import { TimeLiteralContext } from "./BlazeExpressionParser";
import { TimestampLiteralContext } from "./BlazeExpressionParser";
import { DatePartContext } from "./BlazeExpressionParser";
import { TimePartContext } from "./BlazeExpressionParser";
import { TemporalIntervalLiteralContext } from "./BlazeExpressionParser";
import { IdentifierContext } from "./BlazeExpressionParser";


/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by `BlazeExpressionParser`.
 *
 * @param <Result> The return type of the visit operation. Use `void` for
 * operations with no return type.
 */
export interface BlazeExpressionParserVisitor<Result> extends ParseTreeVisitor<Result> {
	/**
	 * Visit a parse tree produced by the `GroupedExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitGroupedExpression?: (ctx: GroupedExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `LiteralExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitLiteralExpression?: (ctx: LiteralExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `PathExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitPathExpression?: (ctx: PathExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `FunctionExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitFunctionExpression?: (ctx: FunctionExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `UnaryMinusExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitUnaryMinusExpression?: (ctx: UnaryMinusExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `UnaryPlusExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitUnaryPlusExpression?: (ctx: UnaryPlusExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `MultiplicativeExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitMultiplicativeExpression?: (ctx: MultiplicativeExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `AdditiveExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitAdditiveExpression?: (ctx: AdditiveExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by the `NamedInvocation`
	 * labeled alternative in `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitNamedInvocation?: (ctx: NamedInvocationContext) => Result;

	/**
	 * Visit a parse tree produced by the `IndexedFunctionInvocation`
	 * labeled alternative in `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitIndexedFunctionInvocation?: (ctx: IndexedFunctionInvocationContext) => Result;

	/**
	 * Visit a parse tree produced by the `GroupedPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitGroupedPredicate?: (ctx: GroupedPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `NegatedPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitNegatedPredicate?: (ctx: NegatedPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `AndPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitAndPredicate?: (ctx: AndPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `OrPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitOrPredicate?: (ctx: OrPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `IsNullPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitIsNullPredicate?: (ctx: IsNullPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `IsEmptyPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitIsEmptyPredicate?: (ctx: IsEmptyPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `EqualityPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitEqualityPredicate?: (ctx: EqualityPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `InequalityPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitInequalityPredicate?: (ctx: InequalityPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `GreaterThanPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitGreaterThanPredicate?: (ctx: GreaterThanPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `GreaterThanOrEqualPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitGreaterThanOrEqualPredicate?: (ctx: GreaterThanOrEqualPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `LessThanPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitLessThanPredicate?: (ctx: LessThanPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `LessThanOrEqualPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitLessThanOrEqualPredicate?: (ctx: LessThanOrEqualPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `InPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitInPredicate?: (ctx: InPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `BetweenPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitBetweenPredicate?: (ctx: BetweenPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by the `BooleanFunction`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitBooleanFunction?: (ctx: BooleanFunctionContext) => Result;

	/**
	 * Visit a parse tree produced by the `PathPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitPathPredicate?: (ctx: PathPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.parsePredicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitParsePredicate?: (ctx: ParsePredicateContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.parseExpression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitParseExpression?: (ctx: ParseExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.parseExpressionOrPredicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitParseExpressionOrPredicate?: (ctx: ParseExpressionOrPredicateContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.parseTemplate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitParseTemplate?: (ctx: ParseTemplateContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.template`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitTemplate?: (ctx: TemplateContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitExpression?: (ctx: ExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitPredicate?: (ctx: PredicateContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.predicateOrExpression`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitPredicateOrExpression?: (ctx: PredicateOrExpressionContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.inList`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitInList?: (ctx: InListContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.path`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitPath?: (ctx: PathContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.pathAttributes`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitPathAttributes?: (ctx: PathAttributesContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.literal`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitLiteral?: (ctx: LiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.stringLiteral`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitStringLiteral?: (ctx: StringLiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.collectionLiteral`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitCollectionLiteral?: (ctx: CollectionLiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.entityLiteral`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitEntityLiteral?: (ctx: EntityLiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitFunctionInvocation?: (ctx: FunctionInvocationContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.dateLiteral`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitDateLiteral?: (ctx: DateLiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.timeLiteral`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitTimeLiteral?: (ctx: TimeLiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.timestampLiteral`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitTimestampLiteral?: (ctx: TimestampLiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.datePart`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitDatePart?: (ctx: DatePartContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.timePart`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitTimePart?: (ctx: TimePartContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.temporalIntervalLiteral`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitTemporalIntervalLiteral?: (ctx: TemporalIntervalLiteralContext) => Result;

	/**
	 * Visit a parse tree produced by `BlazeExpressionParser.identifier`.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	visitIdentifier?: (ctx: IdentifierContext) => Result;
}

