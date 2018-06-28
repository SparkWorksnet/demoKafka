package com.example.produce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class ApplicationConfiguration {
    
    @Bean("uuids")
    List<UUID> uuidList() {
        List<UUID> uuids = new ArrayList<>();
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        return uuids;
    }
    
}
