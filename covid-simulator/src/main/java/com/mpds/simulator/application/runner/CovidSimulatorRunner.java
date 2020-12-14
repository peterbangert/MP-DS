package com.mpds.simulator.application.runner;

import com.mpds.simulator.config.SimulatorProps;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.stats.Stats;
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

    private final SimulatorProps simulatorProps;

    public static String city;

    @Override
    public void run(String... args) throws Exception {
        city=simulatorProps.getCity();
        log.info("Start simulating COVID19 cases in the city: " + city);

        Coordinate size = new Coordinate(this.simulatorProps.getGridRows(), this.simulatorProps.getGridColumns());
        Coordinate binSize = new Coordinate(this.simulatorProps.getBinRows(), this.simulatorProps.getBinColumns());
        GridBins grid = new GridBins(this.domainEventPublisher, size, binSize, this.simulatorProps.getInfectionDistance(), simulatorProps.getDaysInfected(), simulatorProps.getTicksPerDay(), simulatorProps.getPublishInfectionAfterXDays(), simulatorProps.getMinMilliSecPerRound(), false);

        for(int i=0; i<this.simulatorProps.getInitialInfectedPeople(); i++){
            grid.insertPerson(new Person(i, null, (short) (this.simulatorProps.getDaysInfected() * simulatorProps.getTicksPerDay()), (short) -1, (short) -1));
        }
        for(int i = (int) this.simulatorProps.getInitialInfectedPeople(); i<this.simulatorProps.getNumberOfPeople(); i++){
            grid.insertPerson(new Person(i, null, (short) -1, (short) -1, (short) -1));
        }
        // Run forever to imitate a never ending stream of events
        int time = 0;
        Stats.startTime=System.currentTimeMillis();
        while (true) {
            grid.iteration(time);
            time++;
        }
    }
}
