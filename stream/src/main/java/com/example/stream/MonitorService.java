package com.example.stream;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

import javax.annotation.PostConstruct;

@Service
public class MonitorService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MonitorService.class);

    private SystemInfo si;
    private HardwareAbstractionLayer hal;

    @PostConstruct
    public void init() {
        si = new SystemInfo();
        hal = si.getHardware();
    }

    @Scheduled(fixedDelay = 100L)
    public void periodicReport() {
        LOGGER.info(String.format("%d,%s,%d", System.currentTimeMillis(), "mem", (hal.getMemory().getTotal() - hal.getMemory().getAvailable())));
    }

}
