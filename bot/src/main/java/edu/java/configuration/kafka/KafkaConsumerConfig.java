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
/*@Configuration
public class KafkaConsumerConfig2 {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, LinkUpdateRequest> kafkaListenerContainerFactory(
        ConsumerFactory<Integer, LinkUpdateRequest> consumerFactory
        //CommonErrorHandler dlqExceptionHandler
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<Integer, LinkUpdateRequest>();
        factory.setConsumerFactory(consumerFactory);
        //factory.setCommonErrorHandler(dlqExceptionHandler);
        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, LinkUpdateRequest> consumerFactory(ApplicationConfig config) {
        return new DefaultKafkaConsumerFactory<>(consumerProps(config.kafkaConsumerConfig()));
    }

    private Map<String, Object> consumerProps(ApplicationConfig.KafkaConsumerConfig config) {
        var props = new HashMap<String, Object>();

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, LongDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, config.groupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.autoOffsetReset());

        props.put(JsonDeserializer.TRUSTED_PACKAGES, String.join(",", config.trustedPackages()));

        return props;
    }

}*/
