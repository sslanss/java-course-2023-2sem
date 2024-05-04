package edu.java.configuration;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.domain.repository.jooq.JooqTrackingRepository;
import edu.java.domain.service.LinkService;
import edu.java.domain.service.TgChatService;
import edu.java.domain.service.jooq.JooqLinkService;
import edu.java.domain.service.jooq.JooqTgChatService;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfig {

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    @Bean
    public LinkService linkService(
        JooqLinkRepository linkRepository,
        JooqChatRepository tgChatRepository,
        JooqTrackingRepository trackingRepository
    ) {
        return new JooqLinkService(linkRepository, trackingRepository, tgChatRepository);
    }

    @Bean
    public TgChatService tgChatService(
        JooqChatRepository tgChatRepository,
        JooqTrackingRepository trackingRepository
    ) {
        return new JooqTgChatService(tgChatRepository, trackingRepository);
    }
}
