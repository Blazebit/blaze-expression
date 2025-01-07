/*
 * Copyright 2019 - 2025 Blazebit.
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

package com.blazebit.expression.impl;

import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainFunctionTypeResolver;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.DomainOperationTypeResolver;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.domain.runtime.model.DomainPredicateTypeResolver;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.DomainTypeResolverException;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.ArithmeticOperatorType;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.CollectionLiteral;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.EntityLiteral;
import com.blazebit.expression.EnumLiteral;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.FromItem;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.ImplicitRootProvider;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsEmptyPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Join;
import com.blazebit.expression.JoinType;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.Query;
import com.blazebit.expression.SyntaxErrorException;
import com.blazebit.expression.TypeErrorException;
import com.blazebit.expression.impl.PredicateParser.PathContext;
import com.blazebit.expression.impl.PredicateParser.PathPredicateContext;
import com.blazebit.expression.spi.DefaultResolvedLiteral;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PredicateModelGenerator extends PredicateParserBaseVisitor<Object> {

    protected final DomainModel domainModel;
    protected final LiteralFactory literalFactory;
    protected final ExpressionCompiler.Context compileContext;
    protected Literal cachedBooleanTrueLiteral;
    protected Literal cachedBooleanFalseLiteral;
    protected Map<String, DomainType> fromNodes;

    public PredicateModelGenerator(DomainModel domainModel, LiteralFactory literalFactory, ExpressionCompiler.Context compileContext) {
        this.domainModel = domainModel;
        this.literalFactory = literalFactory;
        this.compileContext = compileContext;
    }

    protected DomainType resolveAlias(String alias) {
        if (fromNodes != null) {
            DomainType domainType = fromNodes.get( alias );
            if (domainType != null) {
                return domainType;
            }
        }
        return compileContext.getRootDomainType( alias );
    }

    @Override
    public Expression visitParsePredicate(PredicateParser.ParsePredicateContext ctx) {
        return (Expression) ctx.predicate().accept( this);
    }

    @Override
    public Expression visitParseExpression(PredicateParser.ParseExpressionContext ctx) {
        return (Expression) ctx.expression().accept( this);
    }

    @Override
    public Expression visitParseExpressionOrPredicate(PredicateParser.ParseExpressionOrPredicateContext ctx) {
        return (Expression) ctx.predicateOrExpression().accept( this);
    }

    @Override
    public Expression visitParseTemplate(PredicateParser.ParseTemplateContext ctx) {
        PredicateParser.TemplateContext templateContext = ctx.template();
        if (templateContext == null) {
            return new Literal(literalFactory.ofString(compileContext, ""));
        }
        return (Expression) templateContext.accept( this);
    }

    @Override
    public Query visitParseQuery(PredicateParser.ParseQueryContext ctx) {
        return (Query) ctx.query().accept( this );
    }

    @Override
    public Expression visitTemplate(PredicateParser.TemplateContext ctx) {
        ArithmeticExpression expression;
        int i = 0;
        TerminalNode child = (TerminalNode) ctx.getChild(i);
        if (child.getSymbol().getType() == PredicateLexer.TEXT) {
            expression = new Literal(literalFactory.ofString(compileContext, child.getText()));
            i += 1;
        } else {
            expression = new Literal(literalFactory.ofString(compileContext, ""));
        }
        int childCount = ctx.getChildCount();
        for (; i < childCount; i++) {
            child = (TerminalNode) ctx.getChild(i);
            ArithmeticExpression subExpression;
            if (child.getSymbol().getType() == PredicateLexer.TEXT) {
                subExpression = new Literal(literalFactory.ofString(compileContext, child.getText()));
            } else {
                subExpression = (ArithmeticExpression) ctx.getChild(i + 1).accept(this);
                i += 2;
            }
            expression = createArithmeticExpression(
                expression,
                subExpression,
                ArithmeticOperatorType.PLUS
            );
        }
        return expression;
    }

    @Override
    public Query visitQuery(PredicateParser.QueryContext ctx) {
        List<FromItem> fromItems = visitFromClause( ctx.fromClause() );
        PredicateParser.SelectClauseContext selectClauseContext = ctx.selectClause();
        List<Expression> selectItems;
        boolean distinct = false;
        if ( selectClauseContext == null ) {
            selectItems = new ArrayList<>(fromItems.size());
            for ( FromItem fromItem : fromItems ) {
                selectItems.add( new Path( fromItem.getAlias(), Collections.emptyList(), fromItem.getType() ) );
            }
        } else {
            distinct = selectClauseContext.DISTINCT() != null;
            List<PredicateParser.ExpressionContext> selectItemContexts = selectClauseContext.expression();
            selectItems = new ArrayList<>(selectItemContexts.size());
            for ( PredicateParser.ExpressionContext selectItemContext : selectItemContexts ) {
                selectItems.add( (Expression) selectItemContext.accept( this ) );
            }
        }
        Predicate wherePredicate = visitWhereClause( ctx.whereClause() );
        return new Query( null, selectItems, distinct, fromItems, wherePredicate );
    }

    @Override
    public Predicate visitWhereClause(PredicateParser.WhereClauseContext ctx) {
        if ( ctx == null ) {
            return null;
        }
        return (Predicate) ctx.predicate().accept( this );
    }

    @Override
    public List<FromItem> visitFromClause(PredicateParser.FromClauseContext ctx) {
        List<PredicateParser.FromItemContext> fromItemContexts = ctx.fromItem();
        List<FromItem> fromItems = new ArrayList<>(fromItemContexts.size());
        Map<String, DomainType> finalFromNodes = fromItemContexts.size() > 1 ? new HashMap<>() : null;
        for ( int i = 0; i < fromItemContexts.size(); i++ ) {
            PredicateParser.FromItemContext fromItemContext = fromItemContexts.get( i );
            fromNodes = new HashMap<>();
            fromItems.add( visitFromItem( fromItemContext ) );
            if ( finalFromNodes != null ) {
                finalFromNodes.putAll( fromNodes );
            }
        }
        if ( finalFromNodes != null ) {
            fromNodes = finalFromNodes;
        }
        return fromItems;
    }

    @Override
    public FromItem visitFromItem(PredicateParser.FromItemContext ctx) {
        PredicateParser.FromRootContext fromRootContext = ctx.fromRoot();
        DomainType domainType = domainModel.getType( fromRootContext.domainTypeName().getText() );
        if ( domainType == null ) {
            throw unknownType( fromRootContext.domainTypeName().getText() );
        }
        String alias = visitVariable( fromRootContext.variable() );
        fromNodes.put( alias, domainType );
        List<Join> joins = visitJoins( ctx.join() );
        return new FromItem( domainType, alias, joins );
    }

    private List<Join> visitJoins(List<PredicateParser.JoinContext> joinContexts) {
        List<Join> joins = new ArrayList<>(joinContexts.size());
        for ( PredicateParser.JoinContext joinContext : joinContexts ) {
            TerminalNode terminalNode = (TerminalNode) joinContext.getChild( 0 );
            JoinType joinType;
            switch ( terminalNode.getSymbol().getType() ) {
                case PredicateLexer.LEFT:
                    joinType = JoinType.LEFT;
                    break;
                case PredicateLexer.RIGHT:
                    joinType = JoinType.RIGHT;
                    break;
                case PredicateLexer.FULL:
                    joinType = JoinType.FULL;
                    break;
                default:
                    joinType = JoinType.INNER;
                    break;
            }
            PredicateParser.JoinTargetContext joinTargetContext = joinContext.joinTarget();
            DomainType domainType = domainModel.getType( joinTargetContext.domainTypeName().getText() );
            if ( domainType == null ) {
                throw unknownType( joinTargetContext.domainTypeName().getText() );
            }
            String alias = visitVariable( joinTargetContext.variable() );
            fromNodes.put( alias, domainType );
            joins.add( new Join( domainType, alias, joinType, (Predicate) joinContext.predicate().accept( this ) ) );
        }
        return Collections.unmodifiableList( joins );
    }

    @Override
    public String visitVariable(PredicateParser.VariableContext ctx) {
        return ctx.identifier().getText();
    }

    @Override
    public Expression visitGroupedPredicate(PredicateParser.GroupedPredicateContext ctx) {
        return (Expression) ctx.predicate().accept( this);
    }

    @Override
    public Expression visitNegatedPredicate(PredicateParser.NegatedPredicateContext ctx) {
        return ((Predicate) ctx.predicate().accept(this)).negated();
    }

    @Override
    public Predicate visitOrPredicate(PredicateParser.OrPredicateContext ctx) {
        List<PredicateParser.PredicateContext> predicate = ctx.predicate();
        Predicate left = (Predicate) predicate.get(0).accept(this);
        Predicate right = (Predicate) predicate.get(1).accept(this);

        CompoundPredicate disjunctivePredicate;
        List<Predicate> mutablePredicateList;
        if (left instanceof CompoundPredicate && !((CompoundPredicate) left).isConjunction() && !left.isNegated()) {
            disjunctivePredicate = (CompoundPredicate) left;
            mutablePredicateList = ((UnmodifiableList<Predicate>) disjunctivePredicate.getPredicates()).getDelegate();
        } else {
            disjunctivePredicate = new CompoundPredicate(domainModel.getPredicateDefaultResultType(), unmodifiable(mutablePredicateList = new ArrayList<>(2)), false);
            mutablePredicateList.add(left);
        }
        mutablePredicateList.add(right);
        return disjunctivePredicate;
    }

    @Override
    public Predicate visitAndPredicate(PredicateParser.AndPredicateContext ctx) {
        List<PredicateParser.PredicateContext> predicate = ctx.predicate();
        Predicate left = (Predicate) predicate.get(0).accept(this);
        Predicate right = (Predicate) predicate.get(1).accept(this);

        CompoundPredicate conjunctivePredicate;
        List<Predicate> mutablePredicateList;
        if (left instanceof CompoundPredicate && ((CompoundPredicate) left).isConjunction() && !left.isNegated()) {
            conjunctivePredicate = (CompoundPredicate) left;
            mutablePredicateList = ((UnmodifiableList<Predicate>) conjunctivePredicate.getPredicates()).getDelegate();
        } else {
            conjunctivePredicate = new CompoundPredicate(domainModel.getPredicateDefaultResultType(), unmodifiable(mutablePredicateList = new ArrayList<>(2)), true);
            mutablePredicateList.add(left);
        }
        mutablePredicateList.add(right);
        return conjunctivePredicate;
    }

    private List<Predicate> unmodifiable(List<Predicate> predicates) {
        return new UnmodifiableList<>(predicates);
    }

    @Override
    public Predicate visitIsNullPredicate(PredicateParser.IsNullPredicateContext ctx) {
        Expression left = (Expression) ctx.expression().accept( this);

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicate.NULLNESS);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicate.NULLNESS);
        } else {
            List<DomainType> operandTypes = Collections.singletonList(left.getType());
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicate.NULLNESS, operandTypes);
            } else {
                return new IsNullPredicate(domainType, left, ctx.NOT() != null);
            }
        }
    }

    @Override
    public Predicate visitIsEmptyPredicate(PredicateParser.IsEmptyPredicateContext ctx) {
        Expression left = (Expression) ctx.expression().accept( this);

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicate.COLLECTION);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicate.COLLECTION);
        } else {
            List<DomainType> operandTypes = Collections.singletonList(left.getType());
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicate.COLLECTION, operandTypes);
            } else {
                return new IsEmptyPredicate(domainType, left, ctx.NOT() != null);
            }
        }
    }

    @Override
    public Expression visitInequalityPredicate(PredicateParser.InequalityPredicateContext ctx) {
        return createComparisonPredicate(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ComparisonOperator.NOT_EQUAL
        );
    }

    @Override
    public Expression visitLessThanOrEqualPredicate(PredicateParser.LessThanOrEqualPredicateContext ctx) {
        return createComparisonPredicate(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ComparisonOperator.LOWER_OR_EQUAL
        );
    }

    @Override
    public Expression visitEqualityPredicate(PredicateParser.EqualityPredicateContext ctx) {
        return createComparisonPredicate(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ComparisonOperator.EQUAL
        );
    }

    @Override
    public Expression visitGreaterThanPredicate(PredicateParser.GreaterThanPredicateContext ctx) {
        return createComparisonPredicate(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ComparisonOperator.GREATER
        );
    }

    @Override
    public Expression visitLessThanPredicate(PredicateParser.LessThanPredicateContext ctx) {
        return createComparisonPredicate(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ComparisonOperator.LOWER
        );
    }

    @Override
    public Expression visitGreaterThanOrEqualPredicate(PredicateParser.GreaterThanOrEqualPredicateContext ctx) {
        return createComparisonPredicate(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ComparisonOperator.GREATER_OR_EQUAL
        );
    }

    protected Predicate createComparisonPredicate(ArithmeticExpression left, ArithmeticExpression right, ComparisonOperator comparisonOperator) {
        List<DomainType> operandTypes = Arrays.asList(left.getType(), right.getType());
        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), comparisonOperator.getDomainPredicate());

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), comparisonOperator.getDomainPredicate());
        } else {
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(comparisonOperator.getDomainPredicate(), operandTypes);
            } else {
                return new ComparisonPredicate(domainType, left, right, comparisonOperator);
            }
        }
    }

    @Override
    public Expression visitInPredicate(PredicateParser.InPredicateContext ctx) {
        ArithmeticExpression left = (ArithmeticExpression) ctx.expression().accept(this);
        List<ArithmeticExpression> inItems = getExpressionList(ctx.inList().expression());

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicate.EQUALITY);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicate.EQUALITY);
        } else {
            List<DomainType> operandTypes = new ArrayList<>(inItems.size() + 1);
            operandTypes.add(left.getType());
            for (int i = 0; i < inItems.size(); i++) {
                ArithmeticExpression inItem = inItems.get(i);
                operandTypes.add(inItem.getType());
            }
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicate.EQUALITY, operandTypes);
            } else {
                return new InPredicate(domainType, left, inItems, ctx.NOT() != null);
            }
        }
    }

    @Override
    public Predicate visitBetweenPredicate(PredicateParser.BetweenPredicateContext ctx) {
        ArithmeticExpression left = (ArithmeticExpression) ctx.lhs.accept(this);
        ArithmeticExpression begin = (ArithmeticExpression) ctx.begin.accept(this);
        ArithmeticExpression upper = (ArithmeticExpression) ctx.end.accept(this);

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicate.RELATIONAL);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicate.RELATIONAL);
        } else {
            List<DomainType> operandTypes = Arrays.asList(left.getType(), begin.getType(), upper.getType());
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicate.RELATIONAL, operandTypes);
            } else {
                return new BetweenPredicate(domainType, left, upper, begin);
            }
        }
    }

    @Override
    public Expression visitBooleanFunction(PredicateParser.BooleanFunctionContext ctx) {
        Expression expression = (Expression) super.visitBooleanFunction( ctx);
        DomainType booleanDomainType = domainModel.getPredicateDefaultResultType();
        if (expression.getType() == booleanDomainType) {
            if (expression instanceof Predicate) {
                return expression;
            }

            return new ExpressionPredicate(booleanDomainType, expression, false);
        }

        throw new TypeErrorException("Invalid use of non-boolean returning function: " + ctx.getText());
    }

    @Override
    public Expression visitGroupedExpression(PredicateParser.GroupedExpressionContext ctx) {
        return (Expression) ctx.expression().accept( this);
    }

    @Override
    public Expression visitMultiplicativeExpression(PredicateParser.MultiplicativeExpressionContext ctx) {
        ArithmeticExpression lhs = (ArithmeticExpression) ctx.lhs.accept(this);
        ArithmeticExpression rhs = (ArithmeticExpression) ctx.rhs.accept(this);
        switch (ctx.op.getType()) {
            case PredicateParser.SLASH:
                return createArithmeticExpression(lhs, rhs, ArithmeticOperatorType.DIVIDE);
            case PredicateParser.ASTERISK:
                return createArithmeticExpression(lhs, rhs, ArithmeticOperatorType.MULTIPLY);
            case PredicateParser.PERCENT:
                return createArithmeticExpression(lhs, rhs, ArithmeticOperatorType.MODULO);
            default:
                throw new SyntaxErrorException("Invalid multiplicative operator: " + ctx.op.getText());
        }
    }

    @Override
    public Expression visitAdditiveExpression(PredicateParser.AdditiveExpressionContext ctx) {
        ArithmeticExpression lhs = (ArithmeticExpression) ctx.lhs.accept(this);
        ArithmeticExpression rhs = (ArithmeticExpression) ctx.rhs.accept(this);
        switch (ctx.op.getType()) {
            case PredicateParser.PLUS:
                return createArithmeticExpression(lhs, rhs, ArithmeticOperatorType.PLUS);
            case PredicateParser.MINUS:
                return createArithmeticExpression(lhs, rhs, ArithmeticOperatorType.MINUS);
            default:
                throw new SyntaxErrorException("Invalid additive operator: " + ctx.op.getText());
        }
    }

    protected ArithmeticExpression createArithmeticExpression(ArithmeticExpression left, ArithmeticExpression right, ArithmeticOperatorType operator) {
        List<DomainType> operandTypes = Arrays.asList(left.getType(), right.getType());
        DomainOperationTypeResolver operationTypeResolver = domainModel.getOperationTypeResolver(left.getType().getName(), operator.getDomainOperator());
        if (operationTypeResolver == null) {
            throw missingOperationTypeResolver(left.getType(), operator.getDomainOperator());
        } else {
            DomainType domainType = operationTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolveOperationType(operator.getDomainOperator(), operandTypes);
            } else {
                return new ChainingArithmeticExpression(domainType, left, right, operator);
            }
        }
    }

    @Override
    public Expression visitUnaryMinusExpression(PredicateParser.UnaryMinusExpressionContext ctx) {
        ArithmeticExpression left = (ArithmeticExpression) ctx.expression().accept(this);
        DomainOperationTypeResolver operationTypeResolver = domainModel.getOperationTypeResolver(left.getType().getName(), DomainOperator.UNARY_MINUS);
        if (operationTypeResolver == null) {
            throw missingOperationTypeResolver(left.getType(), DomainOperator.UNARY_MINUS);
        } else {
            List<DomainType> operandTypes = Collections.singletonList(left.getType());
            DomainType domainType = operationTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolveOperationType(DomainOperator.UNARY_MINUS, operandTypes);
            } else {
                return new ArithmeticFactor(domainType, left, true);
            }
        }
    }

    @Override
    public Expression visitUnaryPlusExpression(PredicateParser.UnaryPlusExpressionContext ctx) {
        ArithmeticExpression left = (ArithmeticExpression) ctx.expression().accept(this);
        DomainOperationTypeResolver operationTypeResolver = domainModel.getOperationTypeResolver(left.getType().getName(), DomainOperator.UNARY_PLUS);
        if (operationTypeResolver == null) {
            throw missingOperationTypeResolver(left.getType(), DomainOperator.UNARY_PLUS);
        } else {
            List<DomainType> operandTypes = Collections.singletonList(left.getType());
            DomainType domainType = operationTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolveOperationType(DomainOperator.UNARY_PLUS, operandTypes);
            } else if (domainType == left.getType()) {
                // Don't create a wrapper for a unary plus if the type doesn't change
                return (Expression) ctx.expression().accept( this);
            } else {
                return new ArithmeticFactor(domainType, left, false);

            }
        }
    }

//    @Override
//    public Predicate visitStringInCollectionPredicate(PredicateParser.StringInCollectionPredicateContext ctx) {
//        return new StringInCollectionPredicate((StringAtom) ctx.string_expression().accept(this), (CollectionAtom) ctx.collection_attribute().accept(this), ctx.not != null);
//    }
//    @Override
//    public Expression visitCollectionAttribute(PredicateParser.CollectionAttributeContext ctx) {
//        return new CollectionAtom(new Path(ctx.identifier().getText(), TermType.COLLECTION));
//    }

    @Override
    public Expression visitDateLiteral(PredicateParser.DateLiteralContext ctx) {
        return new Literal(literalFactory.ofDateString(compileContext, ctx.datePart().getText()));
    }

    @Override
    public Expression visitTimeLiteral(PredicateParser.TimeLiteralContext ctx) {
        StringBuilder sb = new StringBuilder(12);
        PredicateParser.TimePartContext timePartContext = ctx.timePart();
        sb.append(timePartContext.getText());

        if (ctx.fraction != null) {
            sb.append('.');
            sb.append(ctx.fraction.getText());
        }
        return new Literal(literalFactory.ofTimeString(compileContext, sb.toString()));
    }

    @Override
    public Expression visitTimestampLiteral(PredicateParser.TimestampLiteralContext ctx) {
        StringBuilder sb = new StringBuilder(23);
        sb.append(ctx.datePart().getText());
        PredicateParser.TimePartContext timePartContext = ctx.timePart();
        if (timePartContext != null) {
            sb.append(' ');
            sb.append(timePartContext.getText());

            if (ctx.fraction != null) {
                sb.append('.');
                sb.append(ctx.fraction.getText());
            }
        }
        return new Literal(literalFactory.ofDateTimeString(compileContext, sb.toString()));
    }

    @Override
    public Expression visitTemporalIntervalLiteral(PredicateParser.TemporalIntervalLiteralContext ctx) {
        int years = parseTemporalAmount(ctx.years, "years");
        int months = parseTemporalAmount(ctx.months, "months");
        int days = parseTemporalAmount(ctx.days, "days");
        int hours = parseTemporalAmount(ctx.hours, "hours");
        int minutes = parseTemporalAmount(ctx.minutes, "minutes");
        int seconds = parseTemporalAmount(ctx.seconds, "seconds");
        return new Literal(literalFactory.ofTemporalAmounts(compileContext, years, months, days, hours, minutes, seconds));
    }

    protected static int parseTemporalAmount(Token token, String field) {
        if (token == null) {
            return 0;
        }
        int amount = 0;
        NumberFormatException exception = null;
        String amountString = token.getText();
        try {
            amount = Integer.parseInt(amountString);
        } catch (NumberFormatException ex) {
            exception = ex;
        }
        if (exception != null || amount < 0) {
            throw new SyntaxErrorException("Illegal value given for temporal field '" + field + "': " + amountString, exception);
        }
        return amount;
    }

    @Override
    public Expression visitCollectionLiteral(PredicateParser.CollectionLiteralContext ctx) {
        List<Literal> literalList = getExpressionList(ctx.literal());
        CollectionDomainType collectionDomainType;
        if (literalList.isEmpty()) {
            collectionDomainType = domainModel.getCollectionType(null);
        } else {
            collectionDomainType = domainModel.getCollectionType(literalList.get(0).getType().getName());
        }

        return new CollectionLiteral(literalList, literalFactory.ofCollectionValues(compileContext, collectionDomainType, literalList));
    }

    @Override
    public Expression visitPath(PredicateParser.PathContext ctx) {
        return createPathExpression(ctx);
    }

    @Override
    public Expression visitPathPredicate(PathPredicateContext ctx) {
        Expression expression = createPathExpression(ctx.path());
        DomainType type = expression.getType();
        if (!type.equals(domainModel.getPredicateDefaultResultType())) {
            throw unsupportedType(expression.getType().toString());
        }
        return new ExpressionPredicate(type, expression, false);
    }

    protected Expression createPathExpression(PathContext ctx) {
        PredicateParser.IdentifierContext identifierContext = ctx.identifier();
        PredicateParser.PathAttributesContext pathAttributesContext = ctx.pathAttributes();
        ArrayList<EntityDomainTypeAttribute> pathAttributes = new ArrayList<>();
        if (identifierContext == null) {
            Expression base = (Expression) ctx.functionInvocation().accept( this);
            DomainType domainType = visitPathAttributes(base.getType(), pathAttributes, pathAttributesContext);
            return new Path((ArithmeticExpression) base, Collections.unmodifiableList(pathAttributes), domainType);
        } else {
            String alias = identifierContext.getText();
            DomainType type = resolveAlias(alias);
            if (type == null) {
                List<PredicateParser.IdentifierContext> identifiers;
                if (pathAttributesContext != null) {
                    identifiers = pathAttributesContext.identifier();
                    if (identifiers.size() == 1) {
                        type = domainModel.getType(alias);
                        if (type instanceof EnumDomainType) {
                            EnumDomainType enumDomainType = (EnumDomainType) type;
                            String enumKey = identifiers.get(0).getText();
                            return new EnumLiteral(enumDomainType.getEnumValues().get(enumKey), literalFactory.ofEnumValue(compileContext, enumDomainType, enumKey));
                        }
                    }
                    ImplicitRootProvider implicitRootProvider = compileContext.getImplicitRootProvider();
                    if (implicitRootProvider != null) {
                        List<String> pathParts = new ArrayList<>(identifiers.size() + 1);
                        pathParts.add(alias);
                        for (PredicateParser.IdentifierContext identifier : identifiers) {
                            pathParts.add(identifier.getText());
                        }
                        String rootAlias = implicitRootProvider.determineImplicitRoot(pathParts, compileContext);
                        if (rootAlias != null) {
                            type = resolveAlias(rootAlias);
                            if (type != null) {
                                type = visitPathAttribute(type, pathAttributes, alias);
                                DomainType domainType = visitPathAttributes(type, pathAttributes, pathAttributesContext);
                                return new Path(rootAlias, Collections.unmodifiableList(pathAttributes), domainType);
                            }
                        }
                    }
                } else {
                    ImplicitRootProvider implicitRootProvider = compileContext.getImplicitRootProvider();
                    if (implicitRootProvider != null) {
                        String rootAlias = implicitRootProvider.determineImplicitRoot(Collections.singletonList(alias), compileContext);
                        if (rootAlias != null) {
                            type = resolveAlias(rootAlias);
                            if (type != null) {
                                DomainType domainType = visitPathAttribute(type, pathAttributes, alias);
                                return new Path(rootAlias, Collections.unmodifiableList(pathAttributes), domainType);
                            }
                        }
                    }
                }
                throw unknownType(alias);
            }
            DomainType domainType = visitPathAttributes(type, pathAttributes, pathAttributesContext);
            return new Path(alias, Collections.unmodifiableList(pathAttributes), domainType);
        }
    }

    protected DomainType visitPathAttribute(DomainType type, ArrayList<EntityDomainTypeAttribute> pathAttributes, String pathElement) {
        if (type instanceof CollectionDomainType) {
            type = ((CollectionDomainType) type).getElementType();
        }
        if (type instanceof EntityDomainType) {
            EntityDomainType entityType = ((EntityDomainType) type);
            EntityDomainTypeAttribute attribute = entityType.getAttribute(pathElement);
            pathAttributes.add(attribute);
            if (attribute == null) {
                throw unknownEntityAttribute(entityType, pathElement);
            } else {
                type = attribute.getType();
            }
        } else {
            throw unsupportedType(type.toString());
        }
        return type;
    }

    protected DomainType visitPathAttributes(DomainType type, ArrayList<EntityDomainTypeAttribute> pathAttributes, PredicateParser.PathAttributesContext pathAttributesContext) {
        if (pathAttributesContext != null) {
            List<PredicateParser.IdentifierContext> identifiers = pathAttributesContext.identifier();
            int size = identifiers.size();
            pathAttributes.ensureCapacity(size);
            for (int pathElemIdx = 0; pathElemIdx < size; pathElemIdx++) {
                String pathElement = identifiers.get(pathElemIdx).getText();
                if (type instanceof CollectionDomainType) {
                    type = ((CollectionDomainType) type).getElementType();
                }
                if (type instanceof EntityDomainType) {
                    EntityDomainType entityType = ((EntityDomainType) type);
                    EntityDomainTypeAttribute attribute = entityType.getAttribute(pathElement);
                    pathAttributes.add(attribute);
                    if (attribute == null) {
                        throw unknownEntityAttribute(entityType, pathElement);
                    } else {
                        type = attribute.getType();
                    }
                } else {
                    throw unsupportedType(type.toString());
                }
            }
        }
        return type;
    }

    @Override
    public Expression visitIndexedFunctionInvocation(PredicateParser.IndexedFunctionInvocationContext ctx) {
        String functionName = ctx.name.getText();
        DomainFunction function = domainModel.getFunction(functionName);
        if (function == null) {
            throw unknownFunction(functionName);
        } else {
            List<Expression> literalList = getExpressionList(ctx.predicateOrExpression());
            if (function.getArgumentCount() != -1 && literalList.size() > function.getArgumentCount()) {
                throw new DomainModelException(String.format("Function '%s' expects at most %d arguments but found %d",
                        function.getName(),
                        function.getArgumentCount(),
                        literalList.size()
                ));
            }
            if (literalList.size() < function.getMinArgumentCount()) {
                throw new DomainModelException(String.format("Function '%s' expects at least %d arguments but found %d",
                                                             function.getName(),
                                                             function.getMinArgumentCount(),
                                                             literalList.size()
                ));
            }
            Map<DomainFunctionArgument, Expression> arguments = new LinkedHashMap<>(literalList.size());
            Map<DomainFunctionArgument, DomainType> argumentTypes = new HashMap<>(literalList.size());
            int i = 0;
            int lastIdx = function.getArguments().size() - 1;
            int end = Math.min(lastIdx, literalList.size());
            for (; i < end; i++) {
                DomainFunctionArgument domainFunctionArgument = function.getArguments().get(i);
                argumentTypes.put(domainFunctionArgument, literalList.get(i).getType());
                arguments.put(domainFunctionArgument, literalList.get(i));
            }
            if (lastIdx != -1) {
                DomainFunctionArgument domainFunctionArgument = function.getArguments().get(lastIdx);
                if (function.getArgumentCount() == -1) {
                    // Varargs
                    List<Expression> varArgs = new ArrayList<>(literalList.size() - i);
                    argumentTypes.put(domainFunctionArgument, domainFunctionArgument.getType());
                    for (; i < literalList.size(); i++) {
                        varArgs.add(literalList.get(i));
                    }
                    arguments.put(domainFunctionArgument, new Literal(new DefaultResolvedLiteral(domainFunctionArgument.getType(), Collections.unmodifiableList(varArgs))));
                } else if (i < literalList.size()) {
                    argumentTypes.put(domainFunctionArgument, literalList.get(i).getType());
                    arguments.put(domainFunctionArgument, literalList.get(i));
                }
            }
            DomainFunctionTypeResolver functionTypeResolver = domainModel.getFunctionTypeResolver(functionName);
            try {
                DomainType functionType = functionTypeResolver.resolveType(domainModel, function, argumentTypes);
                return new FunctionInvocation(function, Collections.unmodifiableMap(arguments), functionType);
            } catch (DomainTypeResolverException ex) {
                throw new DomainModelException(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public Expression visitEntityLiteral(PredicateParser.EntityLiteralContext ctx) {
        String entityOrFunctionName = ctx.name.getText();
        DomainType type = domainModel.getType(entityOrFunctionName);
        if (type instanceof EntityDomainType) {
            EntityDomainType entityDomainType = (EntityDomainType) type;
            List<PredicateParser.IdentifierContext> argNames = ctx.identifier();
            argNames.remove(0);
            return createEntityLiteral(entityDomainType, argNames, getExpressionList(ctx.predicateOrExpression()));
        } else {
            throw unknownType(entityOrFunctionName);
        }
    }

    protected Expression createEntityLiteral(EntityDomainType entityDomainType, List<PredicateParser.IdentifierContext> argNames, List<Expression> literalList) {
        Map<EntityDomainTypeAttribute, Literal> arguments = new LinkedHashMap<>(literalList.size());
        for (int i = 0; i < literalList.size(); i++) {
            EntityDomainTypeAttribute attribute = entityDomainType.getAttribute(argNames.get(i).getText());
            if (attribute == null) {
                throw new DomainModelException("Invalid attribute name '" + argNames.get(i).getText() + "'! Entity '" + entityDomainType.getName() + "' expects the following attribute names: " + entityDomainType.getAttributes().keySet());
            }
            Expression expression = literalList.get(i);
            if (!(expression instanceof Literal)) {
                throw new DomainModelException("Invalid use of non-literal for entity literal at attribute name '" + argNames.get(i).getText() + "'!");
            }
            arguments.put(attribute, (Literal) expression);
        }
        Map<EntityDomainTypeAttribute, Literal> attributeValues = Collections.unmodifiableMap(arguments);
        return new EntityLiteral(attributeValues, literalFactory.ofEntityAttributeValues(compileContext, entityDomainType, attributeValues));
    }

    @Override
    public Expression visitNamedInvocation(PredicateParser.NamedInvocationContext ctx) {
        String entityOrFunctionName = ctx.name.getText();
        DomainFunction function = domainModel.getFunction(entityOrFunctionName);
        if (function == null) {
            DomainType type = domainModel.getType(entityOrFunctionName);
            if (type instanceof EntityDomainType) {
                EntityDomainType entityDomainType = (EntityDomainType) type;
                List<PredicateParser.IdentifierContext> argNames = ctx.identifier();
                argNames.remove(0);
                return createEntityLiteral(entityDomainType, argNames, getExpressionList(ctx.predicateOrExpression()));
            } else {
                throw unknownFunction(entityOrFunctionName);
            }
        } else {
            List<PredicateParser.IdentifierContext> argNames = ctx.identifier();
            argNames.remove(0);
            List<Expression> literalList = getExpressionList(ctx.predicateOrExpression());
            if (function.getArgumentCount() != -1 && literalList.size() > function.getArgumentCount()) {
                throw new DomainModelException(String.format("Function '%s' expects at most %d arguments but found %d",
                        function.getName(),
                        function.getArgumentCount(),
                        literalList.size()
                ));
            }
            if (literalList.size() < function.getMinArgumentCount()) {
                throw new DomainModelException(String.format("Function '%s' expects at least %d arguments but found %d",
                                                             function.getName(),
                                                             function.getMinArgumentCount(),
                                                             literalList.size()
                ));
            }
            Map<DomainFunctionArgument, Expression> arguments = new LinkedHashMap<>(literalList.size());
            Map<DomainFunctionArgument, DomainType> argumentTypes = new HashMap<>(literalList.size());
            for (int i = 0; i < literalList.size(); i++) {
                DomainFunctionArgument domainFunctionArgument = function.getArgument(argNames.get(i).getText());
                if (domainFunctionArgument == null) {
                    List<String> argumentNames = new ArrayList<>(function.getArguments().size());
                    for (DomainFunctionArgument argument : function.getArguments()) {
                        argumentNames.add(argument.getName());
                    }
                    throw new DomainModelException("Invalid argument name '" + argNames.get(i).getText() + "'! Function '" + function.getName() + "' expects the following argument names: " + argumentNames);
                }
                argumentTypes.put(domainFunctionArgument, literalList.get(i).getType());
                arguments.put(domainFunctionArgument, literalList.get(i));
            }
            DomainFunctionTypeResolver functionTypeResolver = domainModel.getFunctionTypeResolver(entityOrFunctionName);
            try {
                DomainType functionType = functionTypeResolver.resolveType(domainModel, function, argumentTypes);
                return new FunctionInvocation(function, Collections.unmodifiableMap(arguments), functionType);
            } catch (DomainTypeResolverException ex) {
                throw new DomainModelException(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public Expression visitStringLiteral(PredicateParser.StringLiteralContext ctx) {
        List<ParseTree> children = ctx.children;
        int size = children.size();
        if (size == 2) {
            return new Literal(literalFactory.ofString(compileContext, ""));
        }
        return new Literal(literalFactory.ofString(compileContext, LiteralFactory.unescapeString(ctx.getText())));
    }

    @Override
    public Expression visitTerminal(TerminalNode node) {
        if (node.getSymbol().getType() == PredicateLexer.EOF) {
            return null;
        }
        switch (node.getSymbol().getType()) {
            case PredicateLexer.TRUE:
                return getBooleanTrueLiteral();
            case PredicateLexer.FALSE:
                return getBooleanFalseLiteral();
            case PredicateLexer.NUMERIC_LITERAL:
                return new Literal(literalFactory.ofNumericString(compileContext, node.getText()));
            case PredicateLexer.INTEGER_LITERAL:
                return new Literal(literalFactory.ofIntegerString(compileContext, node.getText()));
            default:
                throw new IllegalStateException("Terminal node '" + node.getText() + "' not handled");
        }
    }

    @SuppressWarnings("unchecked")
    protected final <T> List<T> getExpressionList(List<? extends ParserRuleContext> items) {
        List<T> expressions = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            expressions.add((T) items.get(i).accept(this));
        }
        return Collections.unmodifiableList(expressions);
    }

    protected Literal getBooleanLiteral(boolean value) {
        return value ? getBooleanTrueLiteral() : getBooleanFalseLiteral();
    }

    protected Literal getBooleanTrueLiteral() {
        if (cachedBooleanTrueLiteral == null) {
            cachedBooleanTrueLiteral = new Literal(literalFactory.ofBoolean(compileContext, true));
        }
        return cachedBooleanTrueLiteral;
    }

    protected Literal getBooleanFalseLiteral() {
        if (cachedBooleanFalseLiteral == null) {
            cachedBooleanFalseLiteral = new Literal(literalFactory.ofBoolean(compileContext, false));
        }
        return cachedBooleanFalseLiteral;
    }

    protected TypeErrorException typeError(DomainType t1, DomainType t2, DomainOperator operator) {
        return new TypeErrorException(String.format("%s %s %s", t1, operator, t2));
    }

    protected DomainModelException missingPredicateTypeResolver(DomainType type, DomainPredicate predicateType) {
        return new DomainModelException(String.format("Missing predicate type resolver for type %s and predicate %s", type, predicateType));
    }

    protected DomainModelException missingOperationTypeResolver(DomainType type, DomainOperator operator) {
        return new DomainModelException(String.format("Missing operation type resolver for type %s and operator %s", type, operator));
    }

    protected TypeErrorException typeError(DomainType t1, DomainType t2, DomainPredicate predicateType) {
        return new TypeErrorException(String.format("%s %s %s", t1, predicateType, t2));
    }

    protected DomainModelException unknownType(String typeName) {
        return new DomainModelException(String.format("Undefined type '%s'", typeName));
    }

    protected DomainModelException unknownEntityAttribute(EntityDomainType entityDomainType, String attributeName) {
        return new DomainModelException(String.format("Attribute %s undefined for entity %s", attributeName, entityDomainType));
    }

    protected DomainModelException unknownFunction(String identifier) {
        return new DomainModelException(String.format("Undefined function '%s'", identifier));
    }

    protected TypeErrorException unsupportedType(String typeName) {
        return new TypeErrorException(String.format("Resolved type for identifier %s is not supported", typeName));
    }

    protected TypeErrorException cannotResolvePredicateType(DomainPredicate predicateType, List<DomainType> operandTypes) {
        return new TypeErrorException(String.format("Cannot resolve predicate type for predicate %s and operand types %s", predicateType, operandTypes));
    }

    protected TypeErrorException cannotResolveOperationType(DomainOperator operator, List<DomainType> operandTypes) {
        return new TypeErrorException(String.format("Cannot resolve operation type for operator %s and operand types %s", operator, operandTypes));
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class UnmodifiableList<E> implements List<E> {

        private final List<E> list;

        UnmodifiableList(List<E> list) {
            this.list = list;
        }

        List<E> getDelegate() {
            return list;
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public boolean isEmpty() {
            return list.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return list.contains(o);
        }

        @Override
        public Object[] toArray() {
            return list.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return list.toArray(a);
        }

        @Override
        public String toString() {
            return list.toString();
        }

        @Override
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                private final Iterator<? extends E> i = list.iterator();

                @Override
                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public E next() {
                    return i.next();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void forEachRemaining(Consumer<? super E> action) {
                    i.forEachRemaining(action);
                }
            };
        }

        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> coll) {
            return list.containsAll(coll);
        }

        @Override
        public boolean addAll(Collection<? extends E> coll) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> coll) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> coll) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        // Override default methods in Collection
        @Override
        public void forEach(Consumer<? super E> action) {
            list.forEach(action);
        }

        @Override
        public boolean removeIf(java.util.function.Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Spliterator<E> spliterator() {
            return (Spliterator<E>) list.spliterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Stream<E> stream() {
            return (Stream<E>) list.stream();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Stream<E> parallelStream() {
            return (Stream<E>) list.parallelStream();
        }

        @Override
        public boolean equals(Object o) {
            return o == this || list.equals(o);
        }

        @Override
        public int hashCode() {
            return list.hashCode();
        }

        @Override
        public E get(int index) {
            return list.get(index);
        }

        @Override
        public E set(int index, E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int index, E element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public E remove(int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int indexOf(Object o) {
            return list.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return list.lastIndexOf(o);
        }

        @Override
        public boolean addAll(int index, Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(UnaryOperator<E> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sort(Comparator<? super E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListIterator<E> listIterator() {
            return listIterator(0);
        }

        @Override
        public ListIterator<E> listIterator(final int index) {
            return new ListIterator<E>() {
                private final ListIterator<? extends E> i = list.listIterator(index);

                @Override
                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public E next() {
                    return i.next();
                }

                @Override
                public boolean hasPrevious() {
                    return i.hasPrevious();
                }

                @Override
                public E previous() {
                    return i.previous();
                }

                @Override
                public int nextIndex() {
                    return i.nextIndex();
                }

                @Override
                public int previousIndex() {
                    return i.previousIndex();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void set(E e) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void add(E e) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public void forEachRemaining(Consumer<? super E> action) {
                    i.forEachRemaining(action);
                }
            };
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            return new UnmodifiableList<>(list.subList(fromIndex, toIndex));
        }

    }

}
