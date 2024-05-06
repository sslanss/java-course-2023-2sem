package edu.java.scrapper.domain.repository.jooq;

import edu.java.domain.model.jooq.tables.pojos.Chats;
import edu.java.domain.model.jooq.tables.pojos.Links;
import edu.java.domain.model.jooq.tables.pojos.Trackings;
import edu.java.domain.repository.jooq.JooqTrackingRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Transactional
@Rollback
@Testcontainers
@SpringBootTest
@TestPropertySource(properties = {"bucket4j.enabled=false"})
@TestPropertySource(properties = {"spring.cache.type=none"})
public class JooqTrackingRepositoryTest extends IntegrationTest {
    @DynamicPropertySource
    static void jooqProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Autowired
    private JooqTrackingRepository jooqTrackingRepository;

    private final static List<Trackings> expected = new ArrayList<>() {{
        add(new Trackings(1L, 1L));
        add(new Trackings(2L, 2L));
        add(new Trackings(2L, 1L));
    }};

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetLinksById() {
        List<Links> expectedLinks = new ArrayList<>() {{
            add(new Links(1L, "https://github.com/sslanss/java-course-2023-2sem",
                OffsetDateTime.parse("2024-02-20T00:00Z")
            ));
            add(new Links(2L, "https://stackoverflow.com/questions/78226097/problems-in-validations-via-" +
                "web-service-in-a-vue-3-application",
                OffsetDateTime.parse("2024-03-26T00:00Z")
            ));
        }};

        assertThat(jooqTrackingRepository.getLinksByChatId(2L)).isEqualTo(expectedLinks);

        assertThat(jooqTrackingRepository.getLinksByChatId(22L)).isEqualTo(Collections.emptyList());

    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetConvertedTableContent() {
        Assertions.assertThat(jooqTrackingRepository.findAll()).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyAddContentToTable() {
        Trackings createdTracking = new Trackings(33L, 2L);
        jooqTrackingRepository.add(createdTracking);

        Assertions.assertThat(jooqTrackingRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            add(createdTracking);
        }});

        assertThatThrownBy(() -> jooqTrackingRepository.add(createdTracking)).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyRemoveContentFromTable() {
        Trackings removingTracking = expected.getFirst();
        jooqTrackingRepository.remove(removingTracking);
        Assertions.assertThat(jooqTrackingRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            remove(0);
        }});

        jooqTrackingRepository.remove(removingTracking);
        Assertions.assertThat(jooqTrackingRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetChatsById() {
        assertThat(jooqTrackingRepository.getChatsByLinkId(2L)).isEqualTo(new ArrayList<>() {{
            add(new Chats(2L));
        }});
        assertThat(jooqTrackingRepository.getChatsByLinkId(23L)).isEqualTo(Collections.emptyList());
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyCheckIfLinkCouldBeDeleted() {
        assertThat(jooqTrackingRepository.findChatsByDeletedLinkId(3L)).isFalse();
        assertThat(jooqTrackingRepository.findChatsByDeletedLinkId(2L)).isTrue();
    }

}
