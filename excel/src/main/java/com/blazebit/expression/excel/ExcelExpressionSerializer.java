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

package com.blazebit.expression.excel;

import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainFunctionVolatility;
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
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.EntityLiteral;
import com.blazebit.expression.EnumLiteral;
import com.blazebit.expression.Expression;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionPredicate;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.FromItem;
import com.blazebit.expression.FunctionInvocation;
import com.blazebit.expression.InPredicate;
import com.blazebit.expression.IsEmptyPredicate;
import com.blazebit.expression.IsNullPredicate;
import com.blazebit.expression.Join;
import com.blazebit.expression.Literal;
import com.blazebit.expression.Path;
import com.blazebit.expression.Predicate;
import com.blazebit.expression.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelExpressionSerializer implements Expression.ResultVisitor<Boolean>, ExpressionSerializer<StringBuilder> {

    public static final String ARGUMENT_SEPARATOR = "excel.argument_separator";
    public static final String CURRENT_ROW = "excel.current_row";
    public static final String CONSTANT_INLINING_INTERPRETER_CONTEXT = "excel.constant_inlining_interpreter_context";

    private final ExpressionService expressionService;
    private final StringBuilder tempSb;
    private StringBuilder sb;
    private Context context;
    private String argumentSeparator;
    private int currentRow;
    private ExpressionInterpreter interpreterForInlining;
    private ExpressionInterpreter.Context interpreterContextForInlining;

    /**
     * Creates a new serializer for serializing as an excel formula to a StringBuilder.
     *
     * @param expressionService The expression service
     */
    public ExcelExpressionSerializer(ExpressionService expressionService) {
        this.expressionService = expressionService;
        this.tempSb = new StringBuilder();
    }

    /**
     * Returns the domain model.
     *
     * @return the domain model
     */
    public ExpressionService getExpressionService() {
        return expressionService;
    }

    /**
     * Returns the current serialization context.
     *
     * @return the current serialization context
     */
    public Context getContext() {
        return context;
    }

    public String getArgumentSeparator() {
        return argumentSeparator;
    }

    /**
     * Returns the current string builder to which the serialization is done.
     *
     * @return the current string builder
     */
    public StringBuilder getStringBuilder() {
        return sb;
    }

    @Override
    public void serializeTo(Expression expression, StringBuilder target) {
        serializeTo(null, expression, target);
    }

    @Override
    public void serializeTo(Context newContext, Expression expression, StringBuilder target) {
        StringBuilder old = sb;
        Context oldContext = context;
        String oldArgumentSeparator = argumentSeparator;
        int oldCurrentRow = currentRow;
        ExpressionInterpreter.Context oldInterpreterContextForInlining = interpreterContextForInlining;
        sb = target;
        context = newContext;
        Object argumentSeparatorValue;
        Object currentRowValue;
        Object constantInliningInterpreterContext;
        if (newContext == null) {
            argumentSeparatorValue = null;
            currentRowValue = null;
            constantInliningInterpreterContext = null;
        } else {
            argumentSeparatorValue = newContext.getContextParameter(ARGUMENT_SEPARATOR);
            currentRowValue = newContext.getContextParameter(CURRENT_ROW);
            constantInliningInterpreterContext = newContext.getContextParameter(CONSTANT_INLINING_INTERPRETER_CONTEXT);
        }
        if (argumentSeparatorValue == null) {
            argumentSeparator = ";";
        } else {
            argumentSeparator = argumentSeparatorValue.toString();
        }
        if (currentRowValue == null) {
            currentRow = 0;
        } else if (currentRowValue instanceof Number) {
            currentRow = ((Number) currentRowValue).intValue();
        } else {
            currentRow = Integer.valueOf(argumentSeparatorValue.toString());
        }
        if (constantInliningInterpreterContext == null) {
            interpreterContextForInlining = null;
        } else if (constantInliningInterpreterContext instanceof ExpressionInterpreter.Context) {
            interpreterContextForInlining = (ExpressionInterpreter.Context) constantInliningInterpreterContext;
        } else {
            throw new IllegalArgumentException("Illegal value given for '" + CONSTANT_INLINING_INTERPRETER_CONTEXT + "'. Expected ExpressionInterpreter.Context but got: " + constantInliningInterpreterContext);
        }
        try {
            expression.accept(this);
        } finally {
            sb = old;
            context = oldContext;
            argumentSeparator = oldArgumentSeparator;
            currentRow = oldCurrentRow;
            interpreterContextForInlining = oldInterpreterContextForInlining;
        }
    }

    private Boolean inlineIfConstant(Expression expression, int startIndex, boolean constant) {
        if (!constant || interpreterContextForInlining == null) {
            return Boolean.FALSE;
        }
        ExpressionInterpreter interpreter = interpreterForInlining;
        if (interpreter == null) {
            interpreter = interpreterForInlining = expressionService.createInterpreter();
        }
        try {
            Object value = interpreter.evaluateAsModelType(expression, interpreterContextForInlining);
            sb.setLength(startIndex);
            return visitLiteral(value, expression.getType());
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Could not inline expression '" + expression + "'", ex);
        }
    }

    @Override
    public Boolean visit(FunctionInvocation e) {
        int startIndex = sb.length();
        ExcelFunctionRenderer renderer = e.getFunction().getMetadata(ExcelFunctionRenderer.class);
        if (renderer == null) {
            if (interpreterContextForInlining != null) {
                return inlineIfConstant(e, startIndex, true);
            }
            throw new IllegalStateException("The domain function '" + e.getFunction().getName() + "' has no registered excel function renderer!");
        }
        Map<DomainFunctionArgument, Expression> arguments = e.getArguments();

        if (arguments.isEmpty()) {
            renderer.render(e.getFunction(), e.getType(), ExcelDomainFunctionArgumentRenderers.EMPTY, sb, this);
            return inlineIfConstant(e, startIndex, e.getFunction().getVolatility() != DomainFunctionVolatility.VOLATILE);
        } else {
            int size = e.getFunction().getArguments().size();
            Expression[] expressions = new Expression[size];
            for (Map.Entry<DomainFunctionArgument, Expression> entry : arguments.entrySet()) {
                DomainFunctionArgument domainFunctionArgument = entry.getKey();
                Expression expression = entry.getValue();
                expressions[domainFunctionArgument.getPosition()] = expression;
            }
            DefaultExcelDomainFunctionArgumentRenderers argumentRenderers = new DefaultExcelDomainFunctionArgumentRenderers(expressions, arguments.size());
            renderer.render(e.getFunction(), e.getType(), argumentRenderers, sb, this);
            return inlineIfConstant(e, startIndex, argumentRenderers.isAllArgumentsConstant() && e.getFunction().getVolatility() != DomainFunctionVolatility.VOLATILE);
        }
    }

    @Override
    public Boolean visit(Literal e) {
        return visitLiteral(e.getValue(), e.getType());
    }

    private Boolean visitLiteral(Object value, DomainType type) {
        if (type.getKind() == DomainType.DomainTypeKind.COLLECTION) {
            if (interpreterContextForInlining != null) {
                // This could be a problem if the literal was the root expression, but we assume this is not the case
                return Boolean.TRUE;
            }
            throw new UnsupportedOperationException("No support for collections in Excel");
        } else {
            ExcelLiteralRenderer literalRenderer = type.getMetadata(ExcelLiteralRenderer.class);
            if (literalRenderer != null) {
                literalRenderer.render(value, type, this);
            } else {
                sb.append(value);
            }
            return Boolean.TRUE;
        }
    }

    private void renderStringLiteral(CharSequence charSequence) {
        sb.append('"');
        for (int i = 0; i < charSequence.length(); i++) {
            final char c = charSequence.charAt(i);
            if (c == '"') {
                sb.append('"');
            }
            sb.append(c);
        }
        sb.append('"');
    }

    @Override
    public Boolean visit(EnumLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Boolean visit(EntityLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Boolean visit(CollectionLiteral e) {
        return visit((Literal) e);
    }

    @Override
    public Boolean visit(Path e) {
        if (e.getBase() != null) {
            if (interpreterContextForInlining != null) {
                return inlineIfConstant(e, sb.length(), true);
            }
            throw new UnsupportedOperationException("De-referencing arithmetic expressions is not supported for excel");
        }

        Object mapping = getExcelMapping(e);
        if (mapping == null) {
            if (interpreterContextForInlining != null) {
                return inlineIfConstant(e, sb.length(), true);
            }
            throw new IllegalArgumentException("Couldn't find an excel mapping for path: " + e);
        }

        if (mapping instanceof ExcelColumn) {
            ExcelColumn excelColumn = (ExcelColumn) mapping;
            if (excelColumn.getSheetName() != null) {
                sb.append(excelColumn.getSheetName()).append('!');
            }
            sb.append(getExcelColumnName(excelColumn.getColumnNumber())).append(currentRow);
            return Boolean.FALSE;
        } else if (mapping instanceof CharSequence) {
            renderStringLiteral((CharSequence) mapping);
            return Boolean.TRUE;
        } else {
            sb.append(mapping);
            return Boolean.TRUE;
        }
    }

    private String getExcelColumnName(int columnNumber) {
        StringBuilder sb = new StringBuilder(3);
        int dividend = columnNumber;

        while (dividend > 0) {
            int modulo = (dividend - 1) % 26;
            sb.insert(0, (char)(65 + modulo));
            dividend = (dividend - modulo) / 26;
        }

        return sb.toString();
    }

    /**
     * Returns the excel mapping for the path.
     *
     * @param p The path
     * @return The excel mapping for the path
     * @throws IllegalStateException when the root variable has no registered excel mapping
     */
    protected Object getExcelMapping(Path p) {
        List<EntityDomainTypeAttribute> attributes = p.getAttributes();
        tempSb.setLength(0);
        tempSb.append(p.getAlias());
        for (int i = 0; i < attributes.size(); i++) {
            tempSb.append('.');
            tempSb.append(attributes.get(i).getName());
        }
        Object mapping = context.getContextParameter(tempSb.toString());
        tempSb.setLength(0);
        return mapping;
    }

    @Override
    public Boolean visit(ArithmeticFactor e) {
        int startIndex = sb.length();
        if (e.isInvertSignum()) {
            sb.append('-');
        }
        return inlineIfConstant(e, startIndex, e.getExpression().accept(this));
    }

    @Override
    public Boolean visit(ExpressionPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        boolean isConstant = e.getExpression().accept(this);
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(ChainingArithmeticExpression e) {
        int startIndex = sb.length();
        ExcelDomainOperatorRenderer operatorRenderer = e.getType().getMetadata(ExcelDomainOperatorRenderer.class);
        return inlineIfConstant(e, startIndex, operatorRenderer.render(e, this));
    }

    @Override
    public Boolean visit(BetweenPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        sb.append("AND(");
        boolean isConstant = e.getLeft().accept(this);
        sb.append(" >= ");
        isConstant = e.getLower().accept(this) && isConstant;
        sb.append(argumentSeparator).append(' ');
        e.getLeft().accept(this);
        sb.append(" <= ");
        isConstant = e.getUpper().accept(this) && isConstant;
        sb.append(')');
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(InPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        List<ArithmeticExpression> inItems = e.getInItems();
        int size = inItems.size();
        if (size > 1) {
            sb.append("OR(");
        }

        boolean isConstant = e.getLeft().accept(this);
        sb.append(" = ");
        isConstant = inItems.get(0).accept(this) && isConstant;

        if (size > 1) {
            for (int i = 1; i < size; i++) {
                sb.append(argumentSeparator).append(' ');
                e.getLeft().accept(this);
                sb.append(" = ");
                isConstant = inItems.get(i).accept(this) && isConstant;
            }

            sb.append(')');
        }

        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(CompoundPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        List<Predicate> predicates = e.getPredicates();
        int size = predicates.size();
        Predicate predicate = predicates.get(0);
        boolean isConstant;
        if (size == 1) {
            isConstant = predicate.accept(this);
        } else {
            sb.append(e.isConjunction() ? "AND(" : "OR(");
            if (predicate instanceof CompoundPredicate && e.isConjunction() != ((CompoundPredicate) predicate).isConjunction()) {
                sb.append('(');
                isConstant = predicate.accept(this);
                sb.append(')');
            } else {
                isConstant = predicate.accept(this);
            }
            for (int i = 1; i < size; i++) {
                predicate = predicates.get(i);
                sb.append(argumentSeparator).append(' ');
                if (predicate instanceof CompoundPredicate && !predicate.isNegated() && e.isConjunction() != ((CompoundPredicate) predicate).isConjunction()) {
                    sb.append('(');
                    isConstant = predicate.accept(this) && isConstant;
                    sb.append(')');
                } else {
                    isConstant = predicate.accept(this) && isConstant;
                }
            }
            sb.append(')');
        }
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(ComparisonPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        boolean isConstant = e.getLeft().accept(this);
        sb.append(' ');
        if (e.getOperator() == ComparisonOperator.NOT_EQUAL) {
            sb.append("<>");
        } else {
            sb.append(e.getOperator().getOperator());
        }
        sb.append(' ');
        isConstant = e.getRight().accept(this) && isConstant;
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(IsNullPredicate e) {
        int startIndex = sb.length();
        boolean negated = e.isNegated();
        if (negated) {
            sb.append("NOT(");
        }
        sb.append("ISNA(");
        boolean isConstant = e.getLeft().accept(this);
        sb.append(')');
        if (negated) {
            sb.append(')');
        }
        return inlineIfConstant(e, startIndex, isConstant);
    }

    @Override
    public Boolean visit(IsEmptyPredicate e) {
        if (interpreterContextForInlining != null) {
            return inlineIfConstant(e, sb.length(), true);
        }
        throw new UnsupportedOperationException("No support for collections in Excel");
    }

    @Override
    public Boolean visit(Query e) {
        throw new UnsupportedOperationException("No support for queries in Excel");
    }

    @Override
    public Boolean visit(FromItem e) {
        throw new UnsupportedOperationException("No support for queries in Excel");
    }

    @Override
    public Boolean visit(Join e) {
        throw new UnsupportedOperationException("No support for queries in Excel");
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private final class DefaultExcelDomainFunctionArgumentRenderers implements ExcelDomainFunctionArgumentRenderers {

        private final Expression[] expressions;
        private final int assignedArguments;
        private boolean allArgumentsConstant = true;

        public DefaultExcelDomainFunctionArgumentRenderers(Expression[] expressions, int assignedArguments) {
            this.expressions = expressions;
            this.assignedArguments = assignedArguments;
        }

        public boolean isAllArgumentsConstant() {
            return allArgumentsConstant;
        }

        @Override
        public Expression getExpression(int position) {
            try {
                return expressions[position];
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            }
        }

        @Override
        public DomainType getType(int position) {
            try {
                return expressions[position].getType();
            } catch (ArrayIndexOutOfBoundsException ex) {
                throw new DomainModelException(ex);
            } catch (NullPointerException ex) {
                return null;
            }
        }

        @Override
        public int assignedArguments() {
            return assignedArguments;
        }

        @Override
        public boolean renderArgument(StringBuilder sb, int position) {
            StringBuilder oldSb = ExcelExpressionSerializer.this.sb;
            ExcelExpressionSerializer.this.sb = sb;
            try {
                if (expressions[position].accept(ExcelExpressionSerializer.this)) {
                    return true;
                } else {
                    allArgumentsConstant = false;
                    return false;
                }
            } finally {
                ExcelExpressionSerializer.this.sb = oldSb;
            }
        }

        @Override
        public void renderArguments(StringBuilder sb) {
            if (assignedArguments != 0) {
                StringBuilder oldSb = ExcelExpressionSerializer.this.sb;
                ExcelExpressionSerializer.this.sb = sb;
                String separator = ExcelExpressionSerializer.this.argumentSeparator + " ";
                try {
                    for (int i = 0; i < assignedArguments; i++) {
                        if (!expressions[i].accept(ExcelExpressionSerializer.this)) {
                            allArgumentsConstant = false;
                        }
                        sb.append(separator);
                    }
                    sb.setLength(sb.length() - 2);
                } finally {
                    ExcelExpressionSerializer.this.sb = oldSb;
                }
            }
        }
    }
}
