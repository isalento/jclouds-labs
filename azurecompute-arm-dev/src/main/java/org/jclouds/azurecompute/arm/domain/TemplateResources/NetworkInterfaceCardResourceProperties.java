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
package org.jclouds.azurecompute.arm.domain.TemplateResources;

import com.google.auto.value.AutoValue;
import org.jclouds.azurecompute.arm.domain.IdReference;
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class NetworkInterfaceCardResourceProperties {

    @AutoValue
    public abstract static class IpConfiguration{

        @AutoValue
        public abstract static class IpConfigurationProperties{

            public abstract String  privateIPAllocationMethod();
            public abstract IdReference publicIPAddress();
            public abstract IdReference subnet();

            @SerializedNames({ "privateIPAllocationMethod", "publicIPAddress", "subnet" })
            public static IpConfigurationProperties create(final String  privateIPAllocationMethod, final IdReference publicIPAddressRef, final IdReference subnetRef) {
                return builder()
                        .privateIPAllocationMethod(privateIPAllocationMethod)
                        .publicIPAddress(publicIPAddressRef)
                        .subnet(subnetRef)
                        .build();
            }

            public static Builder builder() {
                return new AutoValue_NetworkInterfaceCardResourceProperties_IpConfiguration_IpConfigurationProperties.Builder();
            }

            @AutoValue.Builder
            public abstract static class Builder {
                public abstract Builder privateIPAllocationMethod(String privateIPAllocationMethod);
                public abstract Builder publicIPAddress(IdReference publicIPAddress);
                public abstract Builder subnet(IdReference subnet);
                public abstract IpConfigurationProperties build();
            }
        }

        public abstract String  name();
        public abstract IpConfigurationProperties  properties();

        @SerializedNames({ "name", "properties" })
        public static IpConfiguration create(final String  name, IpConfigurationProperties  properties) {
            return builder()
                    .name(name)
                    .properties(properties)
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_NetworkInterfaceCardResourceProperties_IpConfiguration.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder name(String name);
            public abstract Builder properties(IpConfigurationProperties properties);
            public abstract IpConfiguration build();
        }
    }


    public abstract List<IpConfiguration> ipConfigurations();

    @SerializedNames({ "ipConfigurations"})
    public static NetworkInterfaceCardResourceProperties create(final List<IpConfiguration> ipConfigurations) {
        return builder()
                .ipConfigurations(ipConfigurations)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_NetworkInterfaceCardResourceProperties.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder ipConfigurations(List<IpConfiguration> ipConfigurations);
        public abstract NetworkInterfaceCardResourceProperties build();
    }

}
