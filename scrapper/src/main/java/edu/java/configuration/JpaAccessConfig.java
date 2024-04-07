package edu.java.configuration;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcTrackingRepository;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.domain.service.LinkService;
import edu.java.domain.service.TgChatService;
import edu.java.domain.service.jdbc.JdbcLinkService;
import edu.java.domain.service.jdbc.JdbcTgChatService;
import edu.java.domain.service.jpa.JpaLinkService;
import edu.java.domain.service.jpa.JpaTgChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(
        JpaLinkRepository linkRepository,
        JpaChatRepository tgChatRepository
    ) {
        return new JpaLinkService(linkRepository, tgChatRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JpaLinkRepository linkRepository,
        JpaChatRepository tgChatRepository
    ) {
        return new JpaTgChatService(linkRepository, tgChatRepository);
    }
}
