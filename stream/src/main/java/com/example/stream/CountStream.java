package com.example.stream;

import com.example.common.serdes.MeasurementSerde;
import com.example.common.serdes.UUIDSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.UUID;

@Component
public class CountStream {
    
    private static String INPUT_TOPIC = "input";
    private static String OUTPUT_TOPIC = "output";
    private final Properties properties;
    private KafkaStreams streams;
    
    public CountStream(@Qualifier("stream-properties") Properties properties) {
        this.properties = properties;
    }
    
    @PostConstruct
    public void runStream() {
        final StreamsBuilder builder = new StreamsBuilder();
        KTable<UUID, Long> countByKey = builder.stream(INPUT_TOPIC, Consumed.with(new UUIDSerde(), new MeasurementSerde()))
                .groupByKey()
                .count();
        
        countByKey.toStream().to(OUTPUT_TOPIC, Produced.with(new UUIDSerde(), new Serdes.LongSerde()));
        streams = new KafkaStreams(builder.build(), properties);
        streams.start();
    }
    
    @PreDestroy
    public void onDestroy() {
        streams.close();
    }
}
