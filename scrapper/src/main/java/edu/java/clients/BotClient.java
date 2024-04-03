package edu.java.clients;

import edu.java.api_exceptions.BadRequestException;
import edu.java.requests.LinkUpdateRequest;
import edu.java.responses.ApiErrorResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final WebClient webClient;

    private static final String BASE_URL = "http://localhost:8090";

    public BotClient() {
        webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    private boolean isBadRequest(HttpStatusCode httpStatusCode) {
        return httpStatusCode.equals(HttpStatus.BAD_REQUEST);
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
            .block();
    }
}
