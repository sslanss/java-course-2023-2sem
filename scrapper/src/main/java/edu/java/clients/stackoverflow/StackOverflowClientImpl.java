package edu.java.clients.stackoverflow;

import edu.java.api_exceptions.BadRequestException;
import edu.java.api_exceptions.ServerErrorException;
import edu.java.responses.StackOverflowResponse;
import java.time.OffsetDateTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class StackOverflowClientImpl implements StackOverflowClient {
    private final WebClient webClient;
    private final Retry retry;

    public StackOverflowClientImpl(String url, Retry retry) {
        this.webClient = WebClient.builder()
            .baseUrl(url)
            .build();
        this.retry = retry;
    }

    private Mono<Exception> handleError(ClientResponse response) {
        if (response.statusCode().is4xxClientError()) {
            return Mono.error(new BadRequestException());
        }
        return Mono.error(new ServerErrorException(response.statusCode().value()));
    }

    @Override
    public StackOverflowResponse getQuestionUpdate(
        long questionId, @NotNull OffsetDateTime fromDate,
        OffsetDateTime toDate
    ) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("questions/{ids}/answers")
                .queryParam("site", "stackoverflow")
                .queryParam("fromdate", fromDate.toEpochSecond())
                .queryParam("todate", toDate.toEpochSecond())
                .queryParam("sort", "creation")
                .queryParam("order", "desc")
                .build(questionId))
            .retrieve()
            .bodyToMono(StackOverflowResponse.class)
            .retryWhen(retry)
            .block();
    }
}
