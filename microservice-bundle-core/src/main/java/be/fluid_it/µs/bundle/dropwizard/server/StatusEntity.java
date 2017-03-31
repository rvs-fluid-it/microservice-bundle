package be.fluid_it.µs.bundle.dropwizard.server;

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
}
