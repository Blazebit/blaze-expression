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

package com.blazebit.expression.azure.vm;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

import com.blazebit.domain.Domain;
import com.blazebit.domain.boot.model.DomainBuilder;
import com.blazebit.domain.boot.model.MetadataDefinition;
import com.blazebit.domain.boot.model.MetadataDefinitionHolder;
import com.blazebit.domain.declarative.DeclarativeDomain;
import com.blazebit.domain.declarative.DeclarativeDomainConfiguration;
import com.blazebit.domain.declarative.spi.DeclarativeMetadataProcessor;
import com.blazebit.domain.runtime.model.DomainOperator;
import com.blazebit.domain.runtime.model.DomainPredicate;
import com.blazebit.domain.spi.ServiceProvider;
import com.blazebit.expression.DocumentationMetadataDefinition;
import com.blazebit.expression.azure.subscription.DataFetcherMetadataDefinition;
import com.blazebit.expression.azure.vm.model.AdditionalCapabilities;
import com.blazebit.expression.azure.vm.model.AdditionalUnattendContent;
import com.blazebit.expression.azure.vm.model.ApiEntityReference;
import com.blazebit.expression.azure.vm.model.ApiError;
import com.blazebit.expression.azure.vm.model.ApiErrorBase;
import com.blazebit.expression.azure.vm.model.ApplicationProfile;
import com.blazebit.expression.azure.vm.model.AttachDetachDataDisksRequest;
import com.blazebit.expression.azure.vm.model.AvailablePatchSummary;
import com.blazebit.expression.azure.vm.model.BillingProfile;
import com.blazebit.expression.azure.vm.model.BootDiagnostics;
import com.blazebit.expression.azure.vm.model.BootDiagnosticsInstanceView;
import com.blazebit.expression.azure.vm.model.Caching;
import com.blazebit.expression.azure.vm.model.CapacityReservationProfile;
import com.blazebit.expression.azure.vm.model.CloudError;
import com.blazebit.expression.azure.vm.model.CreateOption;
import com.blazebit.expression.azure.vm.model.DataDisk;
import com.blazebit.expression.azure.vm.model.DataDisksToAttach;
import com.blazebit.expression.azure.vm.model.DataDisksToDetach;
import com.blazebit.expression.azure.vm.model.DeleteOption;
import com.blazebit.expression.azure.vm.model.DetachOption;
import com.blazebit.expression.azure.vm.model.DiagnosticsProfile;
import com.blazebit.expression.azure.vm.model.DiffDiskOption;
import com.blazebit.expression.azure.vm.model.DiffDiskPlacement;
import com.blazebit.expression.azure.vm.model.DiffDiskSettings;
import com.blazebit.expression.azure.vm.model.DiskControllerType;
import com.blazebit.expression.azure.vm.model.DiskEncryptionSetParameters;
import com.blazebit.expression.azure.vm.model.DiskEncryptionSettings;
import com.blazebit.expression.azure.vm.model.DiskInstanceView;
import com.blazebit.expression.azure.vm.model.EncryptionIdentity;
import com.blazebit.expression.azure.vm.model.EventGridAndResourceGraph;
import com.blazebit.expression.azure.vm.model.EvictionPolicy;
import com.blazebit.expression.azure.vm.model.ExtendedLocation;
import com.blazebit.expression.azure.vm.model.ExtendedLocationType;
import com.blazebit.expression.azure.vm.model.HardwareProfile;
import com.blazebit.expression.azure.vm.model.ImageReference;
import com.blazebit.expression.azure.vm.model.InnerError;
import com.blazebit.expression.azure.vm.model.InstanceViewStatus;
import com.blazebit.expression.azure.vm.model.KeyVaultKeyReference;
import com.blazebit.expression.azure.vm.model.KeyVaultSecretReference;
import com.blazebit.expression.azure.vm.model.LastPatchInstallationSummary;
import com.blazebit.expression.azure.vm.model.LinuxConfiguration;
import com.blazebit.expression.azure.vm.model.LinuxParameters;
import com.blazebit.expression.azure.vm.model.LinuxPatchSettings;
import com.blazebit.expression.azure.vm.model.LinuxVMGuestPatchAutomaticByPlatformSettings;
import com.blazebit.expression.azure.vm.model.MaintenanceRedeployStatus;
import com.blazebit.expression.azure.vm.model.ManagedDiskParameters;
import com.blazebit.expression.azure.vm.model.NetworkInterfaceReference;
import com.blazebit.expression.azure.vm.model.NetworkInterfaceReferenceProperties;
import com.blazebit.expression.azure.vm.model.NetworkProfile;
import com.blazebit.expression.azure.vm.model.OSDisk;
import com.blazebit.expression.azure.vm.model.OSImageNotificationProfile;
import com.blazebit.expression.azure.vm.model.OSProfile;
import com.blazebit.expression.azure.vm.model.OSProfileProvisioningData;
import com.blazebit.expression.azure.vm.model.PatchInstallationDetail;
import com.blazebit.expression.azure.vm.model.PatchSettings;
import com.blazebit.expression.azure.vm.model.Plan;
import com.blazebit.expression.azure.vm.model.Priority;
import com.blazebit.expression.azure.vm.model.ProxyAgentSettings;
import com.blazebit.expression.azure.vm.model.PublicIPAddressSku;
import com.blazebit.expression.azure.vm.model.Resource;
import com.blazebit.expression.azure.vm.model.ResourceWithOptionalLocation;
import com.blazebit.expression.azure.vm.model.RetrieveBootDiagnosticsDataResult;
import com.blazebit.expression.azure.vm.model.ScheduledEventsAdditionalPublishingTargets;
import com.blazebit.expression.azure.vm.model.ScheduledEventsPolicy;
import com.blazebit.expression.azure.vm.model.ScheduledEventsProfile;
import com.blazebit.expression.azure.vm.model.SecurityProfile;
import com.blazebit.expression.azure.vm.model.SshConfiguration;
import com.blazebit.expression.azure.vm.model.SshPublicKey;
import com.blazebit.expression.azure.vm.model.StorageAccountType;
import com.blazebit.expression.azure.vm.model.StorageProfile;
import com.blazebit.expression.azure.vm.model.SubResource;
import com.blazebit.expression.azure.vm.model.TerminateNotificationProfile;
import com.blazebit.expression.azure.vm.model.UefiSettings;
import com.blazebit.expression.azure.vm.model.UpdateResource;
import com.blazebit.expression.azure.vm.model.UserAssignedIdentitiesValue;
import com.blazebit.expression.azure.vm.model.UserInitiatedReboot;
import com.blazebit.expression.azure.vm.model.UserInitiatedRedeploy;
import com.blazebit.expression.azure.vm.model.VMDiskSecurityProfile;
import com.blazebit.expression.azure.vm.model.VMGalleryApplication;
import com.blazebit.expression.azure.vm.model.VMSizeProperties;
import com.blazebit.expression.azure.vm.model.VaultCertificate;
import com.blazebit.expression.azure.vm.model.VaultSecretGroup;
import com.blazebit.expression.azure.vm.model.VirtualHardDisk;
import com.blazebit.expression.azure.vm.model.VirtualMachine;
import com.blazebit.expression.azure.vm.model.VirtualMachineAgentInstanceView;
import com.blazebit.expression.azure.vm.model.VirtualMachineAssessPatchesResult;
import com.blazebit.expression.azure.vm.model.VirtualMachineCaptureParameters;
import com.blazebit.expression.azure.vm.model.VirtualMachineCaptureResult;
import com.blazebit.expression.azure.vm.model.VirtualMachineExtension;
import com.blazebit.expression.azure.vm.model.VirtualMachineExtensionHandlerInstanceView;
import com.blazebit.expression.azure.vm.model.VirtualMachineExtensionInstanceView;
import com.blazebit.expression.azure.vm.model.VirtualMachineExtensionProperties;
import com.blazebit.expression.azure.vm.model.VirtualMachineExtensionUpdate;
import com.blazebit.expression.azure.vm.model.VirtualMachineExtensionUpdateProperties;
import com.blazebit.expression.azure.vm.model.VirtualMachineExtensionsListResult;
import com.blazebit.expression.azure.vm.model.VirtualMachineHealthStatus;
import com.blazebit.expression.azure.vm.model.VirtualMachineIdentity;
import com.blazebit.expression.azure.vm.model.VirtualMachineInstallPatchesParameters;
import com.blazebit.expression.azure.vm.model.VirtualMachineInstallPatchesResult;
import com.blazebit.expression.azure.vm.model.VirtualMachineInstanceView;
import com.blazebit.expression.azure.vm.model.VirtualMachineIpTag;
import com.blazebit.expression.azure.vm.model.VirtualMachineIpTag2;
import com.blazebit.expression.azure.vm.model.VirtualMachineListResult;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceConfiguration;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceConfiguration2;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceConfigurationProperties;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceConfigurationProperties2;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceDnsSettingsConfiguration;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceDnsSettingsConfiguration2;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceIPConfiguration;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceIPConfiguration2;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceIPConfigurationProperties;
import com.blazebit.expression.azure.vm.model.VirtualMachineNetworkInterfaceIPConfigurationProperties2;
import com.blazebit.expression.azure.vm.model.VirtualMachinePatchStatus;
import com.blazebit.expression.azure.vm.model.VirtualMachineProperties;
import com.blazebit.expression.azure.vm.model.VirtualMachinePublicIPAddressConfiguration;
import com.blazebit.expression.azure.vm.model.VirtualMachinePublicIPAddressConfiguration2;
import com.blazebit.expression.azure.vm.model.VirtualMachinePublicIPAddressConfigurationProperties;
import com.blazebit.expression.azure.vm.model.VirtualMachinePublicIPAddressConfigurationProperties2;
import com.blazebit.expression.azure.vm.model.VirtualMachinePublicIPAddressDnsSettingsConfiguration;
import com.blazebit.expression.azure.vm.model.VirtualMachinePublicIPAddressDnsSettingsConfiguration2;
import com.blazebit.expression.azure.vm.model.VirtualMachineReimageParameters;
import com.blazebit.expression.azure.vm.model.VirtualMachineSize;
import com.blazebit.expression.azure.vm.model.VirtualMachineSizeListResult;
import com.blazebit.expression.azure.vm.model.VirtualMachineSoftwarePatchProperties;
import com.blazebit.expression.azure.vm.model.VirtualMachineUpdate;
import com.blazebit.expression.azure.vm.model.WinRMConfiguration;
import com.blazebit.expression.azure.vm.model.WinRMListener;
import com.blazebit.expression.azure.vm.model.WindowsConfiguration;
import com.blazebit.expression.azure.vm.model.WindowsParameters;
import com.blazebit.expression.azure.vm.model.WindowsVMGuestPatchAutomaticByPlatformSettings;
import com.blazebit.expression.spi.ComparisonOperatorInterpreter;
import com.blazebit.expression.spi.DomainOperatorInterpreter;

/**
 * Utility class to create or extend {@link DeclarativeDomainConfiguration} with the Azure VirtualMachine model.
 *
 * @author Christian Beikov
 * @since 1.0.0
 */
public final class AzureVmModel {

    /**
     * The type name for the Map type.
     */
    public static final String MAP_TYPE_NAME = "Map";
    /**
     * The type name for the Object type.
     */
    public static final String OBJECT_TYPE_NAME = "Object";

    private AzureVmModel() {
    }

    /**
     * Creates and returns a new {@link DeclarativeDomainConfiguration} that contains the Azure VM model.
     * @return a new {@link DeclarativeDomainConfiguration} that contains the Azure VM model
     */
    public static DeclarativeDomainConfiguration createConfiguration() {
        return contribute( Domain.getDefaultProvider().createDefaultBuilder());
    }

    /**
     * Creates and returns a new {@link DeclarativeDomainConfiguration} that contains the Azure VM model.
     *
     * @param domainBuilder The base domain builder to use for the {@link DeclarativeDomainConfiguration}
     * @return a new {@link DeclarativeDomainConfiguration} that contains the Azure VM model
     */
    public static DeclarativeDomainConfiguration contribute(DomainBuilder domainBuilder) {
        return contribute( DeclarativeDomain.getDefaultProvider().createDefaultConfiguration( domainBuilder), domainBuilder);
    }

    /**
     * Registers the Azure VM model on the given {@link DeclarativeDomainConfiguration} and respective {@link DomainBuilder}.
     *
     * @param declarativeDomainConfiguration The base declarative domain configuration to register the Azure VM model on
     * @param domainBuilder The base domain builder to use for the {@link DeclarativeDomainConfiguration}
     * @return the {@link DeclarativeDomainConfiguration} passed as parameter
     */
    public static DeclarativeDomainConfiguration contribute(DeclarativeDomainConfiguration declarativeDomainConfiguration, DomainBuilder domainBuilder) {
        declarativeDomainConfiguration.withTypeResolverDecorator(new AzureVmTypeResolverDecorator());
        contributeDomainTypes(domainBuilder);

        addDomainType(AdditionalCapabilities.class, declarativeDomainConfiguration);
        addDomainType(AdditionalUnattendContent.class, declarativeDomainConfiguration);
        addDomainType(ApiEntityReference.class, declarativeDomainConfiguration);
        addDomainType(ApiError.class, declarativeDomainConfiguration);
        addDomainType(ApiErrorBase.class, declarativeDomainConfiguration);
        addDomainType(ApplicationProfile.class, declarativeDomainConfiguration);
        addDomainType(AttachDetachDataDisksRequest.class, declarativeDomainConfiguration);
        addDomainType(AvailablePatchSummary.class, declarativeDomainConfiguration);
        addDomainType(BillingProfile.class, declarativeDomainConfiguration);
        addDomainType(BootDiagnostics.class, declarativeDomainConfiguration);
        addDomainType(BootDiagnosticsInstanceView.class, declarativeDomainConfiguration);
        addDomainType(Caching.class, declarativeDomainConfiguration);
        addDomainType(CapacityReservationProfile.class, declarativeDomainConfiguration);
        addDomainType(CloudError.class, declarativeDomainConfiguration);
        addDomainType(CreateOption.class, declarativeDomainConfiguration);
        addDomainType(DataDisk.class, declarativeDomainConfiguration);
        addDomainType(DataDisksToAttach.class, declarativeDomainConfiguration);
        addDomainType(DataDisksToDetach.class, declarativeDomainConfiguration);
        addDomainType(DeleteOption.class, declarativeDomainConfiguration);
        addDomainType(DetachOption.class, declarativeDomainConfiguration);
        addDomainType(DiagnosticsProfile.class, declarativeDomainConfiguration);
        addDomainType(DiffDiskOption.class, declarativeDomainConfiguration);
        addDomainType(DiffDiskPlacement.class, declarativeDomainConfiguration);
        addDomainType(DiffDiskSettings.class, declarativeDomainConfiguration);
        addDomainType(DiskControllerType.class, declarativeDomainConfiguration);
        addDomainType(DiskEncryptionSetParameters.class, declarativeDomainConfiguration);
        addDomainType(DiskEncryptionSettings.class, declarativeDomainConfiguration);
        addDomainType(DiskInstanceView.class, declarativeDomainConfiguration);
        addDomainType(EncryptionIdentity.class, declarativeDomainConfiguration);
        addDomainType(EventGridAndResourceGraph.class, declarativeDomainConfiguration);
        addDomainType(EvictionPolicy.class, declarativeDomainConfiguration);
        addDomainType(ExtendedLocation.class, declarativeDomainConfiguration);
        addDomainType(ExtendedLocationType.class, declarativeDomainConfiguration);
        addDomainType(HardwareProfile.class, declarativeDomainConfiguration);
        addDomainType(ImageReference.class, declarativeDomainConfiguration);
        addDomainType(InnerError.class, declarativeDomainConfiguration);
        addDomainType(InstanceViewStatus.class, declarativeDomainConfiguration);
        addDomainType(KeyVaultKeyReference.class, declarativeDomainConfiguration);
        addDomainType(KeyVaultSecretReference.class, declarativeDomainConfiguration);
        addDomainType(LastPatchInstallationSummary.class, declarativeDomainConfiguration);
        addDomainType(LinuxConfiguration.class, declarativeDomainConfiguration);
        addDomainType(LinuxParameters.class, declarativeDomainConfiguration);
        addDomainType(LinuxPatchSettings.class, declarativeDomainConfiguration);
        addDomainType(LinuxVMGuestPatchAutomaticByPlatformSettings.class, declarativeDomainConfiguration);
        addDomainType(MaintenanceRedeployStatus.class, declarativeDomainConfiguration);
        addDomainType(ManagedDiskParameters.class, declarativeDomainConfiguration);
        addDomainType(NetworkInterfaceReference.class, declarativeDomainConfiguration);
        addDomainType(NetworkInterfaceReferenceProperties.class, declarativeDomainConfiguration);
        addDomainType(NetworkProfile.class, declarativeDomainConfiguration);
        addDomainType(OSDisk.class, declarativeDomainConfiguration);
        addDomainType(OSImageNotificationProfile.class, declarativeDomainConfiguration);
        addDomainType(OSProfile.class, declarativeDomainConfiguration);
        addDomainType(OSProfileProvisioningData.class, declarativeDomainConfiguration);
        addDomainType(PatchInstallationDetail.class, declarativeDomainConfiguration);
        addDomainType(PatchSettings.class, declarativeDomainConfiguration);
        addDomainType(Plan.class, declarativeDomainConfiguration);
        addDomainType(Priority.class, declarativeDomainConfiguration);
        addDomainType(ProxyAgentSettings.class, declarativeDomainConfiguration);
        addDomainType(PublicIPAddressSku.class, declarativeDomainConfiguration);
        addDomainType(Resource.class, declarativeDomainConfiguration);
        addDomainType(ResourceWithOptionalLocation.class, declarativeDomainConfiguration);
        addDomainType(RetrieveBootDiagnosticsDataResult.class, declarativeDomainConfiguration);
        addDomainType(ScheduledEventsAdditionalPublishingTargets.class, declarativeDomainConfiguration);
        addDomainType(ScheduledEventsPolicy.class, declarativeDomainConfiguration);
        addDomainType(ScheduledEventsProfile.class, declarativeDomainConfiguration);
        addDomainType(SecurityProfile.class, declarativeDomainConfiguration);
        addDomainType(SshConfiguration.class, declarativeDomainConfiguration);
        addDomainType(SshPublicKey.class, declarativeDomainConfiguration);
        addDomainType(StorageAccountType.class, declarativeDomainConfiguration);
        addDomainType(StorageProfile.class, declarativeDomainConfiguration);
        addDomainType(SubResource.class, declarativeDomainConfiguration);
        addDomainType(TerminateNotificationProfile.class, declarativeDomainConfiguration);
        addDomainType(UefiSettings.class, declarativeDomainConfiguration);
        addDomainType(UpdateResource.class, declarativeDomainConfiguration);
        addDomainType(UserAssignedIdentitiesValue.class, declarativeDomainConfiguration);
        addDomainType(UserInitiatedReboot.class, declarativeDomainConfiguration);
        addDomainType(UserInitiatedRedeploy.class, declarativeDomainConfiguration);
        addDomainType(VaultCertificate.class, declarativeDomainConfiguration);
        addDomainType(VaultSecretGroup.class, declarativeDomainConfiguration);
        addDomainType(VirtualHardDisk.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachine.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineAgentInstanceView.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineAssessPatchesResult.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineCaptureParameters.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineCaptureResult.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineExtension.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineExtensionHandlerInstanceView.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineExtensionInstanceView.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineExtensionProperties.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineExtensionsListResult.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineExtensionUpdate.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineExtensionUpdateProperties.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineHealthStatus.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineIdentity.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineInstallPatchesParameters.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineInstallPatchesResult.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineInstanceView.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineIpTag.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineIpTag2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineListResult.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceConfiguration.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceConfiguration2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceConfigurationProperties.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceConfigurationProperties2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceDnsSettingsConfiguration.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceDnsSettingsConfiguration2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceIPConfiguration.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceIPConfiguration2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceIPConfigurationProperties.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineNetworkInterfaceIPConfigurationProperties2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachinePatchStatus.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineProperties.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachinePublicIPAddressConfiguration.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachinePublicIPAddressConfiguration2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachinePublicIPAddressConfigurationProperties.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachinePublicIPAddressConfigurationProperties2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachinePublicIPAddressDnsSettingsConfiguration.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachinePublicIPAddressDnsSettingsConfiguration2.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineReimageParameters.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineSize.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineSizeListResult.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineSoftwarePatchProperties.class, declarativeDomainConfiguration);
        addDomainType(VirtualMachineUpdate.class, declarativeDomainConfiguration);
        addDomainType(VMDiskSecurityProfile.class, declarativeDomainConfiguration);
        addDomainType(VMGalleryApplication.class, declarativeDomainConfiguration);
        addDomainType(VMSizeProperties.class, declarativeDomainConfiguration);
        addDomainType(WindowsConfiguration.class, declarativeDomainConfiguration);
        addDomainType(WindowsParameters.class, declarativeDomainConfiguration);
        addDomainType(WindowsVMGuestPatchAutomaticByPlatformSettings.class, declarativeDomainConfiguration);
        addDomainType(WinRMConfiguration.class, declarativeDomainConfiguration);
        addDomainType(WinRMListener.class, declarativeDomainConfiguration);
        declarativeDomainConfiguration.withMetadataProcessor( new DeclarativeMetadataProcessor<>() {
            @Override
            public Class<Annotation> getProcessingAnnotation() {
                return null;
            }

            @Override
            public MetadataDefinition<?> process(
                    Class<?> annotatedClass,
                    Annotation annotation,
                    ServiceProvider serviceProvider) {
                if (annotatedClass == VirtualMachine.class) {
                    return new DataFetcherMetadataDefinition( VirtualMachineDataFetcher.INSTANCE );
                }
                return null;
            }
        } );
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
            DocumentationMetadataDefinition.localized(documentationKey, AzureVmModel.class.getClassLoader())
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
