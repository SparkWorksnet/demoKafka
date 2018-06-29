package com.example.stream;

import com.example.common.Measurement;
import com.example.common.serdes.MeasurementSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.Consumed;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class AverageStream {

    private static String DEVICE_DATA_TOPIC = "device-data";
    private static String AVERAGE_5MIN = "5min-average";
    private final Properties properties;
    private KafkaStreams streams;

    public AverageStream(@Qualifier("average-properties") Properties properties) {
        this.properties = properties;
    }
    
    @PostConstruct
    public void runStream() {
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, Measurement> input = builder.stream(DEVICE_DATA_TOPIC, Consumed.with(new Serdes.StringSerde(), new MeasurementSerde()));
        
        input
                .groupByKey()
                .windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5)))
                .reduce(new Reducer<Measurement>() {
                    @Override
                    public Measurement apply(Measurement measurement, Measurement measurement1) {
                        Measurement reduced = new Measurement();
                        reduced.setReading((measurement.getReading() + measurement1.getReading())/2);
                        reduced.setTimestamp((measurement.getTimestamp() < measurement1.getTimestamp()) ? measurement.getTimestamp() : measurement1.getTimestamp());
                        return reduced;
                    }
                })
                .toStream()
                .selectKey((stringWindowed, aDouble) -> stringWindowed.key())
                .mapValues(Measurement::getReading)
                .to(AVERAGE_5MIN, Produced.with(Serdes.String(), Serdes.Double()));

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
