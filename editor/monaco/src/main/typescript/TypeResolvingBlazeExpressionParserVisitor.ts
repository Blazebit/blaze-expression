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

import {BlazeExpressionParserVisitor} from "./blaze-expression-predicate/BlazeExpressionParserVisitor";
import {
    BasicDomainType,
    CollectionDomainType,
    DomainOperator,
    DomainPredicate,
    DomainType,
    DomainTypeResolverException,
    EntityDomainType,
    EnumDomainType
} from "blaze-domain";
import {ErrorNode, ParseTree} from "antlr4ts/tree";
import {
    AdditionExpressionContext,
    AndPredicateContext,
    BetweenPredicateContext,
    BooleanFunctionContext,
    CollectionLiteralContext,
    DatePartContext,
    DivisionExpressionContext,
    EntityLiteralContext,
    EqualityPredicateContext,
    ExpressionContext,
    FunctionExpressionContext,
    FunctionInvocationContext,
    GreaterThanOrEqualPredicateContext,
    GreaterThanPredicateContext,
    GroupedExpressionContext,
    GroupedPredicateContext,
    IdentifierContext,
    IndexedFunctionInvocationContext,
    InequalityPredicateContext,
    InListContext,
    InPredicateContext,
    IsEmptyPredicateContext,
    IsNullPredicateContext,
    LessThanOrEqualPredicateContext,
    LessThanPredicateContext,
    LiteralContext,
    LiteralExpressionContext,
    ModuloExpressionContext,
    MultiplicationExpressionContext,
    NamedInvocationContext,
    NegatedPredicateContext,
    OrPredicateContext,
    ParseExpressionContext,
    ParseExpressionOrPredicateContext,
    ParsePredicateContext,
    ParseTemplateContext,
    PathAttributesContext,
    PathContext,
    PathExpressionContext,
    PathPredicateContext,
    PredicateContext,
    PredicateOrExpressionContext,
    StringLiteralContext,
    SubtractionExpressionContext,
    TemplateContext,
    TemporalIntervalLiteralContext,
    TimePartContext,
    TimestampLiteralContext,
    UnaryMinusExpressionContext,
    UnaryPlusExpressionContext
} from "./blaze-expression-predicate/BlazeExpressionParser";
import {TypeErrorException} from "./TypeErrorException";
import {ParserRuleContext, Token} from "antlr4ts";
import {SyntaxErrorException} from "./SyntaxErrorException";
import {DomainModelException} from "./DomainModelException";
import {TerminalNode} from "antlr4ts/tree/TerminalNode";
import {BlazeExpressionLexer} from "./blaze-expression-predicate/BlazeExpressionLexer";
import {SymbolTable} from "./SymbolTable";
import {LiteralKind} from "./LiteralKind";
import {CollectionLiteral} from "./CollectionLiteral";
import {LiteralFactory} from "./LiteralFactory";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class TypeResolvingBlazeExpressionParserVisitor implements BlazeExpressionParserVisitor<DomainType> {
    symbolTable: SymbolTable;
    private readonly booleanDomainType: BasicDomainType;

    constructor(symbolTable: SymbolTable) {
        this.symbolTable = symbolTable;
        if (symbolTable.model.booleanLiteralResolver == null) {
            this.booleanDomainType = null;
        } else {
            this.booleanDomainType = symbolTable.model.booleanLiteralResolver.resolveLiteral(symbolTable.model.domainModel, LiteralKind.BOOLEAN, 'true');
        }
    }

    visitParsePredicate(ctx: ParsePredicateContext): DomainType {
        return ctx.predicate().accept(this);
    }

    visitParseExpression(ctx: ParseExpressionContext): DomainType {
        return ctx.expression().accept(this);
    }

    visitParseExpressionOrPredicate(ctx: ParseExpressionOrPredicateContext): DomainType {
        return ctx.predicateOrExpression().accept(this);
    }

    visitParseTemplate(ctx: ParseTemplateContext): DomainType {
        let templateContext = ctx.template();
        if (templateContext == null) {
            return this.resolveStringLiteral('');
        }
        return templateContext.accept(this);
    }

    visitTemplate(ctx: TemplateContext) {
        let expressionType: DomainType;
        let i = 0;
        let child: TerminalNode = ctx.getChild(i) as TerminalNode;
        if (child.symbol.type == BlazeExpressionLexer.TEXT) {
            expressionType = this.resolveStringLiteral(child.text);
            i += 1;
        } else {
            expressionType = this.resolveStringLiteral('');
        }
        let childCount = ctx.childCount;
        for (; i < childCount; i++) {
            child = ctx.getChild(i) as TerminalNode;
            let subExpressionType: DomainType;
            let subExpressionCtx;
            if (child.symbol.type == BlazeExpressionLexer.TEXT) {
                subExpressionCtx = child;
                subExpressionType = this.resolveStringLiteral(child.text);
            } else {
                subExpressionCtx = ctx.getChild(i + 1);
                subExpressionType = subExpressionCtx.accept(this);
                i += 2;
            }
            expressionType = this.createArithmeticExpression(
                ctx,
                subExpressionCtx,
                expressionType,
                subExpressionType,
                DomainOperator.PLUS
            );
        }
        return expressionType;
    }

    visitGroupedPredicate(ctx: GroupedPredicateContext): DomainType {
        return ctx.predicate().accept(this);
    }

    visitNegatedPredicate(ctx: NegatedPredicateContext): DomainType {
        return ctx.predicate().accept(this);
    }

    visitOrPredicate(ctx: OrPredicateContext): DomainType {
        let preds = ctx.predicate();
        preds[0].accept(this);
        preds[1].accept(this);
        return this.booleanDomainType;
    }

    visitAndPredicate(ctx: AndPredicateContext): DomainType {
        let preds = ctx.predicate();
        preds[0].accept(this);
        preds[1].accept(this);
        return this.booleanDomainType;
    }

    visitIsNullPredicate(ctx: IsNullPredicateContext): DomainType {
        let left = ctx.expression().accept(this);
        let predicateTypeResolvers = this.symbolTable.model.domainModel.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.NULLNESS]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left.getType(), [1, 2, 3]);
        } else {
            try {
                let domainType = predicateTypeResolver.resolveType(this.symbolTable.model.domainModel, [left]);
                if (domainType == null) {
                    throw this.cannotResolvePredicateType(ctx, DomainPredicate.NULLNESS, [left]);
                } else {
                    return this.booleanDomainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitIsEmptyPredicate(ctx: IsEmptyPredicateContext): DomainType {
        let left = ctx.expression().accept(this);
        let predicateTypeResolvers = this.symbolTable.model.domainModel.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.COLLECTION]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1, 2, 3]);
        } else {
            try {
                let domainType = predicateTypeResolver.resolveType(this.symbolTable.model.domainModel, [left]);
                if (domainType == null) {
                    throw this.cannotResolvePredicateType(ctx, DomainPredicate.COLLECTION, [left]);
                } else {
                    return this.booleanDomainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitInequalityPredicate(ctx: InequalityPredicateContext): DomainType {
        return this.createComparisonPredicate(
            ctx,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainPredicate.EQUALITY
        );
    }

    visitLessThanOrEqualPredicate(ctx: LessThanOrEqualPredicateContext): DomainType {
        return this.createComparisonPredicate(
            ctx,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainPredicate.RELATIONAL
        );
    }

    visitEqualityPredicate(ctx: EqualityPredicateContext): DomainType {
        return this.createComparisonPredicate(
            ctx,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainPredicate.EQUALITY
        );
    }

    visitGreaterThanPredicate(ctx: GreaterThanPredicateContext): DomainType {
        return this.createComparisonPredicate(
            ctx,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainPredicate.RELATIONAL
        );
    }

    visitLessThanPredicate(ctx: LessThanPredicateContext): DomainType {
        return this.createComparisonPredicate(
            ctx,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainPredicate.RELATIONAL
        );
    }

    visitGreaterThanOrEqualPredicate(ctx: GreaterThanOrEqualPredicateContext): DomainType {
        return this.createComparisonPredicate(
            ctx,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainPredicate.RELATIONAL
        );
    }

    createComparisonPredicate(ctx: ParserRuleContext, left: DomainType, right: DomainType, domainPredicate: DomainPredicate): DomainType {
        if (right == null) {
            throw this.incompleteExpression(ctx);
        }
        let operandTypes = [left, right];
        let predicateTypeResolvers = this.symbolTable.model.domainModel.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[domainPredicate]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1]);
        } else {
            try {
                let domainType = predicateTypeResolver.resolveType(this.symbolTable.model.domainModel, operandTypes);
                if (domainType == null) {
                    throw this.cannotResolvePredicateType(ctx, domainPredicate, operandTypes);
                } else {
                    return this.booleanDomainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitInPredicate(ctx: InPredicateContext): DomainType {
        let left: DomainType = ctx.expression().accept(this);
        let operandTypes: DomainType[] = this.getExpressionList(ctx.inList().expression());
        let predicateTypeResolvers = this.symbolTable.model.domainModel.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.EQUALITY]];

        if (operandTypes.length == 0) {
            throw this.incompleteExpression(ctx);
        }

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1]);
        } else {
            operandTypes.unshift(left);
            try {
                let domainType = predicateTypeResolver.resolveType(this.symbolTable.model.domainModel, operandTypes);
                if (domainType == null) {
                    throw this.cannotResolvePredicateType(ctx, DomainPredicate.EQUALITY, operandTypes);
                } else {
                    return this.booleanDomainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitBetweenPredicate(ctx: BetweenPredicateContext): DomainType {
        if (ctx._begin == null || ctx._end == null) {
            throw this.incompleteExpression(ctx);
        }

        let left: DomainType = ctx._lhs.accept(this);
        let lower: DomainType = ctx._begin.accept(this);
        let upper: DomainType = ctx._end.accept(this);
        if (lower == null || upper == null) {
            throw this.incompleteExpression(ctx);
        }

        let predicateTypeResolvers = this.symbolTable.model.domainModel.predicateTypeResolvers[left.name];
        let predicateTypeResolver = predicateTypeResolvers == null ? null : predicateTypeResolvers[DomainPredicate[DomainPredicate.RELATIONAL]];

        if (predicateTypeResolver == null) {
            throw this.missingPredicateTypeResolver(ctx, left, [1]);
        } else {
            let operandTypes = [left, lower, upper];
            try {
                let domainType = predicateTypeResolver.resolveType(this.symbolTable.model.domainModel, operandTypes);
                if (domainType == null) {
                    throw this.cannotResolvePredicateType(ctx, DomainPredicate.RELATIONAL, operandTypes);
                } else {
                    return this.booleanDomainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitBooleanFunction(ctx: BooleanFunctionContext): DomainType {
        let resultType = ctx.functionInvocation().accept(this);
        if (resultType == this.booleanDomainType) {
            return resultType;
        }

        throw new TypeErrorException("Invalid use of non-boolean returning function: " + ctx.text, ctx, -1, -1, -1, -1);
    }

    visitGroupedExpression(ctx: GroupedExpressionContext): DomainType {
        return ctx.expression().accept(this);
    }

    visitAdditionExpression(ctx: AdditionExpressionContext): DomainType {
        return this.createArithmeticExpression(
            ctx,
            ctx._rhs,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainOperator.PLUS
        );
    }

    visitSubtractionExpression(ctx: SubtractionExpressionContext): DomainType {
        return this.createArithmeticExpression(
            ctx,
            ctx._rhs,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainOperator.MINUS
        );
    }

    visitDivisionExpression(ctx: DivisionExpressionContext): DomainType {
        return this.createArithmeticExpression(
            ctx,
            ctx._rhs,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainOperator.DIVISION
        );
    }

    visitMultiplicationExpression(ctx: MultiplicationExpressionContext): DomainType {
        return this.createArithmeticExpression(
            ctx,
            ctx._rhs,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainOperator.MULTIPLICATION
        );
    }

    visitModuloExpression(ctx: ModuloExpressionContext): DomainType {
        return this.createArithmeticExpression(
            ctx,
            ctx._rhs,
            ctx._lhs.accept(this),
            ctx._rhs.accept(this),
            DomainOperator.MODULO
        );
    }

    createArithmeticExpression(ctx: ParserRuleContext, rhsCtx: ParserRuleContext, left: DomainType, right: DomainType, operator: DomainOperator): DomainType {
        if (right == null) {
            throw this.incompleteExpression(rhsCtx);
        }

        let operandTypes = [left, right];
        let operationTypeResolvers = this.symbolTable.model.domainModel.operationTypeResolvers[left.name];
        let operationTypeResolver = operationTypeResolvers == null ? null : operationTypeResolvers[DomainOperator[operator]];
        if (operationTypeResolver == null) {
            throw this.missingOperationTypeResolver(ctx, left, 1);
        } else {
            try {
                let domainType = operationTypeResolver.resolveType(this.symbolTable.model.domainModel, operandTypes);
                if (domainType == null) {
                    throw this.cannotResolveOperationType(ctx, operator, operandTypes);
                } else {
                    return domainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitUnaryMinusExpression(ctx: UnaryMinusExpressionContext): DomainType {
        let left = ctx.expression().accept(this);
        if (left == null) {
            throw this.incompleteExpression(ctx);
        }
        let operandTypes = [left];
        let operationTypeResolvers = this.symbolTable.model.domainModel.operationTypeResolvers[left.name];
        let operationTypeResolver = operationTypeResolvers == null ? null : operationTypeResolvers[DomainOperator[DomainOperator.UNARY_MINUS]];
        if (operationTypeResolver == null) {
            throw this.missingOperationTypeResolver(ctx, left, 0);
        } else {
            try {
                let domainType = operationTypeResolver.resolveType(this.symbolTable.model.domainModel, operandTypes);
                if (domainType == null) {
                    throw this.cannotResolveOperationType(ctx, DomainOperator.UNARY_MINUS, operandTypes);
                } else {
                    return domainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitUnaryPlusExpression(ctx: UnaryPlusExpressionContext): DomainType {
        let left = ctx.expression().accept(this);
        if (left == null) {
            throw this.incompleteExpression(ctx);
        }
        let operandTypes = [left];
        let operationTypeResolvers = this.symbolTable.model.domainModel.operationTypeResolvers[left.name];
        let operationTypeResolver = operationTypeResolvers == null ? null : operationTypeResolvers[DomainOperator[DomainOperator.UNARY_PLUS]];
        if (operationTypeResolver == null) {
            throw this.missingOperationTypeResolver(ctx, left, 0);
        } else {
            try {
                let domainType = operationTypeResolver.resolveType(this.symbolTable.model.domainModel, operandTypes);
                if (domainType == null) {
                    throw this.cannotResolveOperationType(ctx, DomainOperator.UNARY_PLUS, operandTypes);
                } else {
                    return domainType;
                }
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitTimestampLiteral(ctx: TimestampLiteralContext): DomainType {
        return this.symbolTable.model.temporalLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.TIMESTAMP, ctx.text);
    }

    visitTemporalIntervalLiteral(ctx: TemporalIntervalLiteralContext): DomainType {
        this.parseTemporalAmount(ctx, ctx._years, "years");
        this.parseTemporalAmount(ctx, ctx._months, "months");
        this.parseTemporalAmount(ctx, ctx._days, "days");
        this.parseTemporalAmount(ctx, ctx._hours, "hours");
        this.parseTemporalAmount(ctx, ctx._minutes, "minutes");
        this.parseTemporalAmount(ctx, ctx._seconds, "seconds");
        return this.symbolTable.model.temporalLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.INTERVAL, ctx.text);
    }

    parseTemporalAmount(ctx: ParserRuleContext, token: Token, field: string): number {
        if (token == null) {
            return 0;
        }
        let amountString = token.text;
        let amount = parseInt(amountString);
        if (isNaN(amount) || amount < 0) {
            throw new SyntaxErrorException("Illegal value given for temporal field '" + field + "': " + amountString, ctx, -1, -1, -1, -1);
        }
        return amount;
    }

    visitCollectionLiteral(ctx: CollectionLiteralContext): DomainType {
        let literalList = this.getExpressionList(ctx.literal());
        let literal: CollectionLiteral = {
            type: this.symbolTable.model.domainModel.types['Collection[' + literalList[0].name + ']'] as CollectionDomainType,
            values: []
        };
        return this.symbolTable.model.collectionLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.COLLECTION, literal);
    }

    visitPathPredicate(ctx: PathPredicateContext): DomainType {
        let pathCtx = ctx.path();
        let type = this.createPathExpression(pathCtx);
        if (type == this.booleanDomainType) {
            return type;
        }
        throw this.unsupportedType(pathCtx.identifier(), type.name);
    }
    visitPath(ctx: PathContext): DomainType {
        return this.createPathExpression(ctx);
    }
    createPathExpression(ctx: PathContext): DomainType {
        let identifierContext = ctx.identifier();
        let pathAttributesContext = ctx.pathAttributes();
        if (identifierContext == null) {
            let functionInvocation = ctx.functionInvocation();
            return this.resolvePathAttributes(functionInvocation.accept(this), "function " + functionInvocation.children[0].text, pathAttributesContext);
        } else {
            let alias = identifierContext.text;
            let symbol = this.symbolTable.variables[alias];
            if (symbol == null) {
                let identifiers: IdentifierContext[];
                if (pathAttributesContext != null && (identifiers = pathAttributesContext.identifier()).length == 1) {
                    let type = this.symbolTable.model.domainModel.types[alias];
                    if (type instanceof EnumDomainType) {
                        let key = identifiers[0].text;
                        let value = type.enumValues[key];
                        if (value == null) {
                            throw new DomainModelException(this.format("The value '{0}' on the enum domain type '{1}' does not exist!", key, type.name), identifiers[0], -1, -1, -1, -1);
                        }
                        if (this.symbolTable.model.enumLiteralResolver == null) {
                            return type;
                        } else {
                            return this.symbolTable.model.enumLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.ENUM, {
                                enumType: type,
                                value: value
                            });
                        }
                    }
                }
                throw this.unknownType(ctx, alias);
            }
            return this.resolvePathAttributes(symbol.type, "variable " + alias, pathAttributesContext);
        }
    }

    private resolvePathAttributes(type: DomainType, source: string, pathAttributesContext: PathAttributesContext): DomainType {
        if (pathAttributesContext != null) {
            let identifiers = pathAttributesContext.identifier();
            for (let i = 0; i < identifiers.length; i++) {
                let pathElement = identifiers[i].text;
                if (type instanceof CollectionDomainType) {
                    type = type.elementType;
                }
                if (type instanceof EntityDomainType) {
                    let attribute = type.attributes[pathElement];
                    if (attribute == null) {
                        throw this.unknownEntityAttribute(identifiers[i], type, pathElement);
                    } else {
                        type = attribute.type;
                    }
                } else {
                    throw this.cannotDeReference(identifiers[i], pathElement, source, type.name);
                }
                source = "attribute " + pathElement;
            }
        }
        return type;
    }

    visitIndexedFunctionInvocation(ctx: IndexedFunctionInvocationContext): DomainType {
        let functionName = ctx._name.text;
        let func = this.symbolTable.model.domainModel.functions[functionName];
        if (func == null) {
            throw this.unknownFunction(ctx, functionName);
        } else {
            let predicateOrExpressions = ctx.predicateOrExpression();
            let literalList = this.getExpressionList(predicateOrExpressions);
            if (func.argumentCount != -1 && literalList.length > func.argumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at most " + func.argumentCount + " arguments but found " + literalList.length, ctx, -1, -1, -1, -1);
            }
            if (literalList.length < func.minArgumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at least " + func.minArgumentCount + " arguments but found " + literalList.length, ctx, -1, -1, -1, -1);
            }
            let argumentTypes: DomainType[] = [];
            let i = 0;
            let lastIdx = func.arguments.length - 1;
            let end = Math.min(lastIdx, literalList.length);
            for (; i < end; i++) {
                argumentTypes.push(literalList[i]);
            }
            if (lastIdx != -1) {
                let domainFunctionArgument = func.arguments[lastIdx];
                if (func.argumentCount == -1) {
                    // Varargs
                    let elementType: DomainType = domainFunctionArgument.type;
                    if (elementType instanceof CollectionDomainType) {
                        elementType = elementType.elementType;
                    }
                    if (elementType != null) {
                        for (; i < literalList.length; i++) {
                            if (elementType != literalList[i]) {
                                // invalid heterogeneous use for var-args
                                let offending = predicateOrExpressions[i];
                                throw new DomainModelException("Function '" + func.name + "' expects the " + (i + 1) + "th argument to be of type " + elementType.name + " but was " + literalList[i].name, null, offending.start.line, offending.stop.line, offending.start.startIndex + 1, ctx.stop.stopIndex + 2);
                            }
                        }
                    }
                    argumentTypes.push(domainFunctionArgument.type);
                } else if (i < literalList.length) {
                    argumentTypes.push(literalList[i]);
                }
            }
            try {
                return func.resultTypeResolver.resolveType(this.symbolTable.model.domainModel, func, argumentTypes);
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitEntityLiteral(ctx: EntityLiteralContext): DomainType {
        let entityOrFunctionName = ctx._name.text;
        let type = this.symbolTable.model.domainModel.types[entityOrFunctionName];
        if (type instanceof EntityDomainType) {
            let argNames: IdentifierContext[] = ctx.identifier();
            argNames.shift()
            return this.createEntityLiteral(type, argNames, this.getLiteralList(ctx.predicateOrExpression()));
        } else {
            throw this.unknownType(ctx, entityOrFunctionName);
        }
    }

    private createEntityLiteral(type: EntityDomainType, argNames: IdentifierContext[], literalList: DomainType[]): DomainType {
        let args: StringMap<any> = {};
        for (let i = 0; i < literalList.length; i++) {
            let arg = argNames[i].text;
            let attribute = type.attributes[arg];
            if (attribute == null) {
                let argumentNames = "[";
                for (let name in type.attributes) {
                    if (argumentNames.length != 1) {
                        argumentNames += ", ";
                    }
                    argumentNames += name;
                }
                argumentNames += "]";
                throw new DomainModelException("Invalid attribute name '" + arg + "'! Entity '" + type.name + "' expects the following attribute names: " + argumentNames, argNames[i], -1, -1, -1, -1);
            }
            args[attribute.name] = literalList[i];
        }
        return this.symbolTable.model.entityLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.ENTITY, {
            entityType: type,
            attributeValues: args
        });
    }

    visitNamedInvocation(ctx: NamedInvocationContext): DomainType {
        let entityOrFunctionName = ctx._name.text;
        let func = this.symbolTable.model.domainModel.functions[entityOrFunctionName];
        if (func == null) {
            let type = this.symbolTable.model.domainModel.types[entityOrFunctionName];
            if (type instanceof EntityDomainType) {
                let argNames: IdentifierContext[] = ctx.identifier();
                argNames.shift()
                return this.createEntityLiteral(type, argNames, this.getLiteralList(ctx.predicateOrExpression()));
            } else {
                throw this.unknownFunction(ctx, entityOrFunctionName);
            }
        } else {
            let argNames: IdentifierContext[] = ctx.identifier();
            let argumentExpressionTypes = this.getExpressionList(ctx.predicateOrExpression());
            if (func.argumentCount != -1 && argumentExpressionTypes.length > func.argumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at most " + func.argumentCount + " arguments but found " + argumentExpressionTypes.length, ctx, -1, -1, -1, -1);
            }
            if (argumentExpressionTypes.length < func.minArgumentCount) {
                throw new DomainModelException("Function '" + func.name + "' expects at least " + func.minArgumentCount + " arguments but found " + argumentExpressionTypes.length, ctx, -1, -1, -1, -1);
            }
            argNames.shift();
            let argumentTypes: DomainType[] = new Array(func.arguments.length);
            for (let i = 0; i < argumentExpressionTypes.length; i++) {
                let domainFunctionArgument = null;
                let arg = argNames[i].text;
                for (let j = 0; j < func.arguments.length; j++) {
                    if (func.arguments[j].name == arg) {
                        domainFunctionArgument = func.arguments[j];
                        break;
                    }
                }
                if (domainFunctionArgument == null) {
                    let argumentNames = "[";
                    for (let j = 0; j < func.arguments.length; j++) {
                        if (j != 0) {
                            argumentNames += ", ";
                        }
                        argumentNames += func.arguments[j].name;
                    }
                    argumentNames += "]";
                    throw new DomainModelException("Invalid argument name '" + arg + "'! Function '" + func.name + "' expects the following argument names: " + argumentNames, argNames[i], -1, -1, -1, -1);
                }
                argumentTypes[domainFunctionArgument.position] = argumentExpressionTypes[i];
            }
            try {
                return func.resultTypeResolver.resolveType(this.symbolTable.model.domainModel, func, argumentTypes);
            } catch (e) {
                if (e instanceof DomainTypeResolverException) {
                    throw new TypeErrorException(e.message, ctx, -1, -1, -1, -1)
                } else {
                    throw e;
                }
            }
        }
    }

    visitStringLiteral(ctx: StringLiteralContext): DomainType {
        return this.resolveStringLiteral(LiteralFactory.unescapeString(ctx.text));
    }

    visitTerminal(node: TerminalNode): DomainType {
        if (node.symbol.type == BlazeExpressionLexer.EOF) {
            return null;
        }
        switch (node.symbol.type) {
            case BlazeExpressionLexer.TEXT: // We also handle text to avoid unnecessary interpretation errors
                return this.resolveStringLiteral(node.text);
            case BlazeExpressionLexer.TRUE:
            case BlazeExpressionLexer.FALSE:
                return this.symbolTable.model.booleanLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.BOOLEAN, node.text);
            case BlazeExpressionLexer.NUMERIC_LITERAL:
                return this.symbolTable.model.numericLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.NUMERIC, node.text);
            default:
                throw new Error("Terminal node '" + node.text + "' not handled");
        }
    }

    private resolveStringLiteral(text: string): DomainType {
        return this.symbolTable.model.stringLiteralResolver.resolveLiteral(this.symbolTable.model.domainModel, LiteralKind.STRING, text);
    }

    private getExpressionList(items: ParserRuleContext[]): DomainType[] {
        let expressions = [];
        for (let item of items) {
            expressions.push(item.accept(this));
        }
        return expressions;
    }

    private getLiteralList(items: PredicateOrExpressionContext[]): DomainType[] {
        let expressions = [];
        for (let item of items) {
            let child1 = item.getChild(0);
            if (child1 instanceof ExpressionContext) {
                let child2 = child1.getChild(0);
                if (child2 instanceof LiteralContext) {
                    expressions.push(item.accept(this));
                    continue;
                }
            }
            throw new TypeErrorException('Only literals are allowed', child1 as ParserRuleContext, -1, -1, -1, -1);
        }
        return expressions;
    }

    private missingPredicateTypeResolver(ctx: ParserRuleContext, type: DomainType, tokenIndexes: number[]) {
        let symbols = "";
        let startLine;
        let endLine;
        let startCol;
        let endCol;
        for (let i = 0; i < tokenIndexes.length; i++) {
            let j = tokenIndexes[i];
            if (j < ctx.children.length) {
                let sym = (ctx.children[j] as TerminalNode).symbol;
                if (i == 0) {
                    startLine = endLine = sym.line;
                    startCol = sym.startIndex;
                    endCol = sym.stopIndex;
                } else {
                    endLine = sym.line;
                    endCol = sym.stopIndex;
                    symbols += " ";
                }
                symbols += sym.text;
            }
        }

        return new DomainModelException(this.format("Missing predicate type resolver for type {0} and predicate {1}", type.name, symbols), null, startLine, endLine, startCol + 1, endCol + 2);
    }

    private incompleteExpression(ctx: ParserRuleContext) {
        return new DomainModelException(this.format("Incomplete expression"), ctx, -1, -1, -1, -1);
    }

    private missingOperationTypeResolver(ctx: ParserRuleContext, type: DomainType, tokenIndex: number) {
        let sym = (ctx.children[tokenIndex] as TerminalNode).symbol;
        return new DomainModelException(this.format("Missing operation type resolver for type {0} and operator {1}", type.name, sym.text), null, sym.line, sym.line, sym.startIndex + 1, sym.stopIndex + 2);
    }

    private unknownType(ctx: ParserRuleContext, typeName: string) {
        return new DomainModelException(this.format("Undefined type '{0}'", typeName), ctx, -1, -1, -1, -1);
    }

    private unknownEntityAttribute(ctx: ParserRuleContext, entityDomainType: EntityDomainType, attributeName: string) {
        return new DomainModelException(this.format("Attribute {0} undefined for entity {1}", attributeName, entityDomainType.name), ctx, -1, -1, -1, -1);
    }

    private unknownFunction(ctx: ParserRuleContext, identifier: string) {
        return new DomainModelException(this.format("Undefined function '{0}'", identifier), ctx, -1, -1, -1, -1);
    }

    private unsupportedType(ctx: ParserRuleContext, typeName: string) {
        return new TypeErrorException(this.format("Resolved type for identifier {0} is not supported", typeName), ctx, -1, -1, -1, -1);
    }

    private cannotResolvePredicateType(ctx: ParserRuleContext, predicateType: DomainPredicate, operandTypes: DomainType[]) {
        return new TypeErrorException(this.format("Cannot resolve predicate type for predicate {0} and operand types {1}", DomainPredicate[predicateType], TypeResolvingBlazeExpressionParserVisitor.typeNames(operandTypes)), ctx, -1, -1, -1, -1);
    }

    private cannotResolveOperationType(ctx: ParserRuleContext, operator: DomainOperator, operandTypes: DomainType[]) {
        return new TypeErrorException(this.format("Cannot resolve operation type for operator {0} and operand types {1}", DomainOperator[operator], TypeResolvingBlazeExpressionParserVisitor.typeNames(operandTypes)), ctx, -1, -1, -1, -1);
    }

    private cannotDeReference(ctx: ParserRuleContext, attribute: string, source: string, typeName: string) {
        return new TypeErrorException(this.format("Cannot de-reference attribute {0} on {1} because the type {2} is not an entity type", attribute, source, typeName), ctx, -1, -1, -1, -1);
    }

    private static typeNames(operandTypes: DomainType[]): string {
        let typeNames = "[";
        for (let i = 0; i < operandTypes.length; i++) {
            if (i != 0) {
                typeNames += ", ";
            }
            typeNames += operandTypes[i].name;
        }
        return typeNames + "]";
    }

    private format(format: string, ...args): string {
        return format.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] != 'undefined'
                ? args[number]
                : match
                ;
        });
    }

    // Default implementations for traversing composite rules

    visitChildren(ctx) {
        if (ctx.children) {
            let result = null;
            for (let i = 0; i < ctx.children.length; i++) {
                result = ctx.children[i].accept(this);
            }
            return result;
        } else {
            return null;
        }
    }

    visit(tree: ParseTree): DomainType {
        return tree.accept(this);
    }

    visitErrorNode(node: ErrorNode): DomainType {
        return null;
    }

    visitDatePart(ctx: DatePartContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitExpression(ctx: ExpressionContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitFunctionExpression(ctx: FunctionExpressionContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitFunctionInvocation(ctx: FunctionInvocationContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitIdentifier(ctx: IdentifierContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitInList(ctx: InListContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitLiteral(ctx: LiteralContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitLiteralExpression(ctx: LiteralExpressionContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitPathExpression(ctx: PathExpressionContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitPredicate(ctx: PredicateContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitPredicateOrExpression(ctx: PredicateOrExpressionContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitTimePart(ctx: TimePartContext): DomainType {
        return this.visitChildren(ctx);
    }

    visitPathAttributes(ctx: PathAttributesContext): DomainType {
        return this.visitChildren(ctx);
    }
}
