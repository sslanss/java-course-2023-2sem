package edu.java.scrapper.domain.service.jooq;

import edu.java.scrapper.domain.service.AbstractLinkServiceTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
public class JooqTgChatServiceTest extends AbstractLinkServiceTest {
    @DynamicPropertySource
    static void jooqProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jooq");
    }
}
