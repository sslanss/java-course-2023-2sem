package edu.java.scrapper.domain.repository.jdbc;

import edu.java.domain.model.jdbc.Chat;
import edu.java.domain.model.jdbc.Link;
import edu.java.domain.model.jdbc.Tracking;
import edu.java.domain.repository.jdbc.JdbcTrackingRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

@Transactional
@Rollback
@Testcontainers
@SpringBootTest
public class JdbcTrackingRepositoryTest extends IntegrationTest {
    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Autowired
    private JdbcTrackingRepository jdbcTrackingRepository;

    private final static List<Tracking> expected = new ArrayList<>() {{
        add(new Tracking(1L, 1L));
        add(new Tracking(2L, 2L));
        add(new Tracking(2L, 1L));
    }};

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetLinksById() {
        List<Link> expectedLinks = new ArrayList<>() {{
            add(new Link(1L, URI.create("https://github.com/sslanss/java-course-2023-2sem"),
                OffsetDateTime.of(LocalDate.of(2024, 2, 20), LocalTime.of(0, 0,
                    0
                ), ZoneOffset.UTC)
            ));
            add(new Link(2L, URI.create("https://stackoverflow.com/questions/78226097/problems-in-validations-via-" +
                "web-service-in-a-vue-3-application"),
                OffsetDateTime.of(LocalDate.of(2024, 3, 26), LocalTime.of(0, 0,
                    0
                ), ZoneOffset.UTC)
            ));
        }};

        assertThat(jdbcTrackingRepository.getLinksByChatId(2L)).containsExactlyElementsOf(expectedLinks);
        assertThat(jdbcTrackingRepository.getLinksByChatId(2L).size()).isEqualTo(2);

        assertThat(jdbcTrackingRepository.getLinksByChatId(22L)).isEqualTo(Collections.emptyList());

    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetConvertedTableContent() {
        assertThat(jdbcTrackingRepository.findAll()).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyAddContentToTable() {
        Tracking createdTracking = new Tracking(33L, 2L);
        jdbcTrackingRepository.add(createdTracking);

        assertThat(jdbcTrackingRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            add(createdTracking);
        }});

        assertThatThrownBy(() -> jdbcTrackingRepository.add(createdTracking)).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyRemoveContentFromTable() {
        Tracking removingTracking = expected.getFirst();
        jdbcTrackingRepository.remove(removingTracking);
        assertThat(jdbcTrackingRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            remove(0);
        }});

        jdbcTrackingRepository.remove(removingTracking);
        assertThat(jdbcTrackingRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetChatsById() {
        assertThat(jdbcTrackingRepository.getChatsByLinkId(2L)).isEqualTo(new ArrayList<>() {{
            add(new Chat(2L));
        }});
        assertThat(jdbcTrackingRepository.getChatsByLinkId(23L)).isEqualTo(Collections.emptyList());
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyCheckIfLinkCouldBeDeleted() {
        assertThat(jdbcTrackingRepository.findChatsByDeletedLinkId(3L)).isFalse();
        assertThat(jdbcTrackingRepository.findChatsByDeletedLinkId(2L)).isTrue();

    }

}
