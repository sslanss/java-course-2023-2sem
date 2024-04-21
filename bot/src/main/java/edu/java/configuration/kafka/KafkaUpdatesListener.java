package edu.java.configuration.kafka;

import edu.java.bot.service.BotService;
import edu.java.requests.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaUpdatesListener {
    public BotService botService;

    @KafkaListener(topics = "${app.kafka-topics.updates-topic-name}")
    public void sendUpdates(LinkUpdateRequest linkUpdate) {
        botService.sendUpdate(linkUpdate);
        log.info("Update for link {} was sent to chats", linkUpdate.getUrl());
    }
}
