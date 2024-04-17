package edu.java.clients.github;

import edu.java.api_exceptions.BadRequestException;
import edu.java.api_exceptions.ServerErrorException;
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
import reactor.util.retry.Retry;

public class GitHubClientImpl implements GitHubClient {
    private final WebClient webClient;
    private final Retry retry;

    public GitHubClientImpl(@NotNull String url, Retry retry) {
        this.webClient = WebClient.builder()
            .defaultStatusHandler(HttpStatusCode::isError, this::handleError)
            .baseUrl(url)
            .build();
        this.retry = retry;
    }

    private Mono<Exception> handleError(ClientResponse response) {
        if (response.statusCode().equals(HttpStatus.FORBIDDEN)
            || response.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
            return Mono.error(new TooManyRequestsException());
        }
        if (response.statusCode().is4xxClientError()) {
            return Mono.error(new BadRequestException());
        }
        return Mono.error(new ServerErrorException(response.statusCode().value()));
    }

    @Override
    public List<GitHubResponse> getRepositoryUpdate(
        @NotNull String owner, @NotNull String repository,
        @NotNull OffsetDateTime fromDate,
        @NotNull OffsetDateTime toDate
    ) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path("repos/{owner}/{repo}/activity")
                .build(owner, repository))
            .retrieve()
            .bodyToFlux(GitHubResponse.class)
            .retryWhen(retry)
            .filter(gitHubResponse -> gitHubResponse.lastModified().isAfter(fromDate)
                && (gitHubResponse.lastModified().isBefore(toDate) || gitHubResponse.lastModified().isEqual(toDate)))
            .switchIfEmpty(Flux.empty())
            .collectList()
            .block();
    }
}
