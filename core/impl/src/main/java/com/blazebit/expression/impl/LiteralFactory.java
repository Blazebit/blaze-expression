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
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionCompiler;
import com.blazebit.expression.ExpressionService;
import com.blazebit.expression.Literal;
import com.blazebit.expression.SyntaxErrorException;
import com.blazebit.expression.spi.BooleanLiteralResolver;
import com.blazebit.expression.spi.CollectionLiteralResolver;
import com.blazebit.expression.spi.EntityLiteralResolver;
import com.blazebit.expression.spi.EnumLiteralResolver;
import com.blazebit.expression.spi.NumericLiteralResolver;
import com.blazebit.expression.spi.ResolvedLiteral;
import com.blazebit.expression.spi.StringLiteralResolver;
import com.blazebit.expression.spi.TemporalLiteralResolver;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Map;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class LiteralFactory {

    static final char OPEN_BRACKET = '{';

    private static final DateTimeFormatter DATE_LITERAL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter DATE_TIME_LITERAL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter DATE_TIME_MILLISECONDS_LITERAL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter TIME_LITERAL_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter TIME_MILLISECONDS_LITERAL_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(ZoneOffset.UTC);

    private static final String TEMPORAL_INTERVAL_YEARS_FIELD = "years";
    private static final String TEMPORAL_INTERVAL_MONTHS_FIELD = "months";
    private static final String TEMPORAL_INTERVAL_DAYS_FIELD = "days";
    private static final String TEMPORAL_INTERVAL_HOURS_FIELD = "hours";
    private static final String TEMPORAL_INTERVAL_MINUTES_FIELD = "minutes";
    private static final String TEMPORAL_INTERVAL_SECONDS_FIELD = "seconds";

    private final ExpressionService expressionService;
    private final boolean exact;

    public LiteralFactory(ExpressionService expressionService) {
        this.expressionService = expressionService;
        DomainType numericType = expressionService.getDomainModel().getType("Numeric");
        this.exact = numericType == null || numericType.getJavaType() == BigDecimal.class;
    }

    public static String unescapeString(String s) {
        int end = s.length() - 1;
        StringBuilder sb = new StringBuilder(end - 1);
        for (int i = 1; i < end; i++) {
            char c = s.charAt(i);
            if (c == '\\' && (i + 1) < end) {
                final char nextChar = s.charAt(++i);
                switch (nextChar) {
                    case 'b':
                        c = '\b';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    case 'n':
                        c = '\n';
                        break;
                    case 'f':
                        c = '\f';
                        break;
                    case 'r':
                        c = '\r';
                        break;
                    case '\\':
                        c = '\\';
                        break;
                    case '\'':
                        c = '\'';
                        break;
                    case '"':
                        c = '"';
                        break;
                    case '`':
                        c = '`';
                        break;
                    case 'u':
                        c = (char) Integer.parseInt(s.substring(i + 1, i + 5), 16);
                        i += 4;
                        break;
                    default:
                        break;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public static String unescapeTemplateText(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        int end = s.length();
        for (int i = 0; i < end; i++) {
            char c = s.charAt(i);
            if (c == '\\' && (i + 1) < end && s.charAt(i + 1) == '#') {
                continue;
            }
            sb.append(c);
        }
        return end == sb.length() ? s : sb.toString();
    }

    public ResolvedLiteral ofEnumValue(ExpressionCompiler.Context context, EnumDomainType enumDomainType, String value) {
        EnumDomainTypeValue domainEnumValue = enumDomainType.getEnumValues().get(value);
        if (domainEnumValue == null) {
            throw new DomainModelException("The value '" + value + "' on the enum domain type '" + enumDomainType.getName() + "' does not exist!");
        }
        EnumLiteralResolver enumLiteralResolver = expressionService.getEnumLiteralResolver();
        if (enumLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for enum literals defined");
        }
        ResolvedLiteral literal = enumLiteralResolver.resolveLiteral(context, domainEnumValue);
        if (literal == null) {
            throw new DomainModelException("Could not resolve enum literal for: " + domainEnumValue);
        }
        return literal;
    }

    public ResolvedLiteral ofEntityAttributeValues(ExpressionCompiler.Context context, EntityDomainType entityDomainType, Map<EntityDomainTypeAttribute, ? extends Literal> attributeValues) {
        EntityLiteralResolver entityLiteralResolver = expressionService.getEntityLiteralResolver();
        if (entityLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for entity literals defined");
        }
        ResolvedLiteral literal = entityLiteralResolver.resolveLiteral(context, entityDomainType, attributeValues);
        if (literal == null) {
            throw new DomainModelException("Could not resolve entity literal for type '" + entityDomainType + "' and attribute values: " + attributeValues);
        }
        return literal;
    }

    public ResolvedLiteral ofCollectionValues(ExpressionCompiler.Context context, CollectionDomainType collectionDomainType, Collection<? extends Literal> expressions) {
        CollectionLiteralResolver collectionLiteralResolver = expressionService.getCollectionLiteralResolver();
        if (collectionLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for collection literals defined");
        }
        ResolvedLiteral literal = collectionLiteralResolver.resolveLiteral(context, collectionDomainType, expressions);
        if (literal == null) {
            throw new DomainModelException("Could not resolve collection literal for type '" + collectionDomainType + "' and expressions: " + expressions);
        }
        return literal;
    }

    public ResolvedLiteral ofTemporalIntervalString(ExpressionCompiler.Context context, String intervalString) {
        String[] intervalStringParts = intervalString.split("\\s+");
        int years = 0;
        int months = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        for (int i = 0; i < intervalStringParts.length / 2; i++) {
            String amountString = intervalStringParts[2 * i];
            String temporalField = intervalStringParts[2 * i + 1];

            int amount = 0;
            NumberFormatException exception = null;
            try {
                amount = Integer.parseInt(amountString);
            } catch (NumberFormatException ex) {
                exception = ex;
            }

            if (TEMPORAL_INTERVAL_YEARS_FIELD.equalsIgnoreCase(temporalField)) {
                if (exception != null || amount < 0) {
                    throw new SyntaxErrorException("Illegal value given for temporal field '" + TEMPORAL_INTERVAL_YEARS_FIELD + "': " + amountString, exception);
                }
                years = amount;
            } else if (TEMPORAL_INTERVAL_MONTHS_FIELD.equalsIgnoreCase(temporalField)) {
                if (exception != null || amount < 0) {
                    throw new SyntaxErrorException("Illegal value given for temporal field '" + TEMPORAL_INTERVAL_MONTHS_FIELD + "': " + amountString, exception);
                }
                months = amount;
            } else if (TEMPORAL_INTERVAL_DAYS_FIELD.equalsIgnoreCase(temporalField)) {
                if (exception != null || amount < 0) {
                    throw new SyntaxErrorException("Illegal value given for temporal field '" + TEMPORAL_INTERVAL_DAYS_FIELD + "': " + amountString, exception);
                }
                days = amount;
            } else if (TEMPORAL_INTERVAL_HOURS_FIELD.equalsIgnoreCase(temporalField)) {
                if (exception != null || amount < 0) {
                    throw new SyntaxErrorException("Illegal value given for temporal field '" + TEMPORAL_INTERVAL_HOURS_FIELD + "': " + amountString, exception);
                }
                hours = amount;
            } else if (TEMPORAL_INTERVAL_MINUTES_FIELD.equalsIgnoreCase(temporalField)) {
                if (exception != null || amount < 0) {
                    throw new SyntaxErrorException("Illegal value given for temporal field '" + TEMPORAL_INTERVAL_MINUTES_FIELD + "': " + amountString, exception);
                }
                minutes = amount;
            } else if (TEMPORAL_INTERVAL_SECONDS_FIELD.equalsIgnoreCase(temporalField)) {
                if (exception != null || amount < 0) {
                    throw new SyntaxErrorException("Illegal value given for temporal field '" + TEMPORAL_INTERVAL_SECONDS_FIELD + "': " + amountString, exception);
                }
                seconds = amount;
            } else {
                throw new SyntaxErrorException("Illegal temporal field in interval: " + temporalField);
            }
        }

        return ofTemporalAmounts(context, years, months, days, hours, minutes, seconds);
    }

    public ResolvedLiteral ofTemporalAmounts(ExpressionCompiler.Context context, int years, int months, int days, int hours, int minutes, int seconds) {
        TemporalLiteralResolver temporalLiteralResolver = expressionService.getTemporalLiteralResolver();
        if (temporalLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for temporal interval literals defined");
        }
        TemporalInterval interval = new TemporalInterval(years, months, days, hours, minutes, seconds);
        return temporalLiteralResolver.resolveIntervalLiteral(context, interval);
    }

    public void appendInterval(StringBuilder sb, TemporalInterval value) {
        sb.append("INTERVAL");
        int years = value.getYears();
        if (years != 0) {
            sb.append(' ').append(years).append(" YEARS");
        }
        int months = value.getMonths();
        if (months != 0) {
            sb.append(' ').append(months).append(" MONTHS");
        }
        int days = value.getDays();
        if (days != 0) {
            sb.append(' ').append(days).append(" DAYS");
        }
        int hours = value.getHours();
        if (hours != 0) {
            sb.append(' ').append(hours).append(" HOURS");
        }
        int minutes = value.getMinutes();
        if (minutes != 0) {
            sb.append(' ').append(minutes).append(" MINUTES");
        }
        int seconds = value.getSeconds();
        if (seconds != 0) {
            sb.append(' ').append(seconds).append(" SECONDS");
        }
    }

    public ResolvedLiteral ofString(ExpressionCompiler.Context context, String string) {
        StringLiteralResolver stringLiteralResolver = expressionService.getStringLiteralResolver();
        if (stringLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for string literals defined");
        }
        return stringLiteralResolver.resolveLiteral(context, string);
    }

    public void appendString(StringBuilder sb, String value) {
        sb.append('\'');
        int end = value.length();
        sb.ensureCapacity(sb.length() + end + 10);
        for (int i = 0; i < end; i++) {
            char c = value.charAt(i);
            switch (c) {
                case '\b':
                    sb.append('\\').append('b');
                    break;
                case '\t':
                    sb.append('\\').append('t');
                    break;
                case '\n':
                    sb.append('\\').append('n');
                    break;
                case '\f':
                    sb.append('\\').append('f');
                    break;
                case '\r':
                    sb.append('\\').append('r');
                    break;
                case '\\':
                    sb.append('\\').append('\\');
                    break;
                case '\'':
                    sb.append('\\').append('\'');
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        sb.append('\'');
    }

    public void appendTemplateString(StringBuilder sb, String value) {
        int end = value.length();
        sb.ensureCapacity(sb.length() + end + 10);
        for (int i = 0; i < end; i++) {
            char c = value.charAt(i);
            if (c == '#' && (i + 1) < end && value.charAt(i + 1) == '{') {
                sb.append('\\');
            }
            sb.append(c);
        }
    }

    public ResolvedLiteral ofDateString(ExpressionCompiler.Context context, String dateString) {
        try {
            return ofLocalDate(context, LocalDate.parse(dateString, DATE_LITERAL_FORMAT));
        } catch (DateTimeParseException e) {
            throw new SyntaxErrorException("Invalid datetime literal " + dateString, e);
        }
    }

    public ResolvedLiteral ofTimeString(ExpressionCompiler.Context context, String timeString) {
        boolean hasDot = timeString.indexOf('.') != -1;

        try {
            LocalTime localTime;
            if (!hasDot) {
                localTime = OffsetTime.parse(timeString, TIME_LITERAL_FORMAT).toLocalTime();
            } else {
                localTime = OffsetTime.parse(timeString, TIME_MILLISECONDS_LITERAL_FORMAT).toLocalTime();
            }
            return ofLocalTime(context, localTime);
        } catch (DateTimeParseException e) {
            throw new SyntaxErrorException("Invalid datetime literal " + timeString, e);
        }
    }

    public ResolvedLiteral ofDateTimeString(ExpressionCompiler.Context context, String dateTimeString) {
        boolean hasBlank = dateTimeString.indexOf(' ') != -1;
        boolean hasDot = dateTimeString.indexOf('.') != -1;

        try {
            Instant dateTime;
            if (!hasBlank && !hasDot) {
                dateTime = Instant.ofEpochSecond(LocalDate.parse(dateTimeString, DATE_LITERAL_FORMAT).toEpochDay() * 24 * 60 * 60);
            } else if (!hasDot) {
                dateTime = ZonedDateTime.parse(dateTimeString, DATE_TIME_LITERAL_FORMAT).toInstant();
            } else {
                dateTime = ZonedDateTime.parse(dateTimeString, DATE_TIME_MILLISECONDS_LITERAL_FORMAT).toInstant();
            }
            return ofInstant(context, dateTime);
        } catch (DateTimeParseException e) {
            throw new SyntaxErrorException("Invalid datetime literal " + dateTimeString, e);
        }
    }

    public ResolvedLiteral ofLocalDate(ExpressionCompiler.Context context, LocalDate localDate) {
        TemporalLiteralResolver temporalLiteralResolver = expressionService.getTemporalLiteralResolver();
        if (temporalLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for temporal literals defined");
        }
        return temporalLiteralResolver.resolveDateLiteral(context, localDate);
    }

    public ResolvedLiteral ofLocalTime(ExpressionCompiler.Context context, LocalTime localTime) {
        TemporalLiteralResolver temporalLiteralResolver = expressionService.getTemporalLiteralResolver();
        if (temporalLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for temporal literals defined");
        }
        return temporalLiteralResolver.resolveTimeLiteral(context, localTime);
    }

    public ResolvedLiteral ofInstant(ExpressionCompiler.Context context, Instant instant) {
        TemporalLiteralResolver temporalLiteralResolver = expressionService.getTemporalLiteralResolver();
        if (temporalLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for temporal literals defined");
        }
        return temporalLiteralResolver.resolveTimestampLiteral(context, instant);
    }

    public void appendInstant(StringBuilder sb, Instant value) {
        ZonedDateTime dateTime = value.atZone(ZoneOffset.UTC);
        sb.append("TIMESTAMP(");
        if (dateTime.getNano() > 0) {
            DATE_TIME_MILLISECONDS_LITERAL_FORMAT.formatTo(dateTime, sb);
        } else if (dateTime.getSecond() > 0 || dateTime.getMinute() > 0 || dateTime.getHour() > 0) {
            DATE_TIME_LITERAL_FORMAT.formatTo(dateTime, sb);
        } else {
            DATE_LITERAL_FORMAT.formatTo(dateTime, sb);
        }
        sb.append(')');
    }

    public ResolvedLiteral ofNumericString(ExpressionCompiler.Context context, String numericString) {
        try {
            Number numeric;
            if (exact) {
                numeric = new BigDecimal(numericString);
            } else {
                numeric = Double.parseDouble(numericString);
            }
            NumericLiteralResolver numericLiteralResolver = expressionService.getNumericLiteralResolver();
            if (numericLiteralResolver == null) {
                throw new DomainModelException("No literal resolver for numeric literals defined");
            }
            return numericLiteralResolver.resolveLiteral(context, numeric);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(e);
        }
    }

    public ResolvedLiteral ofIntegerString(ExpressionCompiler.Context context, String integerString) {
        try {
            Number integer;
            if (exact) {
                integer = new BigInteger(integerString);
            } else {
                integer = Long.parseLong(integerString);
            }
            NumericLiteralResolver numericLiteralResolver = expressionService.getNumericLiteralResolver();
            if (numericLiteralResolver == null) {
                throw new DomainModelException("No literal resolver for numeric literals defined");
            }
            return numericLiteralResolver.resolveLiteral(context, integer);
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(e);
        }
    }

    public void appendNumeric(StringBuilder sb, Number value) {
        sb.append(value);
    }

    public ResolvedLiteral ofBoolean(ExpressionCompiler.Context context, boolean value) {
        BooleanLiteralResolver booleanLiteralResolver = expressionService.getBooleanLiteralResolver();
        if (booleanLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for boolean literals defined");
        }
        return booleanLiteralResolver.resolveLiteral(context, value);
    }

    public void appendBoolean(StringBuilder sb, boolean value) {
        sb.append(value);
    }
}
