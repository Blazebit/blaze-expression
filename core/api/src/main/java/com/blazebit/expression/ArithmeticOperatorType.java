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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.DomainOperator;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The arithmetic operators.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public enum ArithmeticOperatorType {

    // Note that the operators are ordered by precedence

    /**
     * The * operator.
     */
    MULTIPLY("*", DomainOperator.MULTIPLICATION),
    /**
     * The / operator.
     */
    DIVIDE("/", DomainOperator.DIVISION),
    /**
     * The % operator.
     */
    MODULO("%", DomainOperator.MODULO),
    /**
     * The + operator.
     */
    PLUS("+", DomainOperator.PLUS),
    /**
     * The - operator.
     */
    MINUS("-", DomainOperator.MINUS);

    private static final Map<String, ArithmeticOperatorType> OPERATOR_MAP;

    static {
        Map<String, ArithmeticOperatorType> operatorMap = new HashMap<>();
        for (ArithmeticOperatorType operatorType : EnumSet.allOf(ArithmeticOperatorType.class)) {
            operatorMap.put(operatorType.getOperator(), operatorType);
        }
        OPERATOR_MAP = Collections.unmodifiableMap(operatorMap);
    }

    private final String operator;
    private final DomainOperator domainOperator;

    private ArithmeticOperatorType(String operator, DomainOperator domainOperator) {
        this.operator = operator;
        this.domainOperator = domainOperator;
    }

    /**
     * Returns the string representation of the operator.
     *
     * @return the string representation of the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * Returns the corresponding domain operator.
     *
     * @return the corresponding domain operator
     */
    public DomainOperator getDomainOperator() {
        return domainOperator;
    }

    /**
     * Interprets the given string representation as operator and returns it.
     *
     * @param operator The string representation of the operator
     * @return the arithmetic operator
     * @throws IllegalArgumentException if encountering an invalid operator
     */
    public static ArithmeticOperatorType valueOfOperator(String operator) {
        ArithmeticOperatorType operatorType = OPERATOR_MAP.get(operator);
        if (operatorType == null) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        } else {
            return operatorType;
        }
    }

    /**
     * Returns whether this operator has precedence over the given one.
     *
     * @param operatorType The other operator
     * @return whether this operator has precedence over the given one
     */
    public boolean hasPrecedenceOver(ArithmeticOperatorType operatorType) {
        return ordinal() <= operatorType.ordinal();
    }
}
