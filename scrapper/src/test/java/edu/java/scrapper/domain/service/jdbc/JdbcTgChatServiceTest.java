package edu.java.scrapper.domain.service.jdbc;

import edu.java.domain.service.TgChatService;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.ChatReregisteringException;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
@Testcontainers
public class JdbcTgChatServiceTest extends IntegrationTest {
    @Autowired
    private TgChatService tgChatService;

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