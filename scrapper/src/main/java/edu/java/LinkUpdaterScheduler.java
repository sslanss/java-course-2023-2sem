package edu.java;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{T(java.time.Duration).parse('PT'+'${app.scheduler.interval}').toMillis()}")
    public void update() {
        log.info("Update method was invoked");
    }
}
