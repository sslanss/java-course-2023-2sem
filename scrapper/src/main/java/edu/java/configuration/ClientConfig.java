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
    public GitHubClient gitHubWebClient() {
        return new GitHubClientImpl();
    }

    @Bean
    public StackOverflowClient stackOverflowWebClient() {
        return new StackOverflowClientImpl();
    }

    @Bean
    public BotClient botClient() {
        return new BotClient();
    }
}
