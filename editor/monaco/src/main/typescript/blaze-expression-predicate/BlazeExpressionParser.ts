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
	public static readonly EMPTY = 11;
	public static readonly FALSE = 12;
	public static readonly HOURS = 13;
	public static readonly IN = 14;
	public static readonly INTERVAL = 15;
	public static readonly IS = 16;
	public static readonly MINUTES = 17;
	public static readonly MONTHS = 18;
	public static readonly NOT = 19;
	public static readonly NULL = 20;
	public static readonly OR = 21;
	public static readonly SECONDS = 22;
	public static readonly TIME = 23;
	public static readonly TIMESTAMP = 24;
	public static readonly TRUE = 25;
	public static readonly YEARS = 26;
	public static readonly LESS = 27;
	public static readonly LESS_EQUAL = 28;
	public static readonly GREATER = 29;
	public static readonly GREATER_EQUAL = 30;
	public static readonly EQUAL = 31;
	public static readonly NOT_EQUAL = 32;
	public static readonly PLUS = 33;
	public static readonly MINUS = 34;
	public static readonly ASTERISK = 35;
	public static readonly SLASH = 36;
	public static readonly PERCENT = 37;
	public static readonly LP = 38;
	public static readonly RP = 39;
	public static readonly LB = 40;
	public static readonly RB = 41;
	public static readonly COMMA = 42;
	public static readonly DOT = 43;
	public static readonly COLON = 44;
	public static readonly EXCLAMATION_MARK = 45;
	public static readonly IDENTIFIER = 46;
	public static readonly QUOTED_IDENTIFIER = 47;
	public static readonly EXPRESSION_END = 48;
	public static readonly EXPRESSION_START = 49;
	public static readonly TEXT = 50;
	public static readonly TEXT_IN_QUOTE = 51;
	public static readonly END_QUOTE = 52;
	public static readonly TEXT_IN_DOUBLE_QUOTE = 53;
	public static readonly END_DOUBLE_QUOTE = 54;
	public static readonly RULE_parsePredicate = 0;
	public static readonly RULE_parseExpression = 1;
	public static readonly RULE_parseExpressionOrPredicate = 2;
	public static readonly RULE_parseTemplate = 3;
	public static readonly RULE_template = 4;
	public static readonly RULE_expression = 5;
	public static readonly RULE_predicate = 6;
	public static readonly RULE_predicateOrExpression = 7;
	public static readonly RULE_inList = 8;
	public static readonly RULE_path = 9;
	public static readonly RULE_pathAttributes = 10;
	public static readonly RULE_literal = 11;
	public static readonly RULE_stringLiteral = 12;
	public static readonly RULE_collectionLiteral = 13;
	public static readonly RULE_entityLiteral = 14;
	public static readonly RULE_functionInvocation = 15;
	public static readonly RULE_dateLiteral = 16;
	public static readonly RULE_timeLiteral = 17;
	public static readonly RULE_timestampLiteral = 18;
	public static readonly RULE_datePart = 19;
	public static readonly RULE_timePart = 20;
	public static readonly RULE_temporalIntervalLiteral = 21;
	public static readonly RULE_identifier = 22;
	// tslint:disable:no-trailing-whitespace
	public static readonly ruleNames: string[] = [
		"parsePredicate", "parseExpression", "parseExpressionOrPredicate", "parseTemplate", 
		"template", "expression", "predicate", "predicateOrExpression", "inList", 
		"path", "pathAttributes", "literal", "stringLiteral", "collectionLiteral", 
		"entityLiteral", "functionInvocation", "dateLiteral", "timeLiteral", "timestampLiteral", 
		"datePart", "timePart", "temporalIntervalLiteral", "identifier",
	];

	private static readonly _LITERAL_NAMES: Array<string | undefined> = [
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, undefined, undefined, undefined, undefined, undefined, undefined, 
		undefined, undefined, undefined, undefined, undefined, undefined, "'<'", 
		"'<='", "'>'", "'>='", "'='", undefined, "'+'", "'-'", "'*'", "'/'", "'%'", 
		"'('", "')'", "'['", "']'", "','", "'.'", "':'", "'!'", undefined, undefined, 
		"'}'", "'#{'",
	];
	private static readonly _SYMBOLIC_NAMES: Array<string | undefined> = [
		undefined, "WS", "START_QUOTE", "START_DOUBLE_QUOTE", "INTEGER_LITERAL", 
		"NUMERIC_LITERAL", "LEADING_ZERO_INTEGER_LITERAL", "AND", "BETWEEN", "DATE", 
		"DAYS", "EMPTY", "FALSE", "HOURS", "IN", "INTERVAL", "IS", "MINUTES", 
		"MONTHS", "NOT", "NULL", "OR", "SECONDS", "TIME", "TIMESTAMP", "TRUE", 
		"YEARS", "LESS", "LESS_EQUAL", "GREATER", "GREATER_EQUAL", "EQUAL", "NOT_EQUAL", 
		"PLUS", "MINUS", "ASTERISK", "SLASH", "PERCENT", "LP", "RP", "LB", "RB", 
		"COMMA", "DOT", "COLON", "EXCLAMATION_MARK", "IDENTIFIER", "QUOTED_IDENTIFIER", 
		"EXPRESSION_END", "EXPRESSION_START", "TEXT", "TEXT_IN_QUOTE", "END_QUOTE", 
		"TEXT_IN_DOUBLE_QUOTE", "END_DOUBLE_QUOTE",
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
			this.state = 46;
			this.predicate(0);
			this.state = 47;
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
			this.state = 49;
			this.expression(0);
			this.state = 50;
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
			this.state = 52;
			this.predicateOrExpression();
			this.state = 53;
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
			this.state = 56;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.EXPRESSION_START || _la === BlazeExpressionParser.TEXT) {
				{
				this.state = 55;
				this.template();
				}
			}

			this.state = 58;
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
		this.enterRule(_localctx, 8, BlazeExpressionParser.RULE_template);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 65;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			do {
				{
				this.state = 65;
				this._errHandler.sync(this);
				switch (this._input.LA(1)) {
				case BlazeExpressionParser.TEXT:
					{
					this.state = 60;
					this.match(BlazeExpressionParser.TEXT);
					}
					break;
				case BlazeExpressionParser.EXPRESSION_START:
					{
					{
					this.state = 61;
					this.match(BlazeExpressionParser.EXPRESSION_START);
					this.state = 62;
					this.expression(0);
					this.state = 63;
					this.match(BlazeExpressionParser.EXPRESSION_END);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				this.state = 67;
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
		let _startState: number = 10;
		this.enterRecursionRule(_localctx, 10, BlazeExpressionParser.RULE_expression, _p);
		let _la: number;
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 81;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 3, this._ctx) ) {
			case 1:
				{
				_localctx = new GroupedExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;

				this.state = 70;
				this.match(BlazeExpressionParser.LP);
				this.state = 71;
				this.expression(0);
				this.state = 72;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				{
				_localctx = new LiteralExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 74;
				this.literal();
				}
				break;

			case 3:
				{
				_localctx = new PathExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 75;
				this.path();
				}
				break;

			case 4:
				{
				_localctx = new FunctionExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 76;
				this.functionInvocation();
				}
				break;

			case 5:
				{
				_localctx = new UnaryMinusExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 77;
				this.match(BlazeExpressionParser.MINUS);
				this.state = 78;
				this.expression(4);
				}
				break;

			case 6:
				{
				_localctx = new UnaryPlusExpressionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 79;
				this.match(BlazeExpressionParser.PLUS);
				this.state = 80;
				this.expression(3);
				}
				break;
			}
			this._ctx._stop = this._input.tryLT(-1);
			this.state = 91;
			this._errHandler.sync(this);
			_alt = this.interpreter.adaptivePredict(this._input, 5, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = _localctx;
					{
					this.state = 89;
					this._errHandler.sync(this);
					switch ( this.interpreter.adaptivePredict(this._input, 4, this._ctx) ) {
					case 1:
						{
						_localctx = new MultiplicativeExpressionContext(new ExpressionContext(_parentctx, _parentState));
						(_localctx as MultiplicativeExpressionContext)._lhs = _prevctx;
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_expression);
						this.state = 83;
						if (!(this.precpred(this._ctx, 2))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 2)");
						}
						this.state = 84;
						(_localctx as MultiplicativeExpressionContext)._op = this._input.LT(1);
						_la = this._input.LA(1);
						if (!(((((_la - 35)) & ~0x1F) === 0 && ((1 << (_la - 35)) & ((1 << (BlazeExpressionParser.ASTERISK - 35)) | (1 << (BlazeExpressionParser.SLASH - 35)) | (1 << (BlazeExpressionParser.PERCENT - 35)))) !== 0))) {
							(_localctx as MultiplicativeExpressionContext)._op = this._errHandler.recoverInline(this);
						} else {
							if (this._input.LA(1) === Token.EOF) {
								this.matchedEOF = true;
							}

							this._errHandler.reportMatch(this);
							this.consume();
						}
						this.state = 85;
						(_localctx as MultiplicativeExpressionContext)._rhs = this.expression(3);
						}
						break;

					case 2:
						{
						_localctx = new AdditiveExpressionContext(new ExpressionContext(_parentctx, _parentState));
						(_localctx as AdditiveExpressionContext)._lhs = _prevctx;
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_expression);
						this.state = 86;
						if (!(this.precpred(this._ctx, 1))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 1)");
						}
						this.state = 87;
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
						this.state = 88;
						(_localctx as AdditiveExpressionContext)._rhs = this.expression(2);
						}
						break;
					}
					}
				}
				this.state = 93;
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
		let _startState: number = 12;
		this.enterRecursionRule(_localctx, 12, BlazeExpressionParser.RULE_predicate, _p);
		let _la: number;
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 157;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 10, this._ctx) ) {
			case 1:
				{
				_localctx = new GroupedPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;

				this.state = 95;
				this.match(BlazeExpressionParser.LP);
				this.state = 96;
				this.predicate(0);
				this.state = 97;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				{
				_localctx = new NegatedPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 99;
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
				this.state = 100;
				this.predicate(15);
				}
				break;

			case 3:
				{
				_localctx = new IsNullPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 101;
				this.expression(0);
				this.state = 102;
				this.match(BlazeExpressionParser.IS);
				this.state = 104;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 103;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 106;
				this.match(BlazeExpressionParser.NULL);
				}
				break;

			case 4:
				{
				_localctx = new IsEmptyPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 108;
				this.expression(0);
				this.state = 109;
				this.match(BlazeExpressionParser.IS);
				this.state = 111;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 110;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 113;
				this.match(BlazeExpressionParser.EMPTY);
				}
				break;

			case 5:
				{
				_localctx = new EqualityPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 115;
				(_localctx as EqualityPredicateContext)._lhs = this.expression(0);
				this.state = 116;
				this.match(BlazeExpressionParser.EQUAL);
				this.state = 117;
				(_localctx as EqualityPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 6:
				{
				_localctx = new InequalityPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 119;
				(_localctx as InequalityPredicateContext)._lhs = this.expression(0);
				this.state = 120;
				this.match(BlazeExpressionParser.NOT_EQUAL);
				this.state = 121;
				(_localctx as InequalityPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 7:
				{
				_localctx = new GreaterThanPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 123;
				(_localctx as GreaterThanPredicateContext)._lhs = this.expression(0);
				this.state = 124;
				this.match(BlazeExpressionParser.GREATER);
				this.state = 125;
				(_localctx as GreaterThanPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 8:
				{
				_localctx = new GreaterThanOrEqualPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 127;
				(_localctx as GreaterThanOrEqualPredicateContext)._lhs = this.expression(0);
				this.state = 128;
				this.match(BlazeExpressionParser.GREATER_EQUAL);
				this.state = 129;
				(_localctx as GreaterThanOrEqualPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 9:
				{
				_localctx = new LessThanPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 131;
				(_localctx as LessThanPredicateContext)._lhs = this.expression(0);
				this.state = 132;
				this.match(BlazeExpressionParser.LESS);
				this.state = 133;
				(_localctx as LessThanPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 10:
				{
				_localctx = new LessThanOrEqualPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 135;
				(_localctx as LessThanOrEqualPredicateContext)._lhs = this.expression(0);
				this.state = 136;
				this.match(BlazeExpressionParser.LESS_EQUAL);
				this.state = 137;
				(_localctx as LessThanOrEqualPredicateContext)._rhs = this.expression(0);
				}
				break;

			case 11:
				{
				_localctx = new InPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 139;
				this.expression(0);
				this.state = 141;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 140;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 143;
				this.match(BlazeExpressionParser.IN);
				this.state = 144;
				this.inList();
				}
				break;

			case 12:
				{
				_localctx = new BetweenPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 146;
				(_localctx as BetweenPredicateContext)._lhs = this.expression(0);
				this.state = 148;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.NOT) {
					{
					this.state = 147;
					this.match(BlazeExpressionParser.NOT);
					}
				}

				this.state = 150;
				this.match(BlazeExpressionParser.BETWEEN);
				this.state = 151;
				(_localctx as BetweenPredicateContext)._begin = this.expression(0);
				this.state = 152;
				this.match(BlazeExpressionParser.AND);
				this.state = 153;
				(_localctx as BetweenPredicateContext)._end = this.expression(0);
				}
				break;

			case 13:
				{
				_localctx = new BooleanFunctionContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 155;
				this.functionInvocation();
				}
				break;

			case 14:
				{
				_localctx = new PathPredicateContext(_localctx);
				this._ctx = _localctx;
				_prevctx = _localctx;
				this.state = 156;
				this.path();
				}
				break;
			}
			this._ctx._stop = this._input.tryLT(-1);
			this.state = 167;
			this._errHandler.sync(this);
			_alt = this.interpreter.adaptivePredict(this._input, 12, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					if (this._parseListeners != null) {
						this.triggerExitRuleEvent();
					}
					_prevctx = _localctx;
					{
					this.state = 165;
					this._errHandler.sync(this);
					switch ( this.interpreter.adaptivePredict(this._input, 11, this._ctx) ) {
					case 1:
						{
						_localctx = new AndPredicateContext(new PredicateContext(_parentctx, _parentState));
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_predicate);
						this.state = 159;
						if (!(this.precpred(this._ctx, 14))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 14)");
						}
						this.state = 160;
						this.match(BlazeExpressionParser.AND);
						this.state = 161;
						this.predicate(15);
						}
						break;

					case 2:
						{
						_localctx = new OrPredicateContext(new PredicateContext(_parentctx, _parentState));
						this.pushNewRecursionContext(_localctx, _startState, BlazeExpressionParser.RULE_predicate);
						this.state = 162;
						if (!(this.precpred(this._ctx, 13))) {
							throw this.createFailedPredicateException("this.precpred(this._ctx, 13)");
						}
						this.state = 163;
						this.match(BlazeExpressionParser.OR);
						this.state = 164;
						this.predicate(14);
						}
						break;
					}
					}
				}
				this.state = 169;
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
		this.enterRule(_localctx, 14, BlazeExpressionParser.RULE_predicateOrExpression);
		try {
			this.state = 172;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 13, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 170;
				this.expression(0);
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 171;
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
	public inList(): InListContext {
		let _localctx: InListContext = new InListContext(this._ctx, this.state);
		this.enterRule(_localctx, 16, BlazeExpressionParser.RULE_inList);
		let _la: number;
		try {
			this.state = 186;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 15, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 174;
				this.match(BlazeExpressionParser.LP);
				this.state = 175;
				this.expression(0);
				this.state = 180;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.COMMA) {
					{
					{
					this.state = 176;
					this.match(BlazeExpressionParser.COMMA);
					this.state = 177;
					this.expression(0);
					}
					}
					this.state = 182;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 183;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 185;
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
		this.enterRule(_localctx, 18, BlazeExpressionParser.RULE_path);
		try {
			this.state = 195;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 17, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 188;
				this.identifier();
				this.state = 190;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 16, this._ctx) ) {
				case 1:
					{
					this.state = 189;
					this.pathAttributes();
					}
					break;
				}
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 192;
				this.functionInvocation();
				this.state = 193;
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
		this.enterRule(_localctx, 20, BlazeExpressionParser.RULE_pathAttributes);
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 199;
			this._errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					this.state = 197;
					this.match(BlazeExpressionParser.DOT);
					this.state = 198;
					this.identifier();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				this.state = 201;
				this._errHandler.sync(this);
				_alt = this.interpreter.adaptivePredict(this._input, 18, this._ctx);
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
		this.enterRule(_localctx, 22, BlazeExpressionParser.RULE_literal);
		try {
			this.state = 214;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 19, this._ctx) ) {
			case 1:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 203;
				this.match(BlazeExpressionParser.NUMERIC_LITERAL);
				}
				break;

			case 2:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 204;
				this.match(BlazeExpressionParser.INTEGER_LITERAL);
				}
				break;

			case 3:
				this.enterOuterAlt(_localctx, 3);
				{
				this.state = 205;
				this.stringLiteral();
				}
				break;

			case 4:
				this.enterOuterAlt(_localctx, 4);
				{
				this.state = 206;
				this.match(BlazeExpressionParser.TRUE);
				}
				break;

			case 5:
				this.enterOuterAlt(_localctx, 5);
				{
				this.state = 207;
				this.match(BlazeExpressionParser.FALSE);
				}
				break;

			case 6:
				this.enterOuterAlt(_localctx, 6);
				{
				this.state = 208;
				this.dateLiteral();
				}
				break;

			case 7:
				this.enterOuterAlt(_localctx, 7);
				{
				this.state = 209;
				this.timeLiteral();
				}
				break;

			case 8:
				this.enterOuterAlt(_localctx, 8);
				{
				this.state = 210;
				this.timestampLiteral();
				}
				break;

			case 9:
				this.enterOuterAlt(_localctx, 9);
				{
				this.state = 211;
				this.temporalIntervalLiteral();
				}
				break;

			case 10:
				this.enterOuterAlt(_localctx, 10);
				{
				this.state = 212;
				this.collectionLiteral();
				}
				break;

			case 11:
				this.enterOuterAlt(_localctx, 11);
				{
				this.state = 213;
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
		this.enterRule(_localctx, 24, BlazeExpressionParser.RULE_stringLiteral);
		let _la: number;
		try {
			this.state = 232;
			this._errHandler.sync(this);
			switch (this._input.LA(1)) {
			case BlazeExpressionParser.START_QUOTE:
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 216;
				this.match(BlazeExpressionParser.START_QUOTE);
				this.state = 220;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.TEXT_IN_QUOTE) {
					{
					{
					this.state = 217;
					this.match(BlazeExpressionParser.TEXT_IN_QUOTE);
					}
					}
					this.state = 222;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 223;
				this.match(BlazeExpressionParser.END_QUOTE);
				}
				break;
			case BlazeExpressionParser.START_DOUBLE_QUOTE:
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 224;
				this.match(BlazeExpressionParser.START_DOUBLE_QUOTE);
				this.state = 228;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.TEXT_IN_DOUBLE_QUOTE) {
					{
					{
					this.state = 225;
					this.match(BlazeExpressionParser.TEXT_IN_DOUBLE_QUOTE);
					}
					}
					this.state = 230;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				this.state = 231;
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
		this.enterRule(_localctx, 26, BlazeExpressionParser.RULE_collectionLiteral);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 234;
			this.match(BlazeExpressionParser.LB);
			this.state = 243;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.START_QUOTE) | (1 << BlazeExpressionParser.START_DOUBLE_QUOTE) | (1 << BlazeExpressionParser.INTEGER_LITERAL) | (1 << BlazeExpressionParser.NUMERIC_LITERAL) | (1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.FALSE) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.INTERVAL) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.TIME) | (1 << BlazeExpressionParser.TIMESTAMP) | (1 << BlazeExpressionParser.TRUE) | (1 << BlazeExpressionParser.YEARS))) !== 0) || ((((_la - 40)) & ~0x1F) === 0 && ((1 << (_la - 40)) & ((1 << (BlazeExpressionParser.LB - 40)) | (1 << (BlazeExpressionParser.IDENTIFIER - 40)) | (1 << (BlazeExpressionParser.QUOTED_IDENTIFIER - 40)))) !== 0)) {
				{
				this.state = 235;
				this.literal();
				this.state = 240;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				while (_la === BlazeExpressionParser.COMMA) {
					{
					{
					this.state = 236;
					this.match(BlazeExpressionParser.COMMA);
					this.state = 237;
					this.literal();
					}
					}
					this.state = 242;
					this._errHandler.sync(this);
					_la = this._input.LA(1);
				}
				}
			}

			this.state = 245;
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
		this.enterRule(_localctx, 28, BlazeExpressionParser.RULE_entityLiteral);
		try {
			let _alt: number;
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 247;
			_localctx._name = this.identifier();
			this.state = 248;
			this.match(BlazeExpressionParser.LP);
			this.state = 256;
			this._errHandler.sync(this);
			_alt = this.interpreter.adaptivePredict(this._input, 25, this._ctx);
			while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
				if (_alt === 1) {
					{
					{
					this.state = 249;
					this.identifier();
					this.state = 250;
					this.match(BlazeExpressionParser.EQUAL);
					this.state = 251;
					this.predicateOrExpression();
					this.state = 252;
					this.match(BlazeExpressionParser.COMMA);
					}
					}
				}
				this.state = 258;
				this._errHandler.sync(this);
				_alt = this.interpreter.adaptivePredict(this._input, 25, this._ctx);
			}
			this.state = 259;
			this.identifier();
			this.state = 260;
			this.match(BlazeExpressionParser.EQUAL);
			this.state = 261;
			this.predicateOrExpression();
			this.state = 262;
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
		this.enterRule(_localctx, 30, BlazeExpressionParser.RULE_functionInvocation);
		let _la: number;
		try {
			let _alt: number;
			this.state = 299;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 30, this._ctx) ) {
			case 1:
				_localctx = new NamedInvocationContext(_localctx);
				this.enterOuterAlt(_localctx, 1);
				{
				this.state = 264;
				(_localctx as NamedInvocationContext)._name = this.identifier();
				this.state = 265;
				this.match(BlazeExpressionParser.LP);
				this.state = 280;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.TIME) | (1 << BlazeExpressionParser.TIMESTAMP) | (1 << BlazeExpressionParser.YEARS))) !== 0) || _la === BlazeExpressionParser.IDENTIFIER || _la === BlazeExpressionParser.QUOTED_IDENTIFIER) {
					{
					this.state = 273;
					this._errHandler.sync(this);
					_alt = this.interpreter.adaptivePredict(this._input, 26, this._ctx);
					while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
						if (_alt === 1) {
							{
							{
							this.state = 266;
							this.identifier();
							this.state = 267;
							this.match(BlazeExpressionParser.EQUAL);
							this.state = 268;
							this.predicateOrExpression();
							this.state = 269;
							this.match(BlazeExpressionParser.COMMA);
							}
							}
						}
						this.state = 275;
						this._errHandler.sync(this);
						_alt = this.interpreter.adaptivePredict(this._input, 26, this._ctx);
					}
					this.state = 276;
					this.identifier();
					this.state = 277;
					this.match(BlazeExpressionParser.EQUAL);
					this.state = 278;
					this.predicateOrExpression();
					}
				}

				this.state = 282;
				this.match(BlazeExpressionParser.RP);
				}
				break;

			case 2:
				_localctx = new IndexedFunctionInvocationContext(_localctx);
				this.enterOuterAlt(_localctx, 2);
				{
				this.state = 284;
				(_localctx as IndexedFunctionInvocationContext)._name = this.identifier();
				this.state = 285;
				this.match(BlazeExpressionParser.LP);
				this.state = 295;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if ((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.START_QUOTE) | (1 << BlazeExpressionParser.START_DOUBLE_QUOTE) | (1 << BlazeExpressionParser.INTEGER_LITERAL) | (1 << BlazeExpressionParser.NUMERIC_LITERAL) | (1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.FALSE) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.INTERVAL) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.TIME) | (1 << BlazeExpressionParser.TIMESTAMP) | (1 << BlazeExpressionParser.TRUE) | (1 << BlazeExpressionParser.YEARS))) !== 0) || ((((_la - 33)) & ~0x1F) === 0 && ((1 << (_la - 33)) & ((1 << (BlazeExpressionParser.PLUS - 33)) | (1 << (BlazeExpressionParser.MINUS - 33)) | (1 << (BlazeExpressionParser.LP - 33)) | (1 << (BlazeExpressionParser.LB - 33)) | (1 << (BlazeExpressionParser.EXCLAMATION_MARK - 33)) | (1 << (BlazeExpressionParser.IDENTIFIER - 33)) | (1 << (BlazeExpressionParser.QUOTED_IDENTIFIER - 33)))) !== 0)) {
					{
					this.state = 291;
					this._errHandler.sync(this);
					_alt = this.interpreter.adaptivePredict(this._input, 28, this._ctx);
					while (_alt !== 2 && _alt !== ATN.INVALID_ALT_NUMBER) {
						if (_alt === 1) {
							{
							{
							this.state = 286;
							this.predicateOrExpression();
							this.state = 287;
							this.match(BlazeExpressionParser.COMMA);
							}
							}
						}
						this.state = 293;
						this._errHandler.sync(this);
						_alt = this.interpreter.adaptivePredict(this._input, 28, this._ctx);
					}
					this.state = 294;
					this.predicateOrExpression();
					}
				}

				this.state = 297;
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
		this.enterRule(_localctx, 32, BlazeExpressionParser.RULE_dateLiteral);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 301;
			this.match(BlazeExpressionParser.DATE);
			this.state = 302;
			this.match(BlazeExpressionParser.LP);
			this.state = 303;
			this.datePart();
			this.state = 304;
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
		this.enterRule(_localctx, 34, BlazeExpressionParser.RULE_timeLiteral);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 306;
			this.match(BlazeExpressionParser.TIME);
			this.state = 307;
			this.match(BlazeExpressionParser.LP);
			this.state = 308;
			this.timePart();
			this.state = 311;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.DOT) {
				{
				this.state = 309;
				this.match(BlazeExpressionParser.DOT);
				this.state = 310;
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

			this.state = 313;
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
		this.enterRule(_localctx, 36, BlazeExpressionParser.RULE_timestampLiteral);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 315;
			this.match(BlazeExpressionParser.TIMESTAMP);
			this.state = 316;
			this.match(BlazeExpressionParser.LP);
			this.state = 317;
			this.datePart();
			this.state = 323;
			this._errHandler.sync(this);
			_la = this._input.LA(1);
			if (_la === BlazeExpressionParser.INTEGER_LITERAL || _la === BlazeExpressionParser.LEADING_ZERO_INTEGER_LITERAL) {
				{
				this.state = 318;
				this.timePart();
				this.state = 321;
				this._errHandler.sync(this);
				_la = this._input.LA(1);
				if (_la === BlazeExpressionParser.DOT) {
					{
					this.state = 319;
					this.match(BlazeExpressionParser.DOT);
					this.state = 320;
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

			this.state = 325;
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
		this.enterRule(_localctx, 38, BlazeExpressionParser.RULE_datePart);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 327;
			this.match(BlazeExpressionParser.INTEGER_LITERAL);
			this.state = 328;
			this.match(BlazeExpressionParser.MINUS);
			this.state = 329;
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
			this.state = 330;
			this.match(BlazeExpressionParser.MINUS);
			this.state = 331;
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
		this.enterRule(_localctx, 40, BlazeExpressionParser.RULE_timePart);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 333;
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
			this.state = 334;
			this.match(BlazeExpressionParser.COLON);
			this.state = 335;
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
			this.state = 336;
			this.match(BlazeExpressionParser.COLON);
			this.state = 337;
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
		this.enterRule(_localctx, 42, BlazeExpressionParser.RULE_temporalIntervalLiteral);
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 339;
			this.match(BlazeExpressionParser.INTERVAL);
			this.state = 412;
			this._errHandler.sync(this);
			switch ( this.interpreter.adaptivePredict(this._input, 49, this._ctx) ) {
			case 1:
				{
				{
				this.state = 340;
				_localctx._years = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 341;
				this.match(BlazeExpressionParser.YEARS);
				this.state = 344;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 34, this._ctx) ) {
				case 1:
					{
					this.state = 342;
					_localctx._months = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 343;
					this.match(BlazeExpressionParser.MONTHS);
					}
					break;
				}
				this.state = 348;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 35, this._ctx) ) {
				case 1:
					{
					this.state = 346;
					_localctx._days = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 347;
					this.match(BlazeExpressionParser.DAYS);
					}
					break;
				}
				this.state = 352;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 36, this._ctx) ) {
				case 1:
					{
					this.state = 350;
					_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 351;
					this.match(BlazeExpressionParser.HOURS);
					}
					break;
				}
				this.state = 356;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 37, this._ctx) ) {
				case 1:
					{
					this.state = 354;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 355;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 360;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 38, this._ctx) ) {
				case 1:
					{
					this.state = 358;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 359;
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
				this.state = 362;
				_localctx._months = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 363;
				this.match(BlazeExpressionParser.MONTHS);
				this.state = 366;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 39, this._ctx) ) {
				case 1:
					{
					this.state = 364;
					_localctx._days = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 365;
					this.match(BlazeExpressionParser.DAYS);
					}
					break;
				}
				this.state = 370;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 40, this._ctx) ) {
				case 1:
					{
					this.state = 368;
					_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 369;
					this.match(BlazeExpressionParser.HOURS);
					}
					break;
				}
				this.state = 374;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 41, this._ctx) ) {
				case 1:
					{
					this.state = 372;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 373;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 378;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 42, this._ctx) ) {
				case 1:
					{
					this.state = 376;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 377;
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
				this.state = 380;
				_localctx._days = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 381;
				this.match(BlazeExpressionParser.DAYS);
				this.state = 384;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 43, this._ctx) ) {
				case 1:
					{
					this.state = 382;
					_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 383;
					this.match(BlazeExpressionParser.HOURS);
					}
					break;
				}
				this.state = 388;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 44, this._ctx) ) {
				case 1:
					{
					this.state = 386;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 387;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 392;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 45, this._ctx) ) {
				case 1:
					{
					this.state = 390;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 391;
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
				this.state = 394;
				_localctx._hours = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 395;
				this.match(BlazeExpressionParser.HOURS);
				this.state = 398;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 46, this._ctx) ) {
				case 1:
					{
					this.state = 396;
					_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 397;
					this.match(BlazeExpressionParser.MINUTES);
					}
					break;
				}
				this.state = 402;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 47, this._ctx) ) {
				case 1:
					{
					this.state = 400;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 401;
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
				this.state = 404;
				_localctx._minutes = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 405;
				this.match(BlazeExpressionParser.MINUTES);
				this.state = 408;
				this._errHandler.sync(this);
				switch ( this.interpreter.adaptivePredict(this._input, 48, this._ctx) ) {
				case 1:
					{
					this.state = 406;
					_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
					this.state = 407;
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
				this.state = 410;
				_localctx._seconds = this.match(BlazeExpressionParser.INTEGER_LITERAL);
				this.state = 411;
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
		this.enterRule(_localctx, 44, BlazeExpressionParser.RULE_identifier);
		let _la: number;
		try {
			this.enterOuterAlt(_localctx, 1);
			{
			this.state = 414;
			_la = this._input.LA(1);
			if (!((((_la) & ~0x1F) === 0 && ((1 << _la) & ((1 << BlazeExpressionParser.AND) | (1 << BlazeExpressionParser.BETWEEN) | (1 << BlazeExpressionParser.DATE) | (1 << BlazeExpressionParser.DAYS) | (1 << BlazeExpressionParser.HOURS) | (1 << BlazeExpressionParser.IN) | (1 << BlazeExpressionParser.IS) | (1 << BlazeExpressionParser.MINUTES) | (1 << BlazeExpressionParser.MONTHS) | (1 << BlazeExpressionParser.NOT) | (1 << BlazeExpressionParser.OR) | (1 << BlazeExpressionParser.SECONDS) | (1 << BlazeExpressionParser.TIME) | (1 << BlazeExpressionParser.TIMESTAMP) | (1 << BlazeExpressionParser.YEARS))) !== 0) || _la === BlazeExpressionParser.IDENTIFIER || _la === BlazeExpressionParser.QUOTED_IDENTIFIER)) {
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
		case 5:
			return this.expression_sempred(_localctx as ExpressionContext, predIndex);

		case 6:
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
		"\x03\uC91D\uCABA\u058D\uAFBA\u4F53\u0607\uEA8B\uC241\x038\u01A3\x04\x02" +
		"\t\x02\x04\x03\t\x03\x04\x04\t\x04\x04\x05\t\x05\x04\x06\t\x06\x04\x07" +
		"\t\x07\x04\b\t\b\x04\t\t\t\x04\n\t\n\x04\v\t\v\x04\f\t\f\x04\r\t\r\x04" +
		"\x0E\t\x0E\x04\x0F\t\x0F\x04\x10\t\x10\x04\x11\t\x11\x04\x12\t\x12\x04" +
		"\x13\t\x13\x04\x14\t\x14\x04\x15\t\x15\x04\x16\t\x16\x04\x17\t\x17\x04" +
		"\x18\t\x18\x03\x02\x03\x02\x03\x02\x03\x03\x03\x03\x03\x03\x03\x04\x03" +
		"\x04\x03\x04\x03\x05\x05\x05;\n\x05\x03\x05\x03\x05\x03\x06\x03\x06\x03" +
		"\x06\x03\x06\x03\x06\x06\x06D\n\x06\r\x06\x0E\x06E\x03\x07\x03\x07\x03" +
		"\x07\x03\x07\x03\x07\x03\x07\x03\x07\x03\x07\x03\x07\x03\x07\x03\x07\x03" +
		"\x07\x05\x07T\n\x07\x03\x07\x03\x07\x03\x07\x03\x07\x03\x07\x03\x07\x07" +
		"\x07\\\n\x07\f\x07\x0E\x07_\v\x07\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b" +
		"\x03\b\x03\b\x03\b\x03\b\x05\bk\n\b\x03\b\x03\b\x03\b\x03\b\x03\b\x05" +
		"\br\n\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03" +
		"\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03" +
		"\b\x03\b\x03\b\x03\b\x03\b\x03\b\x05\b\x90\n\b\x03\b\x03\b\x03\b\x03\b" +
		"\x03\b\x05\b\x97\n\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x05\b\xA0" +
		"\n\b\x03\b\x03\b\x03\b\x03\b\x03\b\x03\b\x07\b\xA8\n\b\f\b\x0E\b\xAB\v" +
		"\b\x03\t\x03\t\x05\t\xAF\n\t\x03\n\x03\n\x03\n\x03\n\x07\n\xB5\n\n\f\n" +
		"\x0E\n\xB8\v\n\x03\n\x03\n\x03\n\x05\n\xBD\n\n\x03\v\x03\v\x05\v\xC1\n" +
		"\v\x03\v\x03\v\x03\v\x05\v\xC6\n\v\x03\f\x03\f\x06\f\xCA\n\f\r\f\x0E\f" +
		"\xCB\x03\r\x03\r\x03\r\x03\r\x03\r\x03\r\x03\r\x03\r\x03\r\x03\r\x03\r" +
		"\x05\r\xD9\n\r\x03\x0E\x03\x0E\x07\x0E\xDD\n\x0E\f\x0E\x0E\x0E\xE0\v\x0E" +
		"\x03\x0E\x03\x0E\x03\x0E\x07\x0E\xE5\n\x0E\f\x0E\x0E\x0E\xE8\v\x0E\x03" +
		"\x0E\x05\x0E\xEB\n\x0E\x03\x0F\x03\x0F\x03\x0F\x03\x0F\x07\x0F\xF1\n\x0F" +
		"\f\x0F\x0E\x0F\xF4\v\x0F\x05\x0F\xF6\n\x0F\x03\x0F\x03\x0F\x03\x10\x03" +
		"\x10\x03\x10\x03\x10\x03\x10\x03\x10\x03\x10\x07\x10\u0101\n\x10\f\x10" +
		"\x0E\x10\u0104\v\x10\x03\x10\x03\x10\x03\x10\x03\x10\x03\x10\x03\x11\x03" +
		"\x11\x03\x11\x03\x11\x03\x11\x03\x11\x03\x11\x07\x11\u0112\n\x11\f\x11" +
		"\x0E\x11\u0115\v\x11\x03\x11\x03\x11\x03\x11\x03\x11\x05\x11\u011B\n\x11" +
		"\x03\x11\x03\x11\x03\x11\x03\x11\x03\x11\x03\x11\x03\x11\x07\x11\u0124" +
		"\n\x11\f\x11\x0E\x11\u0127\v\x11\x03\x11\x05\x11\u012A\n\x11\x03\x11\x03" +
		"\x11\x05\x11\u012E\n\x11\x03\x12\x03\x12\x03\x12\x03\x12\x03\x12\x03\x13" +
		"\x03\x13\x03\x13\x03\x13\x03\x13\x05\x13\u013A\n\x13\x03\x13\x03\x13\x03" +
		"\x14\x03\x14\x03\x14\x03\x14\x03\x14\x03\x14\x05\x14\u0144\n\x14\x05\x14" +
		"\u0146\n\x14\x03\x14\x03\x14\x03\x15\x03\x15\x03\x15\x03\x15\x03\x15\x03" +
		"\x15\x03\x16\x03\x16\x03\x16\x03\x16\x03\x16\x03\x16\x03\x17\x03\x17\x03" +
		"\x17\x03\x17\x03\x17\x05\x17\u015B\n\x17\x03\x17\x03\x17\x05\x17\u015F" +
		"\n\x17\x03\x17\x03\x17\x05\x17\u0163\n\x17\x03\x17\x03\x17\x05\x17\u0167" +
		"\n\x17\x03\x17\x03\x17\x05\x17\u016B\n\x17\x03\x17\x03\x17\x03\x17\x03" +
		"\x17\x05\x17\u0171\n\x17\x03\x17\x03\x17\x05\x17\u0175\n\x17\x03\x17\x03" +
		"\x17\x05\x17\u0179\n\x17\x03\x17\x03\x17\x05\x17\u017D\n\x17\x03\x17\x03" +
		"\x17\x03\x17\x03\x17\x05\x17\u0183\n\x17\x03\x17\x03\x17\x05\x17\u0187" +
		"\n\x17\x03\x17\x03\x17\x05\x17\u018B\n\x17\x03\x17\x03\x17\x03\x17\x03" +
		"\x17\x05\x17\u0191\n\x17\x03\x17\x03\x17\x05\x17\u0195\n\x17\x03\x17\x03" +
		"\x17\x03\x17\x03\x17\x05\x17\u019B\n\x17\x03\x17\x03\x17\x05\x17\u019F" +
		"\n\x17\x03\x18\x03\x18\x03\x18\x02\x02\x04\f\x0E\x19\x02\x02\x04\x02\x06" +
		"\x02\b\x02\n\x02\f\x02\x0E\x02\x10\x02\x12\x02\x14\x02\x16\x02\x18\x02" +
		"\x1A\x02\x1C\x02\x1E\x02 \x02\"\x02$\x02&\x02(\x02*\x02,\x02.\x02\x02" +
		"\x07\x03\x02%\'\x03\x02#$\x04\x02\x15\x15//\x04\x02\x06\x06\b\b\b\x02" +
		"\t\f\x0F\x10\x12\x15\x17\x1A\x1C\x1C01\x02\u01DA\x020\x03\x02\x02\x02" +
		"\x043\x03\x02\x02\x02\x066\x03\x02\x02\x02\b:\x03\x02\x02\x02\nC\x03\x02" +
		"\x02\x02\fS\x03\x02\x02\x02\x0E\x9F\x03\x02\x02\x02\x10\xAE\x03\x02\x02" +
		"\x02\x12\xBC\x03\x02\x02\x02\x14\xC5\x03\x02\x02\x02\x16\xC9\x03\x02\x02" +
		"\x02\x18\xD8\x03\x02\x02\x02\x1A\xEA\x03\x02\x02\x02\x1C\xEC\x03\x02\x02" +
		"\x02\x1E\xF9\x03\x02\x02\x02 \u012D\x03\x02\x02\x02\"\u012F\x03\x02\x02" +
		"\x02$\u0134\x03\x02\x02\x02&\u013D\x03\x02\x02\x02(\u0149\x03\x02\x02" +
		"\x02*\u014F\x03\x02\x02\x02,\u0155\x03\x02\x02\x02.\u01A0\x03\x02\x02" +
		"\x0201\x05\x0E\b\x0212\x07\x02\x02\x032\x03\x03\x02\x02\x0234\x05\f\x07" +
		"\x0245\x07\x02\x02\x035\x05\x03\x02\x02\x0267\x05\x10\t\x0278\x07\x02" +
		"\x02\x038\x07\x03\x02\x02\x029;\x05\n\x06\x02:9\x03\x02\x02\x02:;\x03" +
		"\x02\x02\x02;<\x03\x02\x02\x02<=\x07\x02\x02\x03=\t\x03\x02\x02\x02>D" +
		"\x074\x02\x02?@\x073\x02\x02@A\x05\f\x07\x02AB\x072\x02\x02BD\x03\x02" +
		"\x02\x02C>\x03\x02\x02\x02C?\x03\x02\x02\x02DE\x03\x02\x02\x02EC\x03\x02" +
		"\x02\x02EF\x03\x02\x02\x02F\v\x03\x02\x02\x02GH\b\x07\x01\x02HI\x07(\x02" +
		"\x02IJ\x05\f\x07\x02JK\x07)\x02\x02KT\x03\x02\x02\x02LT\x05\x18\r\x02" +
		"MT\x05\x14\v\x02NT\x05 \x11\x02OP\x07$\x02\x02PT\x05\f\x07\x06QR\x07#" +
		"\x02\x02RT\x05\f\x07\x05SG\x03\x02\x02\x02SL\x03\x02\x02\x02SM\x03\x02" +
		"\x02\x02SN\x03\x02\x02\x02SO\x03\x02\x02\x02SQ\x03\x02\x02\x02T]\x03\x02" +
		"\x02\x02UV\f\x04\x02\x02VW\t\x02\x02\x02W\\\x05\f\x07\x05XY\f\x03\x02" +
		"\x02YZ\t\x03\x02\x02Z\\\x05\f\x07\x04[U\x03\x02\x02\x02[X\x03\x02\x02" +
		"\x02\\_\x03\x02\x02\x02][\x03\x02\x02\x02]^\x03\x02\x02\x02^\r\x03\x02" +
		"\x02\x02_]\x03\x02\x02\x02`a\b\b\x01\x02ab\x07(\x02\x02bc\x05\x0E\b\x02" +
		"cd\x07)\x02\x02d\xA0\x03\x02\x02\x02ef\t\x04\x02\x02f\xA0\x05\x0E\b\x11" +
		"gh\x05\f\x07\x02hj\x07\x12\x02\x02ik\x07\x15\x02\x02ji\x03\x02\x02\x02" +
		"jk\x03\x02\x02\x02kl\x03\x02\x02\x02lm\x07\x16\x02\x02m\xA0\x03\x02\x02" +
		"\x02no\x05\f\x07\x02oq\x07\x12\x02\x02pr\x07\x15\x02\x02qp\x03\x02\x02" +
		"\x02qr\x03\x02\x02\x02rs\x03\x02\x02\x02st\x07\r\x02\x02t\xA0\x03\x02" +
		"\x02\x02uv\x05\f\x07\x02vw\x07!\x02\x02wx\x05\f\x07\x02x\xA0\x03\x02\x02" +
		"\x02yz\x05\f\x07\x02z{\x07\"\x02\x02{|\x05\f\x07\x02|\xA0\x03\x02\x02" +
		"\x02}~\x05\f\x07\x02~\x7F\x07\x1F\x02\x02\x7F\x80\x05\f\x07\x02\x80\xA0" +
		"\x03\x02\x02\x02\x81\x82\x05\f\x07\x02\x82\x83\x07 \x02\x02\x83\x84\x05" +
		"\f\x07\x02\x84\xA0\x03\x02\x02\x02\x85\x86\x05\f\x07\x02\x86\x87\x07\x1D" +
		"\x02\x02\x87\x88\x05\f\x07\x02\x88\xA0\x03\x02\x02\x02\x89\x8A\x05\f\x07" +
		"\x02\x8A\x8B\x07\x1E\x02\x02\x8B\x8C\x05\f\x07\x02\x8C\xA0\x03\x02\x02" +
		"\x02\x8D\x8F\x05\f\x07\x02\x8E\x90\x07\x15\x02\x02\x8F\x8E\x03\x02\x02" +
		"\x02\x8F\x90\x03\x02\x02\x02\x90\x91\x03\x02\x02\x02\x91\x92\x07\x10\x02" +
		"\x02\x92\x93\x05\x12\n\x02\x93\xA0\x03\x02\x02\x02\x94\x96\x05\f\x07\x02" +
		"\x95\x97\x07\x15\x02\x02\x96\x95\x03\x02\x02\x02\x96\x97\x03\x02\x02\x02" +
		"\x97\x98\x03\x02\x02\x02\x98\x99\x07\n\x02\x02\x99\x9A\x05\f\x07\x02\x9A" +
		"\x9B\x07\t\x02\x02\x9B\x9C\x05\f\x07\x02\x9C\xA0\x03\x02\x02\x02\x9D\xA0" +
		"\x05 \x11\x02\x9E\xA0\x05\x14\v\x02\x9F`\x03\x02\x02\x02\x9Fe\x03\x02" +
		"\x02\x02\x9Fg\x03\x02\x02\x02\x9Fn\x03\x02\x02\x02\x9Fu\x03\x02\x02\x02" +
		"\x9Fy\x03\x02\x02\x02\x9F}\x03\x02\x02\x02\x9F\x81\x03\x02\x02\x02\x9F" +
		"\x85\x03\x02\x02\x02\x9F\x89\x03\x02\x02\x02\x9F\x8D\x03\x02\x02\x02\x9F" +
		"\x94\x03\x02\x02\x02\x9F\x9D\x03\x02\x02\x02\x9F\x9E\x03\x02\x02\x02\xA0" +
		"\xA9\x03\x02\x02\x02\xA1\xA2\f\x10\x02\x02\xA2\xA3\x07\t\x02\x02\xA3\xA8" +
		"\x05\x0E\b\x11\xA4\xA5\f\x0F\x02\x02\xA5\xA6\x07\x17\x02\x02\xA6\xA8\x05" +
		"\x0E\b\x10\xA7\xA1\x03\x02\x02\x02\xA7\xA4\x03\x02\x02\x02\xA8\xAB\x03" +
		"\x02\x02\x02\xA9\xA7\x03\x02\x02\x02\xA9\xAA\x03\x02\x02\x02\xAA\x0F\x03" +
		"\x02\x02\x02\xAB\xA9\x03\x02\x02\x02\xAC\xAF\x05\f\x07\x02\xAD\xAF\x05" +
		"\x0E\b\x02\xAE\xAC\x03\x02\x02\x02\xAE\xAD\x03\x02\x02\x02\xAF\x11\x03" +
		"\x02\x02\x02\xB0\xB1\x07(\x02\x02\xB1\xB6\x05\f\x07\x02\xB2\xB3\x07,\x02" +
		"\x02\xB3\xB5\x05\f\x07\x02\xB4\xB2\x03\x02\x02\x02\xB5\xB8\x03\x02\x02" +
		"\x02\xB6\xB4\x03\x02\x02\x02\xB6\xB7\x03\x02\x02\x02\xB7\xB9\x03\x02\x02" +
		"\x02\xB8\xB6\x03\x02\x02\x02\xB9\xBA\x07)\x02\x02\xBA\xBD\x03\x02\x02" +
		"\x02\xBB\xBD\x05\f\x07\x02\xBC\xB0\x03\x02\x02\x02\xBC\xBB\x03\x02\x02" +
		"\x02\xBD\x13\x03\x02\x02\x02\xBE\xC0\x05.\x18\x02\xBF\xC1\x05\x16\f\x02" +
		"\xC0\xBF\x03\x02\x02\x02\xC0\xC1\x03\x02\x02\x02\xC1\xC6\x03\x02\x02\x02" +
		"\xC2\xC3\x05 \x11\x02\xC3\xC4\x05\x16\f\x02\xC4\xC6\x03\x02\x02\x02\xC5" +
		"\xBE\x03\x02\x02\x02\xC5\xC2\x03\x02\x02\x02\xC6\x15\x03\x02\x02\x02\xC7" +
		"\xC8\x07-\x02\x02\xC8\xCA\x05.\x18\x02\xC9\xC7\x03\x02\x02\x02\xCA\xCB" +
		"\x03\x02\x02\x02\xCB\xC9\x03\x02\x02\x02\xCB\xCC\x03\x02\x02\x02\xCC\x17" +
		"\x03\x02\x02\x02\xCD\xD9\x07\x07\x02\x02\xCE\xD9\x07\x06\x02\x02\xCF\xD9" +
		"\x05\x1A\x0E\x02\xD0\xD9\x07\x1B\x02\x02\xD1\xD9\x07\x0E\x02\x02\xD2\xD9" +
		"\x05\"\x12\x02\xD3\xD9\x05$\x13\x02\xD4\xD9\x05&\x14\x02\xD5\xD9\x05," +
		"\x17\x02\xD6\xD9\x05\x1C\x0F\x02\xD7\xD9\x05\x1E\x10\x02\xD8\xCD\x03\x02" +
		"\x02\x02\xD8\xCE\x03\x02\x02\x02\xD8\xCF\x03\x02\x02\x02\xD8\xD0\x03\x02" +
		"\x02\x02\xD8\xD1\x03\x02\x02\x02\xD8\xD2\x03\x02\x02\x02\xD8\xD3\x03\x02" +
		"\x02\x02\xD8\xD4\x03\x02\x02\x02\xD8\xD5\x03\x02\x02\x02\xD8\xD6\x03\x02" +
		"\x02\x02\xD8\xD7\x03\x02\x02\x02\xD9\x19\x03\x02\x02\x02\xDA\xDE\x07\x04" +
		"\x02\x02\xDB\xDD\x075\x02\x02\xDC\xDB\x03\x02\x02\x02\xDD\xE0\x03\x02" +
		"\x02\x02\xDE\xDC\x03\x02\x02\x02\xDE\xDF\x03\x02\x02\x02\xDF\xE1\x03\x02" +
		"\x02\x02\xE0\xDE\x03\x02\x02\x02\xE1\xEB\x076\x02\x02\xE2\xE6\x07\x05" +
		"\x02\x02\xE3\xE5\x077\x02\x02\xE4\xE3\x03\x02\x02\x02\xE5\xE8\x03\x02" +
		"\x02\x02\xE6\xE4\x03\x02\x02\x02\xE6\xE7\x03\x02\x02\x02\xE7\xE9\x03\x02" +
		"\x02\x02\xE8\xE6\x03\x02\x02\x02\xE9\xEB\x078\x02\x02\xEA\xDA\x03\x02" +
		"\x02\x02\xEA\xE2\x03\x02\x02\x02\xEB\x1B\x03\x02\x02\x02\xEC\xF5\x07*" +
		"\x02\x02\xED\xF2\x05\x18\r\x02\xEE\xEF\x07,\x02\x02\xEF\xF1\x05\x18\r" +
		"\x02\xF0\xEE\x03\x02\x02\x02\xF1\xF4\x03\x02\x02\x02\xF2\xF0\x03\x02\x02" +
		"\x02\xF2\xF3\x03\x02\x02\x02\xF3\xF6\x03\x02\x02\x02\xF4\xF2\x03\x02\x02" +
		"\x02\xF5\xED\x03\x02\x02\x02\xF5\xF6\x03\x02\x02\x02\xF6\xF7\x03\x02\x02" +
		"\x02\xF7\xF8\x07+\x02\x02\xF8\x1D\x03\x02\x02\x02\xF9\xFA\x05.\x18\x02" +
		"\xFA\u0102\x07(\x02\x02\xFB\xFC\x05.\x18\x02\xFC\xFD\x07!\x02\x02\xFD" +
		"\xFE\x05\x10\t\x02\xFE\xFF\x07,\x02\x02\xFF\u0101\x03\x02\x02\x02\u0100" +
		"\xFB\x03\x02\x02\x02\u0101\u0104\x03\x02\x02\x02\u0102\u0100\x03\x02\x02" +
		"\x02\u0102\u0103\x03\x02\x02\x02\u0103\u0105\x03\x02\x02\x02\u0104\u0102" +
		"\x03\x02\x02\x02\u0105\u0106\x05.\x18\x02\u0106\u0107\x07!\x02\x02\u0107" +
		"\u0108\x05\x10\t\x02\u0108\u0109\x07)\x02\x02\u0109\x1F\x03\x02\x02\x02" +
		"\u010A\u010B\x05.\x18\x02\u010B\u011A\x07(\x02\x02\u010C\u010D\x05.\x18" +
		"\x02\u010D\u010E\x07!\x02\x02\u010E\u010F\x05\x10\t\x02\u010F\u0110\x07" +
		",\x02\x02\u0110\u0112\x03\x02\x02\x02\u0111\u010C\x03\x02\x02\x02\u0112" +
		"\u0115\x03\x02\x02\x02\u0113\u0111\x03\x02\x02\x02\u0113\u0114\x03\x02" +
		"\x02\x02\u0114\u0116\x03\x02\x02\x02\u0115\u0113\x03\x02\x02\x02\u0116" +
		"\u0117\x05.\x18\x02\u0117\u0118\x07!\x02\x02\u0118\u0119\x05\x10\t\x02" +
		"\u0119\u011B\x03\x02\x02\x02\u011A\u0113\x03\x02\x02\x02\u011A\u011B\x03" +
		"\x02\x02\x02\u011B\u011C\x03\x02\x02\x02\u011C\u011D\x07)\x02\x02\u011D" +
		"\u012E\x03\x02\x02\x02\u011E\u011F\x05.\x18\x02\u011F\u0129\x07(\x02\x02" +
		"\u0120\u0121\x05\x10\t\x02\u0121\u0122\x07,\x02\x02\u0122\u0124\x03\x02" +
		"\x02\x02\u0123\u0120\x03\x02\x02\x02\u0124\u0127\x03\x02\x02\x02\u0125" +
		"\u0123\x03\x02\x02\x02\u0125\u0126\x03\x02\x02\x02\u0126\u0128\x03\x02" +
		"\x02\x02\u0127\u0125\x03\x02\x02\x02\u0128\u012A\x05\x10\t\x02\u0129\u0125" +
		"\x03\x02\x02\x02\u0129\u012A\x03\x02\x02\x02\u012A\u012B\x03\x02\x02\x02" +
		"\u012B\u012C\x07)\x02\x02\u012C\u012E\x03\x02\x02\x02\u012D\u010A\x03" +
		"\x02\x02\x02\u012D\u011E\x03\x02\x02\x02\u012E!\x03\x02\x02\x02\u012F" +
		"\u0130\x07\v\x02\x02\u0130\u0131\x07(\x02\x02\u0131\u0132\x05(\x15\x02" +
		"\u0132\u0133\x07)\x02\x02\u0133#\x03\x02\x02\x02\u0134\u0135\x07\x19\x02" +
		"\x02\u0135\u0136\x07(\x02\x02\u0136\u0139\x05*\x16\x02\u0137\u0138\x07" +
		"-\x02\x02\u0138\u013A\t\x05\x02\x02\u0139\u0137\x03\x02\x02\x02\u0139" +
		"\u013A\x03\x02\x02\x02\u013A\u013B\x03\x02\x02\x02\u013B\u013C\x07)\x02" +
		"\x02\u013C%\x03\x02\x02\x02\u013D\u013E\x07\x1A\x02\x02\u013E\u013F\x07" +
		"(\x02\x02\u013F\u0145\x05(\x15\x02\u0140\u0143\x05*\x16\x02\u0141\u0142" +
		"\x07-\x02\x02\u0142\u0144\t\x05\x02\x02\u0143\u0141\x03\x02\x02\x02\u0143" +
		"\u0144\x03\x02\x02\x02\u0144\u0146\x03\x02\x02\x02\u0145\u0140\x03\x02" +
		"\x02\x02\u0145\u0146\x03\x02\x02\x02\u0146\u0147\x03\x02\x02\x02\u0147" +
		"\u0148\x07)\x02\x02\u0148\'\x03\x02\x02\x02\u0149\u014A\x07\x06\x02\x02" +
		"\u014A\u014B\x07$\x02\x02\u014B\u014C\t\x05\x02\x02\u014C\u014D\x07$\x02" +
		"\x02\u014D\u014E\t\x05\x02\x02\u014E)\x03\x02\x02\x02\u014F\u0150\t\x05" +
		"\x02\x02\u0150\u0151\x07.\x02\x02\u0151\u0152\t\x05\x02\x02\u0152\u0153" +
		"\x07.\x02\x02\u0153\u0154\t\x05\x02\x02\u0154+\x03\x02\x02\x02\u0155\u019E" +
		"\x07\x11\x02\x02\u0156\u0157\x07\x06\x02\x02\u0157\u015A\x07\x1C\x02\x02" +
		"\u0158\u0159\x07\x06\x02\x02\u0159\u015B\x07\x14\x02\x02\u015A\u0158\x03" +
		"\x02\x02\x02\u015A\u015B\x03\x02\x02\x02\u015B\u015E\x03\x02\x02\x02\u015C" +
		"\u015D\x07\x06\x02\x02\u015D\u015F\x07\f\x02\x02\u015E\u015C\x03\x02\x02" +
		"\x02\u015E\u015F\x03\x02\x02\x02\u015F\u0162\x03\x02\x02\x02\u0160\u0161" +
		"\x07\x06\x02\x02\u0161\u0163\x07\x0F\x02\x02\u0162\u0160\x03\x02\x02\x02" +
		"\u0162\u0163\x03\x02\x02\x02\u0163\u0166\x03\x02\x02\x02\u0164\u0165\x07" +
		"\x06\x02\x02\u0165\u0167\x07\x13\x02\x02\u0166\u0164\x03\x02\x02\x02\u0166" +
		"\u0167\x03\x02\x02\x02\u0167\u016A\x03\x02\x02\x02\u0168\u0169\x07\x06" +
		"\x02\x02\u0169\u016B\x07\x18\x02\x02\u016A\u0168\x03\x02\x02\x02\u016A" +
		"\u016B\x03\x02\x02\x02\u016B\u019F\x03\x02\x02\x02\u016C\u016D\x07\x06" +
		"\x02\x02\u016D\u0170\x07\x14\x02\x02\u016E\u016F\x07\x06\x02\x02\u016F" +
		"\u0171\x07\f\x02\x02\u0170\u016E\x03\x02\x02\x02\u0170\u0171\x03\x02\x02" +
		"\x02\u0171\u0174\x03\x02\x02\x02\u0172\u0173\x07\x06\x02\x02\u0173\u0175" +
		"\x07\x0F\x02\x02\u0174\u0172\x03\x02\x02\x02\u0174\u0175\x03\x02\x02\x02" +
		"\u0175\u0178\x03\x02\x02\x02\u0176\u0177\x07\x06\x02\x02\u0177\u0179\x07" +
		"\x13\x02\x02\u0178\u0176\x03\x02\x02\x02\u0178\u0179\x03\x02\x02\x02\u0179" +
		"\u017C\x03\x02\x02\x02\u017A\u017B\x07\x06\x02\x02\u017B\u017D\x07\x18" +
		"\x02\x02\u017C\u017A\x03\x02\x02\x02\u017C\u017D\x03\x02\x02\x02\u017D" +
		"\u019F\x03\x02\x02\x02\u017E\u017F\x07\x06\x02\x02\u017F\u0182\x07\f\x02" +
		"\x02\u0180\u0181\x07\x06\x02\x02\u0181\u0183\x07\x0F\x02\x02\u0182\u0180" +
		"\x03\x02\x02\x02\u0182\u0183\x03\x02\x02\x02\u0183\u0186\x03\x02\x02\x02" +
		"\u0184\u0185\x07\x06\x02\x02\u0185\u0187\x07\x13\x02\x02\u0186\u0184\x03" +
		"\x02\x02\x02\u0186\u0187\x03\x02\x02\x02\u0187\u018A\x03\x02\x02\x02\u0188" +
		"\u0189\x07\x06\x02\x02\u0189\u018B\x07\x18\x02\x02\u018A\u0188\x03\x02" +
		"\x02\x02\u018A\u018B\x03\x02\x02\x02\u018B\u019F\x03\x02\x02\x02\u018C" +
		"\u018D\x07\x06\x02\x02\u018D\u0190\x07\x0F\x02\x02\u018E\u018F\x07\x06" +
		"\x02\x02\u018F\u0191\x07\x13\x02\x02\u0190\u018E\x03\x02\x02\x02\u0190" +
		"\u0191\x03\x02\x02\x02\u0191\u0194\x03\x02\x02\x02\u0192\u0193\x07\x06" +
		"\x02\x02\u0193\u0195\x07\x18\x02\x02\u0194\u0192\x03\x02\x02\x02\u0194" +
		"\u0195\x03\x02\x02\x02\u0195\u019F\x03\x02\x02\x02\u0196\u0197\x07\x06" +
		"\x02\x02\u0197\u019A\x07\x13\x02\x02\u0198\u0199\x07\x06\x02\x02\u0199" +
		"\u019B\x07\x18\x02\x02\u019A\u0198\x03\x02\x02\x02\u019A\u019B\x03\x02" +
		"\x02\x02\u019B\u019F\x03\x02\x02\x02\u019C\u019D\x07\x06\x02\x02\u019D" +
		"\u019F\x07\x18\x02\x02\u019E\u0156\x03\x02\x02\x02\u019E\u016C\x03\x02" +
		"\x02\x02\u019E\u017E\x03\x02\x02\x02\u019E\u018C\x03\x02\x02\x02\u019E" +
		"\u0196\x03\x02\x02\x02\u019E\u019C\x03\x02\x02\x02\u019F-\x03\x02\x02" +
		"\x02\u01A0\u01A1\t\x06\x02\x02\u01A1/\x03\x02\x02\x024:CES[]jq\x8F\x96" +
		"\x9F\xA7\xA9\xAE\xB6\xBC\xC0\xC5\xCB\xD8\xDE\xE6\xEA\xF2\xF5\u0102\u0113" +
		"\u011A\u0125\u0129\u012D\u0139\u0143\u0145\u015A\u015E\u0162\u0166\u016A" +
		"\u0170\u0174\u0178\u017C\u0182\u0186\u018A\u0190\u0194\u019A\u019E";
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
	public HOURS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.HOURS, 0); }
	public IN(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.IN, 0); }
	public IS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.IS, 0); }
	public MINUTES(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.MINUTES, 0); }
	public MONTHS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.MONTHS, 0); }
	public NOT(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.NOT, 0); }
	public OR(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.OR, 0); }
	public SECONDS(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.SECONDS, 0); }
	public TIME(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.TIME, 0); }
	public TIMESTAMP(): TerminalNode | undefined { return this.tryGetToken(BlazeExpressionParser.TIMESTAMP, 0); }
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


