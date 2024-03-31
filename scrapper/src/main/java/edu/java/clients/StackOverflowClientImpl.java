package edu.java.clients;

import edu.java.api_exceptions.BadRequestException;
import edu.java.exceptions.ApiErrorException;
import edu.java.exceptions.TooManyRequestsException;
import edu.java.responses.StackOverflowResponse;
import java.time.OffsetDateTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class StackOverflowClientImpl implements StackOverflowClient {
    private static final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClientImpl() {
        this.webClient = WebClient.builder()
            .defaultStatusHandler(HttpStatusCode::isError, this::handleError)
            .baseUrl(BASE_URL)
            .build();
    }

    public StackOverflowClientImpl(String url) {
        this.webClient = WebClient.builder()
            .baseUrl(url)
            .build();
    }

    private Mono<Exception> handleError(ClientResponse response) {
        if (response.statusCode().is4xxClientError()) {
            return Mono.error(new BadRequestException());
        } else if (response.statusCode().equals(HttpStatus.BAD_GATEWAY)) {
            return Mono.error(new TooManyRequestsException());
        }
        return Mono.error(new ApiErrorException());
    }

    @Override
    public StackOverflowResponse getQuestionUpdate(long questionId, @NotNull OffsetDateTime lastChecked) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("questions/{ids}/answers")
                .queryParam("site", "stackoverflow")
                .queryParam("fromdate", lastChecked.toEpochSecond())
                .queryParam("sort", "creation")
                .queryParam("order", "desc")
                .build(questionId))
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .block();
    }
}
