/*
 * Copyright 2019 - 2020 Blazebit.
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
import com.blazebit.domain.runtime.model.DomainPredicateType;
import com.blazebit.domain.runtime.model.DomainPredicateTypeResolver;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.ResolvedLiteral;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.ArithmeticOperatorType;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsEmptyPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.SyntaxErrorException;
import com.blazebit.expression.TypeErrorException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PredicateModelGenerator extends PredicateParserBaseVisitor<Expression> {

    private final DomainModel domainModel;
    private final LiteralFactory literalFactory;
    private final ExpressionCompiler.Context compileContext;
    private DomainType cachedBooleanDomainType;
    private Literal cachedBooleanTrueLiteral;
    private Literal cachedBooleanFalseLiteral;

    public PredicateModelGenerator(DomainModel domainModel, LiteralFactory literalFactory, ExpressionCompiler.Context compileContext) {
        this.domainModel = domainModel;
        this.literalFactory = literalFactory;
        this.compileContext = compileContext;
    }

    @Override
    public Expression visitParsePredicate(PredicateParser.ParsePredicateContext ctx) {
        return ctx.predicate().accept(this);
    }

    @Override
    public Expression visitParseExpression(PredicateParser.ParseExpressionContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public Expression visitGroupedPredicate(PredicateParser.GroupedPredicateContext ctx) {
        return ctx.predicate().accept(this);
    }

    @Override
    public Expression visitNegatedPredicate(PredicateParser.NegatedPredicateContext ctx) {
        Predicate predicate = (Predicate) ctx.predicate().accept(this);
        predicate.setNegated(!predicate.isNegated());
        return predicate;
    }

    @Override
    public Predicate visitOrPredicate(PredicateParser.OrPredicateContext ctx) {
        List<PredicateParser.PredicateContext> predicate = ctx.predicate();
        Predicate left = (Predicate) predicate.get(0).accept(this);
        Predicate right = (Predicate) predicate.get(1).accept(this);

        CompoundPredicate disjunctivePredicate;
        if (left instanceof CompoundPredicate && !((CompoundPredicate) left).isConjunction() && !left.isNegated()) {
            disjunctivePredicate = (CompoundPredicate) left;
            disjunctivePredicate.getPredicates().add(right);
        } else {
            disjunctivePredicate = new CompoundPredicate(getBooleanDomainType(), new ArrayList<>(2), false);
            disjunctivePredicate.getPredicates().add(left);
            disjunctivePredicate.getPredicates().add(right);
        }
        return disjunctivePredicate;
    }

    @Override
    public Predicate visitAndPredicate(PredicateParser.AndPredicateContext ctx) {
        List<PredicateParser.PredicateContext> predicate = ctx.predicate();
        Predicate left = (Predicate) predicate.get(0).accept(this);
        Predicate right = (Predicate) predicate.get(1).accept(this);

        CompoundPredicate conjunctivePredicate;
        if (left instanceof CompoundPredicate && ((CompoundPredicate) left).isConjunction() && !left.isNegated()) {
            conjunctivePredicate = (CompoundPredicate) left;
            conjunctivePredicate.getPredicates().add(right);
        } else {
            conjunctivePredicate = new CompoundPredicate(getBooleanDomainType(), new ArrayList<>(2), true);
            conjunctivePredicate.getPredicates().add(left);
            conjunctivePredicate.getPredicates().add(right);
        }
        return conjunctivePredicate;
    }

    @Override
    public Predicate visitIsNullPredicate(PredicateParser.IsNullPredicateContext ctx) {
        Expression left = ctx.expression().accept(this);

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicateType.NULLNESS);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicateType.NULLNESS);
        } else {
            List<DomainType> operandTypes = Collections.singletonList(left.getType());
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicateType.NULLNESS, operandTypes);
            } else {
                return new IsNullPredicate(domainType, left, ctx.NOT() != null);
            }
        }
    }

    @Override
    public Predicate visitIsEmptyPredicate(PredicateParser.IsEmptyPredicateContext ctx) {
        Expression left = ctx.expression().accept(this);

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicateType.COLLECTION);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicateType.COLLECTION);
        } else {
            List<DomainType> operandTypes = Collections.singletonList(left.getType());
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicateType.COLLECTION, operandTypes);
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

    public Predicate createComparisonPredicate(ArithmeticExpression left, ArithmeticExpression right, ComparisonOperator comparisonOperator) {
        List<DomainType> operandTypes = Arrays.asList(left.getType(), right.getType());
        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), comparisonOperator.getDomainPredicateType());

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), comparisonOperator.getDomainPredicateType());
        } else {
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(comparisonOperator.getDomainPredicateType(), operandTypes);
            } else {
                return new ComparisonPredicate(domainType, left, right, comparisonOperator);
            }
        }
    }

    @Override
    public Expression visitInPredicate(PredicateParser.InPredicateContext ctx) {
        ArithmeticExpression left = (ArithmeticExpression) ctx.expression().accept(this);
        List<ArithmeticExpression> inItems = getExpressionList(ctx.inList().expression());

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicateType.EQUALITY);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicateType.EQUALITY);
        } else {
            List<DomainType> operandTypes = new ArrayList<>(inItems.size() + 1);
            operandTypes.add(left.getType());
            for (int i = 0; i < inItems.size(); i++) {
                ArithmeticExpression inItem = inItems.get(i);
                operandTypes.add(inItem.getType());
            }
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicateType.EQUALITY, operandTypes);
            } else {
                return new InPredicate(domainType, left, inItems, ctx.NOT() != null);
            }
        }
    }

    @Override
    public Predicate visitBetweenPredicate(PredicateParser.BetweenPredicateContext ctx) {
        ArithmeticExpression left = (ArithmeticExpression) ctx.lhs.accept(this);
        ArithmeticExpression lower = (ArithmeticExpression) ctx.start.accept(this);
        ArithmeticExpression upper = (ArithmeticExpression) ctx.end.accept(this);

        DomainPredicateTypeResolver predicateTypeResolver = domainModel.getPredicateTypeResolver(left.getType().getName(), DomainPredicateType.RELATIONAL);

        if (predicateTypeResolver == null) {
            throw missingPredicateTypeResolver(left.getType(), DomainPredicateType.RELATIONAL);
        } else {
            List<DomainType> operandTypes = Arrays.asList(left.getType(), lower.getType(), upper.getType());
            DomainType domainType = predicateTypeResolver.resolveType(domainModel, operandTypes);
            if (domainType == null) {
                throw cannotResolvePredicateType(DomainPredicateType.RELATIONAL, operandTypes);
            } else {
                return new BetweenPredicate(domainType, left, upper, lower);
            }
        }
    }

    @Override
    public Expression visitBooleanFunction(PredicateParser.BooleanFunctionContext ctx) {
        Expression expression = super.visitBooleanFunction(ctx);
        if (expression.getType() == getBooleanDomainType()) {
            if (expression instanceof Predicate) {
                return expression;
            }

            return new ExpressionPredicate(getBooleanDomainType(), expression, false);
        }

        throw new TypeErrorException("Invalid use of non-boolean returning function: " + ctx.getText());
    }

    @Override
    public Expression visitGroupedExpression(PredicateParser.GroupedExpressionContext ctx) {
        return ctx.expression().accept(this);
    }

    @Override
    public Expression visitAdditionExpression(PredicateParser.AdditionExpressionContext ctx) {
        return createArithmeticExpression(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ArithmeticOperatorType.PLUS
        );
    }

    @Override
    public Expression visitSubtractionExpression(PredicateParser.SubtractionExpressionContext ctx) {
        return createArithmeticExpression(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ArithmeticOperatorType.MINUS
        );
    }

    @Override
    public Expression visitDivisionExpression(PredicateParser.DivisionExpressionContext ctx) {
        return createArithmeticExpression(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ArithmeticOperatorType.DIVIDE
        );
    }

    @Override
    public Expression visitMultiplicationExpression(PredicateParser.MultiplicationExpressionContext ctx) {
        return createArithmeticExpression(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ArithmeticOperatorType.MULTIPLY
        );
    }

    @Override
    public Expression visitModuloExpression(PredicateParser.ModuloExpressionContext ctx) {
        return createArithmeticExpression(
                (ArithmeticExpression) ctx.lhs.accept(this),
                (ArithmeticExpression) ctx.rhs.accept(this),
                ArithmeticOperatorType.MODULO
        );
    }

    private Expression createArithmeticExpression(ArithmeticExpression left, ArithmeticExpression right, ArithmeticOperatorType operator) {
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
                return ctx.expression().accept(this);
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
        return new Literal(literalFactory.ofDateTimeString(sb.toString()));
    }

    @Override
    public Expression visitTemporalIntervalLiteral(PredicateParser.TemporalIntervalLiteralContext ctx) {
        int years = parseTemporalAmount(ctx.years, "years");
        int months = parseTemporalAmount(ctx.months, "months");
        int days = parseTemporalAmount(ctx.days, "days");
        int hours = parseTemporalAmount(ctx.hours, "hours");
        int minutes = parseTemporalAmount(ctx.minutes, "minutes");
        int seconds = parseTemporalAmount(ctx.seconds, "seconds");
        return new Literal(literalFactory.ofTemporalAmounts(years, months, days, hours, minutes, seconds));
    }

    private int parseTemporalAmount(Token token, String field) {
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
        List<Expression> literalList = getExpressionList(ctx.literal());
        CollectionDomainType collectionDomainType;
        if (literalList.isEmpty()) {
            collectionDomainType = domainModel.getCollectionType(null);
        } else {
            collectionDomainType = domainModel.getCollectionType(literalList.get(0).getType());
        }

        return new Literal(literalFactory.ofCollectionValues(collectionDomainType, literalList));
    }

    @Override
    public Expression visitPath(PredicateParser.PathContext ctx) {
        List<PredicateParser.IdentifierContext> identifiers = ctx.identifier();
        int size = identifiers.size();
        String alias = identifiers.get(0).getText();
        DomainType type = compileContext.getRootDomainType(alias);
        if (type == null) {
            if (size == 2) {
                type = domainModel.getType(alias);
                if (type instanceof EnumDomainType) {
                    return new Literal(literalFactory.ofEnumValue((EnumDomainType) type, identifiers.get(1).getText()));
                }
            }
            throw unknownType(alias);
        } else {
            List<EntityDomainTypeAttribute> pathAttributes = new ArrayList<>(size);
            for (int pathElemIdx = 1; pathElemIdx < size; pathElemIdx++) {
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

            return new Path(alias, pathAttributes, type);
        }
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
            for (; i < lastIdx; i++) {
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
                    arguments.put(domainFunctionArgument, new Literal(new DefaultResolvedLiteral(domainFunctionArgument.getType(), varArgs)));
                } else {
                    argumentTypes.put(domainFunctionArgument, literalList.get(i).getType());
                    arguments.put(domainFunctionArgument, literalList.get(i));
                }
            }
            DomainFunctionTypeResolver functionTypeResolver = domainModel.getFunctionTypeResolver(functionName);
            DomainType functionType = functionTypeResolver.resolveType(domainModel, function, argumentTypes);
            return new FunctionInvocation(function, arguments, functionType);
        }
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
                List<Expression> literalList = getExpressionList(ctx.predicateOrExpression());
                Map<EntityDomainTypeAttribute, Expression> arguments = new LinkedHashMap<>(literalList.size());
                for (int i = 0; i < literalList.size(); i++) {
                    EntityDomainTypeAttribute attribute = entityDomainType.getAttribute(argNames.get(i).getText());
                    arguments.put(attribute, literalList.get(i));
                }
                return new Literal(literalFactory.ofEntityAttributeValues(entityDomainType, arguments));
            } else {
                throw unknownFunction(entityOrFunctionName);
            }
        } else {
            List<PredicateParser.IdentifierContext> argNames = ctx.identifier();
            List<Expression> literalList = getExpressionList(ctx.predicateOrExpression());
            Map<DomainFunctionArgument, Expression> arguments = new LinkedHashMap<>(literalList.size());
            if (function.getArgumentCount() != -1 && arguments.size() > function.getArgumentCount()) {
                throw new DomainModelException(String.format("Function '%s' expects at most %d arguments but found %d",
                        function.getName(),
                        function.getArgumentCount(),
                        arguments.size()
                ));
            }
            if (arguments.size() < function.getMinArgumentCount()) {
                throw new DomainModelException(String.format("Function '%s' expects at least %d arguments but found %d",
                                                             function.getName(),
                                                             function.getMinArgumentCount(),
                                                             arguments.size()
                ));
            }
            Map<DomainFunctionArgument, DomainType> argumentTypes = new HashMap<>(arguments.size());
            for (int i = 0; i < literalList.size(); i++) {
                DomainFunctionArgument domainFunctionArgument = function.getArgument(argNames.get(i).getText());
                argumentTypes.put(domainFunctionArgument, literalList.get(i).getType());
                arguments.put(domainFunctionArgument, literalList.get(i));
            }
            DomainFunctionTypeResolver functionTypeResolver = domainModel.getFunctionTypeResolver(entityOrFunctionName);
            DomainType functionType = functionTypeResolver.resolveType(domainModel, function, argumentTypes);
            return new FunctionInvocation(function, arguments, functionType);
        }
    }

    @Override
    public Expression visitTerminal(TerminalNode node) {
        if (node.getSymbol().getType() == PredicateLexer.EOF) {
            return null;
        }
        switch (node.getSymbol().getType()) {
            case PredicateLexer.STRING_LITERAL:
                return new Literal(literalFactory.ofString(node.getText()));
            case PredicateLexer.TRUE:
                return getBooleanTrueLiteral();
            case PredicateLexer.FALSE:
                return getBooleanFalseLiteral();
            case PredicateLexer.NUMERIC_LITERAL:
                return new Literal(literalFactory.ofNumericString(node.getText()));
            default:
                throw new IllegalStateException("Terminal node '" + node.getText() + "' not handled");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getExpressionList(List<? extends ParserRuleContext> items) {
        List<T> expressions = new ArrayList<>(items.size());
        for (int i = 0; i < items.size(); i++) {
            expressions.add((T) items.get(i).accept(this));
        }
        return expressions;
    }

    private DomainType getBooleanDomainType() {
        if (cachedBooleanDomainType == null) {
            cachedBooleanDomainType = domainModel.getType(Boolean.class);
            if (cachedBooleanDomainType == null) {
                throw new DomainModelException("No domain type defined for type " + Boolean.class.getName());
            }
        }
        return cachedBooleanDomainType;
    }

    private Literal getBooleanLiteral(boolean value) {
        return value ? getBooleanTrueLiteral() : getBooleanFalseLiteral();
    }

    private Literal getBooleanTrueLiteral() {
        if (cachedBooleanTrueLiteral == null) {
            cachedBooleanTrueLiteral = new Literal(literalFactory.ofBoolean(true));
        }
        return cachedBooleanTrueLiteral;
    }

    private Literal getBooleanFalseLiteral() {
        if (cachedBooleanFalseLiteral == null) {
            cachedBooleanFalseLiteral = new Literal(literalFactory.ofBoolean(false));
        }
        return cachedBooleanFalseLiteral;
    }

    private TypeErrorException typeError(DomainType t1, DomainType t2, DomainOperator operator) {
        return new TypeErrorException(String.format("%s %s %s", t1, operator, t2));
    }

    private DomainModelException missingPredicateTypeResolver(DomainType type, DomainPredicateType predicateType) {
        return new DomainModelException(String.format("Missing predicate type resolver for type %s and predicate %s", type, predicateType));
    }

    private DomainModelException missingOperationTypeResolver(DomainType type, DomainOperator operator) {
        return new DomainModelException(String.format("Missing operation type resolver for type %s and operator %s", type, operator));
    }

    private TypeErrorException typeError(DomainType t1, DomainType t2, DomainPredicateType predicateType) {
        return new TypeErrorException(String.format("%s %s %s", t1, predicateType, t2));
    }

    private DomainModelException unknownType(String typeName) {
        return new DomainModelException(String.format("Undefined type '%s'", typeName));
    }

    private DomainModelException unknownEntityAttribute(EntityDomainType entityDomainType, String attributeName) {
        return new DomainModelException(String.format("Attribute %s undefined for entity %s", attributeName, entityDomainType));
    }

    private DomainModelException unknownFunction(String identifier) {
        return new DomainModelException(String.format("Undefined function '%s'", identifier));
    }

    private TypeErrorException unsupportedType(String typeName) {
        return new TypeErrorException(String.format("Resolved type for identifier %s is not supported", typeName));
    }

    private TypeErrorException cannotResolvePredicateType(DomainPredicateType predicateType, List<DomainType> operandTypes) {
        return new TypeErrorException(String.format("Cannot resolve predicate type for predicate %s and operand types %s", predicateType, operandTypes));
    }

    private TypeErrorException cannotResolveOperationType(DomainOperator operator, List<DomainType> operandTypes) {
        return new TypeErrorException(String.format("Cannot resolve operation type for operator %s and operand types %s", operator, operandTypes));
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class DefaultResolvedLiteral implements ResolvedLiteral {

        private final DomainType type;
        private final Object value;

        public DefaultResolvedLiteral(DomainType type, Object value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public DomainType getType() {
            return type;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DefaultResolvedLiteral that = (DefaultResolvedLiteral) o;
            return Objects.equals(type, that.type) &&
                Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }
    }
}
