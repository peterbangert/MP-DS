package com.mpds.simulator.domain.model.stats;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class PerformanceCallback implements Callback {
    private final Stats stats;

    public PerformanceCallback(Stats stats){
        this.stats=stats;
    }
    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        this.stats.increment();
    }
}
