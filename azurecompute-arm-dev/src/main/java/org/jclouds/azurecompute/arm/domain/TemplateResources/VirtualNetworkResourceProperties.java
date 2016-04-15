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
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.List;
import java.util.Map;

@AutoValue
public abstract class VirtualNetworkResourceProperties{

    @AutoValue
    public abstract static class AddressSpace{

        public abstract List<String>  addressPrefixes();

        @SerializedNames({ "addressPrefixes" })
        public static AddressSpace create(final List<String>  addressPrefixes) {
            return builder()
                    .addressPrefixes(addressPrefixes)
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_VirtualNetworkResourceProperties_AddressSpace.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder addressPrefixes(List<String> addressPrefixes);
            public abstract AddressSpace build();
        }
    }

    @AutoValue
    public abstract static class SubnetRef{

        @AutoValue
        public abstract static class Properties{

            public abstract String  addressPrefix();

            @SerializedNames({ "addressPrefix" })
            public static Properties create(final String  addressPrefix) {
                return builder()
                        .addressPrefix(addressPrefix)
                        .build();
            }

            public static Builder builder() {
                return new AutoValue_VirtualNetworkResourceProperties_SubnetRef_Properties.Builder();
            }

            @AutoValue.Builder
            public abstract static class Builder {
                public abstract Builder addressPrefix(String addressPrefix);
                public abstract Properties build();
            }
        }

        public abstract String name();
        public abstract Properties properties();

        @SerializedNames({ "name", "properties" })
        public static SubnetRef create(final String name, Properties  properties) {
            return builder()
                    .name(name)
                    .properties(properties)
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_VirtualNetworkResourceProperties_SubnetRef.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder name(String name);
            public abstract Builder properties(Properties properties);
            public abstract SubnetRef build();
        }
    }

    public abstract AddressSpace addressSpace();

    public abstract List<SubnetRef>  subnets();

    @SerializedNames({ "addressSpace", "subnets" })
    public static VirtualNetworkResourceProperties create(final AddressSpace addressSpace, final List<SubnetRef> subnets) {
        return builder()
                .addressSpace(addressSpace)
                .subnets(subnets)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_VirtualNetworkResourceProperties.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder addressSpace(AddressSpace addressSpace);
        public abstract Builder subnets(List<SubnetRef> subnets);
        public abstract VirtualNetworkResourceProperties build();
    }

}
