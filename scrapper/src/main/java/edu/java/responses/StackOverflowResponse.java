package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackOverflowResponse(
    @JsonProperty("title") String title,
    @JsonProperty("last_activity_date") OffsetDateTime lastModified
) {
}
