package com.cryptocat.web4;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    public Map<String, Object> producerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        return props;
    }

//    @Bean
//    public NewTopic build() { return TopicBuilder.name("test_topic").build(); }

    @Bean
    public ProducerFactory<String, Block> producerFactory() {
        return new DefaultKafkaProducerFactory<>(
                producerConfig(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public ProducerFactory<String, Block> producerFactory_2() {
        return new DefaultKafkaProducerFactory<>(
                producerConfig(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("test_topic")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name("test_topic_2")
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaTemplate<String, Block> kafkaTemplate(ProducerFactory<String, Block> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaTemplate<String, Block> kafkaTemplate2(ProducerFactory<String, Block> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

}
