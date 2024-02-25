package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record StackOverflowResponse(
    @JsonProperty("title") String title,
    @JsonProperty("last_activity_date") OffsetDateTime lastModified
) {
}
