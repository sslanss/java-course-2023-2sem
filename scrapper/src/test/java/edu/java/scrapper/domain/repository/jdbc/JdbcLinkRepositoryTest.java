package edu.java.scrapper.domain.repository.jdbc;

import edu.java.domain.model.jdbc.Link;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

@Transactional
@Rollback
@Testcontainers
@SpringBootTest
public class JdbcLinkRepositoryTest extends IntegrationTest {
    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    private final static List<Link> expected = new ArrayList<>() {{
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

    private static final String TEST_URI = "https://github.com/test-user/test-repo";

    @Test
    @Transactional
    @Rollback
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyGetConvertedTableContent() {
        assertThat(jdbcLinkRepository.findAll()).containsExactlyElementsOf(expected);
    }

    @Test
    @Transactional
    @Rollback
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyAddContentToTable() {
        Link createdLink = new Link();
        createdLink.setUrl(URI.create(TEST_URI));
        createdLink.setLastChecked(OffsetDateTime.of(LocalDate.now(), LocalTime.of(0, 0,
            0
        ), ZoneOffset.UTC));
        Long id = jdbcLinkRepository.add(createdLink);
        createdLink.setLinkId(id);

        assertThat(jdbcLinkRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            add(createdLink);
        }});
        assertThatThrownBy(() -> jdbcLinkRepository.add(createdLink)).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    @Transactional
    @Rollback
    public void repositoryShouldCorrectlyFindContentFromTable() {
        assertThat(jdbcLinkRepository.getByUrl(expected.getFirst().getUrl()))
            .isEqualTo(Optional.of(expected.getFirst()));
        assertThat(jdbcLinkRepository.getByUrl(URI.create(TEST_URI))).isEqualTo(Optional.empty());

        Link findingLink = jdbcLinkRepository.getByUrl(expected.getFirst().getUrl()).get();
        assertThat(jdbcLinkRepository.getById(findingLink.getLinkId())).isEqualTo(Optional.of(expected.getFirst()));
        assertThat(jdbcLinkRepository.getById(1L)).isEqualTo(Optional.empty());
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyRemoveContentFromTable() {
        Link removingLink = jdbcLinkRepository.getByUrl(expected.getFirst().getUrl()).get();
        jdbcLinkRepository.remove(removingLink);
        assertThat(jdbcLinkRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            remove(0);
        }});

        jdbcLinkRepository.remove(removingLink);
        assertThat(jdbcLinkRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyFindLongestUncheckedLinks() {
        List<Link> uncheckedLinks = jdbcLinkRepository.findLongestUnchecked(10);

        assertThat(uncheckedLinks).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyUpdateLastCheckedField() {
        Link link = jdbcLinkRepository.getByUrl(URI.create("https://github.com/sslanss/java-course-2023-2sem"))
            .get();
        OffsetDateTime newLastChecked = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0, 0,
            0
        ), ZoneOffset.UTC);
        link.setLastChecked(newLastChecked);

        jdbcLinkRepository.updateLastChecked(link);

        assertThat(jdbcLinkRepository.findAll().get(1).getLastChecked()).isEqualTo(newLastChecked);
    }

}
