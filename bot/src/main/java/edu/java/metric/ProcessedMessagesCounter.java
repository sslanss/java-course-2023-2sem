package edu.java.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ProcessedMessagesCounter {

    private final Counter messagesCounter;

    public ProcessedMessagesCounter(MeterRegistry meterRegistry) {
        messagesCounter = meterRegistry.counter("processed_messages_counter");
    }

    public void incrementCounter() {
        messagesCounter.increment();
    }
}
