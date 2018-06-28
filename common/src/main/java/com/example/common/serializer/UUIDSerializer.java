package com.example.common.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
import java.util.UUID;

public class UUIDSerializer implements Serializer<UUID> {
    @Override
    public void configure(Map map, boolean b) {
    
    }
    
    @Override
    public byte[] serialize(String s, UUID uuid) {
        ObjectMapper objectMapper = new ObjectMapper();
        
        byte[] retVal = null;
    
        try {
            retVal = objectMapper.writeValueAsString(uuid).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    
    @Override
    public void close() {
    
    }
}
