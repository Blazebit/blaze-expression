/*
 * Copyright 2019 - 2024 Blazebit.
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A simple implementation of a context for the {@link ExpressionInterpreter}.
 *
 * @param <T> The object provider context type
 * @author Christian Beikov
 * @since 1.0.0
 */
public class ExpressionInterpreterContext<T> implements ExpressionInterpreter.Context {

    private final ExpressionService expressionService;
    private final T rootProviderContext;
    private final Map<String, Object> properties;
    private final Map<String, Object> roots;
    private final Map<String, Function<T, Object>> rootProviders;

    private ExpressionInterpreterContext(ExpressionService expressionService) {
        this.expressionService = expressionService;
        this.rootProviderContext = (T) this;
        this.properties = new HashMap<>();
        this.roots = new HashMap<>();
        this.rootProviders = new HashMap<>();
    }

    /**
     * Creates a new expression interpreter context for the given expression service and root object provider context.
     *
     * @param expressionService The expression service
     * @param rootProviderContext The root object provider context
     */
    public ExpressionInterpreterContext(ExpressionService expressionService, T rootProviderContext) {
        this.expressionService = expressionService;
        this.rootProviderContext = rootProviderContext;
        this.properties = new HashMap<>();
        this.roots = new HashMap<>();
        this.rootProviders = new HashMap<>();
    }

    /**
     * Creates a new expression interpreter context for the given expression service, using the <code>this</code> object as context.
     *
     * @param expressionService The expression service
     * @return the interpreter context
     */
    public static ExpressionInterpreterContext<ExpressionInterpreter.Context> create(ExpressionService expressionService) {
        return new ExpressionInterpreterContext<>(expressionService);
    }

    /**
     * Maps the given root alias to the object.
     *
     * @param rootAlias The expression root alias
     * @param object The object
     * @return <code>this</code> for method chaining
     */
    public ExpressionInterpreterContext<T> withRoot(String rootAlias, Object object) {
        roots.put(rootAlias, object);
        return this;
    }

    /**
     * Returns the alias mappings from root alias to root objects.
     *
     * @return the alias mappings
     */
    public Map<String, Object> getRoots() {
        return roots;
    }

    /**
     * Maps the given root alias to the given object provider.
     *
     * @param rootAlias The expression root alias
     * @param rootProvider The object provider
     * @return <code>this</code> for method chaining
     */
    public ExpressionInterpreterContext<T> withRootProvider(String rootAlias, Function<T, Object> rootProvider) {
        if (rootProviderContext == null) {
            throw new IllegalStateException("No root provider context set, so can't handle root providers!");
        }
        rootProviders.put(rootAlias, rootProvider);
        return this;
    }

    /**
     * Returns the root object provider.
     *
     * @return the root object provider
     */
    public Map<String, Function<T, Object>> getRootProviders() {
        return rootProviders;
    }

    @Override
    public ExpressionService getExpressionService() {
        return expressionService;
    }

    @Override
    public <X> X getProperty(String key) {
        return (X) properties.get(key);
    }

    @Override
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    @Override
    public <X> X getRoot(String alias) {
        Object result = roots.get(alias);
        if (result == null) {
            Function<T, Object> provider = rootProviders.get(alias);
            if (provider != null) {
                result = provider.apply(rootProviderContext);
                roots.put(alias, result);
            }
        }
        return (X) result;
    }

}
