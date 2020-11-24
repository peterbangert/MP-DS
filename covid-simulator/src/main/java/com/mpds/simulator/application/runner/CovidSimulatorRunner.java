package com.mpds.simulator.application.runner;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.application.service.SequenceManager;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CovidSimulatorRunner implements CommandLineRunner {

    private final DomainEventPublisher domainEventPublisher;

    @Override
    public void run(String... args) throws Exception {
        log.info("Start simulating COVID19 cases...");
//        Coordinate size = new Coordinate(1000, 1000);
        Coordinate size = new Coordinate(80, 80);
        Coordinate binSize = new Coordinate(20, 20);
//        Coordinate binSize = new Coordinate(20, 20); --> Works
//        Coordinate overlap = new Coordinate(6, 6);
        Coordinate overlap = new Coordinate(10, 10);
        GridBins grid = new GridBins(this.domainEventPublisher, size, binSize, overlap, 6, 30);
        grid.insertPerson(new Person(0, null, 100, size));
        // Simulate 12000 persons
        // 1000
        for(int i=1; i<1000; i++){
            grid.insertPerson(new Person(i, null, 0, size));
        }
//        for(int i=0; i<500; i++){
        // Run forever to imitate a never ending stream of events
        while (true) {
//            System.out.println(i);
            System.out.println("Current Sequence: " + SequenceManager.currentSequenceNumber);
            grid.iteration();
            SequenceManager.currentSequenceNumber++;
        }
    }
}
