package edu.java.configuration.kafka;

import edu.java.configuration.ApplicationConfig;
import edu.java.requests.LinkUpdateRequest;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaProducerConfig {
    @Bean
    public NewTopic dlqTopic(ApplicationConfig config) {
        return TopicBuilder.name(config.kafkaTopics().dlqName())
            .partitions(10)
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
        template.setDefaultTopic(config.kafkaTopics().dlqName()); // Установка DLQ в качестве топика по умолчанию
        //template.setMessageConverter(new StringJsonMessageConverter(objectMapper)); // Используйте StringJsonMessageConverter для сохранения JSON в виде строки
        return template;
    }
}

/*@Configuration
@Log4j2
public class KafkaProducerConfig {

    @Bean
    public NewTopic dlqTopic(ApplicationConfig config) {
        return TopicBuilder.name(config.kafkaDlqProducerConfig().dlqTopicName())
            .partitions(1)
            .build();
    }

    @Bean
    public ProducerFactory<Integer, String> producerFactory(ApplicationConfig config) {
        return new DefaultKafkaProducerFactory<>(senderProps(config.kafkaDlqProducerConfig()));
    }

    private Map<String, Object> senderProps(ApplicationConfig.KafkaDlqProducerConfig config) {
        var props = new HashMap<String, Object>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.bootstrapServers());
        props.put(ProducerConfig.LINGER_MS_CONFIG, config.lingerMs());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<Integer, String> dlqKafkaTemplate(
        ProducerFactory<Integer, String> producerFactory,
        ApplicationConfig config
    ) {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(config.kafkaDlqProducerConfig().dlqTopicName());
        return kafkaTemplate;
    }

    @Bean
    public CommonErrorHandler dlqExceptionHandler(KafkaTemplate<Integer, String> dlqKafkaTemplate) {
        return new DefaultErrorHandler((r, e) -> {
            log.error("Kafka listener error: {}", e.getMessage());
            try {
                dlqKafkaTemplate.sendDefault((String) r.value()).get();
            } catch (InterruptedException | ExecutionException ex) {
                log.error("Unable to send message to dlq: {}", ex.getMessage());
            }
        }, new FixedBackOff(0L, 0L));
    }

}*/
