package edu.java.bot.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.retry.RetryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import reactor.util.retry.Retry;
import java.time.Duration;
import java.util.Set;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class ScrapperClientTest {
    private static final WireMockServer server = new WireMockServer(wireMockConfig().dynamicPort());
    protected Retry defaultRetry = new RetryBuilder()
        .withMaxAttempts(3)
        .withMaxInterval(Duration.ofSeconds(2))
        .withStatusCodes(Set.of(500, 502, 503))
        .exponential();
    private final ScrapperClient scrapperClient = new ScrapperClient(server.baseUrl(),
        defaultRetry);

    @BeforeAll
    public static void beforeAll() {
        server.start();
    }

    @AfterAll
    public static void afterAll() {
        server.shutdown();
    }



}
