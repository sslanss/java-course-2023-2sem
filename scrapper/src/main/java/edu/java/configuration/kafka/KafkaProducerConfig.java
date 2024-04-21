package edu.java.configuration.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.requests.LinkUpdateRequest;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.Map;

public class KafkaProducerConfig {
    @Bean
    public NewTopic updatesTopic(ApplicationConfig config) {
        return TopicBuilder.name(config.kafkaProducer().topicName())
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public ProducerFactory<Long, LinkUpdateRequest> producerFactory(ApplicationConfig config) {
        return new DefaultKafkaProducerFactory<>(producerProps(config.kafkaProducer()));
    }

    private Map<String, Object> producerProps(ApplicationConfig.KafkaProducerConfig producerConfig) {
        return Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerConfig.bootstrapServers(),
            ProducerConfig.ACKS_CONFIG, producerConfig.acks(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    @Bean
    public KafkaTemplate<Long, LinkUpdateRequest> kafkaTemplate(
        ProducerFactory<Long, LinkUpdateRequest> producerFactory,
        ApplicationConfig config
    ) {
        KafkaTemplate<Long, LinkUpdateRequest> template = new KafkaTemplate<>(producerFactory);
        template.setDefaultTopic(config.kafkaProducer().topicName());
        return template;
    }
}
