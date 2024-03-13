package edu.java.clients;

import edu.java.responses.GitHubResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


public class GitHubClientImpl implements GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClientImpl() {
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    public GitHubClientImpl(String URL) {
        this.webClient = WebClient.builder().baseUrl(URL).build();
    }

    @Override
    public List<GitHubResponse> getRepositoryUpdate(
        @NotNull String owner, @NotNull String repository,
        @NotNull OffsetDateTime lastChecked
    ) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("repos/{owner}/{repo}/activity")
                .queryParam("direction", "asc")
                .build(owner, repository))
            .retrieve()
            .bodyToFlux(GitHubResponse.class)
            .switchIfEmpty(Flux.empty())
            .filter(gitHubResponse -> gitHubResponse.lastModified().isAfter(lastChecked))
            .collectList()
            .block();

        //.onStatus(HttpStatus::is4xxClientError, clientResponse -> )
    }
}
