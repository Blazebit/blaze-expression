// Generated from target/antlr4/BlazeExpressionParser.g4 by ANTLR 4.9.0-SNAPSHOT


import { ParseTreeListener } from "antlr4ts/tree/ParseTreeListener";

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
import { GroupedExpressionContext } from "./BlazeExpressionParser";
import { LiteralExpressionContext } from "./BlazeExpressionParser";
import { PathExpressionContext } from "./BlazeExpressionParser";
import { FunctionExpressionContext } from "./BlazeExpressionParser";
import { UnaryMinusExpressionContext } from "./BlazeExpressionParser";
import { UnaryPlusExpressionContext } from "./BlazeExpressionParser";
import { MultiplicativeExpressionContext } from "./BlazeExpressionParser";
import { AdditiveExpressionContext } from "./BlazeExpressionParser";
import { ParsePredicateContext } from "./BlazeExpressionParser";
import { ParseExpressionContext } from "./BlazeExpressionParser";
import { ParseExpressionOrPredicateContext } from "./BlazeExpressionParser";
import { ParseTemplateContext } from "./BlazeExpressionParser";
import { ParseQueryContext } from "./BlazeExpressionParser";
import { TemplateContext } from "./BlazeExpressionParser";
import { ExpressionContext } from "./BlazeExpressionParser";
import { PredicateContext } from "./BlazeExpressionParser";
import { PredicateOrExpressionContext } from "./BlazeExpressionParser";
import { QueryContext } from "./BlazeExpressionParser";
import { SelectClauseContext } from "./BlazeExpressionParser";
import { FromClauseContext } from "./BlazeExpressionParser";
import { WhereClauseContext } from "./BlazeExpressionParser";
import { FromItemContext } from "./BlazeExpressionParser";
import { FromRootContext } from "./BlazeExpressionParser";
import { JoinContext } from "./BlazeExpressionParser";
import { JoinTargetContext } from "./BlazeExpressionParser";
import { DomainTypeNameContext } from "./BlazeExpressionParser";
import { VariableContext } from "./BlazeExpressionParser";
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
 * This interface defines a complete listener for a parse tree produced by
 * `BlazeExpressionParser`.
 */
export interface BlazeExpressionParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the `NamedInvocation`
	 * labeled alternative in `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 */
	enterNamedInvocation?: (ctx: NamedInvocationContext) => void;
	/**
	 * Exit a parse tree produced by the `NamedInvocation`
	 * labeled alternative in `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 */
	exitNamedInvocation?: (ctx: NamedInvocationContext) => void;

	/**
	 * Enter a parse tree produced by the `IndexedFunctionInvocation`
	 * labeled alternative in `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 */
	enterIndexedFunctionInvocation?: (ctx: IndexedFunctionInvocationContext) => void;
	/**
	 * Exit a parse tree produced by the `IndexedFunctionInvocation`
	 * labeled alternative in `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 */
	exitIndexedFunctionInvocation?: (ctx: IndexedFunctionInvocationContext) => void;

	/**
	 * Enter a parse tree produced by the `GroupedPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterGroupedPredicate?: (ctx: GroupedPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `GroupedPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitGroupedPredicate?: (ctx: GroupedPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `NegatedPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterNegatedPredicate?: (ctx: NegatedPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `NegatedPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitNegatedPredicate?: (ctx: NegatedPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `AndPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterAndPredicate?: (ctx: AndPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `AndPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitAndPredicate?: (ctx: AndPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `OrPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterOrPredicate?: (ctx: OrPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `OrPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitOrPredicate?: (ctx: OrPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `IsNullPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterIsNullPredicate?: (ctx: IsNullPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `IsNullPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitIsNullPredicate?: (ctx: IsNullPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `IsEmptyPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterIsEmptyPredicate?: (ctx: IsEmptyPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `IsEmptyPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitIsEmptyPredicate?: (ctx: IsEmptyPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `EqualityPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterEqualityPredicate?: (ctx: EqualityPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `EqualityPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitEqualityPredicate?: (ctx: EqualityPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `InequalityPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterInequalityPredicate?: (ctx: InequalityPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `InequalityPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitInequalityPredicate?: (ctx: InequalityPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `GreaterThanPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterGreaterThanPredicate?: (ctx: GreaterThanPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `GreaterThanPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitGreaterThanPredicate?: (ctx: GreaterThanPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `GreaterThanOrEqualPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterGreaterThanOrEqualPredicate?: (ctx: GreaterThanOrEqualPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `GreaterThanOrEqualPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitGreaterThanOrEqualPredicate?: (ctx: GreaterThanOrEqualPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `LessThanPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterLessThanPredicate?: (ctx: LessThanPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `LessThanPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitLessThanPredicate?: (ctx: LessThanPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `LessThanOrEqualPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterLessThanOrEqualPredicate?: (ctx: LessThanOrEqualPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `LessThanOrEqualPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitLessThanOrEqualPredicate?: (ctx: LessThanOrEqualPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `InPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterInPredicate?: (ctx: InPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `InPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitInPredicate?: (ctx: InPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `BetweenPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterBetweenPredicate?: (ctx: BetweenPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `BetweenPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitBetweenPredicate?: (ctx: BetweenPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `BooleanFunction`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterBooleanFunction?: (ctx: BooleanFunctionContext) => void;
	/**
	 * Exit a parse tree produced by the `BooleanFunction`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitBooleanFunction?: (ctx: BooleanFunctionContext) => void;

	/**
	 * Enter a parse tree produced by the `PathPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterPathPredicate?: (ctx: PathPredicateContext) => void;
	/**
	 * Exit a parse tree produced by the `PathPredicate`
	 * labeled alternative in `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitPathPredicate?: (ctx: PathPredicateContext) => void;

	/**
	 * Enter a parse tree produced by the `GroupedExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterGroupedExpression?: (ctx: GroupedExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `GroupedExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitGroupedExpression?: (ctx: GroupedExpressionContext) => void;

	/**
	 * Enter a parse tree produced by the `LiteralExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterLiteralExpression?: (ctx: LiteralExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `LiteralExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitLiteralExpression?: (ctx: LiteralExpressionContext) => void;

	/**
	 * Enter a parse tree produced by the `PathExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterPathExpression?: (ctx: PathExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `PathExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitPathExpression?: (ctx: PathExpressionContext) => void;

	/**
	 * Enter a parse tree produced by the `FunctionExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterFunctionExpression?: (ctx: FunctionExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `FunctionExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitFunctionExpression?: (ctx: FunctionExpressionContext) => void;

	/**
	 * Enter a parse tree produced by the `UnaryMinusExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterUnaryMinusExpression?: (ctx: UnaryMinusExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `UnaryMinusExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitUnaryMinusExpression?: (ctx: UnaryMinusExpressionContext) => void;

	/**
	 * Enter a parse tree produced by the `UnaryPlusExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterUnaryPlusExpression?: (ctx: UnaryPlusExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `UnaryPlusExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitUnaryPlusExpression?: (ctx: UnaryPlusExpressionContext) => void;

	/**
	 * Enter a parse tree produced by the `MultiplicativeExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterMultiplicativeExpression?: (ctx: MultiplicativeExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `MultiplicativeExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitMultiplicativeExpression?: (ctx: MultiplicativeExpressionContext) => void;

	/**
	 * Enter a parse tree produced by the `AdditiveExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterAdditiveExpression?: (ctx: AdditiveExpressionContext) => void;
	/**
	 * Exit a parse tree produced by the `AdditiveExpression`
	 * labeled alternative in `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitAdditiveExpression?: (ctx: AdditiveExpressionContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.parsePredicate`.
	 * @param ctx the parse tree
	 */
	enterParsePredicate?: (ctx: ParsePredicateContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.parsePredicate`.
	 * @param ctx the parse tree
	 */
	exitParsePredicate?: (ctx: ParsePredicateContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.parseExpression`.
	 * @param ctx the parse tree
	 */
	enterParseExpression?: (ctx: ParseExpressionContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.parseExpression`.
	 * @param ctx the parse tree
	 */
	exitParseExpression?: (ctx: ParseExpressionContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.parseExpressionOrPredicate`.
	 * @param ctx the parse tree
	 */
	enterParseExpressionOrPredicate?: (ctx: ParseExpressionOrPredicateContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.parseExpressionOrPredicate`.
	 * @param ctx the parse tree
	 */
	exitParseExpressionOrPredicate?: (ctx: ParseExpressionOrPredicateContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.parseTemplate`.
	 * @param ctx the parse tree
	 */
	enterParseTemplate?: (ctx: ParseTemplateContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.parseTemplate`.
	 * @param ctx the parse tree
	 */
	exitParseTemplate?: (ctx: ParseTemplateContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.parseQuery`.
	 * @param ctx the parse tree
	 */
	enterParseQuery?: (ctx: ParseQueryContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.parseQuery`.
	 * @param ctx the parse tree
	 */
	exitParseQuery?: (ctx: ParseQueryContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.template`.
	 * @param ctx the parse tree
	 */
	enterTemplate?: (ctx: TemplateContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.template`.
	 * @param ctx the parse tree
	 */
	exitTemplate?: (ctx: TemplateContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	enterExpression?: (ctx: ExpressionContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.expression`.
	 * @param ctx the parse tree
	 */
	exitExpression?: (ctx: ExpressionContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	enterPredicate?: (ctx: PredicateContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.predicate`.
	 * @param ctx the parse tree
	 */
	exitPredicate?: (ctx: PredicateContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.predicateOrExpression`.
	 * @param ctx the parse tree
	 */
	enterPredicateOrExpression?: (ctx: PredicateOrExpressionContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.predicateOrExpression`.
	 * @param ctx the parse tree
	 */
	exitPredicateOrExpression?: (ctx: PredicateOrExpressionContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.query`.
	 * @param ctx the parse tree
	 */
	enterQuery?: (ctx: QueryContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.query`.
	 * @param ctx the parse tree
	 */
	exitQuery?: (ctx: QueryContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.selectClause`.
	 * @param ctx the parse tree
	 */
	enterSelectClause?: (ctx: SelectClauseContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.selectClause`.
	 * @param ctx the parse tree
	 */
	exitSelectClause?: (ctx: SelectClauseContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.fromClause`.
	 * @param ctx the parse tree
	 */
	enterFromClause?: (ctx: FromClauseContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.fromClause`.
	 * @param ctx the parse tree
	 */
	exitFromClause?: (ctx: FromClauseContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.whereClause`.
	 * @param ctx the parse tree
	 */
	enterWhereClause?: (ctx: WhereClauseContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.whereClause`.
	 * @param ctx the parse tree
	 */
	exitWhereClause?: (ctx: WhereClauseContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.fromItem`.
	 * @param ctx the parse tree
	 */
	enterFromItem?: (ctx: FromItemContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.fromItem`.
	 * @param ctx the parse tree
	 */
	exitFromItem?: (ctx: FromItemContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.fromRoot`.
	 * @param ctx the parse tree
	 */
	enterFromRoot?: (ctx: FromRootContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.fromRoot`.
	 * @param ctx the parse tree
	 */
	exitFromRoot?: (ctx: FromRootContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.join`.
	 * @param ctx the parse tree
	 */
	enterJoin?: (ctx: JoinContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.join`.
	 * @param ctx the parse tree
	 */
	exitJoin?: (ctx: JoinContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.joinTarget`.
	 * @param ctx the parse tree
	 */
	enterJoinTarget?: (ctx: JoinTargetContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.joinTarget`.
	 * @param ctx the parse tree
	 */
	exitJoinTarget?: (ctx: JoinTargetContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.domainTypeName`.
	 * @param ctx the parse tree
	 */
	enterDomainTypeName?: (ctx: DomainTypeNameContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.domainTypeName`.
	 * @param ctx the parse tree
	 */
	exitDomainTypeName?: (ctx: DomainTypeNameContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.variable`.
	 * @param ctx the parse tree
	 */
	enterVariable?: (ctx: VariableContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.variable`.
	 * @param ctx the parse tree
	 */
	exitVariable?: (ctx: VariableContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.inList`.
	 * @param ctx the parse tree
	 */
	enterInList?: (ctx: InListContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.inList`.
	 * @param ctx the parse tree
	 */
	exitInList?: (ctx: InListContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.path`.
	 * @param ctx the parse tree
	 */
	enterPath?: (ctx: PathContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.path`.
	 * @param ctx the parse tree
	 */
	exitPath?: (ctx: PathContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.pathAttributes`.
	 * @param ctx the parse tree
	 */
	enterPathAttributes?: (ctx: PathAttributesContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.pathAttributes`.
	 * @param ctx the parse tree
	 */
	exitPathAttributes?: (ctx: PathAttributesContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.literal`.
	 * @param ctx the parse tree
	 */
	enterLiteral?: (ctx: LiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.literal`.
	 * @param ctx the parse tree
	 */
	exitLiteral?: (ctx: LiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.stringLiteral`.
	 * @param ctx the parse tree
	 */
	enterStringLiteral?: (ctx: StringLiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.stringLiteral`.
	 * @param ctx the parse tree
	 */
	exitStringLiteral?: (ctx: StringLiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.collectionLiteral`.
	 * @param ctx the parse tree
	 */
	enterCollectionLiteral?: (ctx: CollectionLiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.collectionLiteral`.
	 * @param ctx the parse tree
	 */
	exitCollectionLiteral?: (ctx: CollectionLiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.entityLiteral`.
	 * @param ctx the parse tree
	 */
	enterEntityLiteral?: (ctx: EntityLiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.entityLiteral`.
	 * @param ctx the parse tree
	 */
	exitEntityLiteral?: (ctx: EntityLiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 */
	enterFunctionInvocation?: (ctx: FunctionInvocationContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.functionInvocation`.
	 * @param ctx the parse tree
	 */
	exitFunctionInvocation?: (ctx: FunctionInvocationContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.dateLiteral`.
	 * @param ctx the parse tree
	 */
	enterDateLiteral?: (ctx: DateLiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.dateLiteral`.
	 * @param ctx the parse tree
	 */
	exitDateLiteral?: (ctx: DateLiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.timeLiteral`.
	 * @param ctx the parse tree
	 */
	enterTimeLiteral?: (ctx: TimeLiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.timeLiteral`.
	 * @param ctx the parse tree
	 */
	exitTimeLiteral?: (ctx: TimeLiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.timestampLiteral`.
	 * @param ctx the parse tree
	 */
	enterTimestampLiteral?: (ctx: TimestampLiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.timestampLiteral`.
	 * @param ctx the parse tree
	 */
	exitTimestampLiteral?: (ctx: TimestampLiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.datePart`.
	 * @param ctx the parse tree
	 */
	enterDatePart?: (ctx: DatePartContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.datePart`.
	 * @param ctx the parse tree
	 */
	exitDatePart?: (ctx: DatePartContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.timePart`.
	 * @param ctx the parse tree
	 */
	enterTimePart?: (ctx: TimePartContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.timePart`.
	 * @param ctx the parse tree
	 */
	exitTimePart?: (ctx: TimePartContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.temporalIntervalLiteral`.
	 * @param ctx the parse tree
	 */
	enterTemporalIntervalLiteral?: (ctx: TemporalIntervalLiteralContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.temporalIntervalLiteral`.
	 * @param ctx the parse tree
	 */
	exitTemporalIntervalLiteral?: (ctx: TemporalIntervalLiteralContext) => void;

	/**
	 * Enter a parse tree produced by `BlazeExpressionParser.identifier`.
	 * @param ctx the parse tree
	 */
	enterIdentifier?: (ctx: IdentifierContext) => void;
	/**
	 * Exit a parse tree produced by `BlazeExpressionParser.identifier`.
	 * @param ctx the parse tree
	 */
	exitIdentifier?: (ctx: IdentifierContext) => void;
}

