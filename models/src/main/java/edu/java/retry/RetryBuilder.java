package edu.java.retry;

import edu.java.api_exceptions.ServerErrorException;
import java.time.Duration;
import java.util.Set;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class RetryBuilder {
    private int maxAttempts = 0;
    private Duration maxInterval = Duration.ZERO;
    @SuppressWarnings("MagicNumber")
    private Set<Integer> statusCodes = Set.of(500, 502, 503);

    public RetryBuilder withMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public RetryBuilder withMaxInterval(Duration maxInterval) {
        this.maxInterval = maxInterval;
        return this;
    }

    public RetryBuilder withStatusCodes(Set<Integer> statusCodes) {
        this.statusCodes = statusCodes;
        return this;
    }

    public Retry exponential() {
        return Retry.backoff(maxAttempts, maxInterval)
            .filter(this::isServerErrorStatusCode)
            .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> retrySignal.failure()));
    }

    public Retry constant() {
        return Retry.fixedDelay(maxAttempts, maxInterval)
            .filter(this::isServerErrorStatusCode)
            .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> retrySignal.failure()));
    }

    public Retry linear() {
        return new Retry() {
            @Override
            public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
                return flux.flatMap((retrySignal -> {
                    Throwable throwable = retrySignal.failure();

                    if (isServerErrorStatusCode(throwable)) {
                        long currentAttempt = retrySignal.totalRetries() + 1;
                        if (currentAttempt <= maxAttempts) {
                            return Mono.delay(maxInterval.multipliedBy(currentAttempt))
                                .thenReturn(retrySignal.totalRetries());
                        }
                    }
                    return Mono.error(throwable);
                }));
            }
        };
    }

    private boolean isServerErrorStatusCode(Throwable throwable) {
        if (throwable instanceof ServerErrorException) {
            return statusCodes.contains(((ServerErrorException) throwable).getCode());
        }
        return false;
    }
}
