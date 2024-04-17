package edu.java.scrapper.clients;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubClientImpl;
import edu.java.responses.GitHubResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

public class GitHubClientTest extends AbstractClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());

    private final GitHubClient githubClient = new GitHubClientImpl(server.baseUrl(), defaultRetry);

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
    public void shouldGetGitHubResponses() {
        server.stubFor(get(urlPathMatching("/repos/test-owner/test-repository/activity"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(jsonToString("src/test/resources/github.json"))
            )
        );

        List<GitHubResponse> expected = new ArrayList<>() {{
            add(new GitHubResponse("push", OffsetDateTime.of(LocalDate.of(2024, 3, 1),
                LocalTime.of(20, 0, 0), ZoneOffset.UTC
            )));
            add(new GitHubResponse("branch_creation", OffsetDateTime.of(LocalDate.of(2024, 3, 7),
                LocalTime.of(21, 2, 35), ZoneOffset.UTC
            )));
        }};
        List<GitHubResponse> actual = githubClient.getRepositoryUpdate(
            "test-owner",
            "test-repository",
            OffsetDateTime.of(LocalDate.of(2024, 2, 27),
                LocalTime.of(21, 2, 35), ZoneOffset.UTC
            ), OffsetDateTime.now()
        );

        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
