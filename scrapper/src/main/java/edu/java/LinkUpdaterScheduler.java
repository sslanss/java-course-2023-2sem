package edu.java;

import edu.java.updater.LinkUpdateChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LinkUpdaterScheduler {

    private final LinkUpdateChecker linkUpdateChecker;

    public LinkUpdaterScheduler(LinkUpdateChecker linkUpdateChecker) {
        this.linkUpdateChecker = linkUpdateChecker;
    }

    @Scheduled(fixedDelayString = "#{T(java.time.Duration).parse('PT'+'${app.scheduler.interval}').toMillis()}")
    public void update() {
        log.info("Update method was invoked");
        linkUpdateChecker.checkUpdates();
    }
}
