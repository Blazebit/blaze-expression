package com.blazebit.expression.declarative.persistence;

import com.blazebit.domain.declarative.MetadataType;
import com.blazebit.expression.Literal;
import com.blazebit.expression.persistence.PersistenceDomainFunctionArgumentRenderers;
import com.blazebit.expression.persistence.PersistenceExpressionSerializer;
import com.blazebit.expression.persistence.PersistenceFunctionRenderer;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

@MetadataType(PersistenceFunctionRenderer.class)
public class ZonedDateTimePersistenceFunctionRenderer
        implements PersistenceFunctionRenderer {

    @Override
    public void render(com.blazebit.domain.runtime.model.DomainFunction function,
            com.blazebit.domain.runtime.model.DomainType returnType,
            PersistenceDomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb,
            PersistenceExpressionSerializer serializer) {
        String stringValue = (String) ((Literal) argumentRenderers.getExpression(0)).getValue();
        sb.append("{ts '");
        sb.append(Timestamp.valueOf(ZonedDateTime.parse(stringValue).toLocalDateTime()));
        sb.append("'}");
    }
}