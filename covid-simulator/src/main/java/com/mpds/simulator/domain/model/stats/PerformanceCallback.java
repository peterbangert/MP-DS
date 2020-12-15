package com.mpds.simulator.domain.model.stats;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

@Slf4j
public class PerformanceCallback implements Callback {

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        Stats.eventsCount++;

        if(Stats.eventsCount == 300000) {
            long currentTime=System.currentTimeMillis();
            long elasped = currentTime - Stats.startTime;
            double eventsPerSec = 1000.0 * Stats.eventsCount / (double) elasped;
            log.info("Number of events published: " + Stats.eventsCount);
            log.info("Throughput for the first 300000 published events: " + eventsPerSec);
        }
    }
}
