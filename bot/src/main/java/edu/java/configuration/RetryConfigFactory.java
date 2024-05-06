package edu.java.configuration;

import edu.java.retry.RetryBuilder;
import edu.java.retry.RetryStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import reactor.util.retry.Retry;

@Configuration
@EnableConfigurationProperties(ApplicationConfig.class)
public class RetryConfigFactory {
    public Retry createRetry(ApplicationConfig.RetryConfig retryConfig) {
        RetryBuilder builder = new RetryBuilder()
            .withMaxAttempts(retryConfig.maxAttempts())
            .withMaxInterval(retryConfig.maxInterval())
            .withStatusCodes(retryConfig.retryCodes());

        RetryStrategy strategy = retryConfig.strategy();

        switch (strategy) {
            case CONSTANT -> {
                return builder.constant();
            }
            case EXPONENTIAL -> {
                return builder.exponential();
            }
            case LINEAR -> {
                return builder.linear();
            }
            default -> throw new IllegalStateException("Unexpected value: " + strategy);
        }
    }
}
