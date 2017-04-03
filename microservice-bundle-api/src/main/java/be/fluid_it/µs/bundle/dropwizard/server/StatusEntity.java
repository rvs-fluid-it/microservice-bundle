package be.fluid_it.µs.bundle.dropwizard.server;

import be.fluid_it.µs.bundle.dropwizard.services.EnvironmentProvider;
import be.fluid_it.µs.bundle.dropwizard.services.ProviderUtil;
import be.fluid_it.µs.bundle.dropwizard.services.RevisionProvider;
import be.fluid_it.µs.bundle.dropwizard.services.ServiceNameProvider;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusEntity {
    private final int code;
    private final StatusMessage[] messages;

    public StatusEntity(int code, StatusMessage message) {
        this(code, new StatusMessage[]{message});
    }

    @JsonCreator
    public StatusEntity(@JsonProperty("code") int code, @JsonProperty("messages") StatusMessage[] messages) {
        this.code = code;
        this.messages = messages;
    }

    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    @JsonProperty("messages")
    public StatusMessage[] getMessages() {
        return messages;
    }

    @JsonProperty("revision")
    public String getRevision() {
        RevisionProvider provider = ProviderUtil.getProvider(RevisionProvider.class);
        return provider != null ? provider.getRevision() : null;
    }

    @JsonProperty("env")
    public String getEnvironment() {
        EnvironmentProvider provider = ProviderUtil.getProvider(EnvironmentProvider.class);
        return provider != null ? provider.getEnvironment() : null;
    }

    @JsonProperty("service")
    public String getService() {
        ServiceNameProvider provider = ProviderUtil.getProvider(ServiceNameProvider.class);
        return provider != null ? provider.getName() : null;
    }
}
