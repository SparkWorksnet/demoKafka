package com.example.common.serdes;

import com.example.common.deserializer.UUIDDeserializer;
import com.example.common.serializer.UUIDSerializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;
import java.util.UUID;

public class UUIDSerde implements Serde<UUID> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {
    
    }
    
    @Override
    public void close() {
    
    }
    
    @Override
    public Serializer<UUID> serializer() {
        return new UUIDSerializer();
    }
    
    @Override
    public Deserializer<UUID> deserializer() {
        return new UUIDDeserializer();
    }
}
