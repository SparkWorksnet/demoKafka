package com.example.stream;

import com.example.common.Measurement;
import com.example.common.serdes.MeasurementSerde;
import com.example.common.serdes.UUIDSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Initializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class AggregatesStream {
    
    private static String INPUT_TOPIC = "input";
    private static String AGGREGATE_5MIN_STORE = "5min-aggregates-store";
    private static String COUNTS_5MIN = "5min-counts";
    private static String AGGREGATES_5MIN = "5min-aggregates";
    private final Properties properties;
    private KafkaStreams streams;
    
    public AggregatesStream(@Qualifier("aggregate-properties") Properties properties) {
        this.properties = properties;
    }
    
    @PostConstruct
    public void runStream() {
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Measurement> input = builder.stream(INPUT_TOPIC, Consumed.with(new Serdes.StringSerde(), new MeasurementSerde()));
        
        KStream<String, Long> countByKey5min = input
                .groupByKey()
                .windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5)))
                .count()
                .toStream()
                .selectKey((key, value) -> key.key());
    
    
        KStream<String, Double> aggregateByKey5min = input
                .mapValues(Measurement::getReading)
                .groupByKey(Serialized.with(new Serdes.StringSerde(), Serdes.Double()))
                .windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5)))
                .aggregate(
                        new Initializer<Double>() { /* initializer */
                            @Override
                            public Double apply() {
                                return 0.0;
                            }
                        },
                        new Aggregator<String, Double, Double>() {
                            @Override
                            public Double apply(String key, Double value, Double aggregate) {
                                return aggregate + value;
                            }
                        },
                        Materialized.<String, Double, WindowStore<Bytes, byte[]>>as(AGGREGATE_5MIN_STORE)
                ).toStream().selectKey((key, value) -> key.key());
        
        countByKey5min.to(COUNTS_5MIN, Produced.with(new Serdes.StringSerde(), Serdes.Long()));
        aggregateByKey5min.to(AGGREGATES_5MIN, Produced.with(new Serdes.StringSerde(), Serdes.Double()));
        
        Topology topology = builder.build();
        System.out.println(topology.describe());
        streams = new KafkaStreams(topology, properties);
        streams.start();
    }
    
    @PreDestroy
    public void onDestroy() {
        streams.close();
    }
}
