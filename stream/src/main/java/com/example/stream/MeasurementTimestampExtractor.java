package com.example.stream;

import com.example.common.Measurement;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

public class MeasurementTimestampExtractor implements TimestampExtractor {

    @Override
    public long extract(ConsumerRecord<Object, Object> element, long previousElementTimestamp) {
        return ((Measurement) element.value()).getTimestamp();
    }
}
