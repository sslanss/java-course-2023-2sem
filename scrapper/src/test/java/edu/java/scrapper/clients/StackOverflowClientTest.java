package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.StackOverflowClientImpl;
import edu.java.responses.StackOverflowResponse;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class StackOverflowClientTest extends AbstractClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

    private final StackOverflowClient stackOverflowClient = new StackOverflowClientImpl(server.baseUrl());

    @BeforeAll
    public static void beforeAll() {
        server.start();
    }

    @AfterAll
    public static void afterAll() {
        server.shutdown();
    }

    @Test
    @SneakyThrows
    public void clientShouldGetGitHubResponses() {
        server.stubFor(get(urlPathMatching("/questions/\\d+/answers"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/stackoverflow.json"))
            )
        );

        List<StackOverflowResponse.StackOverflowAnswerInfo> expected = new ArrayList<>() {{
            add(new StackOverflowResponse.StackOverflowAnswerInfo(OffsetDateTime.parse("2024-03-08T07:36:24Z")));
            add(new StackOverflowResponse.StackOverflowAnswerInfo(OffsetDateTime.parse("2024-03-07T11:06:40Z")));
        }};
        List<StackOverflowResponse.StackOverflowAnswerInfo> response = stackOverflowClient.getQuestionUpdate(
            1L,
            OffsetDateTime.parse("2024-03-07T08:06:40Z"), OffsetDateTime.now()
        ).items();

        Assertions.assertThat(response).isEqualTo(expected);
    }
}
