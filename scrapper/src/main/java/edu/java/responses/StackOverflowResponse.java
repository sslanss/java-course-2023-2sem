package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackOverflowResponse(List<StackOverflowAnswerInfo> items) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record StackOverflowAnswerInfo(
        @JsonProperty("creation_date") OffsetDateTime creationDate) {
    }
}
