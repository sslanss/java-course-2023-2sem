package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubResponse(
    @JsonProperty("activity_type") String activityType,
    @JsonProperty("timestamp") OffsetDateTime lastModified
) {
}
