package edu.java.bot.client;

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

    public void registerChat(Long id) {
        webClient.post()
            .uri(TG_CHAT_BASE_URL, id)
            .retrieve()
            .onStatus(this::isKnownError, this::handleError)
            .toBodilessEntity()
            .block();
    }

    public void deleteChat(Long id) {
        webClient.delete()
            .uri(TG_CHAT_BASE_URL, id)
            .retrieve()
            .onStatus(this::isKnownError, this::handleError)
            .toBodilessEntity()
            .block();
    }

    public ListLinksResponse getLinks(Long id) {
        return webClient.get()
            .uri(LINKS_BASE_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .retrieve()
            .onStatus(this::isKnownError, this::handleError)
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse trackLink(Long id, @NotNull String link) {
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create(link));

        return webClient.post()
            .uri(LINKS_BASE_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .bodyValue(addLinkRequest)
            .retrieve()
            .onStatus(this::isKnownError, this::handleError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse untrackLink(Long id, @NotNull String link) {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(URI.create(link));

        return webClient.method(HttpMethod.DELETE)
            .uri(LINKS_BASE_URL)
            .header(TG_CHAT_ID_HEADER, String.valueOf(id))
            .bodyValue(removeLinkRequest)
            .retrieve()
            .onStatus(this::isKnownError, this::handleError)
            .bodyToMono(LinkResponse.class)
            .block();
    }

    private boolean isKnownError(HttpStatusCode httpStatusCode) {
        return httpStatusCode.equals(HttpStatus.ALREADY_REPORTED) || httpStatusCode.equals(HttpStatus.NOT_FOUND)
            || httpStatusCode.equals(HttpStatus.BAD_REQUEST)
            || httpStatusCode.equals(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    private Mono<Exception> handleError(ClientResponse response) {
        return response.bodyToMono(ApiErrorResponse.class)
            .flatMap(errorResponse -> Mono.error(new ScrapperApiErrorException(
                errorResponse.getCode(),
                errorResponse.getDescription()
            )));
    }
}
