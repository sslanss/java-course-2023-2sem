package edu.java.configuration;

import edu.java.retry.RetryStrategy;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken,
    @NotNull ClientConfig scrapperClient
) {
    public record RetryConfig(
        int maxAttempts,
        @NotNull Duration maxInterval,
        @NotNull RetryStrategy strategy,
        @NotNull Set<Integer> retryCodes
    ) {
    }

    public record ClientConfig(
        @NotNull String baseUrl,
        @NotNull RetryConfig retry
    ) {
    }
}

