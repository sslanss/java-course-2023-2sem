package edu.java.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubResponse(
    @JsonProperty("title") String title,
    @JsonProperty("updated_at") OffsetDateTime lastModified
) {
}
