package com.example.common.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;
import java.util.UUID;

public class UUIDDeserializer implements Deserializer<UUID> {
    @Override
    public void configure(Map map, boolean b) {
    
    }
    
    @Override
    public UUID deserialize(String s, byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        UUID uuid = null;
        try {
            uuid = objectMapper.readValue(bytes, UUID.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uuid;
    }
    
    @Override
    public void close() {
    
    }
}
