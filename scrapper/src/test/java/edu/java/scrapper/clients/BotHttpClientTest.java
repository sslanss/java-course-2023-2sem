package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.api_exceptions.BadRequestException;
import edu.java.updates_sender.BotHttpClient;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class BotHttpClientTest extends AbstractClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

    private final BotHttpClient botHttpClient = new BotHttpClient(server.baseUrl(), defaultRetry);

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
    public void shouldSendLinkUpdates() {
        server.stubFor(post("/updates")
            .withRequestBody(equalToJson(jsonToString("src/test/resources/json/link_update.json")))
            .willReturn(aResponse()));

        assertThatNoException().isThrownBy(() -> botHttpClient.sendLinkUpdate(1L, URI.create("https://github.com/"),
            "update", List.of(1L, 2L, 3L)
        ));
        server.verify(postRequestedFor(urlPathEqualTo("/updates"))
            .withRequestBody(equalToJson(jsonToString("src/test/resources/json/link_update.json"))));
    }

    @Test
    @SneakyThrows
    public void shouldThrowsBadRequestException() {
        server.stubFor(post("/updates")
            .withRequestBody(equalToJson(jsonToString("src/test/resources/json/link_update.json")
                .replace("\"id\": 1", "\"id\": -1")))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/json/api_error.json"))));

        assertThatThrownBy(() -> botHttpClient.sendLinkUpdate(-1L, URI.create("https://github.com/"),
            "update", List.of(1L, 2L, 3L)
        )).isInstanceOf(BadRequestException.class);
    }
}
