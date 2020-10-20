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

package com.blazebit.expression.persistence;

import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.DomainFunctionDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainFunctionArgument;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.domain.runtime.model.DomainType;
import com.blazebit.domain.runtime.model.StaticDomainPredicateTypeResolvers;
import com.blazebit.expression.ComparisonOperator;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.DomainModelException;
import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.persistence.function.FunctionInvokerMetadataDefinition;
import com.blazebit.expression.persistence.function.FunctionRendererMetadataDefinition;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class TypeUtils {

    /**
     * The default function name of the global destructor.
     */
    public static final String DEFAULT_GLOBAL_DESTRUCTOR_NAME = "TO_STRING";
    /**
     * The default documentation key for the global destructor.
     */
    public static final String DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY = DEFAULT_GLOBAL_DESTRUCTOR_NAME;
    /**
     * The default documentation key for the global destructor argument.
     */
    public static final String DEFAULT_DESTRUCTOR_ARGUMENT_DOCUMENTATION_KEY = DEFAULT_GLOBAL_DESTRUCTOR_NAME + "_VALUE";
    /**
     * The default documentation key for a constructor.
     */
    public static final String DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY = "CONSTRUCTOR";

    private TypeUtils() {
    }

    /**
     * Registers the given stringly type handler for the global destructor under the given destructor name for the given type name.
     *
     * @param domainBuilder The domain builder into which to register the global destructor
     * @param typeName The type name for which to register the global destructor
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, StringlyTypeHandler<?> stringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, DEFAULT_GLOBAL_DESTRUCTOR_NAME, DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY, DEFAULT_DESTRUCTOR_ARGUMENT_DOCUMENTATION_KEY, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler);
    }

    /**
     * Registers the given stringly type handler for the global destructor under the given destructor name for the given type name.
     *
     * @param domainBuilder The domain builder into which to register the global destructor
     * @param typeName The type name for which to register the global destructor
     * @param destructorName The name for the global destructor to register
     * @param destructorDocumentationKey The documentation key for the global destructor
     * @param destructorArgumentDocumentationKey The documentation key for the global destructor argument
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, StringlyTypeHandler<?> stringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, destructorName, destructorDocumentationKey, destructorArgumentDocumentationKey, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler);
    }

    /**
     * Registers the given stringly type handler for the global destructor under the given destructor name for the given type name.
     *
     * @param domainBuilder The domain builder into which to register the global destructor
     * @param typeName The type name for which to register the global destructor
     * @param destructorName The name for the global destructor to register
     * @param destructorDocumentationKey The documentation key for the global destructor
     * @param destructorArgumentDocumentationKey The documentation key for the global destructor argument
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler) {
        DomainFunctionDefinition function = domainBuilder.getFunction(destructorName);
        GlobalStringlyTypeDestructorFunctionHandler handler;
        if (function == null) {
            MetadataDefinition[] destructorArgumentMetadata;
            if (destructorArgumentDocumentationKey == null) {
                destructorArgumentMetadata = new MetadataDefinition[0];
            } else {
                destructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(destructorArgumentDocumentationKey, resourceBundleClassLoader) };
            }
            handler = new GlobalStringlyTypeDestructorFunctionHandler(destructorName);
            domainBuilder.createFunction(destructorName)
                .withArgument("value", destructorArgumentMetadata)
                .withResultType(PersistenceDomainContributor.STRING)
                .withMetadata(new FunctionRendererMetadataDefinition(handler))
                .withMetadata(new FunctionInvokerMetadataDefinition(handler))
                .withMetadata(DocumentationMetadataDefinition.localized(destructorDocumentationKey, resourceBundleClassLoader))
                .build();
        } else {
            handler = (GlobalStringlyTypeDestructorFunctionHandler) ((FunctionInvokerMetadataDefinition) function.getMetadataDefinitions().get(FunctionInvokerMetadataDefinition.class)).build(null);
        }
        handler.destructors.put(typeName, (StringlyTypeHandler<Object>) stringlyTypeHandler);
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param type The java type for which to register a stringly type
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, Class<T> type, String name, StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            type,
            name,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_GLOBAL_DESTRUCTOR_NAME,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param type The java type for which to register a stringly type
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, Class<T> type, String name, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            type,
            name,
            resourceBundleClassLoader,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_GLOBAL_DESTRUCTOR_NAME,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param type The java type for which to register a stringly type
     * @param name The type name of the stringly type to register
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, Class<T> type, String name, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        String upperName = name.toUpperCase();
        registerStringlyType(
            domainBuilder,
            type,
            name,
            upperName,
            upperName + "_TO_STRING",
            constructorDocumentationKey,
            destructorDocumentationKey,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param type The java type for which to register a stringly type
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, Class<T> type, String name, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        String upperName = name.toUpperCase();
        registerStringlyType(
            domainBuilder,
            type,
            name,
            upperName,
            upperName + "_TO_STRING",
            resourceBundleClassLoader,
            constructorDocumentationKey,
            destructorDocumentationKey,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param type The java type for which to register a stringly type
     * @param name The type name of the stringly type to register
     * @param constructorName The name for the constructor to register
     * @param destructorName The name for the destructor to register
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, Class<T> type, String name, String constructorName, String destructorName, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            type,
            name,
            constructorName,
            destructorName,
            stringlyTypeHandler.getClass().getClassLoader(),
            constructorDocumentationKey,
            destructorDocumentationKey,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param type The java type for which to register a stringly type
     * @param name The type name of the stringly type to register
     * @param constructorName The name for the constructor to register
     * @param destructorName The name for the destructor to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, Class<T> type, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            type,
            name,
            constructorName,
            destructorName,
            resourceBundleClassLoader,
            constructorDocumentationKey,
            constructorDocumentationKey == null ? null : (constructorDocumentationKey + "_VALUE"),
            destructorDocumentationKey,
            destructorDocumentationKey == null ? null : (destructorDocumentationKey + "_VALUE"),
            true,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param type The java type for which to register a stringly type
     * @param name The type name of the stringly type to register
     * @param constructorName The name for the constructor to register
     * @param destructorName The name for the destructor to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param constructorArgumentDocumentationKey The documentation key for the constructor argument
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param destructorArgumentDocumentationKey The documentation key for the destructor argument
     * @param registerGlobalDestructor Whether to register the type for so that the global destructor function can destructure it
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, Class<T> type, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String constructorArgumentDocumentationKey,
                                                String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        if (registerGlobalDestructor) {
            registerGlobalStringlyTypeDestructor(domainBuilder, name, stringlyTypeHandler);
        }
        MetadataDefinition[] metadataDefinitions = new MetadataDefinition[typeMetadataDefinitions.length + (constructorDocumentationKey == null ? 2 : 3)];
        System.arraycopy(typeMetadataDefinitions, 0, metadataDefinitions, 0, typeMetadataDefinitions.length);
        metadataDefinitions[typeMetadataDefinitions.length] = new PersistenceDomainContributor.ComparisonOperatorInterpreterMetadataDefinition(new StringlyOperatorHandler(stringlyTypeHandler));
        metadataDefinitions[typeMetadataDefinitions.length + 1] = new StringlyTypeHandlerMetadataDefinition(stringlyTypeHandler);
        if (constructorDocumentationKey != null) {
            metadataDefinitions[typeMetadataDefinitions.length + 2] = DocumentationMetadataDefinition.localized(constructorDocumentationKey, resourceBundleClassLoader);
        }
        domainBuilder.createBasicType(name, type, metadataDefinitions);
        domainBuilder.withPredicateTypeResolver(name, DomainPredicate.EQUALITY, StaticDomainPredicateTypeResolvers.returning(PersistenceDomainContributor.BOOLEAN, type, PersistenceDomainContributor.STRING));
        domainBuilder.withPredicate(name, DomainPredicate.distinguishable());
        StringlyTypeConstructorFunctionHandler constructorHandler = new StringlyTypeConstructorFunctionHandler(stringlyTypeHandler);

        MetadataDefinition[] constructorArgumentMetadata;
        if (constructorArgumentDocumentationKey == null) {
            constructorArgumentMetadata = new MetadataDefinition[0];
        } else {
            constructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(constructorArgumentDocumentationKey, resourceBundleClassLoader) };
        }

        domainBuilder.createFunction(constructorName)
            .withArgument("value", PersistenceDomainContributor.STRING, constructorArgumentMetadata)
            .withResultType(name)
            .withMetadata(new FunctionRendererMetadataDefinition(constructorHandler))
            .withMetadata(new FunctionInvokerMetadataDefinition(constructorHandler))
            .withMetadata(DocumentationMetadataDefinition.localized(constructorDocumentationKey, resourceBundleClassLoader))
            .build();

        if (destructorName != null) {
            MetadataDefinition[] destructorArgumentMetadata;
            if (destructorArgumentDocumentationKey == null) {
                destructorArgumentMetadata = new MetadataDefinition[0];
            } else {
                destructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(destructorArgumentDocumentationKey, resourceBundleClassLoader) };
            }
            StringlyTypeDestructorFunctionHandler destructorHandler = new StringlyTypeDestructorFunctionHandler(stringlyTypeHandler);
            domainBuilder.createFunction(destructorName)
                .withArgument("value", name, destructorArgumentMetadata)
                .withResultType(PersistenceDomainContributor.STRING)
                .withMetadata(new FunctionRendererMetadataDefinition(destructorHandler))
                .withMetadata(new FunctionInvokerMetadataDefinition(destructorHandler))
                .withMetadata(DocumentationMetadataDefinition.localized(destructorDocumentationKey, resourceBundleClassLoader))
                .build();
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class StringlyTypeHandlerMetadataDefinition implements MetadataDefinition<StringlyTypeHandler>, Serializable {

        private final StringlyTypeHandler<?> stringlyTypeHandler;

        public StringlyTypeHandlerMetadataDefinition(StringlyTypeHandler<?> stringlyTypeHandler) {
            this.stringlyTypeHandler = stringlyTypeHandler;
        }

        @Override
        public Class<StringlyTypeHandler> getJavaType() {
            return StringlyTypeHandler.class;
        }

        @Override
        public StringlyTypeHandler<?> build(MetadataDefinitionHolder<?> definitionHolder) {
            return stringlyTypeHandler;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class GlobalStringlyTypeDestructorFunctionHandler implements FunctionRenderer, FunctionInvoker, Serializable {

        private final Map<String, StringlyTypeHandler<Object>> destructors = new ConcurrentHashMap<>();
        private final String name;

        public GlobalStringlyTypeDestructorFunctionHandler(String name) {
            this.name = name;
        }

        @Override
        public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
            DomainFunctionArgument argument = function.getArgument(0);
            StringlyTypeHandler<Object> toString = destructors.get(argument.getType().getName());
            if (toString == null) {
                throw new DomainModelException("Unsupported type for destructure function " + name + " function: " + argument.getType().getName());
            }
            return toString.toString(arguments.get(argument));
        }

        @Override
        public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
            DomainFunctionArgument argument = function.getArgument(0);
            StringlyTypeHandler<Object> handler = destructors.get(argument.getType().getName());
            if (handler == null) {
                throw new DomainModelException("Unsupported type for destructure function " + name + " function: " + argument.getType().getName());
            }
            handler.appendToString(sb, argumentRenderers.get(argument));
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class StringlyTypeConstructorFunctionHandler implements FunctionRenderer, FunctionInvoker, Serializable {

        private final StringlyTypeHandler<Object> stringlyTypeHandler;

        public StringlyTypeConstructorFunctionHandler(StringlyTypeHandler<?> stringlyTypeHandler) {
            this.stringlyTypeHandler = (StringlyTypeHandler<Object>) stringlyTypeHandler;
        }

        @Override
        public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
            DomainFunctionArgument argument = function.getArgument(0);
            return stringlyTypeHandler.fromString((String) arguments.get(argument));
        }

        @Override
        public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
            DomainFunctionArgument argument = function.getArgument(0);
            stringlyTypeHandler.appendFromString(sb, argumentRenderers.get(argument));
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class StringlyTypeDestructorFunctionHandler implements FunctionRenderer, FunctionInvoker, Serializable {

        private final StringlyTypeHandler<Object> stringlyTypeHandler;

        public StringlyTypeDestructorFunctionHandler(StringlyTypeHandler<?> stringlyTypeHandler) {
            this.stringlyTypeHandler = (StringlyTypeHandler<Object>) stringlyTypeHandler;
        }

        @Override
        public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, Map<DomainFunctionArgument, Object> arguments) {
            DomainFunctionArgument argument = function.getArgument(0);
            return stringlyTypeHandler.toString(arguments.get(argument));
        }

        @Override
        public void render(DomainFunction function, DomainType returnType, Map<DomainFunctionArgument, Consumer<StringBuilder>> argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
            DomainFunctionArgument argument = function.getArgument(0);
            stringlyTypeHandler.appendToString(sb, argumentRenderers.get(argument));
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    private static class StringlyOperatorHandler implements ComparisonOperatorInterpreter, Serializable {

        private final StringlyTypeHandler<Object> handler;

        public StringlyOperatorHandler(StringlyTypeHandler<?> handler) {
            this.handler = (StringlyTypeHandler<Object>) handler;
        }

        @Override
        public Boolean interpret(DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
            Object referenceValue;
            if (leftType == rightType) {
                referenceValue = leftValue;
            } else {
                referenceValue = handler.toString(leftValue);
            }
            switch (operator) {
                case EQUAL:
                    return referenceValue.equals(rightValue);
                case NOT_EQUAL:
                    return !referenceValue.equals(rightValue);
                default:
                    break;
            }

            throw new DomainModelException("Can't handle the operator " + operator + " for the arguments [" + leftValue + ", " + rightValue + "]!");
        }
    }
}
