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
package org.jclouds.azurecomputearm;

import static org.jclouds.reflect.Reflection2.typeToken;

import java.net.URI;
import java.util.Properties;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.azurecomputearm.compute.config.AzureComputeServiceContextModule;
import org.jclouds.azurecomputearm.config.AzureComputeHttpApiModule;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.rest.internal.BaseHttpApiMetadata;
import org.jclouds.azurecomputearm.oauth.v2.config.AzureOAuthModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for Microsoft Azure Resource Manager REST API
 */
public class AzureManagementApiMetadata extends BaseHttpApiMetadata<AzureComputeApi> {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public AzureManagementApiMetadata() {
      this(new Builder());
   }

   protected AzureManagementApiMetadata(final Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      final Properties properties = BaseHttpApiMetadata.defaultProperties();
      // Sometimes SSH Authentication failure happens in Azure.
      // It seems that the authorized key is injected after ssh has been started.
      properties.setProperty("jclouds.ssh.max-retries", "15");
      properties.setProperty("jclouds.ssh.retry-auth", "true");
      return properties;
   }

   public static class Builder extends BaseHttpApiMetadata.Builder<AzureComputeApi, Builder> {

      protected Builder() {
         super();

         id("azurecompute-arm")
                 .name("Microsoft Azure Resource Manager REST API")
                 .version("2014-04-01-preview")
                 .identityName("Path to Management Certificate .p12 file, or PEM string")
                 .credentialName("Password to Management Certificate")
                 .endpointName("Resource Manager Endpoint ending in your Subscription Id")
                 .documentation(URI.create("http://msdn.microsoft.com/en-us/library/ee460799"))
                 .defaultProperties(AzureManagementApiMetadata.defaultProperties())
                 .view(typeToken(ComputeServiceContext.class))
                 .defaultModules(ImmutableSet.<Class<? extends Module>>builder()
                         .add(AzureComputeServiceContextModule.class)
                         .add(AzureOAuthModule.class)
                         .add(AzureComputeHttpApiModule.class).build());
      }

      @Override
      public AzureManagementApiMetadata build() {
         return new AzureManagementApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }
   }
}