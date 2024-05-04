package edu.java.bot.client;

import edu.java.api_exceptions.BadRequestException;
import edu.java.requests.AddLinkRequest;
import edu.java.requests.RemoveLinkRequest;
import edu.java.responses.ApiErrorResponse;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final WebClient webClient;
    private static final String TG_CHAT_BASE_URL = "/tg-chat/{id}";

    private static final String LINKS_BASE_URL = "/links";

    public static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    private static final String BASE_URL = "http://localhost:8080";

    public ScrapperClient() {
        webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    private boolean isBadRequest(HttpStatusCode httpStatusCode) {
        return httpStatusCode.equals(HttpStatus.BAD_REQUEST);
    }

    private boolean isAlreadyReported(HttpStatusCode httpStatusCode) {
        return httpStatusCode.equals(HttpStatus.ALREADY_REPORTED);
    }

    private boolean isNotFound(HttpStatusCode httpStatusCode) {
        return httpStatusCode.equals(HttpStatus.NOT_FOUND);
    }

    private Mono<Exception> handleBadRequest(ClientResponse response) {
        return response.bodyToMono(ApiErrorResponse.class)
            .flatMap(errorResponse -> Mono.error(new BadRequestException(
                errorResponse.getCode(),
                errorResponse.getDescription()
            )));
    }

    private Mono<String> handleRegistrationResponse(ClientResponse response) {
        if (isAlreadyReported(response.statusCode())) {
            return response.bodyToMono(ApiErrorResponse.class)
                .map(ApiErrorResponse::getDescription);
        }
        return response.bodyToMono(String.class).map(responseBody -> "Chat was registered");
    }

    private Mono<String> handleDeletingResponse(ClientResponse response) {
        if (isNotFound(response.statusCode())) {
            return response.bodyToMono(ApiErrorResponse.class)
                .map(ApiErrorResponse::getDescription);
        }
        return response.bodyToMono(String.class).map(responseBody -> "Chat was deleted");
    }

    public String registerChat(Integer id) {
        return webClient.post()
            .uri(TG_CHAT_BASE_URL, id)
            .retrieve()
            .onStatus(this::isBadRequest, this::handleBadRequest)
            .bodyToMono(ClientResponse.class)
            .flatMap(this::handleRegistrationResponse)
            .block();
    }

    public String deleteChat(Integer id) {
        return webClient.delete()
            .uri(TG_CHAT_BASE_URL, id)
            .retrieve()
            .onStatus(this::isBadRequest, this::handleBadRequest)
            .bodyToMono(ClientResponse.class)
            .flatMap(this::handleDeletingResponse)
            .block();
    }

    public ListLinksResponse getLinks(Integer id) {
        return webClient.get()
            .uri(LINKS_BASE_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(this::isBadRequest, this::handleBadRequest)
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse trackLink(Integer id, @NotNull String link) {
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(link));

        return webClient.post()
            .uri(LINKS_BASE_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .bodyValue(addLinkRequest)
            .retrieve()
            .onStatus(this::isBadRequest, this::handleBadRequest)
            .bodyToMono(LinkResponse.class)
            //.flatMap()
            .block();
    }

    public LinkResponse untrackLink(Integer id, @NotNull String link) {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(URI.create(link));

        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_BASE_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .bodyValue(BodyInserters.fromValue(removeLinkRequest))
            .retrieve()
            .onStatus(this::isBadRequest, this::handleBadRequest)
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
