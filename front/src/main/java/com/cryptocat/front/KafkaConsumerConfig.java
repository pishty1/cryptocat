package com.cryptocat.front;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Autowired
    Storage storage;

    public Map<String, Object> consumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId");
        return props;
    }

    @Bean
    public ConsumerFactory<String, Block> consumerFactory() {
       return new DefaultKafkaConsumerFactory<>(
               consumerConfig(),
               new StringDeserializer(),
               new JsonDeserializer<>(Block.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Block> kafkaListenerContainerFactory(ConsumerFactory<String, Block> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Block> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @KafkaListener(id = "myId", topics = "test_topic", groupId = "groupId")
    void listener(Block data) {
        System.out.printf("DATA RECEIVED hash: %s created: %s id: %s\n", data.minerHash(), data.created(), data.id());
        storage.setLatestBlock(data);
    }
}
