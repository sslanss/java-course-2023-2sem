package edu.java.scrapper.clients;

import edu.java.retry.RetryBuilder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Set;
import lombok.SneakyThrows;
import org.springframework.test.context.TestPropertySource;
import reactor.util.retry.Retry;

public class AbstractClientTest {
    @SneakyThrows
    protected String jsonToString(String filePath) {
        return Files.readString(Paths.get(filePath));
    }

    protected Retry defaultRetry = new RetryBuilder()
        .withMaxAttempts(1)
        .withMaxInterval(Duration.ofSeconds(2))
        .withStatusCodes(Set.of(500, 502, 503))
        .exponential();
}
