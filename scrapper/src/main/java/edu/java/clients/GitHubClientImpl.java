package edu.java.clients;

import edu.java.responses.GitHubResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.reactive.function.client.WebClient;

public class GitHubClientImpl implements GitHubClient {
    private final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClientImpl() {
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    public GitHubClientImpl(String URL) {
        this.webClient = WebClient.builder().baseUrl(URL).build();
    }

    @Override
    public GitHubResponse getRepositoryUpdate(@NotNull String owner, @NotNull String repository) {
        webClient.get()
            .uri("repos/{owner}/{repo}", owner, repository)
            .retrieve()
            .bodyToMono(GitHubResponse.class)
            .block();
            //.onStatus(HttpStatus::is4xxClientError, clientResponse -> )
        return null;
    }
}
