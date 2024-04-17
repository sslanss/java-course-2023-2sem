package edu.java.clients;

import edu.java.api_exceptions.BadRequestException;
import edu.java.requests.LinkUpdateRequest;
import edu.java.responses.ApiErrorResponse;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class BotClient {
    private final WebClient webClient;

    private final Retry retry;

    private static final String BASE_URL = "http://localhost:8090";

    public BotClient(@NotNull String baseUrl, Retry retry) {
        webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.retry = retry;
    }

    public void sendLinkUpdate(Long id, URI url, String description, List<Long> tgChatIds) {
        LinkUpdateRequest linkUpdate = new LinkUpdateRequest(id, url, description, tgChatIds);

        webClient.post()
            .uri("/updates")
            .body(BodyInserters.fromValue(linkUpdate))
            .retrieve()
            .onStatus(
                (httpStatusCode -> httpStatusCode.equals(HttpStatus.BAD_REQUEST)),
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new BadRequestException(
                            errorResponse.getCode(),
                            errorResponse.getDescription()
                        ))
                    )
            )
            .toBodilessEntity()
            .retryWhen(retry)
            .block();
    }
}
