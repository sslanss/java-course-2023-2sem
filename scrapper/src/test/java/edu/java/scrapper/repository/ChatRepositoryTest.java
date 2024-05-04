package edu.java.scrapper.repository;

import edu.java.domain.model.Chat;
import edu.java.domain.repository.ChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
@Testcontainers
public class ChatRepositoryTest extends IntegrationTest {
    @Autowired
    private ChatRepository chatRepository;

    private final static List<Chat> expected = new ArrayList<>() {{
        add(new Chat(1L));
        add(new Chat(2L));
        add(new Chat(33L));
        add(new Chat(1111111111L));
    }};

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyGetConvertedTableContent() {
        assertThat(chatRepository.findAll()).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyAddContentToTable() {
        Chat createdChat = new Chat(22L);
        chatRepository.add(createdChat);
        assertThat(chatRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            add(createdChat);
        }});

        assertThatThrownBy(() -> chatRepository.add(createdChat)).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyRemoveContentFromTable() {
        Chat removingChat = new Chat(2L);
        chatRepository.remove(removingChat);
        assertThat(chatRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            remove(removingChat);
        }});

        chatRepository.remove(removingChat);
        assertThat(chatRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @Sql("/sql/insert-into-chats-table.sql")
    public void repositoryShouldCorrectlyFindContentFromTable() {
        assertThat(chatRepository.getById(1L)).isEqualTo(Optional.of(new Chat(1L)));
        assertThat(chatRepository.getById(23L)).isEqualTo(Optional.empty());
    }
}

