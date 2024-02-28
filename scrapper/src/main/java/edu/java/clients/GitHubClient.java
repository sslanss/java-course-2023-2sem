package edu.java.clients;

import edu.java.responses.GitHubResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;

public interface GitHubClient {
    List<GitHubResponse> getRepositoryUpdate(
        @NotNull String owner,
        @NotNull String repository,
        @NotNull OffsetDateTime lastChecked
    );
}
