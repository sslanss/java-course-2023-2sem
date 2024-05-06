package edu.java.configuration;

import edu.java.bot.client.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public ScrapperClient scrapperClient(ApplicationConfig config, RetryConfigFactory retryFactory) {
        return new ScrapperClient(config.scrapperClient().baseUrl(),
            retryFactory.createRetry(config.scrapperClient().retry()));
    }
}
