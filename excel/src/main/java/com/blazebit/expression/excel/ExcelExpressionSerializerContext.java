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

import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of a context for the {@link ExcelExpressionSerializer}.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExcelExpressionSerializerContext implements ExpressionSerializer.Context {

    private final ExpressionService expressionService;
    private final Map<String, Object> contextParameters;
    private final Map<String, ExcelColumn> excelColumns;

    /**
     * Creates a new excel expression serializer context.
     *
     * @param expressionService The expression service
     * @param currentRow The current excel row
     */
    public ExcelExpressionSerializerContext(ExpressionService expressionService, int currentRow) {
        this.expressionService = expressionService;
        this.contextParameters = new HashMap<>();
        this.excelColumns = new HashMap<>();
        setCurrentRow(currentRow);
    }

    /**
     * Adds a context parameter with the given name and value.
     *
     * @param contextParameterName The name of the context parameter to add
     * @param value The value
     * @return <code>this</code> for method chaining
     */
    public ExcelExpressionSerializerContext withContextParameter(String contextParameterName, Object value) {
        contextParameters.put(contextParameterName, value);
        return this;
    }

    /**
     * Returns the context parameters.
     *
     * @return the context parameters
     */
    public Map<String, Object> getContextParameters() {
        return contextParameters;
    }

    /**
     * Maps the given expression path to the given excel column number.
     *
     * @param path The expression path for which to add the column number mapping
     * @param columnNumber The 0-based column number
     * @return <code>this</code> for method chaining
     */
    public ExcelExpressionSerializerContext withExcelColumn(String path, int columnNumber) {
        excelColumns.put(path, new ExcelColumn(columnNumber));
        return this;
    }

    /**
     * Maps the given expression path to the given excel column number.
     *
     * @param path The expression path for which to add the column number mapping
     * @param sheetName The name of the sheet on which the column is located
     * @param columnNumber The 0-based column number
     * @return <code>this</code> for method chaining
     */
    public ExcelExpressionSerializerContext withExcelColumn(String path, String sheetName, int columnNumber) {
        excelColumns.put(path, new ExcelColumn(sheetName, columnNumber));
        return this;
    }

    /**
     * Returns the excel column mappings.
     *
     * @return the excel column mappings
     */
    public Map<String, ExcelColumn> getExcelColumns() {
        return excelColumns;
    }

    /**
     * Returns the current excel row.
     *
     * @return the current excel row
     */
    public int getCurrentRow() {
        return (int) contextParameters.get(ExcelExpressionSerializer.CURRENT_ROW);
    }

    /**
     * Sets the current row to the given value.
     *
     * @param currentRow the new current row value
     */
    public void setCurrentRow(int currentRow) {
        contextParameters.put(ExcelExpressionSerializer.CURRENT_ROW, currentRow);
    }

    /**
     * Increments the current row by 1.
     */
    public void incrementCurrentRow() {
        contextParameters.put(ExcelExpressionSerializer.CURRENT_ROW, getCurrentRow() + 1);
    }

    /**
     * Returns the argument separator to use for function arguments.
     *
     * @return the argument separator
     */
    public String getArgumentSeparator() {
        return (String) contextParameters.get(ExcelExpressionSerializer.ARGUMENT_SEPARATOR);
    }

    /**
     * Sets the argument separator to use for function arguments.
     *
     * @param argumentSeparator The argument separator
     */
    public void setArgumentSeparator(String argumentSeparator) {
        contextParameters.put(ExcelExpressionSerializer.ARGUMENT_SEPARATOR, argumentSeparator);
    }

    /**
     * Sets the argument separator to use for function arguments.
     *
     * @param argumentSeparator The argument separator
     * @return <code>this</code> for method chaining
     */
    public ExcelExpressionSerializerContext withArgumentSeparator(String argumentSeparator) {
        contextParameters.put(ExcelExpressionSerializer.ARGUMENT_SEPARATOR, argumentSeparator);
        return this;
    }

    /**
     * Returns the interpreter context that should be used for inlining of paths that are not mapped as excel columns.
     *
     * @return the interpreter context for inlining
     */
    public ExpressionInterpreter.Context getInterpreterContextForInlining() {
        return (ExpressionInterpreter.Context) contextParameters.get(ExcelExpressionSerializer.CONSTANT_INLINING_INTERPRETER_CONTEXT);
    }

    /**
     * Sets the interpreter context to use for inlining of paths.
     *
     * @param interpreterContextForInlining The interpreter context
     */
    public void setInterpreterContextForInlining(ExpressionInterpreter.Context interpreterContextForInlining) {
        contextParameters.put(ExcelExpressionSerializer.CONSTANT_INLINING_INTERPRETER_CONTEXT, interpreterContextForInlining);
    }

    /**
     * Sets the interpreter context to use for inlining of paths.
     *
     * @param interpreterContextForInlining The interpreter context
     * @return <code>this</code> for method chaining
     */
    public ExcelExpressionSerializerContext withInterpreterContextForInlining(ExpressionInterpreter.Context interpreterContextForInlining) {
        contextParameters.put(ExcelExpressionSerializer.CONSTANT_INLINING_INTERPRETER_CONTEXT, interpreterContextForInlining);
        return this;
    }

    @Override
    public ExpressionService getExpressionService() {
        return expressionService;
    }

    @Override
    public <X> X getContextParameter(String contextParameterName) {
        ExcelColumn excelColumn = excelColumns.get(contextParameterName);
        if (excelColumn != null) {
            return (X) excelColumn;
        }
        return (X) contextParameters.get(contextParameterName);
    }
}
