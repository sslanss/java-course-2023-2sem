package edu.java.configuration;

import edu.java.clients.BotClient;
import edu.java.clients.github.GitHubClient;
import edu.java.clients.github.GitHubClientImpl;
import edu.java.clients.stackoverflow.StackOverflowClient;
import edu.java.clients.stackoverflow.StackOverflowClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    @Bean
    public GitHubClient gitHubWebClient(ApplicationConfig config, RetryConfigFactory factory) {
        return new GitHubClientImpl(config.githubClient().baseUrl(),
            factory.createRetry(config.githubClient().retry()));
    }

    @Bean
    public StackOverflowClient stackOverflowWebClient(ApplicationConfig config, RetryConfigFactory factory) {
        return new StackOverflowClientImpl(config.stackOverflowClient().baseUrl(),
            factory.createRetry(config.stackOverflowClient().retry()));
    }

    @Bean
    public BotClient botClient(ApplicationConfig config, RetryConfigFactory factory) {
        return new BotClient(config.botClient().baseUrl(),
            factory.createRetry(config.botClient().retry()));
    }
}
