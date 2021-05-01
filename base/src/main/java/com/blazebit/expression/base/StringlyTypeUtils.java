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

package com.blazebit.expression.base;

import com.blazebit.domain.boot.model.BasicDomainTypeDefinition;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.DomainFunctionBuilder;
import com.blazebit.domain.boot.model.DomainFunctionDefinition;
import com.blazebit.domain.boot.model.DomainTypeDefinition;
import com.blazebit.domain.boot.model.EnumDomainTypeBuilder;
import com.blazebit.domain.boot.model.EnumDomainTypeDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.domain.runtime.model.StaticDomainPredicateTypeResolvers;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.base.function.FunctionInvokerMetadataDefinition;
import com.blazebit.expression.spi.FunctionInvoker;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class StringlyTypeUtils {

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

    private StringlyTypeUtils() {
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
        GlobalStringlyTypeDestructorFunctionInvoker handler;
        if (function == null) {
            MetadataDefinition[] destructorArgumentMetadata;
            if (destructorArgumentDocumentationKey == null) {
                destructorArgumentMetadata = new MetadataDefinition[0];
            } else {
                destructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(destructorArgumentDocumentationKey, resourceBundleClassLoader) };
            }
            handler = new GlobalStringlyTypeDestructorFunctionInvoker(destructorName);
            DomainFunctionBuilder functionBuilder = domainBuilder.createFunction(destructorName)
                .withArgument("value", destructorArgumentMetadata)
                .withResultType(BaseContributor.STRING_TYPE_NAME)
                .withMetadata(new FunctionInvokerMetadataDefinition(handler));
            if (destructorDocumentationKey != null) {
                functionBuilder.withMetadata(DocumentationMetadataDefinition.localized(destructorDocumentationKey, resourceBundleClassLoader));
            }
            functionBuilder.build();
        } else {
            handler = (GlobalStringlyTypeDestructorFunctionInvoker) ((FunctionInvokerMetadataDefinition) function.getMetadataDefinitions().get(FunctionInvoker.class)).build(null);
        }
        handler.destructors.put(typeName, (StringlyTypeHandler<Object>) stringlyTypeHandler);
    }


    /**
     * Registers the type with the given name as stringly type to the given domain builder without constructor or destructor.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     */
    public static void registerSimpleStringlyType(DomainBuilder domainBuilder, String name) {
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
            string -> string
        );
    }

    /**
     * Registers the type with the given name as stringly type to the given domain builder without constructor or destructor.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerSimpleStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
        );
    }

    /**
     * Registers the type with the given name as stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name) {
        registerStringlyType(
            domainBuilder,
            name,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            string -> string
        );
    }

    /**
     * Registers the given stringly type handler to the given domain builder along with constructor function and destructor function for the type with the given type name.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type handler to the given domain builder along with constructor function and destructor function for the type with the given type name.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            resourceBundleClassLoader,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type handler to the given domain builder along with constructor function and destructor function for the type with the given type name.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type handler to the given domain builder along with constructor function and destructor function for the type with the given type name.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type handler to the given domain builder along with constructor function and destructor function for the type with the given type name.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param constructorName The name for the constructor to register
     * @param destructorName The name for the destructor to register
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type handler to the given domain builder along with constructor function and destructor function for the type with the given type name.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param constructorName The name for the constructor to register
     * @param destructorName The name for the destructor to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param constructorDocumentationKey The documentation key for the constructor
     * @param destructorDocumentationKey The documentation key for the destructor
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                                StringlyTypeHandler<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type handler to the given domain builder along with constructor function and destructor function for the type with the given type name.
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
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String constructorArgumentDocumentationKey,
                                                String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, StringlyTypeHandler<?> stringlyTypeHandler) {
        DomainTypeDefinition type = domainBuilder.getType(name);
        if (type == null) {
            throw new IllegalArgumentException("Type does not exist yet: " + name);
        }
        StringlyTypeHandlerMetadataDefinition definition = (StringlyTypeHandlerMetadataDefinition) type.getMetadataDefinitions().get(StringlyTypeHandler.class);
        if (definition != null && definition.build(null) == stringlyTypeHandler) {
            return;
        }
        if (registerGlobalDestructor) {
            registerGlobalStringlyTypeDestructor(domainBuilder, name, stringlyTypeHandler);
        }
        if (type instanceof BasicDomainTypeDefinition) {
            domainBuilder.extendBasicType(name, new BaseContributor.ComparisonOperatorInterpreterMetadataDefinition(new StringlyOperatorInterpreter(stringlyTypeHandler)), new StringlyTypeHandlerMetadataDefinition(stringlyTypeHandler));
        } else if (type instanceof EnumDomainTypeDefinition) {
            EnumDomainTypeBuilder enumDomainTypeBuilder = domainBuilder.extendEnumType(name, (EnumDomainTypeDefinition) type);
            enumDomainTypeBuilder.withMetadata(new BaseContributor.ComparisonOperatorInterpreterMetadataDefinition(new StringlyOperatorInterpreter(stringlyTypeHandler)));
            enumDomainTypeBuilder.withMetadata(new StringlyTypeHandlerMetadataDefinition(stringlyTypeHandler));
            enumDomainTypeBuilder.withMetadata(new EnumValueStringlyTypeAdapter(stringlyTypeHandler));
            enumDomainTypeBuilder.build();
        } else {
            throw new IllegalArgumentException("Can't register stringly type handlers for non-basic and non-enum type: " + type);
        }
        domainBuilder.withOperationTypeResolver(name, DomainOperator.PLUS, domainBuilder.getOperationTypeResolver(BaseContributor.STRING_TYPE_NAME, DomainOperator.PLUS));
        domainBuilder.withOperator(name, DomainOperator.PLUS);
        domainBuilder.withPredicateTypeResolver(name, DomainPredicate.EQUALITY, StaticDomainPredicateTypeResolvers.returning(BaseContributor.BOOLEAN_TYPE_NAME, name, BaseContributor.STRING_TYPE_NAME));
        domainBuilder.withPredicate(name, DomainPredicate.distinguishable());
        if (type instanceof BasicDomainTypeDefinition && constructorName != null) {
            StringlyTypeConstructorFunctionInvoker constructorHandler = new StringlyTypeConstructorFunctionInvoker(stringlyTypeHandler);

            MetadataDefinition[] constructorArgumentMetadata;
            if (constructorArgumentDocumentationKey == null) {
                constructorArgumentMetadata = new MetadataDefinition[0];
            } else {
                constructorArgumentMetadata = new MetadataDefinition[]{ DocumentationMetadataDefinition.localized(constructorArgumentDocumentationKey, resourceBundleClassLoader) };
            }

            DomainFunctionBuilder functionBuilder = domainBuilder.createFunction(constructorName)
                .withArgument("value", BaseContributor.STRING_TYPE_NAME, constructorArgumentMetadata)
                .withResultType(name)
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
            StringlyTypeDestructorFunctionInvoker destructorHandler = new StringlyTypeDestructorFunctionInvoker(stringlyTypeHandler);
            DomainFunctionBuilder functionBuilder = domainBuilder.createFunction(destructorName)
                .withArgument("value", name, destructorArgumentMetadata)
                .withResultType(BaseContributor.STRING_TYPE_NAME)
                .withMetadata(new FunctionInvokerMetadataDefinition(destructorHandler));
            if (destructorDocumentationKey != null) {
                functionBuilder.withMetadata(DocumentationMetadataDefinition.localized(destructorDocumentationKey, resourceBundleClassLoader));
            }
            functionBuilder.build();
        }
    }

}
