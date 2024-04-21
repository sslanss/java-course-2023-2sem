package edu.java.updates_sender;

import edu.java.requests.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@Slf4j
public class ScrapperQueueProducer implements BotUpdatesSender {
    KafkaTemplate<Long, LinkUpdateRequest> producer;

    @Override
    public void sendLinkUpdate(Long id, URI url, String description, List<Long> tgChatIds) {
        CompletableFuture<SendResult<Long, LinkUpdateRequest>>
            future = producer.sendDefault(new LinkUpdateRequest(id, url, description, tgChatIds));

        BiConsumer<SendResult<Long, LinkUpdateRequest>, Throwable> callback = (result, ex) -> {
            if (ex == null) {
                log.info("Send updates for link {} to queue", url);
            } else {
                log.info("Error with sending updates for link {} to queue", url);
            }
        };

        future.whenComplete(callback);
    }
}
