// Generated from target/antlr4/BlazeExpressionParser.g4 by ANTLR 4.9.0-SNAPSHOT


import { ATN } from "antlr4ts/atn/ATN";
import { ATNDeserializer } from "antlr4ts/atn/ATNDeserializer";
import { FailedPredicateException } from "antlr4ts/FailedPredicateException";
import { NotNull } from "antlr4ts/Decorators";
import { NoViableAltException } from "antlr4ts/NoViableAltException";
import { Override } from "antlr4ts/Decorators";
import { Parser } from "antlr4ts/Parser";
import { ParserRuleContext } from "antlr4ts/ParserRuleContext";
import { ParserATNSimulator } from "antlr4ts/atn/ParserATNSimulator";
import { ParseTreeListener } from "antlr4ts/tree/ParseTreeListener";
import { ParseTreeVisitor } from "antlr4ts/tree/ParseTreeVisitor";
import { RecognitionException } from "antlr4ts/RecognitionException";
import { RuleContext } from "antlr4ts/RuleContext";
//import { RuleVersion } from "antlr4ts/RuleVersion";
import { TerminalNode } from "antlr4ts/tree/TerminalNode";
import { Token } from "antlr4ts/Token";
import { TokenStream } from "antlr4ts/TokenStream";
import { Vocabulary } from "antlr4ts/Vocabulary";
import { VocabularyImpl } from "antlr4ts/VocabularyImpl";

import * as Utils from "antlr4ts/misc/Utils";

import { BlazeExpressionParserListener } from "./BlazeExpressionParserListener";
import { BlazeExpressionParserVisitor } from "./BlazeExpressionParserVisitor";


export class BlazeExpressionParser extends Parser {
	public static readonly WS = 1;
	public static readonly START_QUOTE = 2;
	public static readonly START_DOUBLE_QUOTE = 3;
	public static readonly INTEGER_LITERAL = 4;
	public static readonly NUMERIC_LITERAL = 5;
	public static readonly LEADING_ZERO_INTEGER_LITERAL = 6;
	public static readonly AND = 7;
	public static readonly BETWEEN = 8;
	public static readonly DATE = 9;
	public static readonly DAYS = 10;
	public static readonly DISTINCT = 11;
	public static readonly EMPTY = 12;
	public static readonly FALSE = 13;
	public static readonly FROM = 14;
	public static readonly FULL = 15;
	public static readonly HOURS = 16;
	public static readonly IN = 17;
	public static readonly INTERVAL = 18;
	public static readonly IS = 19;
	public static readonly JOIN = 20;
	public static readonly LEFT = 21;
	public static readonly MINUTES = 22;
	public static readonly MONTHS = 23;
	public static readonly NOT = 24;
	public static readonly NULL = 25;
	public static readonly ON = 26;
	public static readonly OR = 27;
	public static readonly RIGHT = 28;
	public static readonly SECONDS = 29;
	public static readonly SELECT = 30;
	public static readonly TIME = 31;
	public static readonly TIMESTAMP = 32;
	public static readonly TRUE = 33;
	public static readonly WHERE = 34;
	public static readonly YEARS = 35;
	public static readonly LESS = 36;
	public static readonly LESS_EQUAL = 37;
	public static readonly GREATER = 38;
	public static readonly GREATER_EQUAL = 39;
	public static readonly EQUAL = 40;
	public static readonly NOT_EQUAL = 41;
	public static readonly PLUS = 42;
	public static readonly MINUS = 43;
	public static readonly ASTERISK = 44;
	public static readonly SLASH = 45;
	public static readonly PERCENT = 46;
	public static readonly LP = 47;
	public static readonly RP = 48;
	public static readonly LB = 49;
	public static readonly RB = 50;
	public static readonly COMMA = 51;
	public static readonly DOT = 52;
	public static readonly COLON = 53;
	public static readonly EXCLAMATION_MARK = 54;
	public static readonly IDENTIFIER = 55;
	public static readonly QUOTED_IDENTIFIER = 56;
	public static readonly EXPRESSION_END = 57;
	public static readonly EXPRESSION_START = 58;
	public static readonly TEXT = 59;
	public static readonly TEXT_IN_QUOTE = 60;
	public static readonly END_QUOTE = 61;
	public static readonly TEXT_IN_DOUBLE_QUOTE = 62;
	public static readonly END_DOUBLE_QUOTE = 63;
	public static readonly AS = 64;
	public static readonly RULE_parsePredicate = 0;
	public static readonly RULE_parseExpression = 1;
	public static readonly RULE_parseExpressionOrPredicate = 2;
	public static readonly RULE_parseTemplate = 3;
	public static readonly RULE_parseQuery = 4;
	public static readonly RULE_template = 5;
	public static readonly RULE_expression = 6;
	public static readonly RULE_predicate = 7;
	public static readonly RULE_predicateOrExpression = 8;
	public static readonly RULE_query = 9;
	public static readonly RULE_selectClause = 10;
	public static readonly RULE_fromClause = 11;
	public static readonly RULE_whereClause = 12;
	public static readonly RULE_fromItem = 13;
	public static readonly RULE_fromRoot = 14;
	public static readonly RULE_join = 15;
	public static readonly RULE_joinTarget = 16;
	public static readonly RULE_domainTypeName = 17;
	public static readonly RULE_variable = 18;
	public static readonly RULE_inList = 19;
	public static readonly RULE_path = 20;
	public static readonly RULE_pathAttributes = 21;
	public static readonly RULE_literal = 22;
	public static readonly RULE_stringLiteral = 23;
	public static readonly RULE_collectionLiteral = 24;
	public static readonly RULE_entityLiteral = 25;
	public static readonly RULE_functionInvocation = 26;
	public static readonly RULE_dateLiteral = 27;
	public static readonly RULE_timeLiteral = 28;
	public static readonly RULE_timestampLiteral = 29;
	public static readonly RULE_datePart = 30;
	public static readonly RULE_timePart = 31;
	public static readonly RULE_temporalIntervalLiteral = 32;
	public static readonly RULE_identifier = 33;
	// tslint:disable:no-trailing-whitespace
	public static readonly ruleNames: string[] = [
		"parsePredicate", "parseExpression", "parseExpressionOrPredicate", "parseTemplate", 
		"parseQuery", "template", "expression", "predicate", "predicateOrExpression", 
		"query", "selectClause", "fromClause", "whereClause", "fromItem", "fromRoot", 
		"join", "joinTarget", "domainTypeName", "variable", "inList", "path", 
		"pathAttributes", "literal", "stringLiteral", "collectionLiteral", "entityLiteral", 
		"functionInvocation", "dateLiteral", "timeLiteral", "timestampLiteral", 
		"datePart", "timePart", "temporalIntervalLiteral", "identifier",
	];

	private static readonly _LITERAL_NAMES: Array<string | undefined> = [
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, "'<'", "'<='", "'>'", "'>='", "'='", undefined, "'+'", "'-'", 
		"'*'", "'/'", "'%'", "'('", "')'", "'['", "']'", "','", "'.'", "':'", 
		"'!'", undefined, undefined, "'}'", "'#{'",
	];
	private static readonly _SYMBOLIC_NAMES: Array<string | undefined> = [
		undefined, "WS", "START_QUOTE", "START_DOUBLE_QUOTE", "INTEGER_LITERAL", 
		"NUMERIC_LITERAL", "LEADING_ZERO_INTEGER_LITERAL", "AND", "BETWEEN", "DATE", 
		"DAYS", "DISTINCT", "EMPTY", "FALSE", "FROM", "FULL", "HOURS", "IN", "INTERVAL", 
		"IS", "JOIN", "LEFT", "MINUTES", "MONTHS", "NOT", "NULL", "ON", "OR", 
		"RIGHT", "SECONDS", "SELECT", "TIME", "TIMESTAMP", "TRUE", "WHERE", "YEARS", 
		"LESS", "LESS_EQUAL", "GREATER", "GREATER_EQUAL", "EQUAL", "NOT_EQUAL", 
		"PLUS", "MINUS", "ASTERISK", "SLASH", "PERCENT", "LP", "RP", "LB", "RB", 
		"COMMA", "DOT", "COLON", "EXCLAMATION_MARK", "IDENTIFIER", "QUOTED_IDENTIFIER", 
		"EXPRESSION_END", "EXPRESSION_START", "TEXT", "TEXT_IN_QUOTE", "END_QUOTE", 
		"TEXT_IN_DOUBLE_QUOTE", "END_DOUBLE_QUOTE", "AS",
	];
	public static readonly VOCABULARY: Vocabulary = new VocabularyImpl(BlazeExpressionParser._LITERAL_NAMES, BlazeExpressionParser._SYMBOLIC_NAMES, []);

	// @Override
	// @NotNull
	public get vocabulary(): Vocabulary {
		return BlazeExpressionParser.VOCABULARY;
	}
	// tslint:enable:no-trailing-whitespace

	// @Override
	public get grammarFileName(): string { return "BlazeExpressionParser.g4"; }

	// @Override
	public get ruleNames(): string[] { return BlazeExpressionParser.ruleNames; }

	// @Override
	public get serializedATN(): string { return BlazeExpressionParser._serializedATN; }

	protected createFailedPredicateException(predicate?: string, message?: string): FailedPredicateException {
		return new FailedPredicateException(this, predicate, message);
	}

	constructor(input: TokenStream) {
		super(input);
		this._interp = new ParserATNSimulator(BlazeExpressionParser._ATN, this);
	}
	// @RuleVersion(0)
	public parsePredicate(): ParsePredicateContext {
		let _localctx: ParsePredicateContext = new ParsePredicateContext(this._ctx, this.state);
		this.enterRule(_localctx, 0, BlazeExpressionParser.RULE_parsePredicate);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 68;
			this.predicate(0);
			this.state = 69;
			this.match(BlazeExpressionParser.EOF);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public parseExpression(): ParseExpressionContext {
		let _localctx: ParseExpressionContext = new ParseExpressionContext(this._ctx, this.state);
		this.enterRule(_localctx, 2, BlazeExpressionParser.RULE_parseExpression);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 71;
			this.expression(0);
			this.state = 72;
			this.match(BlazeExpressionParser.EOF);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public parseExpressionOrPredicate(): ParseExpressionOrPredicateContext {
		let _localctx: ParseExpressionOrPredicateContext = new ParseExpressionOrPredicateContext(this._ctx, this.state);
		this.enterRule(_localctx, 4, BlazeExpressionParser.RULE_parseExpressionOrPredicate);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 74;
			this.predicateOrExpression();
			this.state = 75;
			this.match(BlazeExpressionParser.EOF);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public parseTemplate(): ParseTemplateContext {
		let _localctx: ParseTemplateContext = new ParseTemplateContext(this._ctx, this.state);
		this.enterRule(_localctx, 6, BlazeExpressionParser.RULE_parseTemplate);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 78;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.EXPRESSION_START || _la === BlazeExpressionParser.TEXT) {
				{
				this.state = 77;
				this.template();
				}
			}

			this.state = 80;
			this.match(BlazeExpressionParser.EOF);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public parseQuery(): ParseQueryContext {
		let _localctx: ParseQueryContext = new ParseQueryContext(this._ctx, this.state);
		this.enterRule(_localctx, 8, BlazeExpressionParser.RULE_parseQuery);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 82;
			this.query();
			this.state = 83;
			this.match(BlazeExpressionParser.EOF);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public template(): TemplateContext {
		let _localctx: TemplateContext = new TemplateContext(this._ctx, this.state);
		this.enterRule(_localctx, 10, BlazeExpressionParser.RULE_template);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 90;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				this.state = 90;
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case BlazeExpressionParser.TEXT:
					{
					this.state = 85;
					this.match(BlazeExpressionParser.TEXT);
					}
					break;
				case BlazeExpressionParser.EXPRESSION_START:
					{
					{
					this.state = 86;
					this.match(BlazeExpressionParser.EXPRESSION_START);
					this.state = 87;
					this.expression(0);
					this.state = 88;
					this.match(BlazeExpressionParser.EXPRESSION_END);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				this.state = 92;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			} while (_la === BlazeExpressionParser.EXPRESSION_START || _la === BlazeExpressionParser.TEXT);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public expression(): ExpressionContext;
	public expression(_p: number): ExpressionContext;
	// @RuleVersion(0)
	public expression(_p?: number): ExpressionContext {
		if (_p === undefined) {
			_p = 0;
		}

		let _parentctx: ParserRuleContext = this._ctx;
		let _parentState: number = this.state;
		let _localctx: ExpressionContext = new ExpressionContext(this._ctx, _parentState);
		let _prevctx: ExpressionContext = _localctx;
		let _startState: number = 12;
		this.enterRecursionRule(_localctx, 12, BlazeExpressionParser.RULE_expression, _p);
		let _la: number;
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 106;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 3, this._ctx) ) {
			case 1:
				{
				_localctx = new GroupedExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;

				this.state = 95;
				this.match(BlazeExpressionParser.LP);
				this.state = 96;
				this.expression(0);
				this.state = 97;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				{
				_localctx = new LiteralExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 99;
				this.literal();
				}
				break;

			case 3:
				{
				_localctx = new PathExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 100;
				this.path();
				}
				break;

			case 4:
				{
				_localctx = new FunctionExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 101;
				this.functionInvocation();
				}
				break;

			case 5:
				{
				_localctx = new UnaryMinusExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 102;
				this.match(BlazeExpressionParser.MINUS);
				this.state = 103;
				this.expression(4);
				}
				break;

			case 6:
				{
				_localctx = new UnaryPlusExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 104;
				this.match(BlazeExpressionParser.PLUS);
				this.state = 105;
				this.expression(3);
				}
				break;
			}
			this._ctx._stop = this._input.tryLT(-1);
			this.state = 116;
			this._errHandler.sync(this);
			_alt = this.interpreter.adaptivePredict(this._input, 5, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = _localctx;
					{
					this.state = 114;
					this._errHandler.sync(this);
					switch ( this.interpreter.adaptivePredict(this._input, 4, this._ctx) ) {
					case 1:
						{
						_localctx = new MultiplicativeExpressionContext(new ExpressionContext(_parentctx, _parentState));
						(_localctx as MultiplicativeExpressionContext)._lhs = _prevctx;
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_expression);
						this.state = 108;
						if (!(this.precpred(this._ctx, 2))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 2)");
						}
						this.state = 109;
						(_localctx as MultiplicativeExpressionContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if (!(((((_la - 44)) & ~0x1F) === 0 && ((1 << (_la - 44)) & ((1 << (BlazeExpressionParser.ASTERISK - 44)) | (1 << (BlazeExpressionParser.SLASH - 44)) | (1 << (BlazeExpressionParser.PERCENT - 44)))) !== 0))) {
							(_localctx as MultiplicativeExpressionContext)._op = this._errHandler.recoverInline(this);
						} else {
							if (this._input.LA(1) === Token.EOF) {
								this.matchedEOF = true;
							}

							this._errHandler.reportMatch(this);
							this.consume();
						}
						this.state = 110;
						(_localctx as MultiplicativeExpressionContext)._rhs = this.expression(3);
						}
						break;

					case 2:
						{
						_localctx = new AdditiveExpressionContext(new ExpressionContext(_parentctx, _parentState));
						(_localctx as AdditiveExpressionContext)._lhs = _prevctx;
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_expression);
						this.state = 111;
						if (!(this.precpred(this._ctx, 1))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 1)");
						}
						this.state = 112;
						(_localctx as AdditiveExpressionContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if (!(_la === BlazeExpressionParser.PLUS || _la === BlazeExpressionParser.MINUS)) {
							(_localctx as AdditiveExpressionContext)._op = this._errHandler.recoverInline(this);
						} else {
							if (this._input.LA(1) === Token.EOF) {
								this.matchedEOF = true;
							}

							this._errHandler.reportMatch(this);
							this.consume();
						}
						this.state = 113;
						(_localctx as AdditiveExpressionContext)._rhs = this.expression(2);
						}
						break;
					}
					}
				}
				this.state = 118;
				this._errHandler.sync(this);
				_alt = this.interpreter.adaptivePredict(this._input, 5, this._ctx);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public predicate(): PredicateContext;
	public predicate(_p: number): PredicateContext;
	// @RuleVersion(0)
	public predicate(_p?: number): PredicateContext {
		if (_p === undefined) {
			_p = 0;
		}

		let _parentctx: ParserRuleContext = this._ctx;
		let _parentState: number = this.state;
		let _localctx: PredicateContext = new PredicateContext(this._ctx, _parentState);
		let _prevctx: PredicateContext = _localctx;
		let _startState: number = 14;
		this.enterRecursionRule(_localctx, 14, BlazeExpressionParser.RULE_predicate, _p);
		let _la: number;
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 182;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 10, this._ctx) ) {
			case 1:
				{
				_localctx = new GroupedPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;

				this.state = 120;
				this.match(BlazeExpressionParser.LP);
				this.state = 121;
				this.predicate(0);
				this.state = 122;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				{
				_localctx = new NegatedPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 124;
				_la = this._input.LA(1);
				if (!(_la === BlazeExpressionParser.NOT || _la === BlazeExpressionParser.EXCLAMATION_MARK)) {
				this._errHandler.recoverInline(this);
				} else {
					if (this._input.LA(1) === Token.EOF) {
						this.matchedEOF = true;
					}

					this._errHandler.reportMatch(this);
					this.consume();
				}
				this.state = 125;
				this.predicate(15);
				}
				break;

			case 3:
				{
				_localctx = new IsNullPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 126;
				this.expression(0);
				this.state = 127;
				this.match(BlazeExpressionParser.IS);
				this.state = 129;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 128;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 131;
				this.match(BlazeExpressionParser.NULL);
				}
				break;

			case 4:
				{
				_localctx = new IsEmptyPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 133;
				this.expression(0);
				this.state = 134;
				this.match(BlazeExpressionParser.IS);
				this.state = 136;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 135;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 138;
				this.match(BlazeExpressionParser.EMPTY);
				}
				break;

			case 5:
				{
				_localctx = new EqualityPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 140;
				(_localctx as EqualityPredicateContext)._lhs = this.expression(0);
				this.state = 141;
				this.match(BlazeExpressionParser.EQUAL);
				this.state = 142;
				(_localctx as EqualityPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 6:
				{
				_localctx = new InequalityPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 144;
				(_localctx as InequalityPredicateContext)._lhs = this.expression(0);
				this.state = 145;
				this.match(BlazeExpressionParser.NOT_EQUAL);
				this.state = 146;
				(_localctx as InequalityPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 7:
				{
				_localctx = new GreaterThanPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 148;
				(_localctx as GreaterThanPredicateContext)._lhs = this.expression(0);
				this.state = 149;
				this.match(BlazeExpressionParser.GREATER);
				this.state = 150;
				(_localctx as GreaterThanPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 8:
				{
				_localctx = new GreaterThanOrEqualPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 152;
				(_localctx as GreaterThanOrEqualPredicateContext)._lhs = this.expression(0);
				this.state = 153;
				this.match(BlazeExpressionParser.GREATER_EQUAL);
				this.state = 154;
				(_localctx as GreaterThanOrEqualPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 9:
				{
				_localctx = new LessThanPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 156;
				(_localctx as LessThanPredicateContext)._lhs = this.expression(0);
				this.state = 157;
				this.match(BlazeExpressionParser.LESS);
				this.state = 158;
				(_localctx as LessThanPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 10:
				{
				_localctx = new LessThanOrEqualPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 160;
				(_localctx as LessThanOrEqualPredicateContext)._lhs = this.expression(0);
				this.state = 161;
				this.match(BlazeExpressionParser.LESS_EQUAL);
				this.state = 162;
				(_localctx as LessThanOrEqualPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 11:
				{
				_localctx = new InPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 164;
				this.expression(0);
				this.state = 166;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 165;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 168;
				this.match(BlazeExpressionParser.IN);
				this.state = 169;
				this.inList();
				}
				break;

			case 12:
				{
				_localctx = new BetweenPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 171;
				(_localctx as BetweenPredicateContext)._lhs = this.expression(0);
				this.state = 173;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 172;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 175;
				this.match(BlazeExpressionParser.BETWEEN);
				this.state = 176;
				(_localctx as BetweenPredicateContext)._begin = this.expression(0);
				this.state = 177;
				this.match(BlazeExpressionParser.AND);
				this.state = 178;
				(_localctx as BetweenPredicateContext)._end = this.expression(0);
				}
				break;

			case 13:
				{
				_localctx = new BooleanFunctionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 180;
				this.functionInvocation();
				}
				break;

			case 14:
				{
				_localctx = new PathPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 181;
				this.path();
				}
				break;
			}
			this._ctx._stop = this._input.tryLT(-1);
			this.state = 192;
			this._errHandler.sync(this);
			_alt = this.interpreter.adaptivePredict(this._input, 12, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = _localctx;
					{
					this.state = 190;
					this._errHandler.sync(this);
					switch ( this.interpreter.adaptivePredict(this._input, 11, this._ctx) ) {
					case 1:
						{
						_localctx = new AndPredicateContext(new PredicateContext(_parentctx, _parentState));
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_predicate);
						this.state = 184;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 185;
						this.match(BlazeExpressionParser.AND);
						this.state = 186;
						this.predicate(15);
						}
						break;

					case 2:
						{
						_localctx = new OrPredicateContext(new PredicateContext(_parentctx, _parentState));
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_predicate);
						this.state = 187;
						if (!(this.precpred(this._ctx, 13))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 13)");
						}
						this.state = 188;
						this.match(BlazeExpressionParser.OR);
						this.state = 189;
						this.predicate(14);
						}
						break;
					}
					}
				}
				this.state = 194;
				this._errHandler.sync(this);
				_alt = this.interpreter.adaptivePredict(this._input, 12, this._ctx);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public predicateOrExpression(): PredicateOrExpressionContext {
		let _localctx: PredicateOrExpressionContext = new PredicateOrExpressionContext(this._ctx, this.state);
		this.enterRule(_localctx, 16, BlazeExpressionParser.RULE_predicateOrExpression);
		try {
			this.state = 197;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 13, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 195;
				this.expression(0);
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 196;
				this.predicate(0);
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public query(): QueryContext {
		let _localctx: QueryContext = new QueryContext(this._ctx, this.state);
		this.enterRule(_localctx, 18, BlazeExpressionParser.RULE_query);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 200;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.SELECT) {
				{
				this.state = 199;
				this.selectClause();
				}
			}

			this.state = 202;
			this.fromClause();
			this.state = 204;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.WHERE) {
				{
				this.state = 203;
				this.whereClause();
				}
			}

			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public selectClause(): SelectClauseContext {
		let _localctx: SelectClauseContext = new SelectClauseContext(this._ctx, this.state);
		this.enterRule(_localctx, 20, BlazeExpressionParser.RULE_selectClause);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 206;
			this.match(BlazeExpressionParser.SELECT);
			this.state = 208;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 16, this._ctx) ) {
			case 1:
				{
				this.state = 207;
				this.match(BlazeExpressionParser.DISTINCT);
				}
				break;
			}
			this.state = 210;
			this.expression(0);
			this.state = 215;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la === BlazeExpressionParser.COMMA) {
				{
				{
				this.state = 211;
				this.match(BlazeExpressionParser.COMMA);
				this.state = 212;
				this.expression(0);
				}
				}
				this.state = 217;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public fromClause(): FromClauseContext {
		let _localctx: FromClauseContext = new FromClauseContext(this._ctx, this.state);
		this.enterRule(_localctx, 22, BlazeExpressionParser.RULE_fromClause);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 218;
			this.match(BlazeExpressionParser.FROM);
			this.state = 219;
			this.fromItem();
			this.state = 224;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while (_la === BlazeExpressionParser.COMMA) {
				{
				{
				this.state = 220;
				this.match(BlazeExpressionParser.COMMA);
				this.state = 221;
				this.fromItem();
				}
				}
				this.state = 226;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public whereClause(): WhereClauseContext {
		let _localctx: WhereClauseContext = new WhereClauseContext(this._ctx, this.state);
		this.enterRule(_localctx, 24, BlazeExpressionParser.RULE_whereClause);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 227;
			this.match(BlazeExpressionParser.WHERE);
			this.state = 228;
			this.predicate(0);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public fromItem(): FromItemContext {
		let _localctx: FromItemContext = new FromItemContext(this._ctx, this.state);
		this.enterRule(_localctx, 26, BlazeExpressionParser.RULE_fromItem);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 230;
			this.fromRoot();
			this.state = 234;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			while ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.FULL) | (1 << BlazeExpressionParser.JOIN) | (1 << BlazeExpressionParser.LEFT) | (1 << BlazeExpressionParser.RIGHT))) !== 0)) {
				{
				{
				this.state = 231;
				this.join();
				}
				}
				this.state = 236;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public fromRoot(): FromRootContext {
		let _localctx: FromRootContext = new FromRootContext(this._ctx, this.state);
		this.enterRule(_localctx, 28, BlazeExpressionParser.RULE_fromRoot);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 237;
			this.domainTypeName();
			this.state = 238;
			this.variable();
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public join(): JoinContext {
		let _localctx: JoinContext = new JoinContext(this._ctx, this.state);
		this.enterRule(_localctx, 30, BlazeExpressionParser.RULE_join);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 241;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.FULL) | (1 << BlazeExpressionParser.LEFT) | (1 << BlazeExpressionParser.RIGHT))) !== 0)) {
				{
				this.state = 240;
				_la = this._input.LA(1);
				if (!((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.FULL) | (1 << BlazeExpressionParser.LEFT) | (1 << BlazeExpressionParser.RIGHT))) !== 0))) {
				this._errHandler.recoverInline(this);
				} else {
					if (this._input.LA(1) === Token.EOF) {
						this.matchedEOF = true;
					}

					this._errHandler.reportMatch(this);
					this.consume();
				}
				}
			}

			this.state = 243;
			this.match(BlazeExpressionParser.JOIN);
			this.state = 244;
			this.joinTarget();
			this.state = 245;
			this.match(BlazeExpressionParser.ON);
			this.state = 246;
			this.predicate(0);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public joinTarget(): JoinTargetContext {
		let _localctx: JoinTargetContext = new JoinTargetContext(this._ctx, this.state);
		this.enterRule(_localctx, 32, BlazeExpressionParser.RULE_joinTarget);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 248;
			this.domainTypeName();
			this.state = 249;
			this.variable();
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public domainTypeName(): DomainTypeNameContext {
		let _localctx: DomainTypeNameContext = new DomainTypeNameContext(this._ctx, this.state);
		this.enterRule(_localctx, 34, BlazeExpressionParser.RULE_domainTypeName);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 251;
			this.identifier();
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public variable(): VariableContext {
		let _localctx: VariableContext = new VariableContext(this._ctx, this.state);
		this.enterRule(_localctx, 36, BlazeExpressionParser.RULE_variable);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 254;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.AS) {
				{
				this.state = 253;
				this.match(BlazeExpressionParser.AS);
				}
			}

			this.state = 256;
			this.identifier();
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public inList(): InListContext {
		let _localctx: InListContext = new InListContext(this._ctx, this.state);
		this.enterRule(_localctx, 38, BlazeExpressionParser.RULE_inList);
		let _la: number;
		try {
			this.state = 270;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 23, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 258;
				this.match(BlazeExpressionParser.LP);
				this.state = 259;
				this.expression(0);
				this.state = 264;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.COMMA) {
					{
					{
					this.state = 260;
					this.match(BlazeExpressionParser.COMMA);
					this.state = 261;
					this.expression(0);
					}
					}
					this.state = 266;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 267;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 269;
				this.expression(0);
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public path(): PathContext {
		let _localctx: PathContext = new PathContext(this._ctx, this.state);
		this.enterRule(_localctx, 40, BlazeExpressionParser.RULE_path);
		try {
			this.state = 279;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 25, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 272;
				this.identifier();
				this.state = 274;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 24, this._ctx) ) {
				case 1:
					{
					this.state = 273;
					this.pathAttributes();
					}
					break;
				}
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 276;
				this.functionInvocation();
				this.state = 277;
				this.pathAttributes();
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public pathAttributes(): PathAttributesContext {
		let _localctx: PathAttributesContext = new PathAttributesContext(this._ctx, this.state);
		this.enterRule(_localctx, 42, BlazeExpressionParser.RULE_pathAttributes);
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 283;
			this._errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					this.state = 281;
					this.match(BlazeExpressionParser.DOT);
					this.state = 282;
					this.identifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				this.state = 285;
				this._errHandler.sync(this);
				_alt = this.interpreter.adaptivePredict(this._input, 26, this._ctx);
			} while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public literal(): LiteralContext {
		let _localctx: LiteralContext = new LiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 44, BlazeExpressionParser.RULE_literal);
		try {
			this.state = 298;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 27, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 287;
				this.match(BlazeExpressionParser.NUMERIC_LITERAL);
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 288;
				this.match(BlazeExpressionParser.INTEGER_LITERAL);
				}
				break;

			case 3:
				this.enterOuterAlt(_localctx, 3);
				{
				this.state = 289;
				this.stringLiteral();
				}
				break;

			case 4:
				this.enterOuterAlt(_localctx, 4);
				{
				this.state = 290;
				this.match(BlazeExpressionParser.TRUE);
				}
				break;

			case 5:
				this.enterOuterAlt(_localctx, 5);
				{
				this.state = 291;
				this.match(BlazeExpressionParser.FALSE);
				}
				break;

			case 6:
				this.enterOuterAlt(_localctx, 6);
				{
				this.state = 292;
				this.dateLiteral();
				}
				break;

			case 7:
				this.enterOuterAlt(_localctx, 7);
				{
				this.state = 293;
				this.timeLiteral();
				}
				break;

			case 8:
				this.enterOuterAlt(_localctx, 8);
				{
				this.state = 294;
				this.timestampLiteral();
				}
				break;

			case 9:
				this.enterOuterAlt(_localctx, 9);
				{
				this.state = 295;
				this.temporalIntervalLiteral();
				}
				break;

			case 10:
				this.enterOuterAlt(_localctx, 10);
				{
				this.state = 296;
				this.collectionLiteral();
				}
				break;

			case 11:
				this.enterOuterAlt(_localctx, 11);
				{
				this.state = 297;
				this.entityLiteral();
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public stringLiteral(): StringLiteralContext {
		let _localctx: StringLiteralContext = new StringLiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 46, BlazeExpressionParser.RULE_stringLiteral);
		let _la: number;
		try {
			this.state = 316;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case BlazeExpressionParser.START_QUOTE:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 300;
				this.match(BlazeExpressionParser.START_QUOTE);
				this.state = 304;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.TEXT_IN_QUOTE) {
					{
					{
					this.state = 301;
					this.match(BlazeExpressionParser.TEXT_IN_QUOTE);
					}
					}
					this.state = 306;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 307;
				this.match(BlazeExpressionParser.END_QUOTE);
				}
				break;
			case BlazeExpressionParser.START_DOUBLE_QUOTE:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 308;
				this.match(BlazeExpressionParser.START_DOUBLE_QUOTE);
				this.state = 312;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.TEXT_IN_DOUBLE_QUOTE) {
					{
					{
					this.state = 309;
					this.match(BlazeExpressionParser.TEXT_IN_DOUBLE_QUOTE);
					}
					}
					this.state = 314;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 315;
				this.match(BlazeExpressionParser.END_DOUBLE_QUOTE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public collectionLiteral(): CollectionLiteralContext {
		let _localctx: CollectionLiteralContext = new CollectionLiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 48, BlazeExpressionParser.RULE_collectionLiteral);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 318;
			this.match(BlazeExpressionParser.LB);
			this.state = 327;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.START_QUOTE) | (1 << BlazeExpressionParser.START_DOUBLE_QUOTE) | (1 << BlazeExpressionParser.INTEGER_LITERAL) | (1 << BlazeExpressionParser.NUMERIC_LITERAL) | (1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.DISTINCT) | (1 << BlazeExpressionParser.FALSE) | (1 << BlazeExpressionParser.FROM) | (1 << BlazeExpressionParser.FULL) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.INTERVAL) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.JOIN) | (1 << BlazeExpressionParser.LEFT) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.ON) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.RIGHT) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.SELECT) | (1 << BlazeExpressionParser.TIME))) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & ((1 << (BlazeExpressionParser.TIMESTAMP - 32)) | (1 << (BlazeExpressionParser.TRUE - 32)) | (1 << (BlazeExpressionParser.WHERE - 32)) | (1 << (BlazeExpressionParser.YEARS - 32)) | (1 << (BlazeExpressionParser.LB - 32)) | (1 << (BlazeExpressionParser.IDENTIFIER - 32)) | (1 << (BlazeExpressionParser.QUOTED_IDENTIFIER - 32)))) !== 0)) {
				{
				this.state = 319;
				this.literal();
				this.state = 324;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.COMMA) {
					{
					{
					this.state = 320;
					this.match(BlazeExpressionParser.COMMA);
					this.state = 321;
					this.literal();
					}
					}
					this.state = 326;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				}
			}

			this.state = 329;
			this.match(BlazeExpressionParser.RB);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public entityLiteral(): EntityLiteralContext {
		let _localctx: EntityLiteralContext = new EntityLiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 50, BlazeExpressionParser.RULE_entityLiteral);
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 331;
			_localctx._name = this.identifier();
			this.state = 332;
			this.match(BlazeExpressionParser.LP);
			this.state = 340;
			this._errHandler.sync(this);
			_alt = this.interpreter.adaptivePredict(this._input, 33, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					{
					{
					this.state = 333;
					this.identifier();
					this.state = 334;
					this.match(BlazeExpressionParser.EQUAL);
					this.state = 335;
					this.predicateOrExpression();
					this.state = 336;
					this.match(BlazeExpressionParser.COMMA);
					}
					}
				}
				this.state = 342;
				this._errHandler.sync(this);
				_alt = this.interpreter.adaptivePredict(this._input, 33, this._ctx);
			}
			this.state = 343;
			this.identifier();
			this.state = 344;
			this.match(BlazeExpressionParser.EQUAL);
			this.state = 345;
			this.predicateOrExpression();
			this.state = 346;
			this.match(BlazeExpressionParser.RP);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public functionInvocation(): FunctionInvocationContext {
		let _localctx: FunctionInvocationContext = new FunctionInvocationContext(this._ctx, this.state);
		this.enterRule(_localctx, 52, BlazeExpressionParser.RULE_functionInvocation);
		let _la: number;
		try {
			let _alt: number;
			this.state = 383;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 38, this._ctx) ) {
			case 1:
				_localctx = new NamedInvocationContext(_localctx);
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 348;
				(_localctx as NamedInvocationContext)._name = this.identifier();
				this.state = 349;
				this.match(BlazeExpressionParser.LP);
				this.state = 364;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.DISTINCT) | (1 << BlazeExpressionParser.FROM) | (1 << BlazeExpressionParser.FULL) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.JOIN) | (1 << BlazeExpressionParser.LEFT) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.ON) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.RIGHT) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.SELECT) | (1 << BlazeExpressionParser.TIME))) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & ((1 << (BlazeExpressionParser.TIMESTAMP - 32)) | (1 << (BlazeExpressionParser.WHERE - 32)) | (1 << (BlazeExpressionParser.YEARS - 32)) | (1 << (BlazeExpressionParser.IDENTIFIER - 32)) | (1 << (BlazeExpressionParser.QUOTED_IDENTIFIER - 32)))) !== 0)) {
					{
					this.state = 357;
					this._errHandler.sync(this);
					_alt = this.interpreter.adaptivePredict(this._input, 34, this._ctx);
					while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
						if (_alt === 1) {
							{
							{
							this.state = 350;
							this.identifier();
							this.state = 351;
							this.match(BlazeExpressionParser.EQUAL);
							this.state = 352;
							this.predicateOrExpression();
							this.state = 353;
							this.match(BlazeExpressionParser.COMMA);
							}
							}
						}
						this.state = 359;
						this._errHandler.sync(this);
						_alt = this.interpreter.adaptivePredict(this._input, 34, this._ctx);
					}
					this.state = 360;
					this.identifier();
					this.state = 361;
					this.match(BlazeExpressionParser.EQUAL);
					this.state = 362;
					this.predicateOrExpression();
					}
				}

				this.state = 366;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				_localctx = new IndexedFunctionInvocationContext(_localctx);
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 368;
				(_localctx as IndexedFunctionInvocationContext)._name = this.identifier();
				this.state = 369;
				this.match(BlazeExpressionParser.LP);
				this.state = 379;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.START_QUOTE) | (1 << BlazeExpressionParser.START_DOUBLE_QUOTE) | (1 << BlazeExpressionParser.INTEGER_LITERAL) | (1 << BlazeExpressionParser.NUMERIC_LITERAL) | (1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.DISTINCT) | (1 << BlazeExpressionParser.FALSE) | (1 << BlazeExpressionParser.FROM) | (1 << BlazeExpressionParser.FULL) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.INTERVAL) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.JOIN) | (1 << BlazeExpressionParser.LEFT) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.ON) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.RIGHT) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.SELECT) | (1 << BlazeExpressionParser.TIME))) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & ((1 << (BlazeExpressionParser.TIMESTAMP - 32)) | (1 << (BlazeExpressionParser.TRUE - 32)) | (1 << (BlazeExpressionParser.WHERE - 32)) | (1 << (BlazeExpressionParser.YEARS - 32)) | (1 << (BlazeExpressionParser.PLUS - 32)) | (1 << (BlazeExpressionParser.MINUS - 32)) | (1 << (BlazeExpressionParser.LP - 32)) | (1 << (BlazeExpressionParser.LB - 32)) | (1 << (BlazeExpressionParser.EXCLAMATION_MARK - 32)) | (1 << (BlazeExpressionParser.IDENTIFIER - 32)) | (1 << (BlazeExpressionParser.QUOTED_IDENTIFIER - 32)))) !== 0)) {
					{
					this.state = 375;
					this._errHandler.sync(this);
					_alt = this.interpreter.adaptivePredict(this._input, 36, this._ctx);
					while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
						if (_alt === 1) {
							{
							{
							this.state = 370;
							this.predicateOrExpression();
							this.state = 371;
							this.match(BlazeExpressionParser.COMMA);
							}
							}
						}
						this.state = 377;
						this._errHandler.sync(this);
						_alt = this.interpreter.adaptivePredict(this._input, 36, this._ctx);
					}
					this.state = 378;
					this.predicateOrExpression();
					}
				}

				this.state = 381;
				this.match(BlazeExpressionParser.RP);
				}
				break;
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public dateLiteral(): DateLiteralContext {
		let _localctx: DateLiteralContext = new DateLiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 54, BlazeExpressionParser.RULE_dateLiteral);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 385;
			this.match(BlazeExpressionParser.DATE);
			this.state = 386;
			this.match(BlazeExpressionParser.LP);
			this.state = 387;
			this.datePart();
			this.state = 388;
			this.match(BlazeExpressionParser.RP);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public timeLiteral(): TimeLiteralContext {
		let _localctx: TimeLiteralContext = new TimeLiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 56, BlazeExpressionParser.RULE_timeLiteral);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 390;
			this.match(BlazeExpressionParser.TIME);
			this.state = 391;
			this.match(BlazeExpressionParser.LP);
			this.state = 392;
			this.timePart();
			this.state = 395;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.DOT) {
				{
				this.state = 393;
				this.match(BlazeExpressionParser.DOT);
				this.state = 394;
				_localctx._fraction = this._input.LT(1);
				_la = this._input.LA(1);
				if (!(_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL)) {
					_localctx._fraction = this._errHandler.recoverInline(this);
				} else {
					if (this._input.LA(1) === Token.EOF) {
						this.matchedEOF = true;
					}

					this._errHandler.reportMatch(this);
					this.consume();
				}
				}
			}

			this.state = 397;
			this.match(BlazeExpressionParser.RP);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public timestampLiteral(): TimestampLiteralContext {
		let _localctx: TimestampLiteralContext = new TimestampLiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 58, BlazeExpressionParser.RULE_timestampLiteral);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 399;
			this.match(BlazeExpressionParser.TIMESTAMP);
			this.state = 400;
			this.match(BlazeExpressionParser.LP);
			this.state = 401;
			this.datePart();
			this.state = 407;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL) {
				{
				this.state = 402;
				this.timePart();
				this.state = 405;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.DOT) {
					{
					this.state = 403;
					this.match(BlazeExpressionParser.DOT);
					this.state = 404;
					_localctx._fraction = this._input.LT(1);
					_la = this._input.LA(1);
					if (!(_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL)) {
						_localctx._fraction = this._errHandler.recoverInline(this);
					} else {
						if (this._input.LA(1) === Token.EOF) {
							this.matchedEOF = true;
						}

						this._errHandler.reportMatch(this);
						this.consume();
					}
					}
				}

				}
			}

			this.state = 409;
			this.match(BlazeExpressionParser.RP);
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public datePart(): DatePartContext {
		let _localctx: DatePartContext = new DatePartContext(this._ctx, this.state);
		this.enterRule(_localctx, 60, BlazeExpressionParser.RULE_datePart);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 411;
			this.match(BlazeExpressionParser.INTEGER_LITERAL);
			this.state = 412;
			this.match(BlazeExpressionParser.MINUS);
			this.state = 413;
			_la = this._input.LA(1);
			if (!(_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL)) {
			this._errHandler.recoverInline(this);
			} else {
				if (this._input.LA(1) === Token.EOF) {
					this.matchedEOF = true;
				}

				this._errHandler.reportMatch(this);
				this.consume();
			}
			this.state = 414;
			this.match(BlazeExpressionParser.MINUS);
			this.state = 415;
			_la = this._input.LA(1);
			if (!(_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL)) {
			this._errHandler.recoverInline(this);
			} else {
				if (this._input.LA(1) === Token.EOF) {
					this.matchedEOF = true;
				}

				this._errHandler.reportMatch(this);
				this.consume();
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public timePart(): TimePartContext {
		let _localctx: TimePartContext = new TimePartContext(this._ctx, this.state);
		this.enterRule(_localctx, 62, BlazeExpressionParser.RULE_timePart);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 417;
			_la = this._input.LA(1);
			if (!(_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL)) {
			this._errHandler.recoverInline(this);
			} else {
				if (this._input.LA(1) === Token.EOF) {
					this.matchedEOF = true;
				}

				this._errHandler.reportMatch(this);
				this.consume();
			}
			this.state = 418;
			this.match(BlazeExpressionParser.COLON);
			this.state = 419;
			_la = this._input.LA(1);
			if (!(_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL)) {
			this._errHandler.recoverInline(this);
			} else {
				if (this._input.LA(1) === Token.EOF) {
					this.matchedEOF = true;
				}

				this._errHandler.reportMatch(this);
				this.consume();
			}
			this.state = 420;
			this.match(BlazeExpressionParser.COLON);
			this.state = 421;
			_la = this._input.LA(1);
			if (!(_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL)) {
			this._errHandler.recoverInline(this);
			} else {
				if (this._input.LA(1) === Token.EOF) {
					this.matchedEOF = true;
				}

				this._errHandler.reportMatch(this);
				this.consume();
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public temporalIntervalLiteral(): TemporalIntervalLiteralContext {
		let _localctx: TemporalIntervalLiteralContext = new TemporalIntervalLiteralContext(this._ctx, this.state);
		this.enterRule(_localctx, 64, BlazeExpressionParser.RULE_temporalIntervalLiteral);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 423;
			this.match(BlazeExpressionParser.INTERVAL);
			this.state = 496;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 57, this._ctx) ) {
			case 1:
				{
				{
				this.state = 424;
				_localctx._years = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 425;
				this.match(BlazeExpressionParser.YEARS);
				this.state = 428;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 42, this._ctx) ) {
				case 1:
					{
					this.state = 426;
					_localctx._months = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 427;
					this.match(BlazeExpressionParser.MONTHS);
					}
					break;
				}
				this.state = 432;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 43, this._ctx) ) {
				case 1:
					{
					this.state = 430;
					_localctx._days = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 431;
					this.match(BlazeExpressionParser.DAYS);
					}
					break;
				}
				this.state = 436;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 44, this._ctx) ) {
				case 1:
					{
					this.state = 434;
					_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 435;
					this.match(BlazeExpressionParser.HOURS);
					}
					break;
				}
				this.state = 440;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 45, this._ctx) ) {
				case 1:
					{
					this.state = 438;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 439;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 444;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 46, this._ctx) ) {
				case 1:
					{
					this.state = 442;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 443;
					this.match(BlazeExpressionParser.SECONDS);
					}
					break;
				}
				}
				}
				break;

			case 2:
				{
				{
				this.state = 446;
				_localctx._months = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 447;
				this.match(BlazeExpressionParser.MONTHS);
				this.state = 450;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 47, this._ctx) ) {
				case 1:
					{
					this.state = 448;
					_localctx._days = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 449;
					this.match(BlazeExpressionParser.DAYS);
					}
					break;
				}
				this.state = 454;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 48, this._ctx) ) {
				case 1:
					{
					this.state = 452;
					_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 453;
					this.match(BlazeExpressionParser.HOURS);
					}
					break;
				}
				this.state = 458;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 49, this._ctx) ) {
				case 1:
					{
					this.state = 456;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 457;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 462;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 50, this._ctx) ) {
				case 1:
					{
					this.state = 460;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 461;
					this.match(BlazeExpressionParser.SECONDS);
					}
					break;
				}
				}
				}
				break;

			case 3:
				{
				{
				this.state = 464;
				_localctx._days = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 465;
				this.match(BlazeExpressionParser.DAYS);
				this.state = 468;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 51, this._ctx) ) {
				case 1:
					{
					this.state = 466;
					_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 467;
					this.match(BlazeExpressionParser.HOURS);
					}
					break;
				}
				this.state = 472;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 52, this._ctx) ) {
				case 1:
					{
					this.state = 470;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 471;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 476;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 53, this._ctx) ) {
				case 1:
					{
					this.state = 474;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 475;
					this.match(BlazeExpressionParser.SECONDS);
					}
					break;
				}
				}
				}
				break;

			case 4:
				{
				{
				this.state = 478;
				_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 479;
				this.match(BlazeExpressionParser.HOURS);
				this.state = 482;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 54, this._ctx) ) {
				case 1:
					{
					this.state = 480;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 481;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 486;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 55, this._ctx) ) {
				case 1:
					{
					this.state = 484;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 485;
					this.match(BlazeExpressionParser.SECONDS);
					}
					break;
				}
				}
				}
				break;

			case 5:
				{
				{
				this.state = 488;
				_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 489;
				this.match(BlazeExpressionParser.MINUTES);
				this.state = 492;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 56, this._ctx) ) {
				case 1:
					{
					this.state = 490;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 491;
					this.match(BlazeExpressionParser.SECONDS);
					}
					break;
				}
				}
				}
				break;

			case 6:
				{
				{
				this.state = 494;
				_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 495;
				this.match(BlazeExpressionParser.SECONDS);
				}
				}
				break;
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}
	// @RuleVersion(0)
	public identifier(): IdentifierContext {
		let _localctx: IdentifierContext = new IdentifierContext(this._ctx, this.state);
		this.enterRule(_localctx, 66, BlazeExpressionParser.RULE_identifier);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 498;
			_la = this._input.LA(1);
			if (!((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.DISTINCT) | (1 << BlazeExpressionParser.FROM) | (1 << BlazeExpressionParser.FULL) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.JOIN) | (1 << BlazeExpressionParser.LEFT) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.ON) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.RIGHT) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.SELECT) | (1 << BlazeExpressionParser.TIME))) !== 0) || ((((_la - 32)) & ~0x1F) === 0 && ((1 << (_la - 32)) & ((1 << (BlazeExpressionParser.TIMESTAMP - 32)) | (1 << (BlazeExpressionParser.WHERE - 32)) | (1 << (BlazeExpressionParser.YEARS - 32)) | (1 << (BlazeExpressionParser.IDENTIFIER - 32)) | (1 << (BlazeExpressionParser.QUOTED_IDENTIFIER - 32)))) !== 0))) {
			this._errHandler.recoverInline(this);
			} else {
				if (this._input.LA(1) === Token.EOF) {
					this.matchedEOF = true;
				}

				this._errHandler.reportMatch(this);
				this.consume();
			}
			}
		}
		catch (re) {
			if (re instanceof RecognitionException) {
				_localctx.exception = re;
				this._errHandler.reportError(this, re);
				this._errHandler.recover(this, re);
			} else {
				throw re;
			}
		}
		finally {
			this.exitRule();
		}
		return _localctx;
	}

	public sempred(_localctx: RuleContext, ruleIndex: number, predIndex: number): boolean {
		switch (ruleIndex) {
		case 6:
			return this.expression_sempred(_localctx as ExpressionContext, predIndex);

		case 7:
			return this.predicate_sempred(_localctx as PredicateContext, predIndex);
		}
		return true;
	}
	private expression_sempred(_localctx: ExpressionContext, predIndex: number): boolean {
		switch (predIndex) {
		case 0:
			return this.precpred(this._ctx, 2);

		case 1:
			return this.precpred(this._ctx, 1);
		}
		return true;
	}
	private predicate_sempred(_localctx: PredicateContext, predIndex: number): boolean {
		switch (predIndex) {
		case 2:
			return this.precpred(this._ctx, 14);

		case 3:
			return this.precpred(this._ctx, 13);
		}
		return true;
	}

	public static readonly _serializedATN: string =
		"\x03\uC91D\uCABA\u058D\uAFBA\u4F53\u0607\uEA8B\uC241\x03B\u01F7\x04\x02" +
		"\t\x02\x04\x03\t\x03\x04\x04\t\x04\x04\x05\t\x05\x04\x06\t\x06\x04\x07" +
		"\t\x07\x04\b\t\b\x04\t\t\t\x04\n\t\n\x04\v\t\v\x04\f\t\f\x04\r\t\r\x04" +
		"\x0E\t\x0E\x04\x0F\t\x0F\x04\x10\t\x10\x04\x11\t\x11\x04\x12\t\x12\x04" +
		"\x13\t\x13\x04\x14\t\x14\x04\x15\t\x15\x04\x16\t\x16\x04\x17\t\x17\x04" +
		"\x18\t\x18\x04\x19\t\x19\x04\x1A\t\x1A\x04\x1B\t\x1B\x04\x1C\t\x1C\x04" +
		"\x1D\t\x1D\x04\x1E\t\x1E\x04\x1F\t\x1F\x04 \t \x04!\t!\x04\"\t\"\x04#" +
		"\t#\x03\x02\x03\x02\x03\x02\x03\x03\x03\x03\x03\x03\x03\x04\x03\x04\x03" +
		"\x04\x03\x05\x05\x05Q\n\x05\x03\x05\x03\x05\x03\x06\x03\x06\x03\x06\x03" +
		"\x07\x03\x07\x03\x07\x03\x07\x03\x07\x06\x07]\n\x07\r\x07\x0E\x07^\x03" +
		"\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x05" +
		"\bm\n\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x07\bu\n\b\f\b\x0E\bx\v\b" +
		"\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x05\t\x84" +
		"\n\t\x03\t\x03\t\x03\t\x03\t\x03\t\x05\t\x8B\n\t\x03\t\x03\t\x03\t\x03" +
		"\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03" +
		"\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03\t\x03" +
		"\t\x05\t\xA9\n\t\x03\t\x03\t\x03\t\x03\t\x03\t\x05\t\xB0\n\t\x03\t\x03" +
		"\t\x03\t\x03\t\x03\t\x03\t\x03\t\x05\t\xB9\n\t\x03\t\x03\t\x03\t\x03\t" +
		"\x03\t\x03\t\x07\t\xC1\n\t\f\t\x0E\t\xC4\v\t\x03\n\x03\n\x05\n\xC8\n\n" +
		"\x03\v\x05\v\xCB\n\v\x03\v\x03\v\x05\v\xCF\n\v\x03\f\x03\f\x05\f\xD3\n" +
		"\f\x03\f\x03\f\x03\f\x07\f\xD8\n\f\f\f\x0E\f\xDB\v\f\x03\r\x03\r\x03\r" +
		"\x03\r\x07\r\xE1\n\r\f\r\x0E\r\xE4\v\r\x03\x0E\x03\x0E\x03\x0E\x03\x0F" +
		"\x03\x0F\x07\x0F\xEB\n\x0F\f\x0F\x0E\x0F\xEE\v\x0F\x03\x10\x03\x10\x03" +
		"\x10\x03\x11\x05\x11\xF4\n\x11\x03\x11\x03\x11\x03\x11\x03\x11\x03\x11" +
		"\x03\x12\x03\x12\x03\x12\x03\x13\x03\x13\x03\x14\x05\x14\u0101\n\x14\x03" +
		"\x14\x03\x14\x03\x15\x03\x15\x03\x15\x03\x15\x07\x15\u0109\n\x15\f\x15" +
		"\x0E\x15\u010C\v\x15\x03\x15\x03\x15\x03\x15\x05\x15\u0111\n\x15\x03\x16" +
		"\x03\x16\x05\x16\u0115\n\x16\x03\x16\x03\x16\x03\x16\x05\x16\u011A\n\x16" +
		"\x03\x17\x03\x17\x06\x17\u011E\n\x17\r\x17\x0E\x17\u011F\x03\x18\x03\x18" +
		"\x03\x18\x03\x18\x03\x18\x03\x18\x03\x18\x03\x18\x03\x18\x03\x18\x03\x18" +
		"\x05\x18\u012D\n\x18\x03\x19\x03\x19\x07\x19\u0131\n\x19\f\x19\x0E\x19" +
		"\u0134\v\x19\x03\x19\x03\x19\x03\x19\x07\x19\u0139\n\x19\f\x19\x0E\x19" +
		"\u013C\v\x19\x03\x19\x05\x19\u013F\n\x19\x03\x1A\x03\x1A\x03\x1A\x03\x1A" +
		"\x07\x1A\u0145\n\x1A\f\x1A\x0E\x1A\u0148\v\x1A\x05\x1A\u014A\n\x1A\x03" +
		"\x1A\x03\x1A\x03\x1B\x03\x1B\x03\x1B\x03\x1B\x03\x1B\x03\x1B\x03\x1B\x07" +
		"\x1B\u0155\n\x1B\f\x1B\x0E\x1B\u0158\v\x1B\x03\x1B\x03\x1B\x03\x1B\x03" +
		"\x1B\x03\x1B\x03\x1C\x03\x1C\x03\x1C\x03\x1C\x03\x1C\x03\x1C\x03\x1C\x07" +
		"\x1C\u0166\n\x1C\f\x1C\x0E\x1C\u0169\v\x1C\x03\x1C\x03\x1C\x03\x1C\x03" +
		"\x1C\x05\x1C\u016F\n\x1C\x03\x1C\x03\x1C\x03\x1C\x03\x1C\x03\x1C\x03\x1C" +
		"\x03\x1C\x07\x1C\u0178\n\x1C\f\x1C\x0E\x1C\u017B\v\x1C\x03\x1C\x05\x1C" +
		"\u017E\n\x1C\x03\x1C\x03\x1C\x05\x1C\u0182\n\x1C\x03\x1D\x03\x1D\x03\x1D" +
		"\x03\x1D\x03\x1D\x03\x1E\x03\x1E\x03\x1E\x03\x1E\x03\x1E\x05\x1E\u018E" +
		"\n\x1E\x03\x1E\x03\x1E\x03\x1F\x03\x1F\x03\x1F\x03\x1F\x03\x1F\x03\x1F" +
		"\x05\x1F\u0198\n\x1F\x05\x1F\u019A\n\x1F\x03\x1F\x03\x1F\x03 \x03 \x03" +
		" \x03 \x03 \x03 \x03!\x03!\x03!\x03!\x03!\x03!\x03\"\x03\"\x03\"\x03\"" +
		"\x03\"\x05\"\u01AF\n\"\x03\"\x03\"\x05\"\u01B3\n\"\x03\"\x03\"\x05\"\u01B7" +
		"\n\"\x03\"\x03\"\x05\"\u01BB\n\"\x03\"\x03\"\x05\"\u01BF\n\"\x03\"\x03" +
		"\"\x03\"\x03\"\x05\"\u01C5\n\"\x03\"\x03\"\x05\"\u01C9\n\"\x03\"\x03\"" +
		"\x05\"\u01CD\n\"\x03\"\x03\"\x05\"\u01D1\n\"\x03\"\x03\"\x03\"\x03\"\x05" +
		"\"\u01D7\n\"\x03\"\x03\"\x05\"\u01DB\n\"\x03\"\x03\"\x05\"\u01DF\n\"\x03" +
		"\"\x03\"\x03\"\x03\"\x05\"\u01E5\n\"\x03\"\x03\"\x05\"\u01E9\n\"\x03\"" +
		"\x03\"\x03\"\x03\"\x05\"\u01EF\n\"\x03\"\x03\"\x05\"\u01F3\n\"\x03#\x03" +
		"#\x03#\x02\x02\x04\x0E\x10$\x02\x02\x04\x02\x06\x02\b\x02\n\x02\f\x02" +
		"\x0E\x02\x10\x02\x12\x02\x14\x02\x16\x02\x18\x02\x1A\x02\x1C\x02\x1E\x02" +
		" \x02\"\x02$\x02&\x02(\x02*\x02,\x02.\x020\x022\x024\x026\x028\x02:\x02" +
		"<\x02>\x02@\x02B\x02D\x02\x02\b\x03\x02.0\x03\x02,-\x04\x02\x1A\x1A88" +
		"\x05\x02\x11\x11\x17\x17\x1E\x1E\x04\x02\x06\x06\b\b\b\x02\t\r\x10\x13" +
		"\x15\x1A\x1C\"$%9:\x02\u022B\x02F\x03\x02\x02\x02\x04I\x03\x02\x02\x02" +
		"\x06L\x03\x02\x02\x02\bP\x03\x02\x02\x02\nT\x03\x02\x02\x02\f\\\x03\x02" +
		"\x02\x02\x0El\x03\x02\x02\x02\x10\xB8\x03\x02\x02\x02\x12\xC7\x03\x02" +
		"\x02\x02\x14\xCA\x03\x02\x02\x02\x16\xD0\x03\x02\x02\x02\x18\xDC\x03\x02" +
		"\x02\x02\x1A\xE5\x03\x02\x02\x02\x1C\xE8\x03\x02\x02\x02\x1E\xEF\x03\x02" +
		"\x02\x02 \xF3\x03\x02\x02\x02\"\xFA\x03\x02\x02\x02$\xFD\x03\x02\x02\x02" +
		"&\u0100\x03\x02\x02\x02(\u0110\x03\x02\x02\x02*\u0119\x03\x02\x02\x02" +
		",\u011D\x03\x02\x02\x02.\u012C\x03\x02\x02\x020\u013E\x03\x02\x02\x02" +
		"2\u0140\x03\x02\x02\x024\u014D\x03\x02\x02\x026\u0181\x03\x02\x02\x02" +
		"8\u0183\x03\x02\x02\x02:\u0188\x03\x02\x02\x02<\u0191\x03\x02\x02\x02" +
		">\u019D\x03\x02\x02\x02@\u01A3\x03\x02\x02\x02B\u01A9\x03\x02\x02\x02" +
		"D\u01F4\x03\x02\x02\x02FG\x05\x10\t\x02GH\x07\x02\x02\x03H\x03\x03\x02" +
		"\x02\x02IJ\x05\x0E\b\x02JK\x07\x02\x02\x03K\x05\x03\x02\x02\x02LM\x05" +
		"\x12\n\x02MN\x07\x02\x02\x03N\x07\x03\x02\x02\x02OQ\x05\f\x07\x02PO\x03" +
		"\x02\x02\x02PQ\x03\x02\x02\x02QR\x03\x02\x02\x02RS\x07\x02\x02\x03S\t" +
		"\x03\x02\x02\x02TU\x05\x14\v\x02UV\x07\x02\x02\x03V\v\x03\x02\x02\x02" +
		"W]\x07=\x02\x02XY\x07<\x02\x02YZ\x05\x0E\b\x02Z[\x07;\x02\x02[]\x03\x02" +
		"\x02\x02\\W\x03\x02\x02\x02\\X\x03\x02\x02\x02]^\x03\x02\x02\x02^\\\x03" +
		"\x02\x02\x02^_\x03\x02\x02\x02_\r\x03\x02\x02\x02`a\b\b\x01\x02ab\x07" +
		"1\x02\x02bc\x05\x0E\b\x02cd\x072\x02\x02dm\x03\x02\x02\x02em\x05.\x18" +
		"\x02fm\x05*\x16\x02gm\x056\x1C\x02hi\x07-\x02\x02im\x05\x0E\b\x06jk\x07" +
		",\x02\x02km\x05\x0E\b\x05l`\x03\x02\x02\x02le\x03\x02\x02\x02lf\x03\x02" +
		"\x02\x02lg\x03\x02\x02\x02lh\x03\x02\x02\x02lj\x03\x02\x02\x02mv\x03\x02" +
		"\x02\x02no\f\x04\x02\x02op\t\x02\x02\x02pu\x05\x0E\b\x05qr\f\x03\x02\x02" +
		"rs\t\x03\x02\x02su\x05\x0E\b\x04tn\x03\x02\x02\x02tq\x03\x02\x02\x02u" +
		"x\x03\x02\x02\x02vt\x03\x02\x02\x02vw\x03\x02\x02\x02w\x0F\x03\x02\x02" +
		"\x02xv\x03\x02\x02\x02yz\b\t\x01\x02z{\x071\x02\x02{|\x05\x10\t\x02|}" +
		"\x072\x02\x02}\xB9\x03\x02\x02\x02~\x7F\t\x04\x02\x02\x7F\xB9\x05\x10" +
		"\t\x11\x80\x81\x05\x0E\b\x02\x81\x83\x07\x15\x02\x02\x82\x84\x07\x1A\x02" +
		"\x02\x83\x82\x03\x02\x02\x02\x83\x84\x03\x02\x02\x02\x84\x85\x03\x02\x02" +
		"\x02\x85\x86\x07\x1B\x02\x02\x86\xB9\x03\x02\x02\x02\x87\x88\x05\x0E\b" +
		"\x02\x88\x8A\x07\x15\x02\x02\x89\x8B\x07\x1A\x02\x02\x8A\x89\x03\x02\x02" +
		"\x02\x8A\x8B\x03\x02\x02\x02\x8B\x8C\x03\x02\x02\x02\x8C\x8D\x07\x0E\x02" +
		"\x02\x8D\xB9\x03\x02\x02\x02\x8E\x8F\x05\x0E\b\x02\x8F\x90\x07*\x02\x02" +
		"\x90\x91\x05\x0E\b\x02\x91\xB9\x03\x02\x02\x02\x92\x93\x05\x0E\b\x02\x93" +
		"\x94\x07+\x02\x02\x94\x95\x05\x0E\b\x02\x95\xB9\x03\x02\x02\x02\x96\x97" +
		"\x05\x0E\b\x02\x97\x98\x07(\x02\x02\x98\x99\x05\x0E\b\x02\x99\xB9\x03" +
		"\x02\x02\x02\x9A\x9B\x05\x0E\b\x02\x9B\x9C\x07)\x02\x02\x9C\x9D\x05\x0E" +
		"\b\x02\x9D\xB9\x03\x02\x02\x02\x9E\x9F\x05\x0E\b\x02\x9F\xA0\x07&\x02" +
		"\x02\xA0\xA1\x05\x0E\b\x02\xA1\xB9\x03\x02\x02\x02\xA2\xA3\x05\x0E\b\x02" +
		"\xA3\xA4\x07\'\x02\x02\xA4\xA5\x05\x0E\b\x02\xA5\xB9\x03\x02\x02\x02\xA6" +
		"\xA8\x05\x0E\b\x02\xA7\xA9\x07\x1A\x02\x02\xA8\xA7\x03\x02\x02\x02\xA8" +
		"\xA9\x03\x02\x02\x02\xA9\xAA\x03\x02\x02\x02\xAA\xAB\x07\x13\x02\x02\xAB" +
		"\xAC\x05(\x15\x02\xAC\xB9\x03\x02\x02\x02\xAD\xAF\x05\x0E\b\x02\xAE\xB0" +
		"\x07\x1A\x02\x02\xAF\xAE\x03\x02\x02\x02\xAF\xB0\x03\x02\x02\x02\xB0\xB1" +
		"\x03\x02\x02\x02\xB1\xB2\x07\n\x02\x02\xB2\xB3\x05\x0E\b\x02\xB3\xB4\x07" +
		"\t\x02\x02\xB4\xB5\x05\x0E\b\x02\xB5\xB9\x03\x02\x02\x02\xB6\xB9\x056" +
		"\x1C\x02\xB7\xB9\x05*\x16\x02\xB8y\x03\x02\x02\x02\xB8~\x03\x02\x02\x02" +
		"\xB8\x80\x03\x02\x02\x02\xB8\x87\x03\x02\x02\x02\xB8\x8E\x03\x02\x02\x02" +
		"\xB8\x92\x03\x02\x02\x02\xB8\x96\x03\x02\x02\x02\xB8\x9A\x03\x02\x02\x02" +
		"\xB8\x9E\x03\x02\x02\x02\xB8\xA2\x03\x02\x02\x02\xB8\xA6\x03\x02\x02\x02" +
		"\xB8\xAD\x03\x02\x02\x02\xB8\xB6\x03\x02\x02\x02\xB8\xB7\x03\x02\x02\x02" +
		"\xB9\xC2\x03\x02\x02\x02\xBA\xBB\f\x10\x02\x02\xBB\xBC\x07\t\x02\x02\xBC" +
		"\xC1\x05\x10\t\x11\xBD\xBE\f\x0F\x02\x02\xBE\xBF\x07\x1D\x02\x02\xBF\xC1" +
		"\x05\x10\t\x10\xC0\xBA\x03\x02\x02\x02\xC0\xBD\x03\x02\x02\x02\xC1\xC4" +
		"\x03\x02\x02\x02\xC2\xC0\x03\x02\x02\x02\xC2\xC3\x03\x02\x02\x02\xC3\x11" +
		"\x03\x02\x02\x02\xC4\xC2\x03\x02\x02\x02\xC5\xC8\x05\x0E\b\x02\xC6\xC8" +
		"\x05\x10\t\x02\xC7\xC5\x03\x02\x02\x02\xC7\xC6\x03\x02\x02\x02\xC8\x13" +
		"\x03\x02\x02\x02\xC9\xCB\x05\x16\f\x02\xCA\xC9\x03\x02\x02\x02\xCA\xCB" +
		"\x03\x02\x02\x02\xCB\xCC\x03\x02\x02\x02\xCC\xCE\x05\x18\r\x02\xCD\xCF" +
		"\x05\x1A\x0E\x02\xCE\xCD\x03\x02\x02\x02\xCE\xCF\x03\x02\x02\x02\xCF\x15" +
		"\x03\x02\x02\x02\xD0\xD2\x07 \x02\x02\xD1\xD3\x07\r\x02\x02\xD2\xD1\x03" +
		"\x02\x02\x02\xD2\xD3\x03\x02\x02\x02\xD3\xD4\x03\x02\x02\x02\xD4\xD9\x05" +
		"\x0E\b\x02\xD5\xD6\x075\x02\x02\xD6\xD8\x05\x0E\b\x02\xD7\xD5\x03\x02" +
		"\x02\x02\xD8\xDB\x03\x02\x02\x02\xD9\xD7\x03\x02\x02\x02\xD9\xDA\x03\x02" +
		"\x02\x02\xDA\x17\x03\x02\x02\x02\xDB\xD9\x03\x02\x02\x02\xDC\xDD\x07\x10" +
		"\x02\x02\xDD\xE2\x05\x1C\x0F\x02\xDE\xDF\x075\x02\x02\xDF\xE1\x05\x1C" +
		"\x0F\x02\xE0\xDE\x03\x02\x02\x02\xE1\xE4\x03\x02\x02\x02\xE2\xE0\x03\x02" +
		"\x02\x02\xE2\xE3\x03\x02\x02\x02\xE3\x19\x03\x02\x02\x02\xE4\xE2\x03\x02" +
		"\x02\x02\xE5\xE6\x07$\x02\x02\xE6\xE7\x05\x10\t\x02\xE7\x1B\x03\x02\x02" +
		"\x02\xE8\xEC\x05\x1E\x10\x02\xE9\xEB\x05 \x11\x02\xEA\xE9\x03\x02\x02" +
		"\x02\xEB\xEE\x03\x02\x02\x02\xEC\xEA\x03\x02\x02\x02\xEC\xED\x03\x02\x02" +
		"\x02\xED\x1D\x03\x02\x02\x02\xEE\xEC\x03\x02\x02\x02\xEF\xF0\x05$\x13" +
		"\x02\xF0\xF1\x05&\x14\x02\xF1\x1F\x03\x02\x02\x02\xF2\xF4\t\x05\x02\x02" +
		"\xF3\xF2\x03\x02\x02\x02\xF3\xF4\x03\x02\x02\x02\xF4\xF5\x03\x02\x02\x02" +
		"\xF5\xF6\x07\x16\x02\x02\xF6\xF7\x05\"\x12\x02\xF7\xF8\x07\x1C\x02\x02" +
		"\xF8\xF9\x05\x10\t\x02\xF9!\x03\x02\x02\x02\xFA\xFB\x05$\x13\x02\xFB\xFC" +
		"\x05&\x14\x02\xFC#\x03\x02\x02\x02\xFD\xFE\x05D#\x02\xFE%\x03\x02\x02" +
		"\x02\xFF\u0101\x07B\x02\x02\u0100\xFF\x03\x02\x02\x02\u0100\u0101\x03" +
		"\x02\x02\x02\u0101\u0102\x03\x02\x02\x02\u0102\u0103\x05D#\x02\u0103\'" +
		"\x03\x02\x02\x02\u0104\u0105\x071\x02\x02\u0105\u010A\x05\x0E\b\x02\u0106" +
		"\u0107\x075\x02\x02\u0107\u0109\x05\x0E\b\x02\u0108\u0106\x03\x02\x02" +
		"\x02\u0109\u010C\x03\x02\x02\x02\u010A\u0108\x03\x02\x02\x02\u010A\u010B" +
		"\x03\x02\x02\x02\u010B\u010D\x03\x02\x02\x02\u010C\u010A\x03\x02\x02\x02" +
		"\u010D\u010E\x072\x02\x02\u010E\u0111\x03\x02\x02\x02\u010F\u0111\x05" +
		"\x0E\b\x02\u0110\u0104\x03\x02\x02\x02\u0110\u010F\x03\x02\x02\x02\u0111" +
		")\x03\x02\x02\x02\u0112\u0114\x05D#\x02\u0113\u0115\x05,\x17\x02\u0114" +
		"\u0113\x03\x02\x02\x02\u0114\u0115\x03\x02\x02\x02\u0115\u011A\x03\x02" +
		"\x02\x02\u0116\u0117\x056\x1C\x02\u0117\u0118\x05,\x17\x02\u0118\u011A" +
		"\x03\x02\x02\x02\u0119\u0112\x03\x02\x02\x02\u0119\u0116\x03\x02\x02\x02" +
		"\u011A+\x03\x02\x02\x02\u011B\u011C\x076\x02\x02\u011C\u011E\x05D#\x02" +
		"\u011D\u011B\x03\x02\x02\x02\u011E\u011F\x03\x02\x02\x02\u011F\u011D\x03" +
		"\x02\x02\x02\u011F\u0120\x03\x02\x02\x02\u0120-\x03\x02\x02\x02\u0121" +
		"\u012D\x07\x07\x02\x02\u0122\u012D\x07\x06\x02\x02\u0123\u012D\x050\x19" +
		"\x02\u0124\u012D\x07#\x02\x02\u0125\u012D\x07\x0F\x02\x02\u0126\u012D" +
		"\x058\x1D\x02\u0127\u012D\x05:\x1E\x02\u0128\u012D\x05<\x1F\x02\u0129" +
		"\u012D\x05B\"\x02\u012A\u012D\x052\x1A\x02\u012B\u012D\x054\x1B\x02\u012C" +
		"\u0121\x03\x02\x02\x02\u012C\u0122\x03\x02\x02\x02\u012C\u0123\x03\x02" +
		"\x02\x02\u012C\u0124\x03\x02\x02\x02\u012C\u0125\x03\x02\x02\x02\u012C" +
		"\u0126\x03\x02\x02\x02\u012C\u0127\x03\x02\x02\x02\u012C\u0128\x03\x02" +
		"\x02\x02\u012C\u0129\x03\x02\x02\x02\u012C\u012A\x03\x02\x02\x02\u012C" +
		"\u012B\x03\x02\x02\x02\u012D/\x03\x02\x02\x02\u012E\u0132\x07\x04\x02" +
		"\x02\u012F\u0131\x07>\x02\x02\u0130\u012F\x03\x02\x02\x02\u0131\u0134" +
		"\x03\x02\x02\x02\u0132\u0130\x03\x02\x02\x02\u0132\u0133\x03\x02\x02\x02" +
		"\u0133\u0135\x03\x02\x02\x02\u0134\u0132\x03\x02\x02\x02\u0135\u013F\x07" +
		"?\x02\x02\u0136\u013A\x07\x05\x02\x02\u0137\u0139\x07@\x02\x02\u0138\u0137" +
		"\x03\x02\x02\x02\u0139\u013C\x03\x02\x02\x02\u013A\u0138\x03\x02\x02\x02" +
		"\u013A\u013B\x03\x02\x02\x02\u013B\u013D\x03\x02\x02\x02\u013C\u013A\x03" +
		"\x02\x02\x02\u013D\u013F\x07A\x02\x02\u013E\u012E\x03\x02\x02\x02\u013E" +
		"\u0136\x03\x02\x02\x02\u013F1\x03\x02\x02\x02\u0140\u0149\x073\x02\x02" +
		"\u0141\u0146\x05.\x18\x02\u0142\u0143\x075\x02\x02\u0143\u0145\x05.\x18" +
		"\x02\u0144\u0142\x03\x02\x02\x02\u0145\u0148\x03\x02\x02\x02\u0146\u0144" +
		"\x03\x02\x02\x02\u0146\u0147\x03\x02\x02\x02\u0147\u014A\x03\x02\x02\x02" +
		"\u0148\u0146\x03\x02\x02\x02\u0149\u0141\x03\x02\x02\x02\u0149\u014A\x03" +
		"\x02\x02\x02\u014A\u014B\x03\x02\x02\x02\u014B\u014C\x074\x02\x02\u014C" +
		"3\x03\x02\x02\x02\u014D\u014E\x05D#\x02\u014E\u0156\x071\x02\x02\u014F" +
		"\u0150\x05D#\x02\u0150\u0151\x07*\x02\x02\u0151\u0152\x05\x12\n\x02\u0152" +
		"\u0153\x075\x02\x02\u0153\u0155\x03\x02\x02\x02\u0154\u014F\x03\x02\x02" +
		"\x02\u0155\u0158\x03\x02\x02\x02\u0156\u0154\x03\x02\x02\x02\u0156\u0157" +
		"\x03\x02\x02\x02\u0157\u0159\x03\x02\x02\x02\u0158\u0156\x03\x02\x02\x02" +
		"\u0159\u015A\x05D#\x02\u015A\u015B\x07*\x02\x02\u015B\u015C\x05\x12\n" +
		"\x02\u015C\u015D\x072\x02\x02\u015D5\x03\x02\x02\x02\u015E\u015F\x05D" +
		"#\x02\u015F\u016E\x071\x02\x02\u0160\u0161\x05D#\x02\u0161\u0162\x07*" +
		"\x02\x02\u0162\u0163\x05\x12\n\x02\u0163\u0164\x075\x02\x02\u0164\u0166" +
		"\x03\x02\x02\x02\u0165\u0160\x03\x02\x02\x02\u0166\u0169\x03\x02\x02\x02" +
		"\u0167\u0165\x03\x02\x02\x02\u0167\u0168\x03\x02\x02\x02\u0168\u016A\x03" +
		"\x02\x02\x02\u0169\u0167\x03\x02\x02\x02\u016A\u016B\x05D#\x02\u016B\u016C" +
		"\x07*\x02\x02\u016C\u016D\x05\x12\n\x02\u016D\u016F\x03\x02\x02\x02\u016E" +
		"\u0167\x03\x02\x02\x02\u016E\u016F\x03\x02\x02\x02\u016F\u0170\x03\x02" +
		"\x02\x02\u0170\u0171\x072\x02\x02\u0171\u0182\x03\x02\x02\x02\u0172\u0173" +
		"\x05D#\x02\u0173\u017D\x071\x02\x02\u0174\u0175\x05\x12\n\x02\u0175\u0176" +
		"\x075\x02\x02\u0176\u0178\x03\x02\x02\x02\u0177\u0174\x03\x02\x02\x02" +
		"\u0178\u017B\x03\x02\x02\x02\u0179\u0177\x03\x02\x02\x02\u0179\u017A\x03" +
		"\x02\x02\x02\u017A\u017C\x03\x02\x02\x02\u017B\u0179\x03\x02\x02\x02\u017C" +
		"\u017E\x05\x12\n\x02\u017D\u0179\x03\x02\x02\x02\u017D\u017E\x03\x02\x02" +
		"\x02\u017E\u017F\x03\x02\x02\x02\u017F\u0180\x072\x02\x02\u0180\u0182" +
		"\x03\x02\x02\x02\u0181\u015E\x03\x02\x02\x02\u0181\u0172\x03\x02\x02\x02" +
		"\u01827\x03\x02\x02\x02\u0183\u0184\x07\v\x02\x02\u0184\u0185\x071\x02" +
		"\x02\u0185\u0186\x05> \x02\u0186\u0187\x072\x02\x02\u01879\x03\x02\x02" +
		"\x02\u0188\u0189\x07!\x02\x02\u0189\u018A\x071\x02\x02\u018A\u018D\x05" +
		"@!\x02\u018B\u018C\x076\x02\x02\u018C\u018E\t\x06\x02\x02\u018D\u018B" +
		"\x03\x02\x02\x02\u018D\u018E\x03\x02\x02\x02\u018E\u018F\x03\x02\x02\x02" +
		"\u018F\u0190\x072\x02\x02\u0190;\x03\x02\x02\x02\u0191\u0192\x07\"\x02" +
		"\x02\u0192\u0193\x071\x02\x02\u0193\u0199\x05> \x02\u0194\u0197\x05@!" +
		"\x02\u0195\u0196\x076\x02\x02\u0196\u0198\t\x06\x02\x02\u0197\u0195\x03" +
		"\x02\x02\x02\u0197\u0198\x03\x02\x02\x02\u0198\u019A\x03\x02\x02\x02\u0199" +
		"\u0194\x03\x02\x02\x02\u0199\u019A\x03\x02\x02\x02\u019A\u019B\x03\x02" +
		"\x02\x02\u019B\u019C\x072\x02\x02\u019C=\x03\x02\x02\x02\u019D\u019E\x07" +
		"\x06\x02\x02\u019E\u019F\x07-\x02\x02\u019F\u01A0\t\x06\x02\x02\u01A0" +
		"\u01A1\x07-\x02\x02\u01A1\u01A2\t\x06\x02\x02\u01A2?\x03\x02\x02\x02\u01A3" +
		"\u01A4\t\x06\x02\x02\u01A4\u01A5\x077\x02\x02\u01A5\u01A6\t\x06\x02\x02" +
		"\u01A6\u01A7\x077\x02\x02\u01A7\u01A8\t\x06\x02\x02\u01A8A\x03\x02\x02" +
		"\x02\u01A9\u01F2\x07\x14\x02\x02\u01AA\u01AB\x07\x06\x02\x02\u01AB\u01AE" +
		"\x07%\x02\x02\u01AC\u01AD\x07\x06\x02\x02\u01AD\u01AF\x07\x19\x02\x02" +
		"\u01AE\u01AC\x03\x02\x02\x02\u01AE\u01AF\x03\x02\x02\x02\u01AF\u01B2\x03" +
		"\x02\x02\x02\u01B0\u01B1\x07\x06\x02\x02\u01B1\u01B3\x07\f\x02\x02\u01B2" +
		"\u01B0\x03\x02\x02\x02\u01B2\u01B3\x03\x02\x02\x02\u01B3\u01B6\x03\x02" +
		"\x02\x02\u01B4\u01B5\x07\x06\x02\x02\u01B5\u01B7\x07\x12\x02\x02\u01B6" +
		"\u01B4\x03\x02\x02\x02\u01B6\u01B7\x03\x02\x02\x02\u01B7\u01BA\x03\x02" +
		"\x02\x02\u01B8\u01B9\x07\x06\x02\x02\u01B9\u01BB\x07\x18\x02\x02\u01BA" +
		"\u01B8\x03\x02\x02\x02\u01BA\u01BB\x03\x02\x02\x02\u01BB\u01BE\x03\x02" +
		"\x02\x02\u01BC\u01BD\x07\x06\x02\x02\u01BD\u01BF\x07\x1F\x02\x02\u01BE" +
		"\u01BC\x03\x02\x02\x02\u01BE\u01BF\x03\x02\x02\x02\u01BF\u01F3\x03\x02" +
		"\x02\x02\u01C0\u01C1\x07\x06\x02\x02\u01C1\u01C4\x07\x19\x02\x02\u01C2" +
		"\u01C3\x07\x06\x02\x02\u01C3\u01C5\x07\f\x02\x02\u01C4\u01C2\x03\x02\x02" +
		"\x02\u01C4\u01C5\x03\x02\x02\x02\u01C5\u01C8\x03\x02\x02\x02\u01C6\u01C7" +
		"\x07\x06\x02\x02\u01C7\u01C9\x07\x12\x02\x02\u01C8\u01C6\x03\x02\x02\x02" +
		"\u01C8\u01C9\x03\x02\x02\x02\u01C9\u01CC\x03\x02\x02\x02\u01CA\u01CB\x07" +
		"\x06\x02\x02\u01CB\u01CD\x07\x18\x02\x02\u01CC\u01CA\x03\x02\x02\x02\u01CC" +
		"\u01CD\x03\x02\x02\x02\u01CD\u01D0\x03\x02\x02\x02\u01CE\u01CF\x07\x06" +
		"\x02\x02\u01CF\u01D1\x07\x1F\x02\x02\u01D0\u01CE\x03\x02\x02\x02\u01D0" +
		"\u01D1\x03\x02\x02\x02\u01D1\u01F3\x03\x02\x02\x02\u01D2\u01D3\x07\x06" +
		"\x02\x02\u01D3\u01D6\x07\f\x02\x02\u01D4\u01D5\x07\x06\x02\x02\u01D5\u01D7" +
		"\x07\x12\x02\x02\u01D6\u01D4\x03\x02\x02\x02\u01D6\u01D7\x03\x02\x02\x02" +
		"\u01D7\u01DA\x03\x02\x02\x02\u01D8\u01D9\x07\x06\x02\x02\u01D9\u01DB\x07" +
		"\x18\x02\x02\u01DA\u01D8\x03\x02\x02\x02\u01DA\u01DB\x03\x02\x02\x02\u01DB" +
		"\u01DE\x03\x02\x02\x02\u01DC\u01DD\x07\x06\x02\x02\u01DD\u01DF\x07\x1F" +
		"\x02\x02\u01DE\u01DC\x03\x02\x02\x02\u01DE\u01DF\x03\x02\x02\x02\u01DF" +
		"\u01F3\x03\x02\x02\x02\u01E0\u01E1\x07\x06\x02\x02\u01E1\u01E4\x07\x12" +
		"\x02\x02\u01E2\u01E3\x07\x06\x02\x02\u01E3\u01E5\x07\x18\x02\x02\u01E4" +
		"\u01E2\x03\x02\x02\x02\u01E4\u01E5\x03\x02\x02\x02\u01E5\u01E8\x03\x02" +
		"\x02\x02\u01E6\u01E7\x07\x06\x02\x02\u01E7\u01E9\x07\x1F\x02\x02\u01E8" +
		"\u01E6\x03\x02\x02\x02\u01E8\u01E9\x03\x02\x02\x02\u01E9\u01F3\x03\x02" +
		"\x02\x02\u01EA\u01EB\x07\x06\x02\x02\u01EB\u01EE\x07\x18\x02\x02\u01EC" +
		"\u01ED\x07\x06\x02\x02\u01ED\u01EF\x07\x1F\x02\x02\u01EE\u01EC\x03\x02" +
		"\x02\x02\u01EE\u01EF\x03\x02\x02\x02\u01EF\u01F3\x03\x02\x02\x02\u01F0" +
		"\u01F1\x07\x06\x02\x02\u01F1\u01F3\x07\x1F\x02\x02\u01F2\u01AA\x03\x02" +
		"\x02\x02\u01F2\u01C0\x03\x02\x02\x02\u01F2\u01D2\x03\x02\x02\x02\u01F2" +
		"\u01E0\x03\x02\x02\x02\u01F2\u01EA\x03\x02\x02\x02\u01F2\u01F0\x03\x02" +
		"\x02\x02\u01F3C\x03\x02\x02\x02\u01F4\u01F5\t\x07\x02\x02\u01F5E\x03\x02" +
		"\x02\x02<P\\^ltv\x83\x8A\xA8\xAF\xB8\xC0\xC2\xC7\xCA\xCE\xD2\xD9\xE2\xEC" +
		"\xF3\u0100\u010A\u0110\u0114\u0119\u011F\u012C\u0132\u013A\u013E\u0146" +
		"\u0149\u0156\u0167\u016E\u0179\u017D\u0181\u018D\u0197\u0199\u01AE\u01B2" +
		"\u01B6\u01BA\u01BE\u01C4\u01C8\u01CC\u01D0\u01D6\u01DA\u01DE\u01E4\u01E8" +
		"\u01EE\u01F2";
	public static __ATN: ATN;
	public static get _ATN(): ATN {
		if (!BlazeExpressionParser.__ATN) {
			BlazeExpressionParser.__ATN = new ATNDeserializer().deserialize(Utils.toCharArray(BlazeExpressionParser._serializedATN));
		}

		return BlazeExpressionParser.__ATN;
	}

}

export class ParsePredicateContext extends ParserRuleContext {
	public predicate(): PredicateContext {
		return this.getRuleContext(0, PredicateContext);
	}
	public EOF(): TerminalNode { return this.getToken(BlazeExpressionParser.EOF, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_parsePredicate; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterParsePredicate) {
			listener.enterParsePredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitParsePredicate) {
			listener.exitParsePredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitParsePredicate) {
			return visitor.visitParsePredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class ParseExpressionContext extends ParserRuleContext {
	public expression(): ExpressionContext {
		return this.getRuleContext(0, ExpressionContext);
	}
	public EOF(): TerminalNode { return this.getToken(BlazeExpressionParser.EOF, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_parseExpression; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterParseExpression) {
			listener.enterParseExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitParseExpression) {
			listener.exitParseExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitParseExpression) {
			return visitor.visitParseExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class ParseExpressionOrPredicateContext extends ParserRuleContext {
	public predicateOrExpression(): PredicateOrExpressionContext {
		return this.getRuleContext(0, PredicateOrExpressionContext);
	}
	public EOF(): TerminalNode { return this.getToken(BlazeExpressionParser.EOF, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_parseExpressionOrPredicate; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterParseExpressionOrPredicate) {
			listener.enterParseExpressionOrPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitParseExpressionOrPredicate) {
			listener.exitParseExpressionOrPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitParseExpressionOrPredicate) {
			return visitor.visitParseExpressionOrPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class ParseTemplateContext extends ParserRuleContext {
	public EOF(): TerminalNode { return this.getToken(BlazeExpressionParser.EOF, 0); }
	public template(): TemplateContext | undefined {
		return this.tryGetRuleContext(0, TemplateContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_parseTemplate; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterParseTemplate) {
			listener.enterParseTemplate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitParseTemplate) {
			listener.exitParseTemplate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitParseTemplate) {
			return visitor.visitParseTemplate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class ParseQueryContext extends ParserRuleContext {
	public query(): QueryContext {
		return this.getRuleContext(0, QueryContext);
	}
	public EOF(): TerminalNode { return this.getToken(BlazeExpressionParser.EOF, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_parseQuery; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterParseQuery) {
			listener.enterParseQuery(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitParseQuery) {
			listener.exitParseQuery(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitParseQuery) {
			return visitor.visitParseQuery(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class TemplateContext extends ParserRuleContext {
	public TEXT(): TerminalNode[];
	public TEXT(i: number): TerminalNode;
	public TEXT(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.TEXT);
		} else {
			return this.getToken(BlazeExpressionParser.TEXT, i);
		}
	}
	public EXPRESSION_START(): TerminalNode[];
	public EXPRESSION_START(i: number): TerminalNode;
	public EXPRESSION_START(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.EXPRESSION_START);
		} else {
			return this.getToken(BlazeExpressionParser.EXPRESSION_START, i);
		}
	}
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	public EXPRESSION_END(): TerminalNode[];
	public EXPRESSION_END(i: number): TerminalNode;
	public EXPRESSION_END(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.EXPRESSION_END);
		} else {
			return this.getToken(BlazeExpressionParser.EXPRESSION_END, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_template; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterTemplate) {
			listener.enterTemplate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitTemplate) {
			listener.exitTemplate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitTemplate) {
			return visitor.visitTemplate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class ExpressionContext extends ParserRuleContext {
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_expression; }
	public copyFrom(ctx: ExpressionContext): void {
		super.copyFrom(ctx);
	}
}
export class GroupedExpressionContext extends ExpressionContext {
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public expression(): ExpressionContext {
		return this.getRuleContext(0, ExpressionContext);
	}
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterGroupedExpression) {
			listener.enterGroupedExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitGroupedExpression) {
			listener.exitGroupedExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitGroupedExpression) {
			return visitor.visitGroupedExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class LiteralExpressionContext extends ExpressionContext {
	public literal(): LiteralContext {
		return this.getRuleContext(0, LiteralContext);
	}
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterLiteralExpression) {
			listener.enterLiteralExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitLiteralExpression) {
			listener.exitLiteralExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitLiteralExpression) {
			return visitor.visitLiteralExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class PathExpressionContext extends ExpressionContext {
	public path(): PathContext {
		return this.getRuleContext(0, PathContext);
	}
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterPathExpression) {
			listener.enterPathExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitPathExpression) {
			listener.exitPathExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitPathExpression) {
			return visitor.visitPathExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class FunctionExpressionContext extends ExpressionContext {
	public functionInvocation(): FunctionInvocationContext {
		return this.getRuleContext(0, FunctionInvocationContext);
	}
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterFunctionExpression) {
			listener.enterFunctionExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitFunctionExpression) {
			listener.exitFunctionExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitFunctionExpression) {
			return visitor.visitFunctionExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class UnaryMinusExpressionContext extends ExpressionContext {
	public MINUS(): TerminalNode { return this.getToken(BlazeExpressionParser.MINUS, 0); }
	public expression(): ExpressionContext {
		return this.getRuleContext(0, ExpressionContext);
	}
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterUnaryMinusExpression) {
			listener.enterUnaryMinusExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitUnaryMinusExpression) {
			listener.exitUnaryMinusExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitUnaryMinusExpression) {
			return visitor.visitUnaryMinusExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class UnaryPlusExpressionContext extends ExpressionContext {
	public PLUS(): TerminalNode { return this.getToken(BlazeExpressionParser.PLUS, 0); }
	public expression(): ExpressionContext {
		return this.getRuleContext(0, ExpressionContext);
	}
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterUnaryPlusExpression) {
			listener.enterUnaryPlusExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitUnaryPlusExpression) {
			listener.exitUnaryPlusExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitUnaryPlusExpression) {
			return visitor.visitUnaryPlusExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class MultiplicativeExpressionContext extends ExpressionContext {
	public _lhs!: ExpressionContext;
	public _op!: Token;
	public _rhs!: ExpressionContext;
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	public ASTERISK(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.ASTERISK, 0); }
	public SLASH(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.SLASH, 0); }
	public PERCENT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.PERCENT, 0); }
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterMultiplicativeExpression) {
			listener.enterMultiplicativeExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitMultiplicativeExpression) {
			listener.exitMultiplicativeExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitMultiplicativeExpression) {
			return visitor.visitMultiplicativeExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class AdditiveExpressionContext extends ExpressionContext {
	public _lhs!: ExpressionContext;
	public _op!: Token;
	public _rhs!: ExpressionContext;
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	public PLUS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.PLUS, 0); }
	public MINUS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.MINUS, 0); }
	constructor(ctx: ExpressionContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterAdditiveExpression) {
			listener.enterAdditiveExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitAdditiveExpression) {
			listener.exitAdditiveExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitAdditiveExpression) {
			return visitor.visitAdditiveExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class PredicateContext extends ParserRuleContext {
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_predicate; }
	public copyFrom(ctx: PredicateContext): void {
		super.copyFrom(ctx);
	}
}
export class GroupedPredicateContext extends PredicateContext {
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public predicate(): PredicateContext {
		return this.getRuleContext(0, PredicateContext);
	}
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterGroupedPredicate) {
			listener.enterGroupedPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitGroupedPredicate) {
			listener.exitGroupedPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitGroupedPredicate) {
			return visitor.visitGroupedPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class NegatedPredicateContext extends PredicateContext {
	public predicate(): PredicateContext {
		return this.getRuleContext(0, PredicateContext);
	}
	public NOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NOT, 0); }
	public EXCLAMATION_MARK(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.EXCLAMATION_MARK, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterNegatedPredicate) {
			listener.enterNegatedPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitNegatedPredicate) {
			listener.exitNegatedPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitNegatedPredicate) {
			return visitor.visitNegatedPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class AndPredicateContext extends PredicateContext {
	public predicate(): PredicateContext[];
	public predicate(i: number): PredicateContext;
	public predicate(i?: number): PredicateContext | PredicateContext[] {
		if (i === undefined) {
			return this.getRuleContexts(PredicateContext);
		} else {
			return this.getRuleContext(i, PredicateContext);
		}
	}
	public AND(): TerminalNode { return this.getToken(BlazeExpressionParser.AND, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterAndPredicate) {
			listener.enterAndPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitAndPredicate) {
			listener.exitAndPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitAndPredicate) {
			return visitor.visitAndPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class OrPredicateContext extends PredicateContext {
	public predicate(): PredicateContext[];
	public predicate(i: number): PredicateContext;
	public predicate(i?: number): PredicateContext | PredicateContext[] {
		if (i === undefined) {
			return this.getRuleContexts(PredicateContext);
		} else {
			return this.getRuleContext(i, PredicateContext);
		}
	}
	public OR(): TerminalNode { return this.getToken(BlazeExpressionParser.OR, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterOrPredicate) {
			listener.enterOrPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitOrPredicate) {
			listener.exitOrPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitOrPredicate) {
			return visitor.visitOrPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class IsNullPredicateContext extends PredicateContext {
	public expression(): ExpressionContext {
		return this.getRuleContext(0, ExpressionContext);
	}
	public IS(): TerminalNode { return this.getToken(BlazeExpressionParser.IS, 0); }
	public NULL(): TerminalNode { return this.getToken(BlazeExpressionParser.NULL, 0); }
	public NOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NOT, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterIsNullPredicate) {
			listener.enterIsNullPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitIsNullPredicate) {
			listener.exitIsNullPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitIsNullPredicate) {
			return visitor.visitIsNullPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class IsEmptyPredicateContext extends PredicateContext {
	public expression(): ExpressionContext {
		return this.getRuleContext(0, ExpressionContext);
	}
	public IS(): TerminalNode { return this.getToken(BlazeExpressionParser.IS, 0); }
	public EMPTY(): TerminalNode { return this.getToken(BlazeExpressionParser.EMPTY, 0); }
	public NOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NOT, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterIsEmptyPredicate) {
			listener.enterIsEmptyPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitIsEmptyPredicate) {
			listener.exitIsEmptyPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitIsEmptyPredicate) {
			return visitor.visitIsEmptyPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class EqualityPredicateContext extends PredicateContext {
	public _lhs!: ExpressionContext;
	public _rhs!: ExpressionContext;
	public EQUAL(): TerminalNode { return this.getToken(BlazeExpressionParser.EQUAL, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterEqualityPredicate) {
			listener.enterEqualityPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitEqualityPredicate) {
			listener.exitEqualityPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitEqualityPredicate) {
			return visitor.visitEqualityPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class InequalityPredicateContext extends PredicateContext {
	public _lhs!: ExpressionContext;
	public _rhs!: ExpressionContext;
	public NOT_EQUAL(): TerminalNode { return this.getToken(BlazeExpressionParser.NOT_EQUAL, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterInequalityPredicate) {
			listener.enterInequalityPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitInequalityPredicate) {
			listener.exitInequalityPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitInequalityPredicate) {
			return visitor.visitInequalityPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class GreaterThanPredicateContext extends PredicateContext {
	public _lhs!: ExpressionContext;
	public _rhs!: ExpressionContext;
	public GREATER(): TerminalNode { return this.getToken(BlazeExpressionParser.GREATER, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterGreaterThanPredicate) {
			listener.enterGreaterThanPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitGreaterThanPredicate) {
			listener.exitGreaterThanPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitGreaterThanPredicate) {
			return visitor.visitGreaterThanPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class GreaterThanOrEqualPredicateContext extends PredicateContext {
	public _lhs!: ExpressionContext;
	public _rhs!: ExpressionContext;
	public GREATER_EQUAL(): TerminalNode { return this.getToken(BlazeExpressionParser.GREATER_EQUAL, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterGreaterThanOrEqualPredicate) {
			listener.enterGreaterThanOrEqualPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitGreaterThanOrEqualPredicate) {
			listener.exitGreaterThanOrEqualPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitGreaterThanOrEqualPredicate) {
			return visitor.visitGreaterThanOrEqualPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class LessThanPredicateContext extends PredicateContext {
	public _lhs!: ExpressionContext;
	public _rhs!: ExpressionContext;
	public LESS(): TerminalNode { return this.getToken(BlazeExpressionParser.LESS, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterLessThanPredicate) {
			listener.enterLessThanPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitLessThanPredicate) {
			listener.exitLessThanPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitLessThanPredicate) {
			return visitor.visitLessThanPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class LessThanOrEqualPredicateContext extends PredicateContext {
	public _lhs!: ExpressionContext;
	public _rhs!: ExpressionContext;
	public LESS_EQUAL(): TerminalNode { return this.getToken(BlazeExpressionParser.LESS_EQUAL, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterLessThanOrEqualPredicate) {
			listener.enterLessThanOrEqualPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitLessThanOrEqualPredicate) {
			listener.exitLessThanOrEqualPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitLessThanOrEqualPredicate) {
			return visitor.visitLessThanOrEqualPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class InPredicateContext extends PredicateContext {
	public expression(): ExpressionContext {
		return this.getRuleContext(0, ExpressionContext);
	}
	public IN(): TerminalNode { return this.getToken(BlazeExpressionParser.IN, 0); }
	public inList(): InListContext {
		return this.getRuleContext(0, InListContext);
	}
	public NOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NOT, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterInPredicate) {
			listener.enterInPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitInPredicate) {
			listener.exitInPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitInPredicate) {
			return visitor.visitInPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class BetweenPredicateContext extends PredicateContext {
	public _lhs!: ExpressionContext;
	public _begin!: ExpressionContext;
	public _end!: ExpressionContext;
	public BETWEEN(): TerminalNode { return this.getToken(BlazeExpressionParser.BETWEEN, 0); }
	public AND(): TerminalNode { return this.getToken(BlazeExpressionParser.AND, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	public NOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NOT, 0); }
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterBetweenPredicate) {
			listener.enterBetweenPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitBetweenPredicate) {
			listener.exitBetweenPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitBetweenPredicate) {
			return visitor.visitBetweenPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class BooleanFunctionContext extends PredicateContext {
	public functionInvocation(): FunctionInvocationContext {
		return this.getRuleContext(0, FunctionInvocationContext);
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterBooleanFunction) {
			listener.enterBooleanFunction(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitBooleanFunction) {
			listener.exitBooleanFunction(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitBooleanFunction) {
			return visitor.visitBooleanFunction(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class PathPredicateContext extends PredicateContext {
	public path(): PathContext {
		return this.getRuleContext(0, PathContext);
	}
	constructor(ctx: PredicateContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterPathPredicate) {
			listener.enterPathPredicate(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitPathPredicate) {
			listener.exitPathPredicate(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitPathPredicate) {
			return visitor.visitPathPredicate(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class PredicateOrExpressionContext extends ParserRuleContext {
	public expression(): ExpressionContext | undefined {
		return this.tryGetRuleContext(0, ExpressionContext);
	}
	public predicate(): PredicateContext | undefined {
		return this.tryGetRuleContext(0, PredicateContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_predicateOrExpression; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterPredicateOrExpression) {
			listener.enterPredicateOrExpression(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitPredicateOrExpression) {
			listener.exitPredicateOrExpression(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitPredicateOrExpression) {
			return visitor.visitPredicateOrExpression(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class QueryContext extends ParserRuleContext {
	public fromClause(): FromClauseContext {
		return this.getRuleContext(0, FromClauseContext);
	}
	public selectClause(): SelectClauseContext | undefined {
		return this.tryGetRuleContext(0, SelectClauseContext);
	}
	public whereClause(): WhereClauseContext | undefined {
		return this.tryGetRuleContext(0, WhereClauseContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_query; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterQuery) {
			listener.enterQuery(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitQuery) {
			listener.exitQuery(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitQuery) {
			return visitor.visitQuery(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class SelectClauseContext extends ParserRuleContext {
	public SELECT(): TerminalNode { return this.getToken(BlazeExpressionParser.SELECT, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	public DISTINCT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.DISTINCT, 0); }
	public COMMA(): TerminalNode[];
	public COMMA(i: number): TerminalNode;
	public COMMA(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COMMA);
		} else {
			return this.getToken(BlazeExpressionParser.COMMA, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_selectClause; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterSelectClause) {
			listener.enterSelectClause(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitSelectClause) {
			listener.exitSelectClause(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitSelectClause) {
			return visitor.visitSelectClause(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class FromClauseContext extends ParserRuleContext {
	public FROM(): TerminalNode { return this.getToken(BlazeExpressionParser.FROM, 0); }
	public fromItem(): FromItemContext[];
	public fromItem(i: number): FromItemContext;
	public fromItem(i?: number): FromItemContext | FromItemContext[] {
		if (i === undefined) {
			return this.getRuleContexts(FromItemContext);
		} else {
			return this.getRuleContext(i, FromItemContext);
		}
	}
	public COMMA(): TerminalNode[];
	public COMMA(i: number): TerminalNode;
	public COMMA(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COMMA);
		} else {
			return this.getToken(BlazeExpressionParser.COMMA, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_fromClause; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterFromClause) {
			listener.enterFromClause(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitFromClause) {
			listener.exitFromClause(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitFromClause) {
			return visitor.visitFromClause(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class WhereClauseContext extends ParserRuleContext {
	public WHERE(): TerminalNode { return this.getToken(BlazeExpressionParser.WHERE, 0); }
	public predicate(): PredicateContext {
		return this.getRuleContext(0, PredicateContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_whereClause; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterWhereClause) {
			listener.enterWhereClause(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitWhereClause) {
			listener.exitWhereClause(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitWhereClause) {
			return visitor.visitWhereClause(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class FromItemContext extends ParserRuleContext {
	public fromRoot(): FromRootContext {
		return this.getRuleContext(0, FromRootContext);
	}
	public join(): JoinContext[];
	public join(i: number): JoinContext;
	public join(i?: number): JoinContext | JoinContext[] {
		if (i === undefined) {
			return this.getRuleContexts(JoinContext);
		} else {
			return this.getRuleContext(i, JoinContext);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_fromItem; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterFromItem) {
			listener.enterFromItem(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitFromItem) {
			listener.exitFromItem(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitFromItem) {
			return visitor.visitFromItem(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class FromRootContext extends ParserRuleContext {
	public domainTypeName(): DomainTypeNameContext {
		return this.getRuleContext(0, DomainTypeNameContext);
	}
	public variable(): VariableContext {
		return this.getRuleContext(0, VariableContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_fromRoot; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterFromRoot) {
			listener.enterFromRoot(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitFromRoot) {
			listener.exitFromRoot(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitFromRoot) {
			return visitor.visitFromRoot(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class JoinContext extends ParserRuleContext {
	public JOIN(): TerminalNode { return this.getToken(BlazeExpressionParser.JOIN, 0); }
	public joinTarget(): JoinTargetContext {
		return this.getRuleContext(0, JoinTargetContext);
	}
	public ON(): TerminalNode { return this.getToken(BlazeExpressionParser.ON, 0); }
	public predicate(): PredicateContext {
		return this.getRuleContext(0, PredicateContext);
	}
	public LEFT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.LEFT, 0); }
	public RIGHT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.RIGHT, 0); }
	public FULL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.FULL, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_join; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterJoin) {
			listener.enterJoin(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitJoin) {
			listener.exitJoin(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitJoin) {
			return visitor.visitJoin(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class JoinTargetContext extends ParserRuleContext {
	public domainTypeName(): DomainTypeNameContext {
		return this.getRuleContext(0, DomainTypeNameContext);
	}
	public variable(): VariableContext {
		return this.getRuleContext(0, VariableContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_joinTarget; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterJoinTarget) {
			listener.enterJoinTarget(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitJoinTarget) {
			listener.exitJoinTarget(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitJoinTarget) {
			return visitor.visitJoinTarget(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class DomainTypeNameContext extends ParserRuleContext {
	public identifier(): IdentifierContext {
		return this.getRuleContext(0, IdentifierContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_domainTypeName; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterDomainTypeName) {
			listener.enterDomainTypeName(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitDomainTypeName) {
			listener.exitDomainTypeName(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitDomainTypeName) {
			return visitor.visitDomainTypeName(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class VariableContext extends ParserRuleContext {
	public identifier(): IdentifierContext {
		return this.getRuleContext(0, IdentifierContext);
	}
	public AS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.AS, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_variable; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterVariable) {
			listener.enterVariable(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitVariable) {
			listener.exitVariable(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitVariable) {
			return visitor.visitVariable(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class InListContext extends ParserRuleContext {
	public LP(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.LP, 0); }
	public expression(): ExpressionContext[];
	public expression(i: number): ExpressionContext;
	public expression(i?: number): ExpressionContext | ExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(ExpressionContext);
		} else {
			return this.getRuleContext(i, ExpressionContext);
		}
	}
	public RP(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.RP, 0); }
	public COMMA(): TerminalNode[];
	public COMMA(i: number): TerminalNode;
	public COMMA(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COMMA);
		} else {
			return this.getToken(BlazeExpressionParser.COMMA, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_inList; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterInList) {
			listener.enterInList(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitInList) {
			listener.exitInList(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitInList) {
			return visitor.visitInList(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class PathContext extends ParserRuleContext {
	public identifier(): IdentifierContext | undefined {
		return this.tryGetRuleContext(0, IdentifierContext);
	}
	public pathAttributes(): PathAttributesContext | undefined {
		return this.tryGetRuleContext(0, PathAttributesContext);
	}
	public functionInvocation(): FunctionInvocationContext | undefined {
		return this.tryGetRuleContext(0, FunctionInvocationContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_path; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterPath) {
			listener.enterPath(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitPath) {
			listener.exitPath(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitPath) {
			return visitor.visitPath(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class PathAttributesContext extends ParserRuleContext {
	public DOT(): TerminalNode[];
	public DOT(i: number): TerminalNode;
	public DOT(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.DOT);
		} else {
			return this.getToken(BlazeExpressionParser.DOT, i);
		}
	}
	public identifier(): IdentifierContext[];
	public identifier(i: number): IdentifierContext;
	public identifier(i?: number): IdentifierContext | IdentifierContext[] {
		if (i === undefined) {
			return this.getRuleContexts(IdentifierContext);
		} else {
			return this.getRuleContext(i, IdentifierContext);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_pathAttributes; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterPathAttributes) {
			listener.enterPathAttributes(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitPathAttributes) {
			listener.exitPathAttributes(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitPathAttributes) {
			return visitor.visitPathAttributes(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class LiteralContext extends ParserRuleContext {
	public NUMERIC_LITERAL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NUMERIC_LITERAL, 0); }
	public INTEGER_LITERAL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.INTEGER_LITERAL, 0); }
	public stringLiteral(): StringLiteralContext | undefined {
		return this.tryGetRuleContext(0, StringLiteralContext);
	}
	public TRUE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.TRUE, 0); }
	public FALSE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.FALSE, 0); }
	public dateLiteral(): DateLiteralContext | undefined {
		return this.tryGetRuleContext(0, DateLiteralContext);
	}
	public timeLiteral(): TimeLiteralContext | undefined {
		return this.tryGetRuleContext(0, TimeLiteralContext);
	}
	public timestampLiteral(): TimestampLiteralContext | undefined {
		return this.tryGetRuleContext(0, TimestampLiteralContext);
	}
	public temporalIntervalLiteral(): TemporalIntervalLiteralContext | undefined {
		return this.tryGetRuleContext(0, TemporalIntervalLiteralContext);
	}
	public collectionLiteral(): CollectionLiteralContext | undefined {
		return this.tryGetRuleContext(0, CollectionLiteralContext);
	}
	public entityLiteral(): EntityLiteralContext | undefined {
		return this.tryGetRuleContext(0, EntityLiteralContext);
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_literal; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterLiteral) {
			listener.enterLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitLiteral) {
			listener.exitLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitLiteral) {
			return visitor.visitLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class StringLiteralContext extends ParserRuleContext {
	public START_QUOTE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.START_QUOTE, 0); }
	public END_QUOTE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.END_QUOTE, 0); }
	public TEXT_IN_QUOTE(): TerminalNode[];
	public TEXT_IN_QUOTE(i: number): TerminalNode;
	public TEXT_IN_QUOTE(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.TEXT_IN_QUOTE);
		} else {
			return this.getToken(BlazeExpressionParser.TEXT_IN_QUOTE, i);
		}
	}
	public START_DOUBLE_QUOTE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.START_DOUBLE_QUOTE, 0); }
	public END_DOUBLE_QUOTE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.END_DOUBLE_QUOTE, 0); }
	public TEXT_IN_DOUBLE_QUOTE(): TerminalNode[];
	public TEXT_IN_DOUBLE_QUOTE(i: number): TerminalNode;
	public TEXT_IN_DOUBLE_QUOTE(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.TEXT_IN_DOUBLE_QUOTE);
		} else {
			return this.getToken(BlazeExpressionParser.TEXT_IN_DOUBLE_QUOTE, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_stringLiteral; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterStringLiteral) {
			listener.enterStringLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitStringLiteral) {
			listener.exitStringLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitStringLiteral) {
			return visitor.visitStringLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class CollectionLiteralContext extends ParserRuleContext {
	public LB(): TerminalNode { return this.getToken(BlazeExpressionParser.LB, 0); }
	public RB(): TerminalNode { return this.getToken(BlazeExpressionParser.RB, 0); }
	public literal(): LiteralContext[];
	public literal(i: number): LiteralContext;
	public literal(i?: number): LiteralContext | LiteralContext[] {
		if (i === undefined) {
			return this.getRuleContexts(LiteralContext);
		} else {
			return this.getRuleContext(i, LiteralContext);
		}
	}
	public COMMA(): TerminalNode[];
	public COMMA(i: number): TerminalNode;
	public COMMA(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COMMA);
		} else {
			return this.getToken(BlazeExpressionParser.COMMA, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_collectionLiteral; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterCollectionLiteral) {
			listener.enterCollectionLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitCollectionLiteral) {
			listener.exitCollectionLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitCollectionLiteral) {
			return visitor.visitCollectionLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class EntityLiteralContext extends ParserRuleContext {
	public _name!: IdentifierContext;
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public identifier(): IdentifierContext[];
	public identifier(i: number): IdentifierContext;
	public identifier(i?: number): IdentifierContext | IdentifierContext[] {
		if (i === undefined) {
			return this.getRuleContexts(IdentifierContext);
		} else {
			return this.getRuleContext(i, IdentifierContext);
		}
	}
	public EQUAL(): TerminalNode[];
	public EQUAL(i: number): TerminalNode;
	public EQUAL(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.EQUAL);
		} else {
			return this.getToken(BlazeExpressionParser.EQUAL, i);
		}
	}
	public predicateOrExpression(): PredicateOrExpressionContext[];
	public predicateOrExpression(i: number): PredicateOrExpressionContext;
	public predicateOrExpression(i?: number): PredicateOrExpressionContext | PredicateOrExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(PredicateOrExpressionContext);
		} else {
			return this.getRuleContext(i, PredicateOrExpressionContext);
		}
	}
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	public COMMA(): TerminalNode[];
	public COMMA(i: number): TerminalNode;
	public COMMA(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COMMA);
		} else {
			return this.getToken(BlazeExpressionParser.COMMA, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_entityLiteral; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterEntityLiteral) {
			listener.enterEntityLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitEntityLiteral) {
			listener.exitEntityLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitEntityLiteral) {
			return visitor.visitEntityLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class FunctionInvocationContext extends ParserRuleContext {
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_functionInvocation; }
	public copyFrom(ctx: FunctionInvocationContext): void {
		super.copyFrom(ctx);
	}
}
export class NamedInvocationContext extends FunctionInvocationContext {
	public _name!: IdentifierContext;
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	public identifier(): IdentifierContext[];
	public identifier(i: number): IdentifierContext;
	public identifier(i?: number): IdentifierContext | IdentifierContext[] {
		if (i === undefined) {
			return this.getRuleContexts(IdentifierContext);
		} else {
			return this.getRuleContext(i, IdentifierContext);
		}
	}
	public EQUAL(): TerminalNode[];
	public EQUAL(i: number): TerminalNode;
	public EQUAL(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.EQUAL);
		} else {
			return this.getToken(BlazeExpressionParser.EQUAL, i);
		}
	}
	public predicateOrExpression(): PredicateOrExpressionContext[];
	public predicateOrExpression(i: number): PredicateOrExpressionContext;
	public predicateOrExpression(i?: number): PredicateOrExpressionContext | PredicateOrExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(PredicateOrExpressionContext);
		} else {
			return this.getRuleContext(i, PredicateOrExpressionContext);
		}
	}
	public COMMA(): TerminalNode[];
	public COMMA(i: number): TerminalNode;
	public COMMA(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COMMA);
		} else {
			return this.getToken(BlazeExpressionParser.COMMA, i);
		}
	}
	constructor(ctx: FunctionInvocationContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterNamedInvocation) {
			listener.enterNamedInvocation(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitNamedInvocation) {
			listener.exitNamedInvocation(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitNamedInvocation) {
			return visitor.visitNamedInvocation(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}
export class IndexedFunctionInvocationContext extends FunctionInvocationContext {
	public _name!: IdentifierContext;
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	public identifier(): IdentifierContext {
		return this.getRuleContext(0, IdentifierContext);
	}
	public predicateOrExpression(): PredicateOrExpressionContext[];
	public predicateOrExpression(i: number): PredicateOrExpressionContext;
	public predicateOrExpression(i?: number): PredicateOrExpressionContext | PredicateOrExpressionContext[] {
		if (i === undefined) {
			return this.getRuleContexts(PredicateOrExpressionContext);
		} else {
			return this.getRuleContext(i, PredicateOrExpressionContext);
		}
	}
	public COMMA(): TerminalNode[];
	public COMMA(i: number): TerminalNode;
	public COMMA(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COMMA);
		} else {
			return this.getToken(BlazeExpressionParser.COMMA, i);
		}
	}
	constructor(ctx: FunctionInvocationContext) {
		super(ctx.parent, ctx.invokingState);
		this.copyFrom(ctx);
	}
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterIndexedFunctionInvocation) {
			listener.enterIndexedFunctionInvocation(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitIndexedFunctionInvocation) {
			listener.exitIndexedFunctionInvocation(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitIndexedFunctionInvocation) {
			return visitor.visitIndexedFunctionInvocation(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class DateLiteralContext extends ParserRuleContext {
	public DATE(): TerminalNode { return this.getToken(BlazeExpressionParser.DATE, 0); }
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public datePart(): DatePartContext {
		return this.getRuleContext(0, DatePartContext);
	}
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_dateLiteral; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterDateLiteral) {
			listener.enterDateLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitDateLiteral) {
			listener.exitDateLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitDateLiteral) {
			return visitor.visitDateLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class TimeLiteralContext extends ParserRuleContext {
	public _fraction!: Token;
	public TIME(): TerminalNode { return this.getToken(BlazeExpressionParser.TIME, 0); }
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public timePart(): TimePartContext {
		return this.getRuleContext(0, TimePartContext);
	}
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	public DOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.DOT, 0); }
	public INTEGER_LITERAL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.INTEGER_LITERAL, 0); }
	public LEADING_ZERO_INTEGER_LITERAL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_timeLiteral; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterTimeLiteral) {
			listener.enterTimeLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitTimeLiteral) {
			listener.exitTimeLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitTimeLiteral) {
			return visitor.visitTimeLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class TimestampLiteralContext extends ParserRuleContext {
	public _fraction!: Token;
	public TIMESTAMP(): TerminalNode { return this.getToken(BlazeExpressionParser.TIMESTAMP, 0); }
	public LP(): TerminalNode { return this.getToken(BlazeExpressionParser.LP, 0); }
	public datePart(): DatePartContext {
		return this.getRuleContext(0, DatePartContext);
	}
	public RP(): TerminalNode { return this.getToken(BlazeExpressionParser.RP, 0); }
	public timePart(): TimePartContext | undefined {
		return this.tryGetRuleContext(0, TimePartContext);
	}
	public DOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.DOT, 0); }
	public INTEGER_LITERAL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.INTEGER_LITERAL, 0); }
	public LEADING_ZERO_INTEGER_LITERAL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_timestampLiteral; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterTimestampLiteral) {
			listener.enterTimestampLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitTimestampLiteral) {
			listener.exitTimestampLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitTimestampLiteral) {
			return visitor.visitTimestampLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class DatePartContext extends ParserRuleContext {
	public INTEGER_LITERAL(): TerminalNode[];
	public INTEGER_LITERAL(i: number): TerminalNode;
	public INTEGER_LITERAL(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.INTEGER_LITERAL);
		} else {
			return this.getToken(BlazeExpressionParser.INTEGER_LITERAL, i);
		}
	}
	public MINUS(): TerminalNode[];
	public MINUS(i: number): TerminalNode;
	public MINUS(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.MINUS);
		} else {
			return this.getToken(BlazeExpressionParser.MINUS, i);
		}
	}
	public LEADING_ZERO_INTEGER_LITERAL(): TerminalNode[];
	public LEADING_ZERO_INTEGER_LITERAL(i: number): TerminalNode;
	public LEADING_ZERO_INTEGER_LITERAL(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL);
		} else {
			return this.getToken(BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_datePart; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterDatePart) {
			listener.enterDatePart(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitDatePart) {
			listener.exitDatePart(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitDatePart) {
			return visitor.visitDatePart(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class TimePartContext extends ParserRuleContext {
	public COLON(): TerminalNode[];
	public COLON(i: number): TerminalNode;
	public COLON(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.COLON);
		} else {
			return this.getToken(BlazeExpressionParser.COLON, i);
		}
	}
	public INTEGER_LITERAL(): TerminalNode[];
	public INTEGER_LITERAL(i: number): TerminalNode;
	public INTEGER_LITERAL(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.INTEGER_LITERAL);
		} else {
			return this.getToken(BlazeExpressionParser.INTEGER_LITERAL, i);
		}
	}
	public LEADING_ZERO_INTEGER_LITERAL(): TerminalNode[];
	public LEADING_ZERO_INTEGER_LITERAL(i: number): TerminalNode;
	public LEADING_ZERO_INTEGER_LITERAL(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL);
		} else {
			return this.getToken(BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_timePart; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterTimePart) {
			listener.enterTimePart(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitTimePart) {
			listener.exitTimePart(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitTimePart) {
			return visitor.visitTimePart(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class TemporalIntervalLiteralContext extends ParserRuleContext {
	public _years!: Token;
	public _months!: Token;
	public _days!: Token;
	public _hours!: Token;
	public _minutes!: Token;
	public _seconds!: Token;
	public INTERVAL(): TerminalNode { return this.getToken(BlazeExpressionParser.INTERVAL, 0); }
	public YEARS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.YEARS, 0); }
	public MONTHS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.MONTHS, 0); }
	public DAYS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.DAYS, 0); }
	public HOURS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.HOURS, 0); }
	public MINUTES(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.MINUTES, 0); }
	public SECONDS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.SECONDS, 0); }
	public INTEGER_LITERAL(): TerminalNode[];
	public INTEGER_LITERAL(i: number): TerminalNode;
	public INTEGER_LITERAL(i?: number): TerminalNode | TerminalNode[] {
		if (i === undefined) {
			return this.getTokens(BlazeExpressionParser.INTEGER_LITERAL);
		} else {
			return this.getToken(BlazeExpressionParser.INTEGER_LITERAL, i);
		}
	}
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_temporalIntervalLiteral; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterTemporalIntervalLiteral) {
			listener.enterTemporalIntervalLiteral(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitTemporalIntervalLiteral) {
			listener.exitTemporalIntervalLiteral(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitTemporalIntervalLiteral) {
			return visitor.visitTemporalIntervalLiteral(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


export class IdentifierContext extends ParserRuleContext {
	public IDENTIFIER(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.IDENTIFIER, 0); }
	public QUOTED_IDENTIFIER(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.QUOTED_IDENTIFIER, 0); }
	public AND(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.AND, 0); }
	public BETWEEN(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.BETWEEN, 0); }
	public DATE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.DATE, 0); }
	public DAYS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.DAYS, 0); }
	public DISTINCT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.DISTINCT, 0); }
	public FROM(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.FROM, 0); }
	public FULL(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.FULL, 0); }
	public HOURS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.HOURS, 0); }
	public IN(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.IN, 0); }
	public IS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.IS, 0); }
	public JOIN(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.JOIN, 0); }
	public LEFT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.LEFT, 0); }
	public MINUTES(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.MINUTES, 0); }
	public MONTHS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.MONTHS, 0); }
	public NOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NOT, 0); }
	public ON(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.ON, 0); }
	public OR(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.OR, 0); }
	public RIGHT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.RIGHT, 0); }
	public SECONDS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.SECONDS, 0); }
	public SELECT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.SELECT, 0); }
	public TIME(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.TIME, 0); }
	public TIMESTAMP(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.TIMESTAMP, 0); }
	public WHERE(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.WHERE, 0); }
	public YEARS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.YEARS, 0); }
	constructor(parent: ParserRuleContext | undefined, invokingState: number) {
		super(parent, invokingState);
	}
	// @Override
	public get ruleIndex(): number { return BlazeExpressionParser.RULE_identifier; }
	// @Override
	public enterRule(listener: BlazeExpressionParserListener): void {
		if (listener.enterIdentifier) {
			listener.enterIdentifier(this);
		}
	}
	// @Override
	public exitRule(listener: BlazeExpressionParserListener): void {
		if (listener.exitIdentifier) {
			listener.exitIdentifier(this);
		}
	}
	// @Override
	public accept<Result>(visitor: BlazeExpressionParserVisitor<Result>): Result {
		if (visitor.visitIdentifier) {
			return visitor.visitIdentifier(this);
		} else {
			return visitor.visitChildren(this);
		}
	}
}


