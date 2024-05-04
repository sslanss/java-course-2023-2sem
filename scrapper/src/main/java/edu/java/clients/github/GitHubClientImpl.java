package edu.java.clients.github;

import edu.java.api_exceptions.BadRequestException;
import edu.java.exceptions.ApiErrorException;
import edu.java.exceptions.TooManyRequestsException;
import edu.java.responses.GitHubResponse;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GitHubClientImpl implements GitHubClient {
    private static final String BASE_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitHubClientImpl() {
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    public GitHubClientImpl(@NotNull String url) {
        this.webClient = WebClient.builder()
            .defaultStatusHandler(HttpStatusCode::isError, this::handleError)
            .baseUrl(url)
            .build();
    }

    private Mono<Exception> handleError(ClientResponse response) {
        if (response.statusCode().is4xxClientError()) {
            return Mono.error(new BadRequestException());
        } else if (response.statusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
            return Mono.error(new TooManyRequestsException());
        }
        return Mono.error(new ApiErrorException());
    }

    @Override
    public List<GitHubResponse> getRepositoryUpdate(
        @NotNull String owner, @NotNull String repository,
        @NotNull OffsetDateTime fromDate,
        @NotNull OffsetDateTime toDate
    ) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("repos/{owner}/{repo}/activity")
                //.queryParam("direction", "asc")
                .build(owner, repository))
            .retrieve()
            .bodyToFlux(GitHubResponse.class)
            .filter(gitHubResponse -> gitHubResponse.lastModified().isAfter(fromDate)
                && (gitHubResponse.lastModified().isBefore(toDate) || gitHubResponse.lastModified().isEqual(toDate)))
            .switchIfEmpty(Flux.empty())
            .collectList()
            .block();
    }
}
