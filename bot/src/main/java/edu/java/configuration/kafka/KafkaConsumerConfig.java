package edu.java.configuration.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.requests.LinkUpdateRequest;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest>
    kafkaListenerContainerFactory(
        ConsumerFactory<Long, LinkUpdateRequest> consumerFactory,
        ) {
        ConcurrentKafkaListenerContainerFactory<Long, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<Long, LinkUpdateRequest> consumerFactory(ApplicationConfig config) {
        return new DefaultKafkaConsumerFactory<>(consumerProps(config.kafkaConsumer()));
    }

    private Map<String, Object> consumerProps(ApplicationConfig.KafkaConsumerConfig consumerConfig) {
        return Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerConfig.bootstrapServers(),
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
            ConsumerConfig.GROUP_ID_CONFIG, consumerConfig.groupId(),
            JsonDeserializer.TRUSTED_PACKAGES, "edu.java.models.dto.LinkUpdateRequest",
            JsonDeserializer.VALUE_DEFAULT_TYPE, LinkUpdateRequest.class
        );
    }
}

