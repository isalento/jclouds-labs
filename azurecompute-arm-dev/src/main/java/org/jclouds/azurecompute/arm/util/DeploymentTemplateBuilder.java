/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.azurecompute.arm.util;

import com.google.common.collect.ImmutableMap;
import org.jclouds.azurecompute.arm.domain.IdReference;
import org.jclouds.azurecompute.arm.domain.TemplateResources.NetworkInterfaceCardResourceProperties;
import org.jclouds.azurecompute.arm.domain.TemplateResources.PublicIPAddressResourceProperties;
import org.jclouds.azurecompute.arm.domain.TemplateResources.ResourceDefinition;
import org.jclouds.azurecompute.arm.domain.TemplateResources.StorageResourceProperties;
import org.jclouds.azurecompute.arm.domain.TemplateResources.VirtualMachineResourceProperties;
import org.jclouds.azurecompute.arm.domain.TemplateResources.VirtualNetworkResourceProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeploymentTemplateBuilder {

    public ResourceDefinition getStorageResource() {

        ResourceDefinition storageAccount = ResourceDefinition.builder().name("[variables('StorageAccountName')]")
                .type("Microsoft.Storage/storageAccounts")
                .location("[resourceGroup().location]")
                .apiVersion("2015-06-15")
                .properties(
                        StorageResourceProperties.builder()
                                .accountType("Standard_LRS")
                                .build()
                )
                .build();
        return storageAccount;
    }

    public ResourceDefinition getVirtualNetworkResource() {
        ResourceDefinition virtualNetwork = ResourceDefinition.builder()
                .name("[variables('VirtualNetworkName')]")
                .type("Microsoft.Network/virtualNetworks")
                .location("[resourceGroup().location]")
                .apiVersion("2015-06-15")
                .properties(
                        VirtualNetworkResourceProperties.builder()
                                .addressSpace(
                                        VirtualNetworkResourceProperties.AddressSpace.create(Arrays.asList("[variables('VirtualNetworkPrefix')]"))
                                )
                                .subnets(
                                        Arrays.asList(
                                                VirtualNetworkResourceProperties.SubnetRef.builder()
                                                        .name("[variables('SubnetName')]")
                                                        .properties(
                                                                VirtualNetworkResourceProperties.SubnetRef.Properties.create("[variables('SubnetPrefix')]")
                                                        )
                                                        .build())
                                )
                                .build()
                ).build();
        return virtualNetwork;
    }

    public ResourceDefinition getPublicIpAddress() {
        ResourceDefinition publicIpAddress = ResourceDefinition.builder()
                .name("[variables('PublicIPAddressName')]")
                .type("Microsoft.Network/publicIPAddresses")
                .location("[resourceGroup().location]")
                .apiVersion("2015-06-15")
                .properties(
                        PublicIPAddressResourceProperties.builder()
                                .dnsSettings(
                                        PublicIPAddressResourceProperties.DnsSettings.create("[variables('DnsLablePrefix')]")
                                )
                                .publicIPAllocationMethod("Dynamic")
                )
                .build();
        return publicIpAddress;
    }

    public ResourceDefinition getNetworkInterfaceCard() {

        List<NetworkInterfaceCardResourceProperties.IpConfiguration> ipConfigurations = new ArrayList<NetworkInterfaceCardResourceProperties.IpConfiguration>();

        NetworkInterfaceCardResourceProperties.IpConfiguration ipConfig = NetworkInterfaceCardResourceProperties.IpConfiguration.builder()
                .name("IpConfig1")
                .properties(
                        NetworkInterfaceCardResourceProperties.IpConfiguration.IpConfigurationProperties.builder()
                                .privateIPAllocationMethod("Dynamic")
                                .publicIPAddress(IdReference.create("[resourceId('Microsoft.Network/publicIPAddresses',variables('PublicIPAddressName'))]"))
                                .subnet(IdReference.create("[variables('SubnetRef')]"))
                                .build()
                )
                .build();

        ipConfigurations.add(ipConfig);

        NetworkInterfaceCardResourceProperties networkInterfaceCardResourceProperties = NetworkInterfaceCardResourceProperties.builder()
                .ipConfigurations(ipConfigurations)
                .build();

        ResourceDefinition networkIntefaceCard = ResourceDefinition.builder()
                .name("[variables('NetworkInterfaceCardName')]")
                .type("Microsoft.Network/networkInterfaces")
                .location("[resourceGroup().location]")
                .apiVersion("2015-06-15")
                .dependsOn(Arrays.asList("[concat('Microsoft.Network/publicIPAddresses/', variables('PublicIPAddressName'))]",
                        "[concat('Microsoft.Network/virtualNetworks/', variables('VirtualNetworkName'))]"))
                .properties(networkInterfaceCardResourceProperties)
                .build();
        return networkIntefaceCard;
    }

    public ResourceDefinition getVirtualMachine() {

        //Build OS Profile
        VirtualMachineResourceProperties.OsProfile osProfile = VirtualMachineResourceProperties.OsProfile.builder()
                .adminUsername("[variables('AdminUsername')]")
                .adminPassword("[variables('AdminPassword')]")
                .computerName("[variables('ComputerName')]")
                .build();

        //Build Image Reference
        VirtualMachineResourceProperties.StorageProfile.ImageReference imageReference = VirtualMachineResourceProperties.StorageProfile.ImageReference.builder()
                .publisher("[variables('ImagePublisher')]")
                .offer("[variables('ImageOffer')]")
                .sku("[variables('OSVersion')]")
                .version("latest")
                .build();

        //Build OsDisk
        VirtualMachineResourceProperties.StorageProfile.OsDisk osDisk = VirtualMachineResourceProperties.StorageProfile.OsDisk.builder()
                .name("[variables('OsDiskName')]")
                .vhd(
                    VirtualMachineResourceProperties.StorageProfile.Vhd.create("[concat('http://',variables('StorageAccountName'),'.blob.core.windows.net/',variables('VmStorageAccountContainerName'),'/',variables('OsDiskName'),'.vhd')]")
                )
                .caching("ReadWrite")
                .createOption("FromImage")
                .build();

        //Create Data Disk(s) and add to list
        List<VirtualMachineResourceProperties.StorageProfile.DataDisk> dataDisks = new ArrayList<VirtualMachineResourceProperties.StorageProfile.DataDisk>();
        VirtualMachineResourceProperties.StorageProfile.DataDisk dataDisk = VirtualMachineResourceProperties.StorageProfile.DataDisk.builder()
                .name("[variables('DataDiskName')]")
                .diskSizeGB("[variables('DataDiskSize')]")
                .lun("0")
                .vhd(
                    VirtualMachineResourceProperties.StorageProfile.Vhd.create("[concat('http://',variables('StorageAccountName'),'.blob.core.windows.net/',variables('VmStorageAccountContainerName'),'/',variables('DataDiskName'),'.vhd')]")
                )
                .createOption("Empty")
                .build();
        dataDisks.add(dataDisk);

        //Create Storage Profile
        VirtualMachineResourceProperties.StorageProfile storageProfile = VirtualMachineResourceProperties.StorageProfile.builder()
                .imageReference(imageReference)
                .osDisk(osDisk)
                .dataDisks(dataDisks)
                .build();

        //Create Network Profile for this VM (links to network interface cards)
        VirtualMachineResourceProperties.NetworkProfile networkProfile = VirtualMachineResourceProperties.NetworkProfile.create(
                Arrays.asList(
                        IdReference.create("[resourceId('Microsoft.Network/networkInterfaces',variables('NetworkInterfaceCardName'))]")
                ));

        //Boot Diagnostics
        VirtualMachineResourceProperties.DiagnosticsProfile diagnosticsProfile = VirtualMachineResourceProperties.DiagnosticsProfile.create(
                VirtualMachineResourceProperties.DiagnosticsProfile.BootDiagnostics.builder()
                    .enabled("true")
                    .storageUri("[concat('http://',variables('StorageAccountName'),'.blob.core.windows.net')]")
                    .build());

        //Build VirtualMachine properties based on above properties.
        VirtualMachineResourceProperties properties = VirtualMachineResourceProperties.builder()
                .hardwareProfile(
                        VirtualMachineResourceProperties.HardwareProfile.create("[variables('VmSize')]")
                )
                .osProfile(osProfile)
                .storageProfile(storageProfile)
                .networkProfile(networkProfile)
                .diagnosticsProfile(diagnosticsProfile)
                .build();

        ResourceDefinition virtualMachine = ResourceDefinition.builder()
                .name("[variables('VirtualMachineName')]")
                .type("Microsoft.Compute/virtualMachines")
                .location("[resourceGroup().location]")
                .apiVersion("2015-06-15")
                .dependsOn(Arrays.asList("[concat('Microsoft.Storage/storageAccounts/', variables('StorageAccountName'))]",
                        "[concat('Microsoft.Network/networkInterfaces/', variables('NetworkInterfaceCardName'))]"))
                .tags(ImmutableMap.of("displayName", "VirtualMachine"))
                .properties(properties)
                .build();

        return virtualMachine;
    }

}
