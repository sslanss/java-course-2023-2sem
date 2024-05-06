package edu.java.configuration.kafka;

import edu.java.configuration.ApplicationConfig;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaProducerConfig {
    @Bean
    public NewTopic dlqTopic(ApplicationConfig config) {
        return TopicBuilder.name(config.kafkaProducer().topicName())
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public ProducerFactory<Long, String> producerFactory(ApplicationConfig config) {
        return new DefaultKafkaProducerFactory<>(producerProps(config.kafkaProducer()));
    }

    private Map<String, Object> producerProps(ApplicationConfig.KafkaProducerConfig producerConfig) {
        return Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerConfig.bootstrapServers(),
            ProducerConfig.ACKS_CONFIG, producerConfig.acks(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );
    }

    @Bean
    public KafkaTemplate<Long, String> kafkaTemplate(
        ProducerFactory<Long, String> producerFactory,
        ApplicationConfig config
    ) {
        KafkaTemplate<Long, String> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic(config.kafkaProducer().topicName());
        return template;
    }

    @Bean
    public DefaultErrorHandler dlqErrorHandler(KafkaTemplate<Long, String> kafkaTemplate) {
        return new DefaultErrorHandler((rec, exception) -> {
            CompletableFuture<SendResult<Long, String>>
                future = kafkaTemplate.sendDefault(String.valueOf(rec)); //не уверена что будет работать

            BiConsumer<SendResult<Long, String>, Throwable> callback = (result, ex) -> {
                if (ex == null) {
                    log.info("Send invalid linkUpdate to dead letter queue");
                } else {
                    log.info("Error with sending invalid linkUpdate to dead letter queue");
                }
            };

            future.whenComplete(callback);
        }, new FixedBackOff(0L, 2L));
    }
}
