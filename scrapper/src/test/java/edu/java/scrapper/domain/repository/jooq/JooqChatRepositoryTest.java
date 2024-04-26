package edu.java.scrapper.domain.repository.jooq;

import edu.java.domain.model.jooq.tables.pojos.Chats;
import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
@Testcontainers
public class JooqChatRepositoryTest extends IntegrationTest {

    @DynamicPropertySource
    static void jooqProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Autowired JooqChatRepository jooqChatRepository;

    private final static List<Chats> expected = new ArrayList<>() {{
        add(new Chats(1L));
        add(new Chats(2L));
        add(new Chats(33L));
        add(new Chats(1111111111L));
    }};

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyGetConvertedTableContent() {
        assertThat(jooqChatRepository.findAll()).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyAddContentToTable() {
        Chats createdChat = new Chats(22L);
        jooqChatRepository.add(createdChat);
        assertThat(jooqChatRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            add(createdChat);
        }});

        assertThatThrownBy(() -> jooqChatRepository.add(createdChat)).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyRemoveContentFromTable() {
        Chats removingChat = new Chats(2L);
        jooqChatRepository.remove(removingChat);
        assertThat(jooqChatRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            remove(removingChat);
        }});

        jooqChatRepository.remove(removingChat);
        assertThat(jooqChatRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyFindContentFromTable() {
        assertThat(jooqChatRepository.getById(1L)).isEqualTo(Optional.of(new Chats(1L)));
        assertThat(jooqChatRepository.getById(23L)).isEqualTo(Optional.empty());
    }
}
