package com.example.common.deserializer;

import com.example.common.Measurement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class MeasurementDeserializer implements Deserializer<Measurement> {
    @Override
    public void configure(Map map, boolean b) {
    
    }
    
    @Override
    public Measurement deserialize(String s, byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        Measurement measurement = null;
        try {
            measurement = objectMapper.readValue(bytes, Measurement.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return measurement;
    }
    
    @Override
    public void close() {
    
    }
}
