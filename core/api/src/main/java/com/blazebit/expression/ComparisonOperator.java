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

package com.blazebit.expression;

import com.blazebit.domain.runtime.model.DomainPredicate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The comparison operators.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public enum ComparisonOperator {

    /**
     * The &gt; operator.
     */
    GREATER(">", DomainPredicate.RELATIONAL),
    /**
     * The &gt;= operator.
     */
    GREATER_OR_EQUAL(">=", DomainPredicate.RELATIONAL),
    /**
     * The &lt; operator.
     */
    LOWER("<", DomainPredicate.RELATIONAL),
    /**
     * The &lt;= operator.
     */
    LOWER_OR_EQUAL("<=", DomainPredicate.RELATIONAL),
    /**
     * The = operator.
     */
    EQUAL("=", DomainPredicate.EQUALITY),
    /**
     * The != or &lt;&gt; operator.
     */
    NOT_EQUAL("!=", DomainPredicate.EQUALITY);

    private static final Map<String, ComparisonOperator> OPERATOR_MAP;

    static {
        Map<String, ComparisonOperator> operatorMap = new HashMap<>();

        for (ComparisonOperator operatorType : ComparisonOperator.values()) {
            operatorMap.put(operatorType.getOperator(), operatorType);
        }

        operatorMap.put("<>", NOT_EQUAL);
        OPERATOR_MAP = Collections.unmodifiableMap(operatorMap);
    }

    private final String operator;
    private final DomainPredicate domainPredicate;

    private ComparisonOperator(String operator, DomainPredicate domainPredicate) {
        this.operator = operator;
        this.domainPredicate = domainPredicate;
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
     * Returns the corresponding domain predicate type.
     *
     * @return the corresponding domain predicate type
     */
    public DomainPredicate getDomainPredicate() {
        return domainPredicate;
    }

    /**
     * Interprets the given string representation as operator and returns it.
     *
     * @param operator The string representation of the operator
     * @return the comparison operator
     * @throws IllegalArgumentException if encountering an invalid operator
     */
    public static ComparisonOperator valueOfOperator(String operator) {
        ComparisonOperator operatorType = OPERATOR_MAP.get(operator);
        if (operatorType == null) {
            throw new IllegalArgumentException("Invalid operator: " + operator);
        } else {
            return operatorType;
        }
    }
}
