package com.mpds.simulator.application.scheduler;

import com.mpds.simulator.domain.model.DomainEvent;
import com.mpds.simulator.domain.model.InfectionReported;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor
public class DomainEventScheduler {

    private final DomainEventPublisher domainEventPublisher;

    //Schedule event every 10seconds
    @Scheduled(fixedDelay = 10000)
    public void scheduleEvent(){
        log.info("Start scheduling event....");
        Long personId = new Random().nextLong();
        DomainEvent domainEvent = new InfectionReported(personId, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));

        log.info("Publishing the following event to Kafka: "+ domainEvent.toString());
        domainEventPublisher.sendMessages(domainEvent).subscribe();
    }
}
