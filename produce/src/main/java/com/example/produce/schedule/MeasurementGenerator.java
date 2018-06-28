package com.example.produce.schedule;

import com.example.common.Measurement;
import com.sun.org.apache.bcel.internal.generic.LOOKUPSWITCH;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.apache.logging.log4j.LogManager.getLogger;

@Component
public class MeasurementGenerator {
    
    private final KafkaProducer<UUID, Measurement> producer;
    private final List<UUID> uuids;
    private Map<UUID, Double> aggregates = new HashMap<>();
    private Map<UUID, Long> counts = new HashMap<>();
    
    private final Logger LOGGER = getLogger(MeasurementGenerator.class);
    
    public MeasurementGenerator(@Qualifier("uuids") List<UUID> uuids, @Qualifier("producer") KafkaProducer<UUID, Measurement> producer) {
        this.producer = producer;
        this.uuids = uuids;
    }
    
    
    @Scheduled(fixedRate = 1000L)
    public void sendMeasurements() {
        for(UUID uuid : uuids) {
            if (new Random().nextBoolean()) {
                ProducerRecord<UUID, Measurement> record = recordGenerator("input", uuid, createMeasurement());
                Double preValue = aggregates.get(uuid);
                Long preCount = counts.get(uuid);
                aggregates.put(uuid, (preValue != null) ? preValue + record.value().getReading() : record.value().getReading());
                counts.put(uuid, (preCount != null) ? (preCount + 1) : 1);
                LOGGER.info("Aggregate Map key -> " + uuid + " :  value -> " + aggregates.get(uuid).toString());
                LOGGER.info("Counts  Map key -> " + uuid + " :  value -> " + counts.get(uuid).toString());
                LOGGER.info("Sending to \"" + record.topic() + "\" -> key : " + record.key() + " - " + record.value());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                producer.send(record);
            }
        }
    }
    
    private ProducerRecord<UUID, Measurement> recordGenerator(String topic, UUID uuid, Measurement measurment) {
        return new ProducerRecord<UUID, Measurement>(topic, uuid, measurment);
    }
    
    
    private Measurement createMeasurement() {
        return new Measurement(new Random().nextDouble(), System.currentTimeMillis());
    }
}
