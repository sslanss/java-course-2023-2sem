package edu.java.scrapper.domain.service;

import edu.java.domain.service.LinkService;
import edu.java.exceptions.tracker_exceptions.AlreadyTrackedLinkException;
import edu.java.exceptions.tracker_exceptions.ChatNotFoundException;
import edu.java.exceptions.tracker_exceptions.UntrackedLinkException;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@Rollback
@Testcontainers
@TestPropertySource(properties = {"bucket4j.enabled=false"})
@TestPropertySource(properties = {"spring.cache.type=none"})
public class AbstractLinkServiceTest extends IntegrationTest {
    @Autowired
    private LinkService linkService;
    @MockBean
    private CacheManager cacheManager;

    private static final String TEST_URI = "https://github.com/test-user/test-repo";

    @Test
    @Sql({"/sql/insert-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void serviceShouldCorrectlyGetTracking() {
        assertThat(linkService.listAllTrackedLinks(2L).getLinks().size()).isEqualTo(2);

        assertThat(linkService.listAllTrackedLinks(33L).getLinks().size()).isEqualTo(0);

        assertThatThrownBy(() -> linkService.listAllTrackedLinks(32L))
            .isInstanceOf(ChatNotFoundException.class);
    }

    @Test
    @Sql({"/sql/insert-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void serviceShouldCorrectlyAddTracking() {
        linkService.add(1L, URI.create(TEST_URI));
        linkService.add(2L, URI.create(TEST_URI));

        assertThatThrownBy(() -> linkService.add(34L, URI.create(TEST_URI)))
            .isInstanceOf(ChatNotFoundException.class);

        assertThatThrownBy(() -> linkService.add(1L, URI.create(TEST_URI)))
            .isInstanceOf(AlreadyTrackedLinkException.class);

    }

    @Test
    @Sql({"/sql/insert-into-links-table.sql",
        "/sql/insert-into-chats-table.sql", "/sql/insert-into-trackings-table.sql"})
    public void serviceShouldCorrectlyRemoveTracking() {
        linkService.remove(1L, URI.create("https://github.com/sslanss/java-course-2023-2sem"));
        linkService.remove(2L, URI.create("https://github.com/sslanss/java-course-2023-2sem"));

        assertThatThrownBy(() -> linkService.remove(34L, URI.create(TEST_URI)))
            .isInstanceOf(ChatNotFoundException.class);

        assertThatThrownBy(() -> linkService.remove(1L, URI.create(TEST_URI)))
            .isInstanceOf(UntrackedLinkException.class);
    }
}
