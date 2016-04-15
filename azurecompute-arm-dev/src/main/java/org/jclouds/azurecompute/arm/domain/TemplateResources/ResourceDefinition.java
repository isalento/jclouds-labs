package org.jclouds.azurecompute.arm.domain.TemplateResources;

import com.google.auto.value.AutoValue;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.List;
import java.util.Map;

@AutoValue
public abstract class ResourceDefinition {

    public abstract String name();

    public abstract String type();

    public abstract String location();

    public abstract String apiVersion();

    @Nullable
    public abstract List<String> dependsOn();

    @Nullable
    public abstract Map<String, String> tags();

    @Nullable
    public abstract Object properties();

    @SerializedNames({"name", "type", "location", "apiVersion", "dependsOn" , "tags", "properties"})
    public static ResourceDefinition create(final String name,
                                                final String type,
                                                final String location,
                                                final String apiVersion,
                                                final List<String> dependsOn,
                                                final Map<String, String> tags,
                                                final Object properties) {
        return builder()
                .name(name)
                .type(type)
                .location(location)
                .apiVersion(apiVersion)
                .dependsOn(dependsOn)
                .tags(tags)
                .properties(properties)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_ResourceDefinition.Builder();
    }

@AutoValue.Builder
public abstract static class Builder {
    public abstract Builder name(String name);
    public abstract Builder type(String type);
    public abstract Builder location(String location);
    public abstract Builder apiVersion(String apiVersion);
    public abstract Builder dependsOn(List<String> dependencies);
    public abstract Builder tags(Map<String, String> tags);
    public abstract Builder properties(Object properties);
    public abstract ResourceDefinition build();
}

}