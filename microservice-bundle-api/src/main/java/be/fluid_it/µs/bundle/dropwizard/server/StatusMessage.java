package be.fluid_it.µs.bundle.dropwizard.server;

import be.fluid_it.µs.bundle.dropwizard.services.EnvironmentProvider;
import be.fluid_it.µs.bundle.dropwizard.services.ProviderUtil;
import be.fluid_it.µs.bundle.dropwizard.services.RevisionProvider;
import be.fluid_it.µs.bundle.dropwizard.services.ServiceNameProvider;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static be.fluid_it.µs.bundle.dropwizard.server.StatusMessage.Type.ERROR;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusMessage {
    public enum Type {
        INFO, WARNING, ERROR
    };

    private final Type type;
    private final String id;
    private final String key;

    private final String message;
    private final String details;

    public StatusMessage(Type type, String id, String message) {
        this(type, id, null, message, null);
    }

    public StatusMessage(String id, String message) {
        this(ERROR, id, message);
    }

    public StatusMessage(Type type,  String message) {
        this(ERROR, null, message);
    }

    @JsonCreator
    public StatusMessage(@JsonProperty("type") Type type,
                         @JsonProperty("id") String id,
                         @JsonProperty("key") String key,
                         @JsonProperty("message") String message,
                         @JsonProperty("details") String details) {
        this.type = type;
        this.id = id;
        this.key = key;
        this.message = message;
        this.details = details;
    }

    @JsonProperty("type")
    public Type getType() {
        return type;
    }

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("id")
    public String getId() {
        return this.id;
    }

    @JsonProperty("details")
    public String getDetails() {
        return details;
    }

}
