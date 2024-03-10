package edu.java.bot.client;

import edu.java.exceptions.BadRequestException;
import edu.java.responses.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final WebClient client;

    public ScrapperClient(WebClient client) {
        this.client = client;
    }

    private boolean isBadRequest(HttpStatusCode httpStatusCode){
        return httpStatusCode.equals(HttpStatus.BAD_REQUEST);
    }

    public String registerChat(Integer id){
        return client.post()
            .uri("/tg-chat/{id}", id)
            .retrieve()
            .onStatus(this::isBadRequest,
                response -> response.bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new BadRequestException(
                        errorResponse.getCode(),
                        errorResponse.getDescription()
                    ))))
            .bodyToMono(ClientResponse.class)
            .flatMap((response -> {
                if (response.statusCode().equals(HttpStatus.ALREADY_REPORTED)) {
                    return response.bodyToMono(ApiErrorResponse.class)
                        .map(responseBody -> responseBody.getCode() + responseBody.getDescription());
                }
                return response.bodyToMono(String.class).map(responseBody -> "Chat is already registered:");
            }))
            .block();
    }
}
