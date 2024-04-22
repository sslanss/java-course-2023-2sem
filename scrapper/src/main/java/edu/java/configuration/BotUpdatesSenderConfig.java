package edu.java.configuration;

import edu.java.requests.LinkUpdateRequest;
import edu.java.updates_sender.BotHttpClient;
import edu.java.updates_sender.BotUpdatesSender;
import edu.java.updates_sender.ScrapperQueueProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class BotUpdatesSenderConfig {
    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public BotUpdatesSender scrapperQueueProducer(KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate) {
        return new ScrapperQueueProducer(kafkaTemplate);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public BotUpdatesSender botClient(ApplicationConfig config, RetryConfigFactory factory) {
        return new BotHttpClient(
            config.botClient().baseUrl(),
            factory.createRetry(config.botClient().retry())
        );
    }
}
