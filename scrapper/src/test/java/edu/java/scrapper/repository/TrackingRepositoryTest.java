package edu.java.scrapper.repository;

import edu.java.domain.model.Chat;
import edu.java.domain.model.Link;
import edu.java.domain.model.Tracking;
import edu.java.domain.repository.TrackingRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@Rollback
@Testcontainers
public class TrackingRepositoryTest extends IntegrationTest {
    @Autowired
    private TrackingRepository trackingRepository;

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

        assertThat(trackingRepository.getLinksByChatId(2L)).isEqualTo(expectedLinks);

        assertThat(trackingRepository.getLinksByChatId(22L)).isEqualTo(Collections.emptyList());

    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetConvertedTableContent() {
        Assertions.assertThat(trackingRepository.findAll()).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyAddContentToTable() {
        Tracking createdTracking = new Tracking(33L, 2L);
        trackingRepository.add(createdTracking);

        Assertions.assertThat(trackingRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            add(createdTracking);
        }});

        assertThatThrownBy(() -> trackingRepository.add(createdTracking)).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyRemoveContentFromTable() {
        Tracking removingTracking = expected.getFirst();
        trackingRepository.remove(removingTracking);
        Assertions.assertThat(trackingRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            remove(0);
        }});

        trackingRepository.remove(removingTracking);
        Assertions.assertThat(trackingRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyGetChatsById() {
        assertThat(trackingRepository.getChatsByLinkId(2L)).isEqualTo(new ArrayList<>() {{
            add(new Chat(2L));
        }});
        assertThat(trackingRepository.getChatsByLinkId(23L)).isEqualTo(Collections.emptyList());
    }

    @Test
    @Sql({"/sql/insert-with-ids-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void repositoryShouldCorrectlyCheckIfLinkCouldBeDeleted() {
        assertThat(trackingRepository.findChatsByDeletedLinkId(3L)).isFalse();
        assertThat(trackingRepository.findChatsByDeletedLinkId(2L)).isTrue();

    }

}
