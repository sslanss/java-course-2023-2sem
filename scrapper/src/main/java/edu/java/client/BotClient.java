package edu.java.client;

import edu.java.exceptions.BadRequestException;
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
    private final WebClient client;

    public BotClient(WebClient client) {
        this.client = client;
    }

    private boolean isBadRequest(HttpStatusCode httpStatusCode) {
        return httpStatusCode.equals(HttpStatus.BAD_REQUEST);
    }

    public void sendLinkUpdate(Long id, String url, String description, List<Long> tgChatIds) {
        LinkUpdateRequest linkUpdate = new LinkUpdateRequest(id, URI.create(url), description, tgChatIds);

        client.post()
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
