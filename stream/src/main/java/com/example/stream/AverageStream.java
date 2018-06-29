package com.example.stream;

import com.example.common.Measurement;
import com.example.common.serdes.MeasurementSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
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
                .mapValues(Measurement::getReading)
                .groupByKey()
                .windowedBy(TimeWindows.of(TimeUnit.MINUTES.toMillis(5)))
                .reduce(new Reducer<Double>() {
                    @Override
                    public Double apply(Double measurement, Double measurement1) {
                        return (measurement + measurement1) / 2;

                    }
                })
                .toStream()
                .selectKey((key, value) -> key.key())
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
