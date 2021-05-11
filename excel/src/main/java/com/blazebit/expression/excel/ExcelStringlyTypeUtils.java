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

package com.blazebit.expression.excel;

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
import com.blazebit.expression.excel.function.ExcelFunctionRendererMetadataDefinition;
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
public class ExcelStringlyTypeUtils {

    private ExcelStringlyTypeUtils() {
    }

    /**
     * Registers the given stringly type handler for the global destructor under the given destructor name for the given type name.
     *
     * @param domainBuilder The domain builder into which to register the global destructor
     * @param typeName The type name for which to register the global destructor
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, DEFAULT_GLOBAL_DESTRUCTOR_NAME, DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY, DEFAULT_DESTRUCTOR_ARGUMENT_DOCUMENTATION_KEY, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler);
    }

    /**
     * Registers the given stringly type handler for the global destructor under the given destructor name for the given type name.
     *
     * @param domainBuilder The domain builder into which to register the global destructor
     * @param typeName The type name for which to register the global destructor
     * @param stringlyTypeHandler The stringly type handler to register
     * @param excelStringlyTypeHandler The stringly type handler for excel to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, DEFAULT_GLOBAL_DESTRUCTOR_NAME, DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY, DEFAULT_DESTRUCTOR_ARGUMENT_DOCUMENTATION_KEY, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler, excelStringlyTypeHandler);
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
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The stringly type handler for excel to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        registerGlobalStringlyTypeDestructor(domainBuilder, typeName, destructorName, destructorDocumentationKey, destructorArgumentDocumentationKey, stringlyTypeHandler.getClass().getClassLoader(), stringlyTypeHandler, excelStringlyTypeHandler);
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
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, ClassLoader resourceBundleClassLoader, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The stringly type handler for excel to register
     */
    public static void registerGlobalStringlyTypeDestructor(DomainBuilder domainBuilder, String typeName, String destructorName, String destructorDocumentationKey, String destructorArgumentDocumentationKey, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        DomainFunctionDefinition function = domainBuilder.getFunction(destructorName);
        if (function == null) {
            StringlyTypeUtils.registerGlobalStringlyTypeDestructor(domainBuilder, typeName, destructorName, destructorDocumentationKey, destructorArgumentDocumentationKey, resourceBundleClassLoader, stringlyTypeHandler);
            function = domainBuilder.getFunction(destructorName);
        }
        Map<Class<?>, MetadataDefinition<?>> metadataDefinitions = function.getMetadataDefinitions();
        ExcelFunctionRendererMetadataDefinition metadataDefinition = (ExcelFunctionRendererMetadataDefinition) metadataDefinitions.get(ExcelFunctionRenderer.class);
        ExcelGlobalStringlyTypeDestructorFunctionRenderer renderer = metadataDefinition == null ? null : (ExcelGlobalStringlyTypeDestructorFunctionRenderer) metadataDefinition.build(null);
        if (renderer == null) {
            GlobalStringlyTypeDestructorFunctionInvoker handler = (GlobalStringlyTypeDestructorFunctionInvoker) ((FunctionInvokerMetadataDefinition) metadataDefinitions.get(FunctionInvoker.class)).build(null);
            renderer = new ExcelGlobalStringlyTypeDestructorFunctionRenderer(handler, destructorName);
            domainBuilder.extendFunction(destructorName, new ExcelFunctionRendererMetadataDefinition(renderer));
        }
        renderer.destructors.put(typeName, excelStringlyTypeHandler);
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
            ExcelStringlyTypeHandler.INSTANCE
        );
    }

    /**
     * Registers the given stringly type to the given domain builder without constructor or destructor.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerSimpleStringlyType(DomainBuilder domainBuilder, String name, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerSimpleStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
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
            excelStringlyTypeHandler
        );
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            excelStringlyTypeHandler
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
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        registerStringlyType(
            domainBuilder,
            name,
            resourceBundleClassLoader,
            DEFAULT_CONSTRUCTOR_DOCUMENTATION_KEY,
            DEFAULT_DESTRUCTOR_DOCUMENTATION_KEY,
            stringlyTypeHandler,
            excelStringlyTypeHandler
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
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorDocumentationKey, String destructorDocumentationKey, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
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
            excelStringlyTypeHandler
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
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
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
            excelStringlyTypeHandler
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
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, String constructorDocumentationKey, String destructorDocumentationKey, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
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
            excelStringlyTypeHandler
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
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String destructorDocumentationKey,
                                            StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
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
            excelStringlyTypeHandler
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
                                            String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, ClassLoader resourceBundleClassLoader, String constructorDocumentationKey, String constructorArgumentDocumentationKey,
                                            String destructorDocumentationKey, String destructorArgumentDocumentationKey, boolean registerGlobalDestructor, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        if (stringlyTypeHandler != null) {
            StringlyTypeUtils.registerStringlyType(domainBuilder, name, constructorDocumentationKey, destructorDocumentationKey, resourceBundleClassLoader, constructorDocumentationKey, constructorArgumentDocumentationKey, destructorDocumentationKey, destructorArgumentDocumentationKey, registerGlobalDestructor, stringlyTypeHandler);
        }
        registerExcelStringlyType(domainBuilder, name, constructorName, destructorName, registerGlobalDestructor, stringlyTypeHandler, excelStringlyTypeHandler);
    }

    /**
     * Registers the given stringly type to the given domain builder along with constructor function and destructor function.
     *
     * @param domainBuilder The domain builder into which to register the type and construct and destructor
     * @param name The type name of the stringly type to register
     * @param stringlyTypeHandler The stringly type handler to register
     */
    public static void registerExcelStringlyType(DomainBuilder domainBuilder, String name, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerExcelStringlyType(
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerExcelStringlyType(DomainBuilder domainBuilder, String name, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerExcelStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
            true,
            stringlyTypeHandler,
            excelStringlyTypeHandler
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
    public static void registerExcelStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, ExcelStringlyTypeHandlerCombined<?> stringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, resourceBundleClassLoader, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerExcelStringlyType(
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerExcelStringlyType(DomainBuilder domainBuilder, String name, ClassLoader resourceBundleClassLoader, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        StringlyTypeUtils.registerStringlyType(domainBuilder, name, resourceBundleClassLoader, stringlyTypeHandler);
        String upperName = name.toUpperCase();
        registerExcelStringlyType(
            domainBuilder,
            name,
            upperName,
            upperName + "_TO_STRING",
            true,
            stringlyTypeHandler,
            excelStringlyTypeHandler
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
     * @param excelStringlyTypeHandler The excel stringly type handler to register
     */
    public static void registerExcelStringlyType(DomainBuilder domainBuilder, String name, String constructorName, String destructorName, boolean registerGlobalDestructor, StringlyTypeHandler<?> stringlyTypeHandler, ExcelStringlyTypeHandler excelStringlyTypeHandler) {
        DomainTypeDefinition type = domainBuilder.getType(name);
        if (type == null) {
            throw new IllegalArgumentException("Type does not exist yet: " + name);
        }
        if (registerGlobalDestructor) {
            registerGlobalStringlyTypeDestructor(domainBuilder, name, stringlyTypeHandler, excelStringlyTypeHandler);
        }
        if (type instanceof BasicDomainTypeDefinition) {
            domainBuilder.extendBasicType(name, new ExcelStringlyTypeHandlerMetadataDefinition(excelStringlyTypeHandler));
        } else if (type instanceof EnumDomainTypeDefinition) {
            EnumDomainTypeBuilder enumDomainTypeBuilder = domainBuilder.extendEnumType(name, (EnumDomainTypeDefinition) type);
            enumDomainTypeBuilder.withMetadata(new ExcelStringlyTypeHandlerMetadataDefinition(excelStringlyTypeHandler));
            enumDomainTypeBuilder.build();
        } else {
            throw new IllegalArgumentException("Can't register stringly type handlers for non-basic and non-enum type: " + type);
        }
        if (type instanceof BasicDomainTypeDefinition && constructorName != null) {
            ExcelStringlyTypeConstructorFunctionRenderer constructorHandler = new ExcelStringlyTypeConstructorFunctionRenderer(excelStringlyTypeHandler);
            domainBuilder.extendFunction(destructorName, new ExcelFunctionRendererMetadataDefinition(constructorHandler));
        }

        if (destructorName != null && !registerGlobalDestructor) {
            DomainFunctionDefinition existingFunction = domainBuilder.getFunction(destructorName);
            ExcelFunctionRendererMetadataDefinition metadataDefinition = existingFunction == null ? null : (ExcelFunctionRendererMetadataDefinition) existingFunction.getMetadataDefinitions().get(ExcelFunctionRenderer.class);
            if (metadataDefinition != null && metadataDefinition.build(null) instanceof ExcelGlobalStringlyTypeDestructorFunctionRenderer) {
                throw new IllegalStateException("Can't register a destructor for stringly type '" + name + "' under the function name '" + destructorName + "' as a global destructor is already registered under this name!");
            }
            ExcelStringlyTypeDestructorFunctionRenderer destructorHandler = new ExcelStringlyTypeDestructorFunctionRenderer(excelStringlyTypeHandler);
            domainBuilder.extendFunction(destructorName, new ExcelFunctionRendererMetadataDefinition(destructorHandler));
        }
    }

}
