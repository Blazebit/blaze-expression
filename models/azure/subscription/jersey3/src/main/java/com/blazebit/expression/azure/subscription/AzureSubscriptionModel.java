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

package com.blazebit.expression.azure.subscription;

import java.io.Serializable;
import java.util.Map;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.declarative.DeclarativeDomain;
import com.blazebit.domain.declarative.DeclarativeDomainConfiguration;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.azure.subscription.model.AvailabilityZoneMappings;
import com.blazebit.expression.azure.subscription.model.AvailabilityZonePeers;
import com.blazebit.expression.azure.subscription.model.CheckResourceNameResult;
import com.blazebit.expression.azure.subscription.model.CheckZonePeersRequest;
import com.blazebit.expression.azure.subscription.model.CheckZonePeersResult;
import com.blazebit.expression.azure.subscription.model.CloudError;
import com.blazebit.expression.azure.subscription.model.ErrorAdditionalInfo;
import com.blazebit.expression.azure.subscription.model.ErrorAdditionalInfo2;
import com.blazebit.expression.azure.subscription.model.ErrorDetail;
import com.blazebit.expression.azure.subscription.model.ErrorResponse;
import com.blazebit.expression.azure.subscription.model.ErrorResponse2;
import com.blazebit.expression.azure.subscription.model.Location;
import com.blazebit.expression.azure.subscription.model.LocationListResult;
import com.blazebit.expression.azure.subscription.model.LocationMetadata;
import com.blazebit.expression.azure.subscription.model.ManagedByTenant;
import com.blazebit.expression.azure.subscription.model.Operation;
import com.blazebit.expression.azure.subscription.model.Operation2;
import com.blazebit.expression.azure.subscription.model.Operation2Display;
import com.blazebit.expression.azure.subscription.model.OperationDisplay;
import com.blazebit.expression.azure.subscription.model.OperationListResult;
import com.blazebit.expression.azure.subscription.model.OperationListResult2;
import com.blazebit.expression.azure.subscription.model.PairedRegion;
import com.blazebit.expression.azure.subscription.model.Peers;
import com.blazebit.expression.azure.subscription.model.ResourceName;
import com.blazebit.expression.azure.subscription.model.Subscription;
import com.blazebit.expression.azure.subscription.model.SubscriptionListResult;
import com.blazebit.expression.azure.subscription.model.SubscriptionPolicies;
import com.blazebit.expression.azure.subscription.model.TenantIdDescription;
import com.blazebit.expression.azure.subscription.model.TenantListResult;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainOperatorInterpreter;

/**
 * Utility class to create or extend {@link DeclarativeDomainConfiguration} with the Azure Subscription model.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class AzureSubscriptionModel {

    /**
     * The type name for the Map type.
     */
    public static final String MAP_TYPE_NAME = "Map";
    /**
     * The type name for the Object type.
     */
    public static final String OBJECT_TYPE_NAME = "Object";

    private AzureSubscriptionModel() {
    }

    /**
     * Creates and returns a new {@link DeclarativeDomainConfiguration} that contains the Azure Subscription model.
     * @return a new {@link DeclarativeDomainConfiguration} that contains the Azure Subscription model
     */
    public static DeclarativeDomainConfiguration createConfiguration() {
        return contribute( Domain.getDefaultProvider().createDefaultBuilder());
    }

    /**
     * Creates and returns a new {@link DeclarativeDomainConfiguration} that contains the Azure Subscription model.
     *
     * @param domainBuilder The base domain builder to use for the {@link DeclarativeDomainConfiguration}
     * @return a new {@link DeclarativeDomainConfiguration} that contains the Azure Subscription model
     */
    public static DeclarativeDomainConfiguration contribute(DomainBuilder domainBuilder) {
        return contribute( DeclarativeDomain.getDefaultProvider().createDefaultConfiguration( domainBuilder), domainBuilder);
    }

    /**
     * Registers the Azure Subscription model on the given {@link DeclarativeDomainConfiguration} and respective {@link DomainBuilder}.
     *
     * @param declarativeDomainConfiguration The base declarative domain configuration to register the Azure Subscription model on
     * @param domainBuilder The base domain builder to use for the {@link DeclarativeDomainConfiguration}
     * @return the {@link DeclarativeDomainConfiguration} passed as parameter
     */
    public static DeclarativeDomainConfiguration contribute(DeclarativeDomainConfiguration declarativeDomainConfiguration, DomainBuilder domainBuilder) {
        declarativeDomainConfiguration.withTypeResolverDecorator(new AzureSubscriptionTypeResolverDecorator());
        contributeDomainTypes(domainBuilder);
        addDomainType(AvailabilityZoneMappings.class, declarativeDomainConfiguration);
        addDomainType(AvailabilityZonePeers.class, declarativeDomainConfiguration);
        addDomainType(CheckResourceNameResult.class, declarativeDomainConfiguration);
        addDomainType(CheckZonePeersRequest.class, declarativeDomainConfiguration);
        addDomainType(CheckZonePeersResult.class, declarativeDomainConfiguration);
        addDomainType(CloudError.class, declarativeDomainConfiguration);
        addDomainType(ErrorAdditionalInfo.class, declarativeDomainConfiguration);
        addDomainType(ErrorAdditionalInfo2.class, declarativeDomainConfiguration);
        addDomainType(ErrorDetail.class, declarativeDomainConfiguration);
        addDomainType(ErrorResponse.class, declarativeDomainConfiguration);
        addDomainType(ErrorResponse2.class, declarativeDomainConfiguration);
        addDomainType(Location.class, declarativeDomainConfiguration);
        addDomainType(LocationListResult.class, declarativeDomainConfiguration);
        addDomainType(LocationMetadata.class, declarativeDomainConfiguration);
        addDomainType(ManagedByTenant.class, declarativeDomainConfiguration);
        addDomainType(Operation.class, declarativeDomainConfiguration);
        addDomainType(Operation2.class, declarativeDomainConfiguration);
        addDomainType(Operation2Display.class, declarativeDomainConfiguration);
        addDomainType(OperationDisplay.class, declarativeDomainConfiguration);
        addDomainType(OperationListResult.class, declarativeDomainConfiguration);
        addDomainType(OperationListResult2.class, declarativeDomainConfiguration);
        addDomainType(PairedRegion.class, declarativeDomainConfiguration);
        addDomainType(Peers.class, declarativeDomainConfiguration);
        addDomainType(ResourceName.class, declarativeDomainConfiguration);
        addDomainType(Subscription.class, declarativeDomainConfiguration);
        addDomainType(SubscriptionListResult.class, declarativeDomainConfiguration);
        addDomainType(SubscriptionPolicies.class, declarativeDomainConfiguration);
        addDomainType(TenantIdDescription.class, declarativeDomainConfiguration);
        addDomainType(TenantListResult.class, declarativeDomainConfiguration);
        return declarativeDomainConfiguration;
    }

    private static void addDomainType(
            Class<?> topLevelType,
            DeclarativeDomainConfiguration declarativeDomainConfiguration) {
        declarativeDomainConfiguration.addDomainType( topLevelType );
        for ( Class<?> declaredClass : topLevelType.getDeclaredClasses() ) {
            addDomainType( declaredClass, declarativeDomainConfiguration );
        }
    }

    private static void contributeDomainTypes(DomainBuilder domainBuilder) {
        createBasicType(domainBuilder, Map.class, MAP_TYPE_NAME, null, DomainPredicate.comparable(), handlersFor(
                MapOperatorInterpreter.INSTANCE, "MAP"));
        createBasicType(domainBuilder, Object.class, OBJECT_TYPE_NAME, null, DomainPredicate.comparable(), handlersFor(
                ObjectOperatorInterpreter.INSTANCE, "OBJECT"));
    }

    private static <T extends ComparisonOperatorInterpreter & DomainOperatorInterpreter> MetadataDefinition<?>[] handlersFor(T instance, String documentationKey) {
        return new MetadataDefinition[] {
            new ComparisonOperatorInterpreterMetadataDefinition(instance),
            new DomainOperatorInterpreterMetadataDefinition(instance),
            DocumentationMetadataDefinition.localized(documentationKey, AzureSubscriptionModel.class.getClassLoader())
        };
    }

    private static void createBasicType(DomainBuilder domainBuilder, Class<?> type, String name, DomainOperator[] operators, DomainPredicate[] predicates, MetadataDefinition<?>... metadataDefinitions) {
        domainBuilder.createBasicType(name, type, metadataDefinitions);
        if (operators != null && operators.length != 0) {
            domainBuilder.withOperator(name, operators);
        }
        if (predicates != null && predicates.length != 0) {
            domainBuilder.withPredicate(name, predicates);
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    static class ComparisonOperatorInterpreterMetadataDefinition implements MetadataDefinition<ComparisonOperatorInterpreter>, Serializable {

        private final ComparisonOperatorInterpreter comparisonOperatorInterpreter;

        /**
         * Creates a metadata definition for the given {@link ComparisonOperatorInterpreter}.
         *
         * @param comparisonOperatorInterpreter The comparison operator interpreter
         */
        public ComparisonOperatorInterpreterMetadataDefinition(ComparisonOperatorInterpreter comparisonOperatorInterpreter) {
            this.comparisonOperatorInterpreter = comparisonOperatorInterpreter;
        }

        @Override
        public Class<ComparisonOperatorInterpreter> getJavaType() {
            return ComparisonOperatorInterpreter.class;
        }

        @Override
        public ComparisonOperatorInterpreter build(MetadataDefinitionHolder definitionHolder) {
            return comparisonOperatorInterpreter;
        }
    }

    /**
     * @author Christian Beikov
     * @since 1.0.0
     */
    static class DomainOperatorInterpreterMetadataDefinition implements MetadataDefinition<DomainOperatorInterpreter> {

        private final DomainOperatorInterpreter domainOperatorInterpreter;

        /**
         * Creates a metadata definition for the given {@link DomainOperatorInterpreter}.
         *
         * @param domainOperatorInterpreter The domain operator interpreter
         */
        public DomainOperatorInterpreterMetadataDefinition(DomainOperatorInterpreter domainOperatorInterpreter) {
            this.domainOperatorInterpreter = domainOperatorInterpreter;
        }

        @Override
        public Class<DomainOperatorInterpreter> getJavaType() {
            return DomainOperatorInterpreter.class;
        }

        @Override
        public DomainOperatorInterpreter build(MetadataDefinitionHolder definitionHolder) {
            return domainOperatorInterpreter;
        }
    }

}
