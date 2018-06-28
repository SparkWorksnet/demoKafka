package com.example.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)public class Measurement {
    
    private Double reading;
    
    private Long timestamp;
    
    public Measurement() {
    }
    
    public Measurement(Double reading, Long timestamp) {
        this.reading = reading;
        this.timestamp = timestamp;
    }
    
    public Double getReading() {
        return reading;
    }
    
    public void setReading(Double reading) {
        this.reading = reading;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    
    @Override
    public String toString() {
        return "Measurement{" + "reading=" + reading + ", timestamp=" + timestamp + '}';
    }
}
