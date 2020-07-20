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

import com.blazebit.domain.runtime.model.BooleanLiteralResolver;
import com.blazebit.domain.runtime.model.CollectionDomainType;
import com.blazebit.domain.runtime.model.CollectionLiteralResolver;
import com.blazebit.domain.runtime.model.DomainModel;
import com.blazebit.domain.runtime.model.EntityDomainType;
import com.blazebit.domain.runtime.model.EntityDomainTypeAttribute;
import com.blazebit.domain.runtime.model.EntityLiteralResolver;
import com.blazebit.domain.runtime.model.EnumDomainType;
import com.blazebit.domain.runtime.model.EnumDomainTypeValue;
import com.blazebit.domain.runtime.model.EnumLiteralResolver;
import com.blazebit.domain.runtime.model.NumericLiteralResolver;
import com.blazebit.domain.runtime.model.ResolvedLiteral;
import com.blazebit.domain.runtime.model.StringLiteralResolver;
import com.blazebit.domain.runtime.model.TemporalInterval;
import com.blazebit.domain.runtime.model.TemporalLiteralResolver;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.Expression;
import com.blazebit.expression.SyntaxErrorException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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

    private static final DateTimeFormatter DATE_LITERAL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter DATE_TIME_LITERAL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter DATE_TIME_MILLISECONDS_LITERAL_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneOffset.UTC);

    private static final String TEMPORAL_INTERVAL_YEARS_FIELD = "years";
    private static final String TEMPORAL_INTERVAL_MONTHS_FIELD = "months";
    private static final String TEMPORAL_INTERVAL_DAYS_FIELD = "days";
    private static final String TEMPORAL_INTERVAL_HOURS_FIELD = "hours";
    private static final String TEMPORAL_INTERVAL_MINUTES_FIELD = "minutes";
    private static final String TEMPORAL_INTERVAL_SECONDS_FIELD = "seconds";

    private final DomainModel domainModel;
    private final NumericLiteralResolver numericLiteralResolver;
    private final BooleanLiteralResolver booleanLiteralResolver;
    private final StringLiteralResolver stringLiteralResolver;
    private final TemporalLiteralResolver temporalLiteralResolver;
    private final EnumLiteralResolver enumLiteralResolver;
    private final EntityLiteralResolver entityLiteralResolver;
    private final CollectionLiteralResolver collectionLiteralResolver;

    public LiteralFactory(DomainModel domainModel) {
        this.domainModel = domainModel;
        this.numericLiteralResolver = domainModel.getNumericLiteralResolver();
        this.booleanLiteralResolver = domainModel.getBooleanLiteralResolver();
        this.stringLiteralResolver = domainModel.getStringLiteralResolver();
        this.temporalLiteralResolver = domainModel.getTemporalLiteralResolver();
        this.enumLiteralResolver = domainModel.getEnumLiteralResolver();
        this.entityLiteralResolver = domainModel.getEntityLiteralResolver();
        this.collectionLiteralResolver = domainModel.getCollectionLiteralResolver();
    }

    public ResolvedLiteral ofEnumValue(EnumDomainType enumDomainType, String value) {
        EnumDomainTypeValue domainEnumValue = enumDomainType.getEnumValues().get(value);
        if (domainEnumValue == null) {
            throw new DomainModelException("The value '" + value + "' on the enum domain type '" + enumDomainType.getName() + "' does not exist!");
        }
        if (enumLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for enum literals defined");
        }
        ResolvedLiteral literal = enumLiteralResolver.resolveLiteral(domainModel, domainEnumValue);
        if (literal == null) {
            throw new DomainModelException("Could not resolve enum literal for: " + domainEnumValue);
        }
        return literal;
    }

    public void appendEnumValue(StringBuilder sb, EnumDomainTypeValue domainEnumValue) {
        sb.append(domainEnumValue.getOwner().getName()).append('.').append(domainEnumValue.getValue());
    }

    public ResolvedLiteral ofEntityAttributeValues(EntityDomainType entityDomainType, Map<EntityDomainTypeAttribute, Expression> attributeValues) {
        if (entityLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for entity literals defined");
        }
        ResolvedLiteral literal = entityLiteralResolver.resolveLiteral(domainModel, entityDomainType, attributeValues);
        if (literal == null) {
            throw new DomainModelException("Could not resolve entity literal for type '" + entityDomainType + "' and attribute values: " + attributeValues);
        }
        return literal;
    }

    public ResolvedLiteral ofCollectionValues(CollectionDomainType collectionDomainType, Collection<Expression> expressions) {
        if (collectionLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for collection literals defined");
        }
        ResolvedLiteral literal = collectionLiteralResolver.resolveLiteral(domainModel, collectionDomainType, expressions);
        if (literal == null) {
            throw new DomainModelException("Could not resolve collection literal for type '" + collectionDomainType + "' and expressions: " + expressions);
        }
        return literal;
    }

    public ResolvedLiteral ofTemporalIntervalString(String intervalString) {
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

        return ofTemporalAmounts(years, months, days, hours, minutes, seconds);
    }

    public ResolvedLiteral ofTemporalAmounts(int years, int months, int days, int hours, int minutes, int seconds) {
        TemporalInterval interval = new TemporalInterval(years, months, days, hours, minutes, seconds);
        if (temporalLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for temporal interval literals defined");
        }
        return temporalLiteralResolver.resolveIntervalLiteral(domainModel, interval);
    }

    public ResolvedLiteral ofQuotedString(String quotedString) {
        final char quoteChar;
        if (quotedString.length() >= 2 && ((quoteChar = quotedString.charAt(0)) == '\'' || quoteChar == '"') && quotedString.charAt(quotedString.length() - 1) == quoteChar) {
            StringBuilder sb = new StringBuilder();
            int endIndex = quotedString.length() - 1;
            for (int i = 1; i < endIndex; i++) {
                char c = quotedString.charAt(i);
                // Double quote
                if (c == quoteChar) {
                    if (quotedString.charAt(i + 1) != quoteChar) {
                        throw new SyntaxErrorException("String quoting unbalanced [" + quotedString + "]");
                    }
                    i++;
                }
                sb.append(c);
            }

            return ofString(sb.toString());
        } else {
            throw new SyntaxErrorException("String not quoted [" + quotedString + "]");
        }
    }

    public ResolvedLiteral ofString(String string) {
        if (stringLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for string literals defined");
        }
        return stringLiteralResolver.resolveLiteral(domainModel, string);
    }

    public void appendString(StringBuilder sb, String value) {
        sb.append('\'');
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            // Double quote
            if (c == '\'') {
                sb.append('\'');
            }
            sb.append(c);
        }
        sb.append('\'');
    }

    public ResolvedLiteral ofDateTimeString(String dateTimeString) {
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
            return ofInstant(dateTime);
        } catch (DateTimeParseException e) {
            throw new SyntaxErrorException("Invalid datetime literal " + dateTimeString, e);
        }
    }

    public ResolvedLiteral ofInstant(Instant instant) {
        if (temporalLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for temporal literals defined");
        }
        return temporalLiteralResolver.resolveTimestampLiteral(domainModel, instant);
    }

    public void appendInstant(StringBuilder sb, Instant value) {
        ZonedDateTime dateTime = value.atZone(ZoneOffset.UTC);
        if (dateTime.getNano() > 0) {
            DATE_TIME_MILLISECONDS_LITERAL_FORMAT.formatTo(dateTime, sb);
        } else if (dateTime.getSecond() > 0 || dateTime.getMinute() > 0 || dateTime.getHour() > 0) {
            DATE_TIME_LITERAL_FORMAT.formatTo(dateTime, sb);
        } else {
            DATE_LITERAL_FORMAT.formatTo(dateTime, sb);
        }
    }

    public ResolvedLiteral ofNumericString(String numericString) {
        try {
            return ofBigDecimal(new BigDecimal(numericString));
        } catch (NumberFormatException e) {
            throw new SyntaxErrorException(e);
        }
    }

    public ResolvedLiteral ofBigDecimal(BigDecimal bigDecimal) {
        if (numericLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for numeric literals defined");
        }
        return numericLiteralResolver.resolveLiteral(domainModel, bigDecimal);
    }

    public void appendNumeric(StringBuilder sb, Number value) {
        sb.append(value);
    }

    public ResolvedLiteral ofBoolean(boolean value) {
        if (booleanLiteralResolver == null) {
            throw new DomainModelException("No literal resolver for boolean literals defined");
        }
        return booleanLiteralResolver.resolveLiteral(domainModel, value);
    }

    public void appendBoolean(StringBuilder sb, boolean value) {
        sb.append(value);
    }
}
