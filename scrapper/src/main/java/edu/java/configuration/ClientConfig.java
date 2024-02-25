package edu.java.configuration;

import edu.java.clients.GitHubClient;
import edu.java.clients.GitHubClientImpl;
import edu.java.clients.StackOverflowClient;
import edu.java.clients.StackOverflowClientImpl;
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
        //return WebClient.create("https://api.stackexchange.com");
    }
}
