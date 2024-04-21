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
    @NotNull
    AccessType databaseAccessType,
    @NotNull boolean useQueue,
    @NotNull ClientConfig githubClient,
    @NotNull ClientConfig stackOverflowClient,
    @NotNull ClientConfig botClient,
    @NotNull KafkaProducerConfig kafkaProducer

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

    public record KafkaProducerConfig(
        @NotNull String bootstrapServers,
        @NotNull String acks,
        @NotNull String topicName
    ) {
    }
}
