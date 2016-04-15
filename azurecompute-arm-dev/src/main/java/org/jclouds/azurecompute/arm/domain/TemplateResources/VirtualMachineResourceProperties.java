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
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.List;

@AutoValue
public abstract class VirtualMachineResourceProperties {


    @AutoValue
    public abstract static class HardwareProfile{

        public abstract String vmSize();

        @SerializedNames({"vmSize"})
        public static HardwareProfile create(final String  vmSize) {
            return builder().vmSize(vmSize).build();
        }
        public static Builder builder() {
            return new AutoValue_VirtualMachineResourceProperties_HardwareProfile.Builder();
        }
        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder vmSize(String vmSize);
            public abstract HardwareProfile build();
        }
    }

    @AutoValue
    public abstract static class OsProfile{

        public abstract String computerName();
        public abstract String adminUsername();
        public abstract String adminPassword();

        @SerializedNames({"computerName", "adminUsername", "adminPassword"})
        public static OsProfile create(final String computerName, final String adminUsername, final String adminPassword) {
            return builder()
                    .computerName(computerName)
                    .adminUsername(adminUsername)
                    .adminPassword(adminPassword)
                    .build();
        }
        public static Builder builder() {
            return new AutoValue_VirtualMachineResourceProperties_OsProfile.Builder();
        }
        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder computerName(String computerName);
            public abstract Builder adminUsername(String adminUsername);
            public abstract Builder adminPassword(String adminPassword);
            public abstract OsProfile build();
        }
    }

    @AutoValue
    public abstract static class StorageProfile{

        @AutoValue
        public abstract static class ImageReference{

            public abstract String publisher();
            public abstract String offer();
            public abstract String sku();
            public abstract String version();

            @SerializedNames({"publisher", "offer", "sku", "version"})
            public static ImageReference create(final String publisher, final String offer, final String sku, final String version) {
                return builder()
                        .publisher(publisher)
                        .offer(offer)
                        .sku(sku)
                        .version(version)
                        .build();
            }

            public static Builder builder() {
                return new AutoValue_VirtualMachineResourceProperties_StorageProfile_ImageReference.Builder();
            }

            @AutoValue.Builder
            public abstract static class Builder {
                public abstract Builder publisher(String publisher);
                public abstract Builder offer(String offer);
                public abstract Builder sku(String sku);
                public abstract Builder version(String version);
                public abstract ImageReference build();
            }
        }

        @AutoValue
        public abstract static class Vhd{

            public abstract String uri();

            @SerializedNames({"uri"})
            public static Vhd create(final String  uri) {
                return builder().uri(uri).build();
            }
            public static Builder builder() {
                return new AutoValue_VirtualMachineResourceProperties_StorageProfile_Vhd.Builder();
            }
            @AutoValue.Builder
            public abstract static class Builder {
                public abstract Builder uri(String uri);
                public abstract Vhd build();
            }
        }

        @AutoValue
        public abstract static class OsDisk{

            public abstract String name();
            public abstract String caching();
            public abstract String createOption();
            public abstract Vhd vhd();

            @SerializedNames({"name", "caching", "createOption", "vhd"})
            public static OsDisk create(final String name, final String caching, final String createOption, final Vhd vhd) {
                return builder()
                        .name(name)
                        .caching(caching)
                        .createOption(createOption)
                        .vhd(vhd)
                        .build();
            }

            public static Builder builder() {
                return new AutoValue_VirtualMachineResourceProperties_StorageProfile_OsDisk.Builder();
            }

            @AutoValue.Builder
            public abstract static class Builder {
                public abstract Builder name(String name);
                public abstract Builder caching(String caching);
                public abstract Builder createOption(String createOption);
                public abstract Builder vhd(Vhd vhd);
                public abstract OsDisk build();
            }
        }

        @AutoValue
        public abstract static class DataDisk {

            public abstract String name();

            public abstract String diskSizeGB();

            public abstract String lun();

            public abstract String createOption();

            public abstract Vhd vhd();

            @SerializedNames({"name", "diskSizeGB", "lun", "createOption", "vhd"})
            public static DataDisk create(final String name, final String diskSizeGB, final String lun, final String createOption, final Vhd vhd) {
                return builder()
                        .name(name)
                        .diskSizeGB(diskSizeGB)
                        .lun(lun)
                        .createOption(createOption)
                        .vhd(vhd)
                        .build();
            }

            public static Builder builder() {
                return new AutoValue_VirtualMachineResourceProperties_StorageProfile_DataDisk.Builder();
            }

            @AutoValue.Builder
            public abstract static class Builder {
                public abstract Builder name(String name);

                public abstract Builder diskSizeGB(String diskSizeGB);

                public abstract Builder createOption(String createOption);

                public abstract Builder lun(String lun);

                public abstract Builder vhd(Vhd vhd);

                public abstract DataDisk build();
            }
        }


        public abstract ImageReference imageReference();
        public abstract OsDisk osDisk();
        public abstract List<DataDisk> dataDisks();

        @SerializedNames({"imageReference", "osDisk", "dataDisks" })
        public static StorageProfile create(final ImageReference imageReference, final OsDisk osDisk, final List<DataDisk> dataDisks ) {
            return builder()
                    .imageReference(imageReference)
                    .osDisk(osDisk)
                    .dataDisks(dataDisks)
                    .build();
        }

        public static Builder builder() {
            return new AutoValue_VirtualMachineResourceProperties_StorageProfile.Builder();
        }
        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder imageReference(ImageReference imageReference);
            public abstract Builder osDisk(OsDisk osDisk);
            public abstract Builder dataDisks(List<DataDisk> dataDisks);
            public abstract StorageProfile build();
        }
    }

    @AutoValue
    public abstract static class NetworkProfile{

        public abstract List<IdReference> networkInterfaces();

        @SerializedNames({"networkInterfaces"})
        public static NetworkProfile create(final List<IdReference>  networkInterfaces) {
            return builder().networkInterfaces(networkInterfaces).build();
        }
        public static Builder builder() {
            return new AutoValue_VirtualMachineResourceProperties_NetworkProfile.Builder();
        }
        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder networkInterfaces(List<IdReference> networkInterfaces);
            public abstract NetworkProfile build();
        }
    }

    @AutoValue
    public abstract static class DiagnosticsProfile{


        @AutoValue
        public abstract static class BootDiagnostics{

            public abstract String enabled();
            public abstract String storageUri();

            @SerializedNames({"enabled", "storageUri"})
            public static BootDiagnostics create(final String enabled, final String storageUri) {
                return builder()
                        .enabled(enabled)
                        .storageUri(storageUri)
                        .build();
            }

            public static Builder builder() {
                return new AutoValue_VirtualMachineResourceProperties_DiagnosticsProfile_BootDiagnostics.Builder();
            }
            @AutoValue.Builder
            public abstract static class Builder {
                public abstract Builder enabled(String enabled);
                public abstract Builder storageUri(String storageUri);
                public abstract BootDiagnostics build();
            }
        }

        public abstract BootDiagnostics bootDiagnostics();

        @SerializedNames({"bootDiagnostics"})
        public static DiagnosticsProfile create(final BootDiagnostics  bootDiagnostics) {
            return builder().bootDiagnostics(bootDiagnostics).build();
        }
        public static Builder builder() {
            return new AutoValue_VirtualMachineResourceProperties_DiagnosticsProfile.Builder();
        }

        @AutoValue.Builder
        public abstract static class Builder {
            public abstract Builder bootDiagnostics(BootDiagnostics bootDiagnostics);
            public abstract DiagnosticsProfile build();
        }
    }

    public abstract HardwareProfile hardwareProfile();

    public abstract OsProfile osProfile();

    public abstract StorageProfile storageProfile();

    @Nullable
    public abstract NetworkProfile networkProfile();

    @Nullable
    public abstract DiagnosticsProfile diagnosticsProfile();

    @SerializedNames({ "hardwareProfile", "osProfile", "storageProfile", "networkProfile", "diagnosticsProfile" })
    public static VirtualMachineResourceProperties create(final HardwareProfile hardwareProfile,
                                                          final OsProfile osProfile,
                                                          final StorageProfile  storageProfile,
                                                          final NetworkProfile networkProfile,
                                                          final DiagnosticsProfile diagnosticsProfile) {
        return builder()
                .hardwareProfile(hardwareProfile)
                .osProfile(osProfile)
                .storageProfile(storageProfile)
                .networkProfile(networkProfile)
                .diagnosticsProfile(diagnosticsProfile)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_VirtualMachineResourceProperties.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder hardwareProfile(HardwareProfile hardwareProfile);
        public abstract Builder osProfile(OsProfile osProfile);
        public abstract Builder storageProfile(StorageProfile storageProfile);
        public abstract Builder networkProfile(NetworkProfile networkProfile);
        public abstract Builder diagnosticsProfile(DiagnosticsProfile diagnosticsProfile);
        public abstract VirtualMachineResourceProperties build();
    }

}
