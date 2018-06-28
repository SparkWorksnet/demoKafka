package com.example.common.serializer;

import com.example.common.Measurement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class MeasurmentSerializer implements Serializer<Measurement> {
    @Override
    public void configure(Map map, boolean b) {
    
    }
    
    @Override
    public byte[] serialize(String s, Measurement measurement) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        byte[] retVal = null;
    
        try {
            retVal = objectMapper.writeValueAsString(measurement).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    
    @Override
    public void close() {
    
    }
}
