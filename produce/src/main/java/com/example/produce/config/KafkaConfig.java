package com.example.produce.config;


import com.example.common.Measurement;
import com.example.common.serializer.MeasurmentSerializer;
import com.example.common.serializer.UUIDSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.UUID;

@Configuration
public class KafkaConfig {
    
    @Bean("producer-properties")
    Properties properties () {
        Properties properties = new Properties();
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MeasurmentSerializer.class);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "message-producer");
        return properties;
    }
    @Bean("producer")
    KafkaProducer<UUID, Measurement> kafkaProducer(@Qualifier("producer-properties") Properties properties){
        return new KafkaProducer<UUID, Measurement>(properties);
    }

}
