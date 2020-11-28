package com.mpds.simulator.application.runner;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.events.DomainEvent;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

    private final List finalDomainList = new ArrayList();

    private long start;
    private long completionTime;

    @Override
    public void run(String... args) throws Exception {
        log.info("Start simulating COVID19 cases...");
        Coordinate size = new Coordinate(this.gridRows, this.gridColumns);
        Coordinate binSize = new Coordinate(this.binRows, this.binColumns);
        Coordinate overlap = new Coordinate(this.infectionDistance, this.infectionDistance);
        GridBins grid = new GridBins(this.domainEventPublisher, size, binSize, overlap, this.infectionDistance, 30);
        grid.insertPerson(new Person(0, null, 100, size));

        Scheduler newBoundedElastic = Schedulers.newBoundedElastic(100, 500000, "Custom-Elastic-Thread");
        // Simulate 1000 persons
        for (int i = 1; i < this.numberOfPeople; i++) {
            grid.insertPerson(new Person(i, null, 0, size));
        }
        // Run forever to imitate a never ending stream of events
//        int time = 0;

        for (int i = 0; i < 1; i++) {
//        while (true) {

//            System.out.println("Current Sequence: " + SequenceManager.currentSequenceNumber);
            grid.iteration(i);

            List<DomainEvent> toBePublishedEvents = grid.getDomainEventList();

            this.finalDomainList.addAll(toBePublishedEvents);
//            System.out.println("T++");


//            this.domainEventPublisher.publishEvents(Flux.fromIterable(toBePublishedEvents)).blockLast();

//            this.domainEventPublisher.publishEvents(Flux.fromIterable(toBePublishedEvents)).subscribe();

//            time++;
        }
        System.out.println("Running producer performance test using non-reactive API, class=" + this.getClass().getSimpleName()  + " messageSize=" + finalDomainList.size());
        System.out.println("++Events:      " + finalDomainList.size());
        CountDownLatch latch = new CountDownLatch(finalDomainList.size());
        Flux flux = Flux.fromIterable(finalDomainList).parallel().runOn(Schedulers.boundedElastic()).sequential().publishOn(Schedulers.boundedElastic());
        this.start=System.currentTimeMillis();
        Disposable disposable = this.domainEventPublisher.publishEvents(flux, latch).parallel().runOn(Schedulers.boundedElastic()).subscribe();

        latch.await();

        disposable.dispose();

        if(completionTime==0) this.completionTime=System.currentTimeMillis();

        long elasped = completionTime - start;
        double recordsPerSec = 1000.0 * this.finalDomainList.size() / (double) elasped;
        System.out.println("Records/s: "+ recordsPerSec);


//        disposable.dispose();
        System.out.println("DONE!!!");

    }

//    @Override
//    public void run(String... args) throws Exception {
//        this.start=System.currentTimeMillis();
//        log.info("Start simulating COVID19 cases...");
//        Coordinate size = new Coordinate(this.gridRows, this.gridColumns);
//        Coordinate binSize = new Coordinate(this.binRows, this.binColumns);
//        Coordinate overlap = new Coordinate(this.infectionDistance, this.infectionDistance);
//        GridBins grid = new GridBins(this.domainEventPublisher, size, binSize, overlap, this.infectionDistance, 30);
//        grid.insertPerson(new Person(0, null, 100, size));
//        // Simulate 1000 persons
//        for (int i = 1; i < this.numberOfPeople; i++) {
//            grid.insertPerson(new Person(i, null, 0, size));
//        }
//        // Run forever to imitate a never ending stream of events
////        int time = 0;
//
//        for (int i = 0; i < 1; i++) {
////        while (true) {
//
////            System.out.println("Current Sequence: " + SequenceManager.currentSequenceNumber);
//            grid.iteration(i);
//
//            List<DomainEvent> toBePublishedEvents = grid.getDomainEventList();
//
//            this.finalDomainList.addAll(toBePublishedEvents);
////            System.out.println("T++");
//
//
////            this.domainEventPublisher.publishEvents(Flux.fromIterable(toBePublishedEvents)).blockLast();
//
////            this.domainEventPublisher.publishEvents(Flux.fromIterable(toBePublishedEvents)).subscribe();
//
////            time++;
//        }
//        System.out.println("Running producer performance test using non-reactive API, class=" + this.getClass().getSimpleName()  + " messageSize=" + finalDomainList.size());
//        System.out.println("++Events:      " + finalDomainList.size());
//        CountDownLatch latch = new CountDownLatch(finalDomainList.size());
//        Flux flux = Flux.fromIterable(finalDomainList).parallel().runOn(Schedulers.boundedElastic()).sequential().publishOn(Schedulers.boundedElastic());
//
//        Disposable disposable = this.domainEventPublisher.publishEvents(flux, latch).parallel().runOn(Schedulers.boundedElastic()).subscribe();
//
//        latch.await();
//
//        disposable.dispose();
//
//        if(completionTime==0) this.completionTime=System.currentTimeMillis();
//
//        long elasped = completionTime - start;
//        double recordsPerSec = 1000.0 * this.finalDomainList.size() / (double) elasped;
//        System.out.println("Records/s: "+ recordsPerSec);
//
//
////        disposable.dispose();
//        System.out.println("DONE!!!");
//
//    }
}
