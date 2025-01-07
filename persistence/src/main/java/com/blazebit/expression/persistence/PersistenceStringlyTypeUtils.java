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

package com.blazebit.expression.persistence;

import com.blazebit.domain.boot.model.BasicDomainTypeDefinition;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.DomainFunctionDefinition;
import com.blazebit.domain.boot.model.DomainTypeDefinition;
import com.blazebit.domain.boot.model.EnumDomainTypeBuilder;
import com.blazebit.domain.boot.model.EnumDomainTypeDefinition;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.expression.base.GlobalStringlyTypeDestructorFunctionInvoker;
import com.blazebit.expression.base.StringlyTypeHandler;
import com.blazebit.expression.base.StringlyTypeUtils;
import com.blazebit.expression.base.function.FunctionInvokerMetadataDefinition;
import com.blazebit.expression.persistence.function.PersistenceFunctionRendererMetadataDefinition;
import com.blazebit.expression.spi.FunctionInvoker;

import java.util.Map;

import static com.blazebit.expression.base.StringlyTypeUtils.DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY;
import static com.blazebit.expression.base.StringlyTypeUtils.DEFAULT_DESTRUCTOR_ARGUMENT_DOCUMENTATION_KEY;
import static com.blazebit.expression.base.StringlyTypeUtils.DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY;
import static com.blazebit.expression.base.StringlyTypeUtils.DEFAULT_GLOBAL_DESTRUCTOR_NAME;

/**
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceStringlyTypeUtils {

    private PersistenceStringlyTypeUtils() {
    }

    /**
     * Registers the given stringly type handler for the global destructor under the given destructor name for the given type name.
     *
     * @param domainBuilder The domain builder into which to register the global destructor
     * @param typeName The type name for which to register the global destructor
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, DEFAULT_GLOBAL_DESTRUCTOR_NAME, DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY, DEFAULT_DESTRUCTOR_ARGUMENT_DOCUMENTATION_KEY, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler);
    }

    /**
     * Registers the given stringly type handler for the global destructor under the given destructor name for the given type name.
     *
     * @param domainBuilder The domain builder into which to register the global destructor
     * @param typeName The type name for which to register the global destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The stringly type handler for persistence to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, DEFAULT_GLOBAL_DESTRUCTOR_NAME, DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY, DEFAULT_DESTRUCTOR_ARGUMENT_DOCUMENTATION_KEY, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler, persistenceStringlyTypeHandler);
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
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The stringly type handler for persistence to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, destructorName, destructorDocumentationKey, destructorArgumentDocumentationKey, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler, persistenceStringlyTypeHandler);
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
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, ClassLoader resourceBundleClassLoader, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, destructorName, destructorDocumentationKey, destructorArgumentDocumentationKey, resourceBundleClassLoader, stringlyTypeHandler, stringlyTypeHandler);
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
     * @param persistenceStringlyTypeHandler The stringly type handler for persistence to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        DomainFunctionDefinition function = domainBuilder.getFunction(destructorName);
        if (function == null) {
            StringlyTypeUtils.registerGlobalStringlyTypeDestructor(domainBuilder, typeName, destructorName, destructorDocumentationKey, destructorArgumentDocumentationKey, resourceBundleClassLoader, stringlyTypeHandler);
            function = domainBuilder.getFunction(destructorName);
        }
        Map<Class<?>, MetadataDefinition<?>> metadataDefinitions = function.getMetadataDefinitions();
        PersistenceFunctionRendererMetadataDefinition metadataDefinition = (PersistenceFunctionRendererMetadataDefinition) metadataDefinitions.get(PersistenceFunctionRenderer.class);
        PersistenceGlobalStringlyTypeDestructorFunctionRenderer renderer = metadataDefinition == null ? null : (PersistenceGlobalStringlyTypeDestructorFunctionRenderer) metadataDefinition.build(null);
        if (renderer == null) {
            GlobalStringlyTypeDestructorFunctionInvoker handler = (GlobalStringlyTypeDestructorFunctionInvoker) ((FunctionInvokerMetadataDefinition) metadataDefinitions.get(FunctionInvoker.class)).build(null);
            renderer = new PersistenceGlobalStringlyTypeDestructorFunctionRenderer(handler, destructorName);
            domainBuilder.extendFunction(destructorName, new PersistenceFunctionRendererMetadataDefinition(renderer));
        }
        renderer.destructors.put(typeName, persistenceStringlyTypeHandler);
    }

    /**
     * Registers the given stringly type to the given domain builder without constructor or destructor.
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
            string -> string,
            PersistenceStringlyTypeHandler.INSTANCE
        );
    }

    /**
     * Registers the given stringly type to the given domain builder without constructor or destructor.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerSimpleStringlyType(DomainBuilder domainBuilder, String name, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder without constructor or destructor.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerSimpleStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
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
            persistenceStringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            persistenceStringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            resourceBundleClassLoader,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            resourceBundleClassLoader,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            persistenceStringlyTypeHandler
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
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorDocumentationKey, String destructorDocumentationKey, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
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
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
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
            persistenceStringlyTypeHandler
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
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
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
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
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
            persistenceStringlyTypeHandler
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
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, String constructorDocumentationKey, String destructorDocumentationKey, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
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
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
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
            persistenceStringlyTypeHandler
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
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
            stringlyTypeHandler
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
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
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
            persistenceStringlyTypeHandler
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
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String constructorArgumentDocumentationKey,
                                            String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            constructorName,
            destructorName,
            resourceBundleClassLoader,
            constructorDocumentationKey,
            constructorArgumentDocumentationKey,
            destructorDocumentationKey,
            destructorArgumentDocumentationKey,
            registerGlobalDestructor,
            stringlyTypeHandler,
            stringlyTypeHandler
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
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String constructorArgumentDocumentationKey,
                                            String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        if (stringlyTypeHandler != null) {
            StringlyTypeUtils.registerStringlyType(domainBuilder, name, constructorDocumentationKey, destructorDocumentationKey, resourceBundleClassLoader, constructorDocumentationKey, constructorArgumentDocumentationKey, destructorDocumentationKey, destructorArgumentDocumentationKey, registerGlobalDestructor, stringlyTypeHandler);
        }
        registerPersistenceStringlyType(domainBuilder, name, constructorName, destructorName, registerGlobalDestructor, stringlyTypeHandler, persistenceStringlyTypeHandler);
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerPersistenceStringlyType(DomainBuilder domainBuilder, String name, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerPersistenceStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
            true,
            stringlyTypeHandler,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerPersistenceStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerPersistenceStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
            true,
            stringlyTypeHandler,
            persistenceStringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerPersistenceStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, PersistenceStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, resourceBundleClassLoader, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerPersistenceStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
            true,
            stringlyTypeHandler,
            stringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param resourceBundleClassLoader The class loader to use for loading the documentation resource bundle
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerPersistenceStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, resourceBundleClassLoader, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerPersistenceStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
            true,
            stringlyTypeHandler,
            persistenceStringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param constructorName The name for the constructor to register
     * @param destructorName The name for the destructor to register
     * @param registerGlobalDestructor Whether to register the type for so that the global destructor function can destructure it
     * @param stringlyTypeHandler The stringly type handler to register
     * @param persistenceStringlyTypeHandler The persistence stringly type handler to register
     */
    public static void registerPersistenceStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, boolean registerGlobalDestructor, StringlyTypeHandler<?> stringlyTypeHandler, PersistenceStringlyTypeHandler persistenceStringlyTypeHandler) {
        DomainTypeDefinition type = domainBuilder.getType(name);
        if (type == null) {
            throw new IllegalArgumentException("Type does not exist yet: " + name);
        }
        if (registerGlobalDestructor) {
            registerGlobalStringlyTypeDestructor(domainBuilder, name, stringlyTypeHandler, persistenceStringlyTypeHandler);
        }
        if (type instanceof BasicDomainTypeDefinition) {
            domainBuilder.extendBasicType(name, new PersistenceStringlyTypeHandlerMetadataDefinition(persistenceStringlyTypeHandler));
        } else if (type instanceof EnumDomainTypeDefinition) {
            EnumDomainTypeBuilder enumDomainTypeBuilder = domainBuilder.extendEnumType(name, (EnumDomainTypeDefinition) type);
            enumDomainTypeBuilder.withMetadata(new PersistenceStringlyTypeHandlerMetadataDefinition(persistenceStringlyTypeHandler));
            enumDomainTypeBuilder.build();
        } else {
            throw new IllegalArgumentException("Can't register stringly type handlers for non-basic and non-enum type: " + type);
        }
        if (type instanceof BasicDomainTypeDefinition && constructorName != null) {
            PersistenceStringlyTypeConstructorFunctionRenderer constructorHandler = new PersistenceStringlyTypeConstructorFunctionRenderer(persistenceStringlyTypeHandler);
            domainBuilder.extendFunction(destructorName, new PersistenceFunctionRendererMetadataDefinition(constructorHandler));
        }

        if (destructorName != null && !registerGlobalDestructor) {
            DomainFunctionDefinition existingFunction = domainBuilder.getFunction(destructorName);
            PersistenceFunctionRendererMetadataDefinition metadataDefinition = existingFunction == null ? null : (PersistenceFunctionRendererMetadataDefinition) existingFunction.getMetadataDefinitions().get(PersistenceFunctionRenderer.class);
            if (metadataDefinition != null && metadataDefinition.build(null) instanceof PersistenceGlobalStringlyTypeDestructorFunctionRenderer) {
                throw new IllegalStateException("Can't register a destructor for stringly type '" + name + "' under the function name '" + destructorName + "' as a global destructor is already registered under this name!");
            }
            PersistenceStringlyTypeDestructorFunctionRenderer destructorHandler = new PersistenceStringlyTypeDestructorFunctionRenderer(persistenceStringlyTypeHandler);
            domainBuilder.extendFunction(destructorName, new PersistenceFunctionRendererMetadataDefinition(destructorHandler));
        }
    }

}
