package edu.java.scrapper.domain.service.jpa;

import edu.java.scrapper.domain.service.AbstractLinkServiceTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
public class JpaLinkServiceTest extends AbstractLinkServiceTest {
    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }
}
