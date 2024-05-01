package edu.java.scrapper.domain.repository.jooq;

import edu.java.domain.model.jooq.tables.pojos.Links;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.IntegrationTest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
public class JooqLinkRepositoryTest extends IntegrationTest {

    @DynamicPropertySource
    static void jooqProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }

    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    private final static List<Links> expected = new ArrayList<>() {{
        add(new Links(1L, "https://github.com/sslanss/java-course-2023-2sem",
            OffsetDateTime.parse("2024-02-20T00:00Z")
        ));
        add(new Links(2L, "https://stackoverflow.com/questions/78226097/problems-in-validations-via-" +
            "web-service-in-a-vue-3-application", OffsetDateTime.parse("2024-03-26T00:00Z"))
        );
    }};

    private static final String TEST_URI = "https://github.com/test-user/test-repo";

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyGetConvertedTableContent() {
        assertThat(jooqLinkRepository.findAll()).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyAddContentToTable() {
        Links createdLink = new Links();
        createdLink.setUrl(TEST_URI);
        createdLink.setLastCheckedAt(OffsetDateTime.parse(
            "2024-03-26T00:00Z",
            DateTimeFormatter.ISO_OFFSET_DATE_TIME
        ));
        Long id = jooqLinkRepository.add(createdLink);
        createdLink.setLinkId(id);

        assertThat(jooqLinkRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            add(createdLink);
        }});
        assertThatThrownBy(() -> jooqLinkRepository.add(createdLink)).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyFindContentFromTable() {
        assertThat(jooqLinkRepository.getByUrl(expected.getFirst().getUrl()))
            .isEqualTo(Optional.of(expected.getFirst()));
        assertThat(jooqLinkRepository.getByUrl(TEST_URI)).isEqualTo(Optional.empty());

        Links findingLink = jooqLinkRepository.getByUrl(expected.getFirst().getUrl()).get();
        assertThat(jooqLinkRepository.getById(findingLink.getLinkId())).isEqualTo(Optional.of(expected.getFirst()));
        assertThat(jooqLinkRepository.getById(1L)).isEqualTo(Optional.empty());
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyRemoveContentFromTable() {
        Links removingLink = jooqLinkRepository.getByUrl(expected.getFirst().getUrl()).get();
        jooqLinkRepository.remove(removingLink);
        assertThat(jooqLinkRepository.findAll()).containsExactlyElementsOf(new ArrayList<>(expected) {{
            remove(0);
        }});

        jooqLinkRepository.remove(removingLink);
        assertThat(jooqLinkRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyFindLongestUncheckedLinks() {
        List<Links> uncheckedLinks = jooqLinkRepository.findLongestUnchecked(10);

        assertThat(uncheckedLinks).containsExactlyElementsOf(expected);
    }

    @Test
    @Sql("/sql/insert-into-links-table.sql")
    public void repositoryShouldCorrectlyUpdateLastCheckedField() {
        Links link = jooqLinkRepository.getByUrl("https://github.com/sslanss/java-course-2023-2sem")
            .get();
        OffsetDateTime newLastChecked = OffsetDateTime.of(LocalDate.now(), LocalTime.of(0, 0,
            0
        ), ZoneOffset.UTC);
        link.setLastCheckedAt(newLastChecked);

        jooqLinkRepository.updateLastChecked(link);

        assertThat(jooqLinkRepository.findAll().get(1).getLastCheckedAt()).isEqualTo(newLastChecked);
    }
}
