package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.responses.LinkResponse;
import edu.java.responses.ListLinksResponse;
import edu.java.retry.RetryBuilder;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.util.retry.Retry;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThatNoException;

public class ScrapperClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
    protected Retry defaultRetry = new RetryBuilder()
        .withMaxAttempts(3)
        .withMaxInterval(Duration.ofSeconds(2))
        .withStatusCodes(Set.of(500, 502, 503))
        .exponential();
    private final ScrapperClient scrapperClient = new ScrapperClient(
        server.baseUrl(),
        defaultRetry
    );

    public static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";

    @BeforeAll
    public static void beforeAll() {
        server.start();
    }

    @AfterAll
    public static void afterAll() {
        server.shutdown();
    }

    @SneakyThrows
    private String jsonToString(String filePath) {
        return Files.readString(Paths.get(filePath));
    }

    @Test
    public void shouldRegisterChat() {
        server.stubFor(post("/tg-chat/1")
            .willReturn(aResponse()
                .withStatus(200)));

        assertThatNoException().isThrownBy(() -> scrapperClient.registerChat(1L));
        server.verify(postRequestedFor(urlPathEqualTo("/tg-chat/1")));
    }

    @Test
    public void shouldDeleteChat() {
        server.stubFor(delete("/tg-chat/2")
            .willReturn(aResponse()
                .withStatus(200)));

        assertThatNoException().isThrownBy(() -> scrapperClient.deleteChat(2L));
        server.verify(deleteRequestedFor(urlPathEqualTo("/tg-chat/2")));
    }

    @Test
    public void shouldGetLinks() {
        server.stubFor(get("/links")
            .withHeader(TG_CHAT_ID_HEADER, equalTo("1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/json/list_links.json"))
                .withStatus(200)));
        ListLinksResponse expected = new ListLinksResponse(List.of(
            new LinkResponse(
                1L,
                URI.create("https://github.com/1")
            ),
            new LinkResponse(2L, URI.create("https://github.com/2"))
        ), 2);

        Assertions.assertThat(scrapperClient.getLinks(1L)).isEqualTo(expected);
    }

    @Test
    public void shouldTrackLink() {
        server.stubFor(post("/links")
            .withHeader(TG_CHAT_ID_HEADER, equalTo("1"))
            .withRequestBody(equalToJson(jsonToString("src/test/resources/json/link_request.json")))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/json/link_response.json"))
                .withStatus(200)));
        LinkResponse expected = new LinkResponse(1L, URI.create("https://github.com/"));

        Assertions.assertThat(scrapperClient.trackLink(1L, "https://github.com/")).isEqualTo(expected);
    }

    @Test
    public void shouldUntrackLink() {
        server.stubFor(delete("/links")
            .withHeader(TG_CHAT_ID_HEADER, equalTo("1"))
            .withRequestBody(equalToJson(jsonToString("src/test/resources/json/link_request.json")))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/json/link_response.json"))
                .withStatus(200)));
        LinkResponse expected = new LinkResponse(1L, URI.create("https://github.com/"));

        Assertions.assertThat(scrapperClient.untrackLink(1L, "https://github.com/")).isEqualTo(expected);
    }

    @Test
    public void shouldThrowsScrapperApiException() {
        server.stubFor(delete("/links")
            .withHeader(TG_CHAT_ID_HEADER, equalTo("1"))
            .withRequestBody(equalToJson(jsonToString("src/test/resources/json/link_request.json")))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/json/api_error.json"))
                .withStatus(404)));

        Assertions.assertThatThrownBy(() -> scrapperClient.untrackLink(1L, "https://github.com/"))
            .isInstanceOf(ScrapperApiException.class);

    }
}
