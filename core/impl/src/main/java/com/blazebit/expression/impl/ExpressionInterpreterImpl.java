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

import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.expression.ArithmeticExpression;
import com.blazebit.expression.ArithmeticFactor;
import com.blazebit.expression.BetweenPredicate;
import com.blazebit.expression.ChainingArithmeticExpression;
import com.blazebit.expression.CollectionLiteral;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.ComparisonPredicate;
import com.blazebit.expression.CompoundPredicate;
import com.blazebit.expression.DataFetcherData;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.EntityLiteral;
import com.blazebit.expression.EnumLiteral;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionInterpreterContext;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.FromItem;
import com.blazebit.expression.FromNode;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsEmptyPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Join;
import com.blazebit.expression.JoinType;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.Query;
import com.blazebit.expression.spi.AttributeAccessor;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DataFetcher;
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.DomainOperatorInterpreter;
import com.blazebit.expression.spi.FunctionInvoker;
import com.blazebit.expression.spi.TypeAdapter;
import com.blazebit.expression.spi.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionInterpreterImpl implements Expression.ResultVisitor<Object>, ExpressionInterpreter {

    protected final ExpressionService expressionService;
    protected Context context;
    protected TypeAdapter typeAdapter;
    protected Map<String, Object> dataMap;

    public ExpressionInterpreterImpl(ExpressionService expressionService) {
        this.expressionService = expressionService;
    }

    protected <T> T evaluate(Expression expression, Context interpreterContext, boolean asModelType) {
        Context oldContext = context;
        if (interpreterContext == null) {
            context = ExpressionInterpreterContext.create(expressionService);
        } else {
            context = interpreterContext;
        }
        try {
            Object value = expression.accept(this);
            if (typeAdapter != null && asModelType) {
                value = typeAdapter.toModelType(context, value, expression.getType());
            }
            return (T) value;
        } finally {
            context = oldContext;
            typeAdapter = null;
        }
    }

    @Override
    public <T> T evaluateAs(Expression expression, Context interpreterContext, Class<T> resultClass) {
        Context oldContext = context;
        if (interpreterContext == null) {
            context = ExpressionInterpreterContext.create(expressionService);
        } else {
            context = interpreterContext;
        }
        try {
            Object value = expression.accept(this);
            if (value == null || resultClass.isInstance(value)) {
                //noinspection unchecked
                return (T) value;
            }
            Map<Class<?>, TypeConverter<?, ?>> converterMap = expressionService.getConverters().get(resultClass);
            TypeConverter<Object, T> converter = null;
            if (converterMap != null) {
                //noinspection unchecked
                converter = (TypeConverter<Object, T>) converterMap.get(value.getClass());
            }
            if (converter == null) {
                throw new IllegalArgumentException("No converter found for converting " + value.getClass().getName() + " to " + resultClass.getName() );
            }
            return converter.convert(context, value, expression.getType());
        } finally {
            context = oldContext;
            typeAdapter = null;
        }
    }

    @Override
    public <T> T evaluate(Expression expression, Context interpreterContext) {
        return evaluate(expression, interpreterContext, false);
    }
    @Override
    public <T> T evaluateAsModelType(Expression expression, Context interpreterContext) {
        return evaluate(expression, interpreterContext, true);
    }

    @Override
    public Boolean evaluate(Predicate expression, Context interpreterContext) {
        return Boolean.TRUE.equals(evaluate((Expression) expression, interpreterContext));
    }

    @Override
    public Object visit(ArithmeticFactor e) {
        try {
            Object result = e.getExpression().accept(this);
            if (result == null) {
                return null;
            }
            if (e.isInvertSignum()) {
                return arithmetic(e.getType(), e.getType(), null, result, null, DomainOperator.UNARY_MINUS);
            }
            return result;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(ExpressionPredicate e) {
        try {
            Boolean result = (Boolean) e.getExpression().accept(this);
            if (result == null) {
                return null;
            }
            return (e.isNegated() != result);
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(BetweenPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            Object lower = e.getLower().accept(this);
            if (lower == null) {
                return null;
            }
            Object upper = e.getUpper().accept(this);
            if (upper == null) {
                return null;
            }

            Boolean testValue = e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
            Boolean compare = compare(e.getLeft().getType(), e.getLower().getType(), left, lower, ComparisonOperator.GREATER_OR_EQUAL);
            if (compare == null) {
                return null;
            } else if (testValue.equals(compare)) {
                return testValue;
            }
            compare = compare(e.getLeft().getType(), e.getUpper().getType(), left, upper, ComparisonOperator.LOWER_OR_EQUAL);
            if (compare == null) {
                return null;
            } else if (testValue.equals(compare)) {
                return testValue;
            }
            return Boolean.TRUE;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(InPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            List<ArithmeticExpression> inItems = e.getInItems();
            Boolean testValue = e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
            for (int i = 0; i < inItems.size(); i++) {
                ArithmeticExpression inItem = inItems.get(i);
                Object value = inItem.accept(this);
                if (value == null) {
                    return null;
                }
                Boolean b = compare(e.getLeft().getType(), inItem.getType(), left, value, ComparisonOperator.EQUAL);
                if (!testValue.equals(b)) {
                    return b;
                }
            }

            return testValue;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(ChainingArithmeticExpression e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            Object right = e.getRight().accept(this);
            if (right == null) {
                return null;
            }

            return arithmetic(e.getType(), e.getLeft().getType(), e.getRight().getType(), left, right, e.getOperator().getDomainOperator());
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(CompoundPredicate e) {
        try {
            List<Predicate> predicates = e.getPredicates();
            int size = predicates.size();
            if (e.isConjunction()) {
                if (size == 0) {
                    return e.isNegated();
                }
                for (int i = 0; i < predicates.size(); i++) {
                    Object result = predicates.get(i).accept(this);
                    if (result == null) {
                        return null;
                    } else if (!Boolean.TRUE.equals(result)) {
                        return e.isNegated();
                    }
                }
                return !e.isNegated();
            } else {
                if (size == 0) {
                    return !e.isNegated();
                }
                for (int i = 0; i < predicates.size(); i++) {
                    Object result = predicates.get(i).accept(this);
                    if (result == null) {
                        return null;
                    } else if (Boolean.TRUE.equals(result)) {
                        return !e.isNegated();
                    }
                }
                return e.isNegated();
            }
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(ComparisonPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            Object right = e.getRight().accept(this);
            if (right == null) {
                return null;
            }

            Boolean compare = compare(e.getLeft().getType(), e.getRight().getType(), left, right, e.getOperator());
            if (compare == null) {
                return null;
            } else if (e.isNegated()) {
                return !compare;
            } else {
                return compare;
            }
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(IsNullPredicate e) {
        // LEFT  NN NOT R
        // NULL  F   F  T
        // NULL  F   T  F
        // VAL   T   F  F
        // VAL   T   T  T
        try {
            return (e.getLeft().accept(this) != null) == e.isNegated() ? Boolean.TRUE : Boolean.FALSE;
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(IsEmptyPredicate e) {
        try {
            Object left = e.getLeft().accept(this);
            if (left == null) {
                return null;
            }
            if (e.isNegated()) {
                return ((Iterable<?>) left).iterator().hasNext();
            } else {
                return !((Iterable<?>) left).iterator().hasNext();
            }
        } finally {
            typeAdapter = null;
        }
    }

    @Override
    public Object visit(Path e) {
        Object value;
        if (e.getBase() == null) {
            value = resolveRoot(e.getAlias());
        } else {
            value = e.getBase().accept(this);
        }
        List<EntityDomainTypeAttribute> attributes = e.getAttributes();
        if (attributes.isEmpty()) {
            typeAdapter = null;
        } else {
            int size = attributes.size();
            for (int i = 0; i < size; i++) {
                if (value == null) {
                    return null;
                }
                EntityDomainTypeAttribute attribute = attributes.get(i);
                AttributeAccessor attributeAccessor = attribute.getMetadata(AttributeAccessor.class);
                if (attributeAccessor == null) {
                    throw new IllegalArgumentException("No attribute accessor available for attribute: " + attribute);
                }
                value = attributeAccessor.getAttribute(context, value, attribute);
                typeAdapter = attribute.getMetadata(TypeAdapter.class);
                if (typeAdapter != null) {
                    value = typeAdapter.toInternalType(context, value, attribute.getType());
                }
            }
        }
        return value;
    }

    @Override
    public Object visit(FunctionInvocation e) {
        DomainFunction domainFunction = e.getFunction();
        FunctionInvoker functionInvoker = domainFunction.getMetadata(FunctionInvoker.class);
        if (functionInvoker == null) {
            throw new IllegalArgumentException("No function invoker available for function: " + domainFunction);
        }

        Map<DomainFunctionArgument, Expression> arguments = e.getArguments();
        DomainFunctionArguments argumentValues;

        if (arguments.isEmpty()) {
            argumentValues = DomainFunctionArguments.EMPTY;
        } else {
            int size = domainFunction.getArguments().size();
            Object[] values = new Object[size];
            DomainType[] types = new DomainType[size];
            for (Map.Entry<DomainFunctionArgument, Expression> entry : arguments.entrySet()) {
                DomainFunctionArgument domainFunctionArgument = entry.getKey();
                Expression expression = entry.getValue();
                Object argumentValue = expression.accept(this);
                TypeAdapter argumentAdapter = domainFunctionArgument.getMetadata(TypeAdapter.class);
                if (argumentAdapter != null) {
                    argumentValue = argumentAdapter.toModelType(context, argumentValue, domainFunctionArgument.getType());
                }
                types[domainFunctionArgument.getPosition()] = expression.getType();
                values[domainFunctionArgument.getPosition()] = argumentValue;
            }
            argumentValues = new DefaultDomainFunctionArguments(values, types, arguments.size());
        }

        typeAdapter = domainFunction.getMetadata(TypeAdapter.class);
        Object result = functionInvoker.invoke(context, domainFunction, argumentValues);
        if (typeAdapter != null) {
            return typeAdapter.toInternalType(context, result, domainFunction.getResultType());
        }
        return result;
    }

    @Override
    public Object visit(Literal e) {
        typeAdapter = null;
        if (e.getType().getKind() == DomainType.DomainTypeKind.COLLECTION) {
            Collection<Expression> collection = (Collection<Expression>) e.getValue();
            if (collection.isEmpty()) {
                return Collections.emptyList();
            }
            List<Object> resolved = new ArrayList<>(collection.size());
            for (Expression expression : collection) {
                resolved.add(expression.accept(this));
            }
            typeAdapter = e.getType().getMetadata(TypeAdapter.class);
            return resolved;
        } else {
            typeAdapter = e.getType().getMetadata(TypeAdapter.class);
            return e.getValue();
        }
    }

    @Override
    public Object visit(EnumLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Object visit(EntityLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Object visit(CollectionLiteral e) {
        return visit((Literal) e);
    }

    protected Object resolveRoot(String alias) {
        if (dataMap != null) {
            Object object = dataMap.get( alias );
            if (object != null) {
                return object;
            }
        }
        return context.getRoot(alias);
    }

    @Override
    public List<Object> visit(Query e) {
        List<FromItem> fromItems = e.getFromItems();
        if (fromItems.size() == 1 && fromItems.get( 0 ).getJoins().isEmpty()) {
            return visitSimpleQuery(e);
        }
        return visitJoinQuery( e );
    }

    private List<Object> visitSimpleQuery(Query e) {
        FromItem fromItem = e.getFromItems().get( 0 );
        List<?> data = getData( fromItem );
        List<Object> result = new ArrayList<>();
        dataMap = new HashMap<>( 1 );
        dataMap.put( fromItem.getAlias(), null );
        Map.Entry<String, Object> entry = dataMap.entrySet().iterator().next();
        Predicate predicate = e.getWherePredicate();
        Set<Object> uniqueResults = e.isDistinct() ? new HashSet<>() : null;
        for ( Object object : data ) {
            entry.setValue( object );
            Object predicateResult = predicate == null ? Boolean.TRUE : predicate.accept( this );
            if (predicateResult == Boolean.TRUE) {
                Object selectResult = visitSelectItems( e );
                if ( uniqueResults == null || uniqueResults.add( selectResult ) ) {
                    result.add( selectResult );
                }
            }
        }
        return result;
    }

    private Object visitSelectItems(Query e) {
        List<Expression> selectItems = e.getSelectItems();
        if (selectItems.size() == 1) {
            return selectItems.get( 0 ).accept( this );
        }
        Object[] array = new Object[selectItems.size()];
        for ( int i = 0; i < selectItems.size(); i++ ) {
            array[i] = selectItems.get( i ).accept( this );
        }
        return array;
    }

    private List<Object> visitJoinQuery(Query e) {
        FromNode[] fromNodes = tupleNodes( e.getFromItems() );

        Map.Entry<String, Object>[] entries = new Map.Entry[fromNodes.length];
        dataMap = new HashMap<>(fromNodes.length);
        for ( int i = 0; i < fromNodes.length; i++ ) {
            dataMap.put( fromNodes[i].getAlias(), i );
        }
        for ( Map.Entry<String, Object> entry : dataMap.entrySet() ) {
            entries[(int) entry.getValue()] = entry;
        }

        TupleList tuples = joinTuples( fromNodes, entries );

        List<Object> result = new ArrayList<>();
        Predicate predicate = e.getWherePredicate();
        Set<Object> uniqueResults = e.isDistinct() ? new HashSet<>() : null;
        for ( Object[] tuple : tuples ) {
            initDataMap( entries, tuple );
            Object predicateResult = predicate == null ? Boolean.TRUE : predicate.accept( this );
            if (predicateResult == Boolean.TRUE) {
                Object selectResult = visitSelectItems( e );
                if ( uniqueResults == null || uniqueResults.add( selectResult ) ) {
                    result.add( selectResult );
                }
            }
        }
        return result;
    }

    private TupleList joinTuples(FromNode[] fromNodes, Map.Entry<String, Object>[] entries) {
        TupleList tuples = null;
        for ( int fromNodeIndex = 0; fromNodeIndex < fromNodes.length; fromNodeIndex++ ) {
            List<?> data = getData( fromNodes[fromNodeIndex] );
            if ( tuples == null ) {
                tuples = new TupleList( data.size() );
                for ( Object object : data ) {
                    Object[] tuple = new Object[fromNodes.length];
                    tuple[fromNodeIndex] = object;
                    tuples.add( tuple );
                }
            } else {
                if ( fromNodes[fromNodeIndex] instanceof Join) {
                    Join join = (Join) fromNodes[fromNodeIndex];
                    Path[] eqHashJoinPaths;
                    if ( data.isEmpty() ) {
                        if ( join.getJoinType() == JoinType.INNER || join.getJoinType() == JoinType.RIGHT ) {
                            // empty data on RHS produces empty tuples
                            tuples.clear();
                        }
                    } else if ( (eqHashJoinPaths = determineEqHashJoinPaths( join ) ) != null ) {
                        tuples = hashJoin( entries, data, fromNodeIndex, tuples, join, eqHashJoinPaths );
                    } else {
                        tuples = nestedLoopJoin( entries, data, fromNodeIndex, tuples, join );
                    }
                } else if (data.isEmpty()) {
                    // Cross join with empty data on the RHS
                    tuples.clear();
                } else {
                    // Cross join
                    int lhsTuples = tuples.size();
                    tuples.ensureCapacity( tuples.size() * data.size() );
                    for ( int i = 0; i < lhsTuples; i++ ) {
                        tuples.get( i )[fromNodeIndex] = data.get( 0 );
                    }
                    for ( int i = 1; i < data.size(); i++ ) {
                        for ( int j = 0; j < lhsTuples; j++ ) {
                            Object[] newTuple = tuples.get( j ).clone();
                            newTuple[fromNodeIndex] = data.get( i );
                            tuples.add( newTuple );
                        }
                    }
                }
            }
        }
        return tuples;
    }

    private Path[] determineEqHashJoinPaths(Join join) {
        Predicate joinPredicate = join.getJoinPredicate();
        if (!joinPredicate.isNegated() && joinPredicate instanceof ComparisonPredicate) {
            ComparisonPredicate comparisonPredicate = (ComparisonPredicate) joinPredicate;
            if (comparisonPredicate.getOperator() == ComparisonOperator.EQUAL) {
                ArithmeticExpression lhs = comparisonPredicate.getLeft();
                ArithmeticExpression rhs = comparisonPredicate.getRight();
                String alias = join.getAlias();
                if ( lhs instanceof Path && rhs instanceof Path ) {
                    Path lhsPath = (Path) lhs;
                    Path rhsPath = (Path) rhs;
                    if (alias.equals( lhsPath.getAlias() )) {
                        return new Path[]{ rhsPath, lhsPath };
                    } else if (alias.equals( rhsPath.getAlias() )) {
                        return new Path[]{ lhsPath, rhsPath };
                    }
                }
            }
        }
        return null;
    }

    private TupleList nestedLoopJoin(
            Map.Entry<String, Object>[] entries,
            List<?> data,
            int fromNodeIndex,
            TupleList tuples,
            Join join) {
        BitSet matchedBitSet = join.getJoinType() == JoinType.RIGHT || join.getJoinType() == JoinType.FULL ? new BitSet( data.size() ) : null;
        for ( int i = 0, tuplesSize = tuples.size(); i < tuplesSize; i++ ) {
            Object[] tuple = tuples.get( i );
            initDataMap( entries, tuple );
            int matches = 0;
            for ( int j = 0; j < data.size(); j++ ) {
                Object rhsObject = data.get( j );
                entries[fromNodeIndex].setValue( rhsObject );
                Object predicateResult = join.getJoinPredicate().accept( this );
                if ( predicateResult == Boolean.TRUE ) {
                    if (matchedBitSet != null) {
                        matchedBitSet.set( j );
                    }
                    if ( matches == 0 ) {
                        tuple[fromNodeIndex] = rhsObject;
                    } else {
                        Object[] clonedTuple = tuple.clone();
                        clonedTuple[fromNodeIndex] = rhsObject;
                        tuples.add( clonedTuple );
                    }
                    matches++;
                }
            }
            if (matches == 0 && (join.getJoinType() == JoinType.INNER || join.getJoinType() == JoinType.RIGHT)) {
                // TODO: optimize by storing deleted bitmap
                tuples.remove( i );
                i--;
                tuplesSize--;
            }
        }
        if (matchedBitSet != null) {
            for ( int i = matchedBitSet.nextClearBit( 0 ); i >= 0; i = matchedBitSet.nextClearBit( i + 1 ) ) {
                if ( i == data.size() ) {
                    break;
                }
                Object[] tuple = new Object[entries.length];
                tuple[fromNodeIndex] = data.get( i );
                tuples.add( tuple );
            }
        }
        return tuples;
    }

    private TupleList hashJoin(
            Map.Entry<String, Object>[] entries,
            List<?> data,
            int fromNodeIndex,
            TupleList tuples,
            Join join,
            Path[] eqHashJoinPaths) {
        if (tuples.size() < data.size()) {
            return hashJoinTuples( entries, data, fromNodeIndex, tuples, join, eqHashJoinPaths );
        } else {
            return hashJoinObjects( entries, data, fromNodeIndex, tuples, join, eqHashJoinPaths );
        }
    }

    private TupleList hashJoinTuples(
            Map.Entry<String, Object>[] entries,
            List<?> data,
            int fromNodeIndex,
            TupleList tuples,
            Join join,
            Path[] eqHashJoinPaths) {
        HashMap<Object, Object> map = new HashMap<>( tuples.size() );
        int lhsIndex = findEntry( entries, eqHashJoinPaths[0].getAlias());
        Map.Entry<String, Object> lhsEntry = entries[lhsIndex];
        for ( int i = 0; i < tuples.size(); i++ ) {
            lhsEntry.setValue( tuples.get( i )[lhsIndex] );
            Object lhsValue = eqHashJoinPaths[0].accept( this );
            Integer tupleIndex = i;
            Object existing = map.putIfAbsent( lhsValue, tupleIndex );
            if ( existing != null ) {
                IndexList objects;
                if ( existing instanceof IndexList ) {
                    objects = (IndexList) existing;
                } else {
                    objects = new IndexList();
                }
                objects.add( tupleIndex );
                map.put( lhsValue, objects );
            }
        }

        TupleList joinedTuples = new TupleList();
        Map.Entry<String, Object> rhsEntry = entries[fromNodeIndex];
        BitSet matchedBitSet = join.getJoinType() == JoinType.LEFT || join.getJoinType() == JoinType.FULL ? new BitSet( tuples.size() ) : null;
        for ( int i = 0, objectsSize = data.size(); i < objectsSize; i++ ) {
            Object rhsObject = data.get( i );
            rhsEntry.setValue( rhsObject );
            Object rhsValue = eqHashJoinPaths[1].accept( this );
            Object matches = map.get( rhsValue );
            if (matches == null) {
                if (join.getJoinType() == JoinType.RIGHT || join.getJoinType() == JoinType.FULL) {
                    Object[] tuple = new Object[entries.length];
                    tuple[fromNodeIndex] = rhsObject;
                    joinedTuples.add( tuple );
                }
            } else if (matches instanceof IndexList ) {
                IndexList indexList = (IndexList) matches;
                int tupleIndex = indexList.get( 0 );
                if (matchedBitSet != null) {
                    matchedBitSet.set( tupleIndex );
                }
                Object[] tuple = tuples.get( tupleIndex ).clone();
                tuple[fromNodeIndex] = rhsObject;
                joinedTuples.add( tuple );
                for ( int j = 1; j < indexList.size(); j++ ) {
                    tupleIndex = indexList.get( j );
                    if (matchedBitSet != null) {
                        matchedBitSet.set( tupleIndex );
                    }
                    tuple = tuples.get( tupleIndex ).clone();
                    tuple[fromNodeIndex] = rhsObject;
                    joinedTuples.add( tuple );
                }
            } else {
                int tupleIndex = (int) matches;
                if (matchedBitSet != null) {
                    matchedBitSet.set( tupleIndex );
                }
                Object[] tuple = tuples.get( tupleIndex ).clone();
                tuple[fromNodeIndex] = rhsObject;
                joinedTuples.add( tuple );
            }
        }
        if (matchedBitSet != null) {
            for ( int i = matchedBitSet.nextClearBit( 0 ); i >= 0; i = matchedBitSet.nextClearBit( i + 1 ) ) {
                if ( i == tuples.size() ) {
                    break;
                }
                joinedTuples.add( tuples.get( i ) );
            }
        }
        return joinedTuples;
    }

    private TupleList hashJoinObjects(
            Map.Entry<String, Object>[] entries,
            List<?> data,
            int fromNodeIndex,
            TupleList tuples,
            Join join,
            Path[] eqHashJoinPaths) {
        HashMap<Object, Object> map = new HashMap<>( data.size() );
        Map.Entry<String, Object> rhsEntry = entries[fromNodeIndex];
        for ( int i = 0; i < data.size(); i++ ) {
            rhsEntry.setValue( data.get( i ) );
            Object rhsValue = eqHashJoinPaths[1].accept( this );
            Integer rhsIndex = i;
            Object existing = map.putIfAbsent( rhsValue, rhsIndex );
            if ( existing != null ) {
                IndexList objects;
                if ( existing instanceof IndexList ) {
                    objects = (IndexList) existing;
                } else {
                    objects = new IndexList();
                }
                objects.add( rhsIndex );
                map.put( rhsValue, objects );
            }
        }

        int lhsIndex = findEntry( entries, eqHashJoinPaths[0].getAlias());
        Map.Entry<String, Object> lhsEntry = entries[lhsIndex];
        BitSet matchedBitSet = join.getJoinType() == JoinType.RIGHT || join.getJoinType() == JoinType.FULL ? new BitSet( data.size() ) : null;
        for ( int i = 0, tuplesSize = tuples.size(); i < tuplesSize; i++ ) {
            Object[] tuple = tuples.get( i );
            lhsEntry.setValue( tuple[lhsIndex] );
            Object lhsValue = eqHashJoinPaths[0].accept( this );
            Object matches = map.get( lhsValue );
            if (matches == null ) {
                if (join.getJoinType() == JoinType.INNER || join.getJoinType() == JoinType.RIGHT) {
                    // TODO: optimize by storing deleted bitmap
                    tuples.remove( i );
                    i--;
                    tuplesSize--;
                }
            } else if (matches instanceof IndexList ) {
                IndexList indexList = (IndexList) matches;
                int rhsIndex = indexList.get( 0 );
                tuple[fromNodeIndex] = data.get( rhsIndex );
                if (matchedBitSet != null) {
                    matchedBitSet.set( rhsIndex );
                }
                for ( int j = 1; j < indexList.size(); j++ ) {
                    rhsIndex = indexList.get( j );
                    if (matchedBitSet != null) {
                        matchedBitSet.set( rhsIndex );
                    }
                    Object[] clonedTuple = tuple.clone();
                    tuple[fromNodeIndex] = data.get( rhsIndex );
                    tuples.add( clonedTuple );
                }
            } else {
                int rhsIndex = (int) matches;
                if (matchedBitSet != null) {
                    matchedBitSet.set( rhsIndex );
                }
                tuple[fromNodeIndex] = data.get( rhsIndex );
            }
        }
        if (matchedBitSet != null) {
            for ( int i = matchedBitSet.nextClearBit( 0 ); i >= 0; i = matchedBitSet.nextClearBit( i + 1 ) ) {
                if ( i == data.size() ) {
                    break;
                }
                Object[] tuple = new Object[entries.length];
                tuple[fromNodeIndex] = data.get( i );
                tuples.add( tuple );
            }
        }
        return tuples;
    }

    private int findEntry(Map.Entry<String, Object>[] entries, String alias) {
        for ( int i = 0; i < entries.length; i++ ) {
            if ( alias.equals( entries[i].getKey() ) ) {
                return i;
            }
        }

        throw new IllegalStateException("No entry found for alias: " + alias);
    }

    private static void initDataMap(Map.Entry<String, Object>[] entries, Object[] tuple) {
        for ( int i = 0; i < tuple.length; i++ ) {
            entries[i].setValue( tuple[i] );
        }
    }

    private FromNode[] tupleNodes(List<FromItem> fromItems) {
        int size = fromItems.size();
        for ( FromItem fromItem : fromItems ) {
            size += fromItem.getJoins().size();
        }
        FromNode[] tupleNodes = new FromNode[size];
        int index = 0;
        for ( FromItem fromItem : fromItems ) {
            tupleNodes[index++] = fromItem;
            for ( Join join : fromItem.getJoins() ) {
                tupleNodes[index++] = join;
            }
        }
        return tupleNodes;
    }

    protected List<?> getData(FromNode fromNode) {
        DataFetcherData dataFetcherData = context.getDataFetcherData();
        String domainTypeName = fromNode.getType().getName();
        List<?> cachedData = dataFetcherData.getDataForDomainType( domainTypeName );
        if (cachedData != null) {
            return cachedData;
        }
        DataFetcher dataFetcher = fromNode.getType().getMetadata( DataFetcher.class );
        if (dataFetcher == null) {
            throw new IllegalArgumentException("No data fetcher available for type: " + fromNode.getType());
        }
        List<?> fetchedData = dataFetcher.fetch( context );
        dataFetcherData.setDataForDomainType( domainTypeName, fetchedData );
        return fetchedData;
    }

    @Override
    public Object visit(FromItem e) {
        return null;
    }

    @Override
    public Object visit(Join e) {
        return null;
    }

    protected Boolean compare(DomainType leftType, DomainType rightType, Object left, Object right, ComparisonOperator operator) {
        ComparisonOperatorInterpreter comparisonOperatorInterpreter = leftType.getMetadata(ComparisonOperatorInterpreter.class);
        if (comparisonOperatorInterpreter == null) {
            throw new IllegalArgumentException("No comparison operator interpreter available for type: " + leftType);
        }
        return comparisonOperatorInterpreter.interpret(context, leftType, rightType, left, right, operator);
    }

    protected Object arithmetic(DomainType targetType, DomainType leftType, DomainType rightType, Object left, Object right, DomainOperator operator) {
        DomainOperatorInterpreter domainOperatorInterpreter = targetType.getMetadata(DomainOperatorInterpreter.class);
        if (domainOperatorInterpreter == null) {
            throw new IllegalArgumentException("No domain operator interpreter available for type: " + targetType);
        }
        return domainOperatorInterpreter.interpret(context, targetType, leftType, rightType, left, right, operator);
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    protected static final class DefaultDomainFunctionArguments implements DomainFunctionArguments {

        private final Object[] values;
        private final DomainType[] types;
        private final int assignedArguments;

        public DefaultDomainFunctionArguments(Object[] values, DomainType[] types, int assignedArguments) {
            this.values = values;
            this.types = types;
            this.assignedArguments = assignedArguments;
        }

        @Override
        public Object getValue(int position) {
            try {
                return values[position];
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            }
        }

        @Override
        public DomainType getType(int position) {
            try {
                return types[position];
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            }
        }

        @Override
        public int assignedArguments() {
            return assignedArguments;
        }
    }


    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static final class TupleList extends ArrayList<Object[]> {
        public TupleList(int initialCapacity) {
            super( initialCapacity );
        }

        public TupleList() {
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static final class IndexList {
        private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
        private int[] elementData;
        private int size;

        public IndexList() {
            elementData = new int[10];
        }

        private int[] grow(int minCapacity) {
            return elementData = Arrays.copyOf( elementData, newCapacity(minCapacity));
        }

        private int[] grow() {
            return grow(size + 1);
        }
        private int newCapacity(int minCapacity) {
            // overflow-conscious code
            int oldCapacity = elementData.length;
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity <= 0) {
                if (minCapacity < 0) {
                    // overflow
                    throw new OutOfMemoryError();
                }

                return minCapacity;
            }
            return (newCapacity - MAX_ARRAY_SIZE <= 0)
                    ? newCapacity
                    : hugeCapacity(minCapacity);
        }
        private static int hugeCapacity(int minCapacity) {
            if (minCapacity < 0) {
                // overflow
                throw new OutOfMemoryError();
            }
            return (minCapacity > MAX_ARRAY_SIZE)
                    ? Integer.MAX_VALUE
                    : MAX_ARRAY_SIZE;
        }

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public int get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index out of range: " + index);
            }
            return elementData[index];
        }
        public void add(int e) {
            if (size == elementData.length) {
                elementData = grow();
            }
            elementData[size] = e;
            size = size + 1;
        }
    }
}
