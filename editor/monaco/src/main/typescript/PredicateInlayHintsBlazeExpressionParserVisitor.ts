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
    DomainType,
} from "blaze-domain";
import {ErrorNode, ParseTree} from "antlr4ts/tree";
import {
    AdditiveExpressionContext,
    AndPredicateContext,
    BetweenPredicateContext,
    BooleanFunctionContext,
    CollectionLiteralContext,
    DatePartContext,
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
    LiteralExpressionContext, MultiplicativeExpressionContext,
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
    TemplateContext,
    TemporalIntervalLiteralContext,
    TimePartContext,
    TimestampLiteralContext,
    UnaryMinusExpressionContext,
    UnaryPlusExpressionContext
} from "./blaze-expression-predicate/BlazeExpressionParser";
import {TerminalNode} from "antlr4ts/tree/TerminalNode";
import {BlazeExpressionLexer} from "./blaze-expression-predicate/BlazeExpressionLexer";
import {SymbolTable} from "./SymbolTable";
import * as monaco from "monaco-editor/esm/vs/editor/editor.api";

/**
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
export class TypeResolvingBlazeExpressionParserVisitor implements BlazeExpressionParserVisitor<void> {
    symbolTable: SymbolTable;
    hints: monaco.languages.InlayHint[]

    constructor(symbolTable: SymbolTable) {
        this.symbolTable = symbolTable;
        this.hints = [];
    }

    visitParsePredicate(ctx: ParsePredicateContext): void {
        ctx.predicate().accept(this);
    }

    visitParseExpression(ctx: ParseExpressionContext): void {
        ctx.expression().accept(this);
    }

    visitParseExpressionOrPredicate(ctx: ParseExpressionOrPredicateContext): void {
        ctx.predicateOrExpression().accept(this);
    }

    visitParseTemplate(ctx: ParseTemplateContext): void {
        let templateContext = ctx.template();
        if (templateContext == null) {
            return null;
        }
        return templateContext.accept(this);
    }

    visitTemplate(ctx: TemplateContext): void {
        this.visitChildren(ctx);
    }

    visitGroupedPredicate(ctx: GroupedPredicateContext): void {
        ctx.predicate().accept(this);
    }

    visitNegatedPredicate(ctx: NegatedPredicateContext): void {
        ctx.predicate().accept(this);
    }

    visitOrPredicate(ctx: OrPredicateContext): void {
        for (let element of ctx.predicate()) {
            element.accept(this);
        }
    }

    visitAndPredicate(ctx: AndPredicateContext): void {
        for (let element of ctx.predicate()) {
            element.accept(this);
        }
    }

    visitIsNullPredicate(ctx: IsNullPredicateContext): void {
        ctx.expression().accept(this);
    }

    visitIsEmptyPredicate(ctx: IsEmptyPredicateContext): void {
        ctx.expression().accept(this);
    }

    visitInequalityPredicate(ctx: InequalityPredicateContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitLessThanOrEqualPredicate(ctx: LessThanOrEqualPredicateContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitEqualityPredicate(ctx: EqualityPredicateContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitGreaterThanPredicate(ctx: GreaterThanPredicateContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitLessThanPredicate(ctx: LessThanPredicateContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitGreaterThanOrEqualPredicate(ctx: GreaterThanOrEqualPredicateContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitInPredicate(ctx: InPredicateContext): void {
        ctx.expression().accept(this);
        for (let item of ctx.inList().expression()) {
            item.accept(this);
        }
    }

    visitBetweenPredicate(ctx: BetweenPredicateContext): void {
        ctx._lhs.accept(this);
        if (ctx._begin != null) {
            ctx._begin.accept(this);
        }
        if (ctx._end != null) {
            ctx._end.accept(this);
        }
    }

    visitBooleanFunction(ctx: BooleanFunctionContext): void {
        ctx.functionInvocation().accept(this);
    }

    visitGroupedExpression(ctx: GroupedExpressionContext): void {
        ctx.expression().accept(this);
    }

    visitAdditiveExpression(ctx: AdditiveExpressionContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitMultiplicativeExpression(ctx: MultiplicativeExpressionContext): void {
        ctx._lhs.accept(this);
        ctx._rhs.accept(this);
    }

    visitUnaryMinusExpression(ctx: UnaryMinusExpressionContext): void {
        ctx.expression().accept(this);
    }

    visitUnaryPlusExpression(ctx: UnaryPlusExpressionContext): void {
        ctx.expression().accept(this);
    }

    visitTimestampLiteral(ctx: TimestampLiteralContext): void {
    }

    visitTemporalIntervalLiteral(ctx: TemporalIntervalLiteralContext): void {
    }

    visitCollectionLiteral(ctx: CollectionLiteralContext): void {
        for (let element of ctx.literal()) {
            element.accept(this);
        }
    }

    visitPathPredicate(ctx: PathPredicateContext): void {
        let pathCtx = ctx.path();
        let functionInvocation = pathCtx.functionInvocation();
        if (functionInvocation != null) {
            functionInvocation.accept(this);
        }
    }
    visitPath(ctx: PathContext): void {
        let functionInvocation = ctx.functionInvocation();
        if (functionInvocation != null) {
            functionInvocation.accept(this);
        }
    }

    visitIndexedFunctionInvocation(ctx: IndexedFunctionInvocationContext): void {
        let functionName = ctx._name.text;
        let func = this.symbolTable.model.domainModel.getFunction(functionName);
        let predicateOrExpressions = ctx.predicateOrExpression();
        if (func != null) {
            let end = Math.min(func.arguments.length, predicateOrExpressions.length);
            for (let i = 0; i < end; i++) {
                let arg = func.arguments[i];
                this.hints.push(
                    {
                        label: arg.name + ':',
                        kind: monaco.languages.InlayHintKind.Parameter,
                        position: {
                            lineNumber: predicateOrExpressions[i].start.line,
                            column: predicateOrExpressions[i].start.startIndex + 1
                        },
                        tooltip: {
                            isTrusted: true,
                            value: (arg.type == null ? 'any' : arg.type.name) + ' ' + arg.name + '\n\n' + arg.documentation
                        }
                    }
                );
            }
        }
        for (let element of predicateOrExpressions) {
            element.accept(this);
        }
    }

    visitEntityLiteral(ctx: EntityLiteralContext): void {
    }

    visitNamedInvocation(ctx: NamedInvocationContext): void {
    }

    visitStringLiteral(ctx: StringLiteralContext): void {
    }

    visitTerminal(node: TerminalNode): void {
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
