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
package org.jclouds.azurecompute.arm.compute.functions;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.collect.Sets;
import org.jclouds.azurecompute.arm.AzureComputeApi;
import org.jclouds.azurecompute.arm.domain.ComputeNode;
import org.jclouds.azurecompute.arm.domain.Deployment;
import org.jclouds.azurecompute.arm.domain.PublicIPAddress;
import org.jclouds.azurecompute.arm.domain.VMDeployment;
import org.jclouds.azurecompute.arm.domain.VirtualMachineInstance;
import org.jclouds.azurecompute.arm.util.DeploymentTemplateBuilder;
import org.jclouds.azurecompute.arm.util.GetEnumValue;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.functions.GroupNamingConvention;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import org.jclouds.domain.LoginCredentials;

public class DeploymentToNodeMetadata implements Function<VMDeployment, NodeMetadata> {

   public static final String AZURE_LOGIN_USERNAME = DeploymentTemplateBuilder.getLoginUserUsername();
   public static final String AZURE_LOGIN_PASSWORD = DeploymentTemplateBuilder.getLoginPassword();

   private static final Map<ComputeNode.Status, NodeMetadata.Status> INSTANCESTATUS_TO_NODESTATUS =
           ImmutableMap.<ComputeNode.Status, NodeMetadata.Status>builder().
                   put(ComputeNode.Status.GOOD, NodeMetadata.Status.RUNNING).
                   put(ComputeNode.Status.BAD, NodeMetadata.Status.ERROR).
                   put(ComputeNode.Status.UNRECOGNIZED, NodeMetadata.Status.UNRECOGNIZED).
                   build();

   // When using the Deployment API to deploy an ARM template, the deployment goes through
   // stages.  Accepted -> Running -> Succeeded.  Only when the deployment has SUCCEEDED is
   // the resource deployed using the template actually ready.
   //
   // To get details about the resource(s) deployed via template, one needs to query the
   // various resources after the deployment has "SUCCEEDED".
   private static final Map<Deployment.ProvisioningState, NodeMetadata.Status> STATUS_TO_NODESTATUS =
           ImmutableMap.<Deployment.ProvisioningState, NodeMetadata.Status>builder().
                   put(Deployment.ProvisioningState.ACCEPTED, NodeMetadata.Status.PENDING).
                   put(Deployment.ProvisioningState.READY, NodeMetadata.Status.PENDING).
                   put(Deployment.ProvisioningState.RUNNING, NodeMetadata.Status.PENDING).
                   put(Deployment.ProvisioningState.CANCELED, NodeMetadata.Status.TERMINATED).
                   put(Deployment.ProvisioningState.FAILED, NodeMetadata.Status.ERROR).
                   put(Deployment.ProvisioningState.DELETED, NodeMetadata.Status.TERMINATED).
                   put(Deployment.ProvisioningState.SUCCEEDED, NodeMetadata.Status.RUNNING).
                   put(Deployment.ProvisioningState.UNRECOGNIZED, NodeMetadata.Status.UNRECOGNIZED).
                   build();

   public static Deployment.ProvisioningState provisioningStateFromString(final String text) {
      return (Deployment.ProvisioningState) GetEnumValue.fromValueOrDefault(text, Deployment.ProvisioningState.UNRECOGNIZED);
   }

   private final AzureComputeApi api;

   private final Supplier<Set<? extends Location>> locations;

   private final GroupNamingConvention nodeNamingConvention;

   private final VMImageToImage vmImageToImage;

   private final VMHardwareToHardware vmHardwareToHardware;

   private final Map<String, Credentials> credentialStore;

   @Inject
   DeploymentToNodeMetadata(
           AzureComputeApi api,
           @Memoized Supplier<Set<? extends Location>> locations,
           GroupNamingConvention.Factory namingConvention, VMImageToImage vmImageToImage,
           VMHardwareToHardware vmHardwareToHardware, Map<String, Credentials> credentialStore) {

      this.nodeNamingConvention = namingConvention.createWithoutPrefix();
      this.locations = locations;
      this.vmImageToImage = vmImageToImage;
      this.vmHardwareToHardware = vmHardwareToHardware;
      this.credentialStore = credentialStore;
      this.api = api;
   }

   @Override
   public NodeMetadata apply(final VMDeployment from) {
      final NodeMetadataBuilder builder = new NodeMetadataBuilder();
      final Deployment deployment = from.deployment;
      builder.id(deployment.name());
      builder.providerId(deployment.name());
      builder.name(deployment.name());
      String group =  this.nodeNamingConvention.extractGroup(deployment.name());
      builder.group(group);

      NodeMetadata.Status status = STATUS_TO_NODESTATUS.get(provisioningStateFromString(deployment.properties().provisioningState()));
      if (status == NodeMetadata.Status.RUNNING && from.vm != null && from.vm.statuses() != null) {
         List<VirtualMachineInstance.VirtualMachineStatus> statuses = from.vm.statuses();
         for (int c = 0; c < statuses.size(); c++) {
            if (statuses.get(c).code().substring(0, 10).equals("PowerState")) {
               if (statuses.get(c).displayStatus().equals("VM running")) {
                  status = NodeMetadata.Status.RUNNING;
               } else if (statuses.get(c).displayStatus().equals("VM stopped")) {
                  status = NodeMetadata.Status.SUSPENDED;
               }
               break;
            }
         }
      }

      builder.status(status);

      Credentials credentials = credentialStore.get("node#" + from.deployment.name());
      if (credentials == null) {
         credentials = new Credentials(AZURE_LOGIN_USERNAME, AZURE_LOGIN_PASSWORD);
      }
      builder.credentials(LoginCredentials.fromCredentials(credentials));

      final Set<String> publicIpAddresses = Sets.newLinkedHashSet();
      if (from.ipAddressList != null) {
         for (int c = 0; c < from.ipAddressList.size(); c++) {
            PublicIPAddress ip = from.ipAddressList.get(c);
            if (ip != null && ip.properties() != null && ip.properties().ipAddress() != null)
            {
               publicIpAddresses.add(ip.properties().ipAddress());
               break;
            }

         }
         if (publicIpAddresses.size() > 0)
            builder.publicAddresses(publicIpAddresses);
      }

      return builder.build();
   }
}