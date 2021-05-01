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

import com.blazebit.expression.ExpressionInterpreter;
import com.blazebit.expression.ExpressionSerializer;
import com.blazebit.expression.ExpressionService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * A simple implementation of a context for the {@link PersistenceExpressionSerializer}.
 *
 * @param <T> The alias provider context type
 * @author Christian Beikov
 * @since 1.0.0
 */
public class PersistenceExpressionSerializerContext<T> implements ExpressionSerializer.Context {

    private final ExpressionService expressionService;
    private final T aliasProviderContext;
    private final Map<String, Object> contextParameters;
    private final Map<String, String> aliases;
    private final Map<String, Function<T, String>> aliasProviders;

    /**
     * Creates a new persistence expression serializer context.
     *
     * @param expressionService The expression service
     * @param aliasProviderContext The context for alias providers
     */
    public PersistenceExpressionSerializerContext(ExpressionService expressionService, T aliasProviderContext) {
        this(expressionService, aliasProviderContext, Collections.emptyMap());
    }

    /**
     * Creates a new persistence expression serializer context.
     *
     * @param expressionService The expression service
     * @param aliasProviderContext The context for alias providers
     * @param aliasProviders The persistence query alias providers
     */
    public PersistenceExpressionSerializerContext(ExpressionService expressionService, T aliasProviderContext, Map<String, Function<T, String>> aliasProviders) {
        this.expressionService = expressionService;
        this.aliasProviderContext = aliasProviderContext;
        this.contextParameters = new HashMap<>();
        this.aliases = new HashMap<>();
        this.aliasProviders = new HashMap<>(aliasProviders);
    }

    /**
     * Adds a context parameter with the given name and value.
     *
     * @param contextParameterName The name of the context parameter to add
     * @param value The value
     * @return <code>this</code> for method chaining
     */
    public PersistenceExpressionSerializerContext<T> withContextParameter(String contextParameterName, Object value) {
        contextParameters.put(contextParameterName, value);
        return this;
    }

    /**
     * Returns the context parameters.
     *
     * @return the context parameters
     */
    public Map<String, Object> getContextParameters() {
        return contextParameters;
    }

    /**
     * Maps the given root alias to the given persistence query alias.
     *
     * @param rootAlias The expression root alias
     * @param queryAlias The persistence query alias
     * @return <code>this</code> for method chaining
     */
    public PersistenceExpressionSerializerContext<T> withAlias(String rootAlias, String queryAlias) {
        aliases.put(rootAlias, queryAlias);
        return this;
    }

    /**
     * Returns the alias mappings from root alias to persistence query alias.
     *
     * @return the alias mappings
     */
    public Map<String, String> getAliases() {
        return aliases;
    }

    /**
     * Maps the given root alias to the given persistence query alias provider.
     *
     * @param rootAlias The expression root alias
     * @param aliasProvider The persistence query alias provider
     * @return <code>this</code> for method chaining
     */
    public PersistenceExpressionSerializerContext<T> withAliasProvider(String rootAlias, Function<T, String> aliasProvider) {
        if (aliasProviderContext == null) {
            throw new IllegalStateException("No alias provider context set, so can't handle alias providers!");
        }
        aliasProviders.put(rootAlias, aliasProvider);
        return this;
    }

    /**
     * Returns the persistence query alias provider.
     *
     * @return the persistence query alias provider
     */
    public Map<String, Function<T, String>> getAliasProviders() {
        return aliasProviders;
    }

    /**
     * Returns the interpreter context that should be used for inlining of paths that have no persistence related renderers.
     *
     * @return the interpreter context for inlining
     */
    public ExpressionInterpreter.Context getInterpreterContextForInlining() {
        return (ExpressionInterpreter.Context) contextParameters.get(PersistenceExpressionSerializer.CONSTANT_INLINING_INTERPRETER_CONTEXT);
    }

    /**
     * Sets the interpreter context to use for inlining of paths.
     *
     * @param interpreterContextForInlining The interpreter context
     */
    public void setInterpreterContextForInlining(ExpressionInterpreter.Context interpreterContextForInlining) {
        contextParameters.put(PersistenceExpressionSerializer.CONSTANT_INLINING_INTERPRETER_CONTEXT, interpreterContextForInlining);
    }

    /**
     * Returns the paths that should be inlined i.e. interpreted and rendered as constant.
     *
     * @return the paths to inline
     */
    public Set<String> getPathsToInline() {
        return (Set<String>) contextParameters.get(PersistenceExpressionSerializer.PATHS_TO_INLINE);
    }

    /**
     * Sets the paths that should be inlined i.e. interpreted and rendered as constant.
     *
     * @param pathsToInline The paths to inline
     */
    public void setPathsToInline(Set<String> pathsToInline) {
        contextParameters.put(PersistenceExpressionSerializer.PATHS_TO_INLINE, pathsToInline);
    }

    @Override
    public ExpressionService getExpressionService() {
        return expressionService;
    }

    @Override
    public <X> X getContextParameter(String contextParameterName) {
        String alias = aliases.get(contextParameterName);
        if (alias == null) {
            Function<T, String> provider = aliasProviders.get(contextParameterName);
            if (provider == null) {
                return (X) contextParameters.get(contextParameterName);
            }
            alias = provider.apply(aliasProviderContext);
            aliases.put(contextParameterName, alias);
        }
        return (X) alias;
    }
}
