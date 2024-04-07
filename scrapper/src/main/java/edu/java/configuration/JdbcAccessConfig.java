package edu.java.configuration;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcTrackingRepository;
import edu.java.domain.service.LinkService;
import edu.java.domain.service.TgChatService;
import edu.java.domain.service.jdbc.JdbcLinkService;
import edu.java.domain.service.jdbc.JdbcTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcAccessConfig {
    @Bean
    public LinkService linkService(
        JdbcLinkRepository linkRepository,
        JdbcChatRepository tgChatRepository,
        JdbcTrackingRepository trackingRepository
    ) {
        return new JdbcLinkService(linkRepository, trackingRepository, tgChatRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JdbcChatRepository tgChatRepository,
        JdbcTrackingRepository trackingRepository
    ) {
        return new JdbcTgChatService(tgChatRepository, trackingRepository);
    }
}
