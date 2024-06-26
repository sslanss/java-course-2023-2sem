package edu.java;

import edu.java.updater.LinkUpdateChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ConditionalOnProperty(value = "app.scheduler.enable", havingValue = "true")
public class LinkUpdaterScheduler {

    private final LinkUpdateChecker linkUpdateChecker;

    public LinkUpdaterScheduler(LinkUpdateChecker linkUpdateChecker) {
        this.linkUpdateChecker = linkUpdateChecker;
    }

    @Scheduled(fixedDelayString = "#{T(java.time.Duration).parse('PT'+'${app.scheduler.interval}').toMillis()}")
    public void update() {
        log.info("Check updates method was invoked");
        linkUpdateChecker.checkUpdates();
    }
}
