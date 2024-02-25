package edu.java.clients;

import edu.java.responses.StackOverflowResponse;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClientImpl implements StackOverflowClient {
    private final String BASE_URL = "https://api.stackexchange.com";
    private final WebClient webClient;

    public StackOverflowClientImpl() {
        this.webClient = WebClient.builder().baseUrl(BASE_URL).build();
    }

    public StackOverflowClientImpl(String URL) {
        this.webClient = WebClient.builder().baseUrl(URL).build();
    }
    @Override
    public StackOverflowResponse getQuestionUpdate(long questionId) {
        return null;
    }
}
