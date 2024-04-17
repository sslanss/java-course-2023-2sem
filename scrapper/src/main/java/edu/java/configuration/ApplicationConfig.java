package edu.java.configuration;

import edu.java.retry.RetryStrategy;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Set;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@EnableScheduling
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    @NotNull ClientConfig githubClient,
    @NotNull ClientConfig stackOverflowClient,
    @NotNull ClientConfig botClient,
    @NotNull
    AccessType databaseAccessType
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

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

    public enum AccessType {
        JDBC, JPA,
        JOOQ
    }
}
