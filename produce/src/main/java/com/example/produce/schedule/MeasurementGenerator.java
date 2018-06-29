package com.example.produce.schedule;

import com.example.common.Measurement;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class MeasurementGenerator {

    @Value("${data.devices.path}")
    private String devicesPath;

    private static String DEVICE_DATA_TOPIC = "device-data";

    private final KafkaProducer<String, Measurement> producer;

    private final Logger LOGGER = getLogger(MeasurementGenerator.class);

    public MeasurementGenerator(@Qualifier("producer") KafkaProducer<String, Measurement> producer) {
        this.producer = producer;
    }


    @PostConstruct
    public void sendMeasurements() throws Exception {
        LOGGER.info("Preparing to send messages for devices in path {}", devicesPath);

        File dir = new File(devicesPath);
        final ConcurrentMap<String, BufferedReader> files = new ConcurrentHashMap<>();
        if (dir.isDirectory()) {
            for (final File file : dir.listFiles()) {
                LOGGER.info("file:" + file.getName());
                final BufferedReader br = new BufferedReader(new FileReader(file));
                files.put(file.getName(), br);
            }
            LOGGER.info("!!!Files have been opened successfully!!!");
        } else {
            LOGGER.info("!!!{} is not directory!!!", dir.toString());

        }
        do {
            for (final String key : files.keySet()) {
                final BufferedReader bufferedReader = files.get(key);
                final String line = bufferedReader.readLine();
                if (line != null) {
                    final String[] parts = line.split(",");

                    ProducerRecord<String, Measurement> record = recordGenerator(
                            DEVICE_DATA_TOPIC,
                            parts[0],
                            createMeasurement(Long.parseLong(parts[1]),
                                    Double.parseDouble(parts[2])));
                    LOGGER.info("topic: " + record.topic() + ", key: " + record.key() + ", value:" + record.value());
                    producer.send(record);
                } else {
                    files.remove(key);
                }
            }
        } while (!files.isEmpty());
        LOGGER.info("!!!Data have been loaded successfully!!!");
    }

    private ProducerRecord<String, Measurement> recordGenerator(String topic, String uri, Measurement measurment) {
        return new ProducerRecord<String, Measurement>(topic, null, measurment.getTimestamp(), uri, measurment);
    }


    private Measurement createMeasurement(Long timestamp, Double reading) {
        return new Measurement(reading, timestamp);
    }
}
