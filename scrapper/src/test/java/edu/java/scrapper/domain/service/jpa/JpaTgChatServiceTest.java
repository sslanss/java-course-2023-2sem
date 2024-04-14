package edu.java.scrapper.domain.service.jpa;

import edu.java.scrapper.domain.service.AbstractTgChatServiceTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

public class JpaTgChatServiceTest extends AbstractTgChatServiceTest {
    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }
}
