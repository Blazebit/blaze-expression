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

/**
 * A class that models an excel column.
 *
 * @author Christian Beikov
 * @since 1.0.0
 * @see ExcelExpressionSerializer
 * @see ExcelExpressionSerializerContext
 */
public final class ExcelColumn {

    private final String sheetName;
    private final int columnNumber;

    /**
     * Creates a new excel column with the given 0-based number.
     *
     * @param columnNumber The 0-based column number
     */
    public ExcelColumn(int columnNumber) {
        this(null, columnNumber);
    }

    /**
     * Creates a new excel column with the given 0-based number.
     *
     * @param sheetName The name of the sheet on which the column is located
     * @param columnNumber The 0-based column number
     */
    public ExcelColumn(String sheetName, int columnNumber) {
        if (columnNumber < 0) {
            throw new IllegalArgumentException("Column number must be greater or equal to 0: " + columnNumber);
        }
        this.sheetName = sheetName;
        this.columnNumber = columnNumber;
    }

    /**
     * Returns the 0-based column number.
     *
     * @return the 0-based column number
     */
    public int getColumnNumber() {
        return columnNumber;
    }

    /**
     * Returns the name of the sheet the column is located.
     *
     * @return the name of the sheet the column is located
     */
    public String getSheetName() {
        return sheetName;
    }
}
