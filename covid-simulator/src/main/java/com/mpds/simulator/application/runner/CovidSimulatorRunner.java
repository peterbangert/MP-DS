package com.mpds.simulator.application.runner;

import com.mpds.simulator.application.service.SequenceManager;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CovidSimulatorRunner implements CommandLineRunner {

    @Value("${simulator.people}")
    private long numberOfPeople;

    @Value("${simulator.grid-rows}")
    private int gridRows;

    @Value("${simulator.grid-columns}")
    private int gridColumns;

    @Value("${simulator.bin-rows}")
    private int binRows;

    @Value("${simulator.bin-columns}")
    private int binColumns;

    @Value("${simulator.infection.distance}")
    private int infectionDistance;

    private final DomainEventPublisher domainEventPublisher;

    @Override
    public void run(String... args) throws Exception {
        log.info("Start simulating COVID19 cases...");
        Coordinate size = new Coordinate(this.gridRows, this.gridColumns);
        Coordinate binSize = new Coordinate(this.binRows, this.binColumns);
        Coordinate overlap = new Coordinate(this.infectionDistance, this.infectionDistance);
        GridBins grid = new GridBins(this.domainEventPublisher, size, binSize, overlap, this.infectionDistance, 30);
        grid.insertPerson(new Person(0, null, 100, size));
        // Simulate 1000 persons
        for(int i=1; i<this.numberOfPeople; i++){
            grid.insertPerson(new Person(i, null, 0, size));
        }
        // Run forever to imitate a never ending stream of events
        while (true) {
//            System.out.println("Current Sequence: " + SequenceManager.currentSequenceNumber);
            grid.iteration();
            SequenceManager.currentSequenceNumber++;
        }
    }
}
