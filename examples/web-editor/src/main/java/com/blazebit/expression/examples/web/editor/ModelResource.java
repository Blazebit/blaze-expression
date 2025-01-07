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

package com.blazebit.expression.examples.web.editor;

import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.ExpressionService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Locale;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
@Path("model")
public class ModelResource {

    @Context
    HttpHeaders headers;

    @Inject
    ExpressionService expressionService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModel() {
        Locale locale = headers.getLanguage();
        String json = expressionService.serialize(String.class, "json", Collections.singletonMap(DocumentationMetadataDefinition.LOCALE_PROPERTY, locale));
        return Response.ok(json).build();
    }

}
