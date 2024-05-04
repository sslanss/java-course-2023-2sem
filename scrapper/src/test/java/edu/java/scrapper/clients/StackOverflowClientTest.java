package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import edu.java.api_exceptions.ServerErrorException;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.StackOverflowClientImpl;
import edu.java.clients.responses.StackOverflowResponse;
import java.time.OffsetDateTime;
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

    private final StackOverflowClient stackOverflowClient = new StackOverflowClientImpl(
        server.baseUrl(),
        defaultRetry
    );
    private static final String TEST_URL = "/questions/\\d+/answers";

    private static final List<StackOverflowResponse.StackOverflowAnswerInfo> expected =
        List.of(
            new StackOverflowResponse.StackOverflowAnswerInfo(OffsetDateTime.parse("2024-03-08T07:36:24Z")),
            new StackOverflowResponse.StackOverflowAnswerInfo(OffsetDateTime.parse("2024-03-07T11:06:40Z"))
        );

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
        server.stubFor(get(urlPathMatching(TEST_URL))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/json/stackoverflow.json"))
            )
        );

        List<StackOverflowResponse.StackOverflowAnswerInfo> actual = stackOverflowClient.getQuestionUpdate(
            1L,
            OffsetDateTime.parse("2024-03-07T08:06:40Z"), OffsetDateTime.now()
        ).items();

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    public void shouldRetryAfterGetServerErrorRequest() {
        server.stubFor(get(urlPathMatching(TEST_URL))
            .inScenario("Retry Scenario")
            .whenScenarioStateIs(Scenario.STARTED)
            .willReturn(aResponse()
                .withStatus(500))
            .willSetStateTo("Retry")
        );
        server.stubFor(get(urlPathMatching(TEST_URL))
            .inScenario("Retry Scenario")
            .whenScenarioStateIs("Retry")
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody(jsonToString("src/test/resources/json/stackoverflow.json")))
        );

        List<StackOverflowResponse.StackOverflowAnswerInfo> actual = stackOverflowClient.getQuestionUpdate(
            1L,
            OffsetDateTime.parse("2024-03-07T08:06:40Z"), OffsetDateTime.now()
        ).items();

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionAfterRetryAttemptsExpired() {
        int retryAttempts = 3;
        for (int i = 0; i < retryAttempts; i++) {
            server.stubFor(get(urlPathMatching(TEST_URL))
                .inScenario("Failed Retry Scenario")
                .whenScenarioStateIs(i == 0 ? Scenario.STARTED : "Retry " + i)
                .willReturn(aResponse()
                    .withStatus(i < retryAttempts - 1 ? 500 : 200)
                )
                .willSetStateTo("Retry " + (i + 1))
            );
        }

        Assertions.assertThatThrownBy(() -> stackOverflowClient.getQuestionUpdate(
            1L,
            OffsetDateTime.parse("2024-03-07T08:06:40Z"), OffsetDateTime.now()
        ).items()).isInstanceOf(ServerErrorException.class);
    }
}
