/*
 * Copyright 2019 - 2021 Blazebit.
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
import com.blazebit.domain.boot.model.DomainFunctionBuilder;
import com.blazebit.domain.boot.model.DomainFunctionDefinition;
import com.blazebit.domain.boot.model.EnumDomainTypeBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.runtime.model.DomainFunction;
import com.blazebit.domain.runtime.model.DomainOperator;
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
import com.blazebit.expression.spi.DomainFunctionArguments;
import com.blazebit.expression.spi.FunctionInvoker;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
            DomainFunctionBuilder functionBuilder = domainBuilder.createFunction(destructorName)
                .withArgument("value", destructorArgumentMetadata)
                .withResultType(PersistenceContributor.STRING_TYPE_NAME)
                .withMetadata(new FunctionRendererMetadataDefinition(handler))
                .withMetadata(new FunctionInvokerMetadataDefinition(handler));
            if (destructorDocumentationKey != null) {
                functionBuilder.withMetadata(DocumentationMetadataDefinition.localized(destructorDocumentationKey, resourceBundleClassLoader));
            }
            functionBuilder.build();
        } else {
            handler = (GlobalStringlyTypeDestructorFunctionHandler) ((FunctionInvokerMetadataDefinition) function.getMetadataDefinitions().get(FunctionInvoker.class)).build(null);
        }
        handler.destructors.put(typeName, (StringlyTypeHandler<Object>) stringlyTypeHandler);
    }

    /**
     * Registers the given stringly type to the given domain builder without constructor or destructor.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerSimpleStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            name,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            false,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            name,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            name,
            resourceBundleClassLoader,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            typeMetadataDefinitions
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        String upperName = name.toUpperCase();
        registerStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
            stringlyTypeHandler.getClass().getClassLoader(),
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
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        String upperName = name.toUpperCase();
        registerStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
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
     * @param name The type name of the stringly type to register
     * @param constructorName The name for the constructor to register
     * @param destructorName The name for the destructor to register
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param typeMetadataDefinitions Metadata definitions to attach to the basic type
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
            name,
            constructorName,
            destructorName,
            stringlyTypeHandler.getClass().getClassLoader(),
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
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        registerStringlyType(
            domainBuilder,
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
    public static <T> void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String constructorArgumentDocumentationKey,
                                                String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, StringlyTypeHandler<T> stringlyTypeHandler, MetadataDefinition<?>... typeMetadataDefinitions) {
        if (registerGlobalDestructor) {
            registerGlobalStringlyTypeDestructor(domainBuilder, name, stringlyTypeHandler);
        }
        MetadataDefinition[] metadataDefinitions = new MetadataDefinition[typeMetadataDefinitions.length + 2];
        System.arraycopy(typeMetadataDefinitions, 0, metadataDefinitions, 0, typeMetadataDefinitions.length);
        metadataDefinitions[typeMetadataDefinitions.length] = new PersistenceContributor.ComparisonOperatorInterpreterMetadataDefinition(new StringlyOperatorHandler(stringlyTypeHandler));
        metadataDefinitions[typeMetadataDefinitions.length + 1] = new StringlyTypeHandlerMetadataDefinition(stringlyTypeHandler);
        domainBuilder.createBasicType(name, metadataDefinitions);
        domainBuilder.withOperationTypeResolver(name, DomainOperator.PLUS, domainBuilder.getOperationTypeResolver(PersistenceContributor.STRING_TYPE_NAME, DomainOperator.PLUS));
        domainBuilder.withOperator(name, DomainOperator.PLUS);
        domainBuilder.withPredicateTypeResolver(name, DomainPredicate.EQUALITY, StaticDomainPredicateTypeResolvers.returning(PersistenceContributor.BOOLEAN_TYPE_NAME, name, PersistenceContributor.STRING_TYPE_NAME));
        domainBuilder.withPredicate(name, DomainPredicate.distinguishable());
        if (constructorName != null) {
            StringlyTypeConstructorFunctionHandler constructorHandler = new StringlyTypeConstructorFunctionHandler(stringlyTypeHandler);

            MetadataDefinition[] constructorArgumentMetadata;
            if (constructorArgumentDocumentationKey == null) {
                constructorArgumentMetadata = new MetadataDefinition[0];
            } else {
                constructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(constructorArgumentDocumentationKey, resourceBundleClassLoader) };
            }

            DomainFunctionBuilder functionBuilder = domainBuilder.createFunction(constructorName)
                .withArgument("value", PersistenceContributor.STRING_TYPE_NAME, constructorArgumentMetadata)
                .withResultType(name)
                .withMetadata(new FunctionRendererMetadataDefinition(constructorHandler))
                .withMetadata(new FunctionInvokerMetadataDefinition(constructorHandler));
            if (constructorDocumentationKey != null) {
                functionBuilder.withMetadata(DocumentationMetadataDefinition.localized(constructorDocumentationKey, resourceBundleClassLoader));
            }
            functionBuilder.build();
        }

        if (destructorName != null) {
            MetadataDefinition[] destructorArgumentMetadata;
            if (destructorArgumentDocumentationKey == null) {
                destructorArgumentMetadata = new MetadataDefinition[0];
            } else {
                destructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(destructorArgumentDocumentationKey, resourceBundleClassLoader) };
            }
            StringlyTypeDestructorFunctionHandler destructorHandler = new StringlyTypeDestructorFunctionHandler(stringlyTypeHandler);
            DomainFunctionBuilder functionBuilder = domainBuilder.createFunction(destructorName)
                .withArgument("value", name, destructorArgumentMetadata)
                .withResultType(PersistenceContributor.STRING_TYPE_NAME)
                .withMetadata(new FunctionRendererMetadataDefinition(destructorHandler))
                .withMetadata(new FunctionInvokerMetadataDefinition(destructorHandler));
            if (destructorDocumentationKey != null) {
                functionBuilder.withMetadata(DocumentationMetadataDefinition.localized(destructorDocumentationKey, resourceBundleClassLoader));
            }
            functionBuilder.build();
        }
    }

    /**
     * Registers the given stringly enum type to the given domain builder along with the global destructor function support.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param enumDomainTypeBuilder The builder for the enum domain type for which to register the stringly type handler
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyEnumType(DomainBuilder domainBuilder, EnumDomainTypeBuilder enumDomainTypeBuilder) {
        registerStringlyEnumType(
            domainBuilder,
            enumDomainTypeBuilder,
            null,
            TypeUtils.class.getClassLoader(),
            null,
            null,
            true,
            string -> string
        );
    }

    /**
     * Registers the given stringly enum type to the given domain builder along with the global destructor function support.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param enumDomainTypeBuilder The builder for the enum domain type for which to register the stringly type handler
     * @param stringlyTypeHandler The stringly type handler to register
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyEnumType(DomainBuilder domainBuilder, EnumDomainTypeBuilder enumDomainTypeBuilder, StringlyTypeHandler<T> stringlyTypeHandler) {
        registerStringlyEnumType(
            domainBuilder,
            enumDomainTypeBuilder,
            null,
            stringlyTypeHandler.getClass().getClassLoader(),
            null,
            null,
            true,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly enum type to the given domain builder along with a destructor function and global destructor function support.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param enumDomainTypeBuilder The builder for the enum domain type for which to register the stringly type handler
     * @param destructorName The name for the destructor to register
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyEnumType(DomainBuilder domainBuilder, EnumDomainTypeBuilder enumDomainTypeBuilder, String destructorName,
                                                    String destructorDocumentationKey, StringlyTypeHandler<T> stringlyTypeHandler) {
        registerStringlyEnumType(
            domainBuilder,
            enumDomainTypeBuilder,
            destructorName,
            stringlyTypeHandler.getClass().getClassLoader(),
            destructorDocumentationKey,
            destructorDocumentationKey == null ? null : (destructorDocumentationKey + "_VALUE"),
            true,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly enum type to the given domain builder along with a destructor function and global destructor function support.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param enumDomainTypeBuilder The builder for the enum domain type for which to register the stringly type handler
     * @param destructorName The name for the destructor to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyEnumType(DomainBuilder domainBuilder, EnumDomainTypeBuilder enumDomainTypeBuilder, String destructorName, ClassLoader resourceBundleClassLoader,
                                                    String destructorDocumentationKey, StringlyTypeHandler<T> stringlyTypeHandler) {
        registerStringlyEnumType(
            domainBuilder,
            enumDomainTypeBuilder,
            destructorName,
            resourceBundleClassLoader,
            destructorDocumentationKey,
            destructorDocumentationKey == null ? null : (destructorDocumentationKey + "_VALUE"),
            true,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly enum type to the given domain builder along with a destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param enumDomainTypeBuilder The builder for the enum domain type for which to register the stringly type handler
     * @param destructorName The name for the destructor to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param destructorArgumentDocumentationKey The documentation key for the destructor argument
     * @param registerGlobalDestructor Whether to register the type for so that the global destructor function can destructure it
     * @param stringlyTypeHandler The stringly type handler to register
     * @param <T> The java type of the stringly type
     */
    public static <T> void registerStringlyEnumType(DomainBuilder domainBuilder, EnumDomainTypeBuilder enumDomainTypeBuilder, String destructorName, ClassLoader resourceBundleClassLoader,
                                                    String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, StringlyTypeHandler<T> stringlyTypeHandler) {
        String name = enumDomainTypeBuilder.getName();
        if (registerGlobalDestructor) {
            registerGlobalStringlyTypeDestructor(domainBuilder, name, stringlyTypeHandler);
        }
        enumDomainTypeBuilder.withMetadata(new PersistenceContributor.ComparisonOperatorInterpreterMetadataDefinition(new StringlyOperatorHandler(stringlyTypeHandler)));
        enumDomainTypeBuilder.withMetadata(new StringlyTypeHandlerMetadataDefinition(stringlyTypeHandler));
        domainBuilder.withOperationTypeResolver(name, DomainOperator.PLUS, domainBuilder.getOperationTypeResolver(PersistenceContributor.STRING_TYPE_NAME, DomainOperator.PLUS));
        domainBuilder.withOperator(name, DomainOperator.PLUS);
        domainBuilder.withPredicateTypeResolver(name, DomainPredicate.EQUALITY, StaticDomainPredicateTypeResolvers.returning(PersistenceContributor.BOOLEAN_TYPE_NAME, name, PersistenceContributor.STRING_TYPE_NAME));
        domainBuilder.withPredicate(name, DomainPredicate.distinguishable());

        if (destructorName != null) {
            MetadataDefinition[] destructorArgumentMetadata;
            if (destructorArgumentDocumentationKey == null) {
                destructorArgumentMetadata = new MetadataDefinition[0];
            } else {
                destructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(destructorArgumentDocumentationKey, resourceBundleClassLoader) };
            }
            StringlyTypeDestructorFunctionHandler destructorHandler = new StringlyTypeDestructorFunctionHandler(stringlyTypeHandler);
            DomainFunctionBuilder functionBuilder = domainBuilder.createFunction(destructorName)
                .withArgument("value", name, destructorArgumentMetadata)
                .withResultType(PersistenceContributor.STRING_TYPE_NAME)
                .withMetadata(new FunctionRendererMetadataDefinition(destructorHandler))
                .withMetadata(new FunctionInvokerMetadataDefinition(destructorHandler));
            if (destructorDocumentationKey != null) {
                functionBuilder.withMetadata(DocumentationMetadataDefinition.localized(destructorDocumentationKey, resourceBundleClassLoader));
            }
            functionBuilder.build();
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
        public StringlyTypeHandler<?> build(MetadataDefinitionHolder definitionHolder) {
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
        public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
            DomainType type = arguments.getType(0);
            if (type == null) {
                return null;
            }
            StringlyTypeHandler<Object> toString = destructors.get(type.getName());
            if (toString == null) {
                throw new DomainModelException("Unsupported type for destructure function " + name + " function: " + type.getName());
            }
            return toString.destruct(arguments.getValue(0));
        }

        @Override
        public void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
            String argumentTypeName = argumentRenderers.getType(0).getName();
            StringlyTypeHandler<Object> handler = destructors.get(argumentTypeName);
            if (handler == null) {
                throw new DomainModelException("Unsupported type for destructure function " + this.name + " function: " + argumentTypeName);
            }
            handler.appendDestructTo(sb, subBuilder -> argumentRenderers.renderArgument(subBuilder, 0));
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
        public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
            Object value = arguments.getValue(0);
            if (value == null) {
                return null;
            }
            return stringlyTypeHandler.construct((String) value);
        }

        @Override
        public void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
            stringlyTypeHandler.appendConstructTo(sb, subBuilder -> argumentRenderers.renderArgument(subBuilder, 0));
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
        public Object invoke(ExpressionInterpreter.Context context, DomainFunction function, DomainFunctionArguments arguments) {
            Object value = arguments.getValue(0);
            if (value == null) {
                return null;
            }
            return stringlyTypeHandler.destruct(value);
        }

        @Override
        public void render(DomainFunction function, DomainType returnType, DomainFunctionArgumentRenderers argumentRenderers, StringBuilder sb, PersistenceExpressionSerializer serializer) {
            stringlyTypeHandler.appendDestructTo(sb, subBuilder -> argumentRenderers.renderArgument(subBuilder, 0));
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
        public Boolean interpret(ExpressionInterpreter.Context context, DomainType leftType, DomainType rightType, Object leftValue, Object rightValue, ComparisonOperator operator) {
            Object referenceValue;
            if (leftType == rightType) {
                referenceValue = leftValue;
            } else {
                referenceValue = handler.destruct(leftValue);
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
