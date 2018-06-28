package com.example.stream.config;


import com.example.common.serdes.MeasurementSerde;
import com.example.common.serdes.UUIDSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {
    
    @Bean("stream-properties")
    public Properties properties () {
        Properties properties = new Properties();
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, MeasurementSerde.class);
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, "client-id-message-stream");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "application-id-stream");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return properties;
    }
    
    @Bean("aggregate-properties")
    public Properties aggreGateProperties () {
        Properties properties = new Properties();
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.DoubleSerde.class);
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, "client-id-aggregate-stream");
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "application-id-aggregate");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return properties;
    }
    
}
