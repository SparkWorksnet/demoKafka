package com.example.common.serdes;

import com.example.common.Measurement;
import com.example.common.deserializer.MeasurementDeserializer;
import com.example.common.serializer.MeasurmentSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MeasurementSerde implements Serde<Measurement> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {
    
    }
    
    @Override
    public void close() {
    
    }
    
    @Override
    public Serializer<Measurement> serializer() {
        return new MeasurmentSerializer();
    }
    
    @Override
    public Deserializer<Measurement> deserializer() {
        return new MeasurementDeserializer();
    }
}
