package edu.java.scrapper.domain.service;

import edu.java.domain.service.TgChatService;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.ChatReregisteringException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
@Testcontainers
@TestPropertySource(properties = {"bucket4j.enabled=false"})
@TestPropertySource(properties = {"spring.cache.type=none"})
public class AbstractTgChatServiceTest extends IntegrationTest {
    @Autowired
    private TgChatService tgChatService;
    @MockBean
    private CacheManager cacheManager;

    @Test
    public void serviceShouldRegister() {
        tgChatService.register(10L);
        assertThatThrownBy(() -> tgChatService.register(10L)).isInstanceOf(ChatReregisteringException.class);
    }

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void serviceShouldUnregister() {
        tgChatService.unregister(1L);
        assertThatThrownBy(() -> tgChatService.unregister(10L)).isInstanceOf(ChatNotFoundException.class);
    }
}
