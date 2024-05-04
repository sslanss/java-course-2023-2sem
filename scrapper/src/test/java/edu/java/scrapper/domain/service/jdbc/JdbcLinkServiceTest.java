package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.service.AbstractLinkServiceTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class JdbcLinkServiceTest extends AbstractLinkServiceTest {
    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jdbc");
    }
}
