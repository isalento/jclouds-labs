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
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class PublicIPAddressResourceProperties {

    @AutoValue
    public abstract static class DnsSettings{

        public abstract String  domainNameLabel();

        @SerializedNames({ "domainNameLabel" })
        public static DnsSettings create(final String  domainNameLabel) {
            return builder()
                    .domainNameLabel(domainNameLabel)
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_PublicIPAddressResourceProperties_DnsSettings.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder domainNameLabel(String domainNameLabel);
            public abstract DnsSettings build();
        }
    }

    public abstract String publicIPAllocationMethod();

    public abstract DnsSettings  dnsSettings();

    @SerializedNames({ "publicIPAllocationMethod", "dnsSettings" })
    public static PublicIPAddressResourceProperties create(final String publicIPAllocationMethod, final DnsSettings dnsSettings) {
        return builder()
                .publicIPAllocationMethod(publicIPAllocationMethod)
                .dnsSettings(dnsSettings)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_PublicIPAddressResourceProperties.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder publicIPAllocationMethod(String publicIPAllocationMethod);
        public abstract Builder dnsSettings(DnsSettings dnsSettings);
        public abstract PublicIPAddressResourceProperties build();
    }

}
