package edu.java.scrapper.domain.service.jpa;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Transactional
@Rollback
@Testcontainers
public class JpaLinkServiceTest {
}
