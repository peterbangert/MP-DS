package com.mpds.simulator.application.runner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpds.simulator.config.KafkaProducerProps;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.events.DomainEvent;
import com.mpds.simulator.domain.model.events.PersonContact;
import com.mpds.simulator.domain.model.stats.Stats;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisherReactive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
@Slf4j
public class CovidSimulatorRunner implements CommandLineRunner {
    private final KafkaProducerProps kafkaProducerProps;

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

    private final DomainEventPublisherReactive domainEventPublisherReactive;

    private final List finalDomainList = new ArrayList();

    private Stats stats;

    public static long start;
    public static long completionTime;

    public static long listSize;

//    @Override
//    public void run(String... args) throws Exception {
////        sendBytes();
//        log.info("Start simulating COVID19 cases...");
//        Coordinate size = new Coordinate(this.gridRows, this.gridColumns);
//        Coordinate binSize = new Coordinate(this.binRows, this.binColumns);
//        Coordinate overlap = new Coordinate(this.infectionDistance, this.infectionDistance);
//        GridBins grid = new GridBins(this.domainEventPublisherReactive, size, binSize, overlap, this.infectionDistance, 30);
//        grid.insertPerson(new Person(0, null, 100, size));
//
//        Scheduler newBoundedElastic = Schedulers.newBoundedElastic(100, 500000, "Custom-Elastic-Thread");
//        // Simulate 1000 persons
//        for (int i = 1; i < this.numberOfPeople; i++) {
//            grid.insertPerson(new Person(i, null, 0, size));
//        }
//        for (int i = 0; i < 2; i++) {
//            grid.iteration(i);
//
//            List<DomainEvent> toBePublishedEvents = grid.getDomainEventList();
//
//            this.finalDomainList.addAll(toBePublishedEvents);
//        }
//        System.out.println("Running producer performance test using non-reactive API, class=" + this.getClass().getSimpleName() + " messageSize=" + finalDomainList.size());
//        System.out.println("++Events:      " + finalDomainList.size());
//        CountDownLatch latch = new CountDownLatch(finalDomainList.size());
//        Flux flux = Flux.fromIterable(finalDomainList);
//        this.start = System.currentTimeMillis();
//        Disposable disposable = this.domainEventPublisherReactive.publishAsByteEvents(flux, latch).publishOn(Schedulers.parallel()).subscribe();
//
//        latch.await();
//        if (completionTime == 0) this.completionTime = System.currentTimeMillis();
//
//        disposable.dispose();
//
//        long elasped = completionTime - start;
//        double recordsPerSec = 1000.0 * this.finalDomainList.size() / (double) elasped;
//        System.out.println("Records/s: " + recordsPerSec);
//
//
////        disposable.dispose();
//        System.out.println("DONE!!!");
//
//    }

    @Override
    public void run(String... args) throws Exception {
        stats = new Stats();
//        sendBytes();
        log.info("Start simulating COVID19 cases...");
        Coordinate size = new Coordinate(this.gridRows, this.gridColumns);
        Coordinate binSize = new Coordinate(this.binRows, this.binColumns);
        Coordinate overlap = new Coordinate(this.infectionDistance, this.infectionDistance);
        GridBins grid = new GridBins(this.domainEventPublisherReactive, size, binSize, overlap, this.infectionDistance, 30);
        grid.insertPerson(new Person(0, null, 100, size));

        // Simulate 1000 persons
        for (int i = 1; i < this.numberOfPeople; i++) {
            grid.insertPerson(new Person(i, null, 0, size));
        }
        for (int i = 0; i < 2; i++) {
            grid.iteration(i);

            List<DomainEvent> toBePublishedEvents = grid.getDomainEventList();

            this.finalDomainList.addAll(toBePublishedEvents);
        }
        System.out.println("Running producer performance test using non-reactive API, class=" + this.getClass().getSimpleName() + " messageSize=" + finalDomainList.size());
        listSize=finalDomainList.size();
        System.out.println("++Events:      " + listSize);
//        CountDownLatch latch = new CountDownLatch(finalDomainList.size());
        this.start = System.currentTimeMillis();
        finalDomainList.forEach(domainEvent -> {
            this.domainEventPublisher.publishAsByteEvents((DomainEvent) domainEvent, stats);
        });
//        Flux flux = Flux.fromIterable(finalDomainList);
//        this.start = System.currentTimeMillis();
//        Disposable disposable = this.domainEventPublisherReactive.publishAsByteEvents(flux, latch).publishOn(Schedulers.parallel()).subscribe();
//
//        latch.await();

//        if (completionTime == 0)
//            this.completionTime = System.currentTimeMillis();
//
////        disposable.dispose();
//
//        long elasped = completionTime - start;
//        double recordsPerSec = 1000.0 * this.finalDomainList.size() / (double) elasped;
//        System.out.println("Records/s: " + recordsPerSec);
//
//
////        disposable.dispose();
//        System.out.println("DONE!!!");

    }

    public void sendBytes() throws JsonProcessingException {
        DomainEvent domainEvent = new PersonContact(0L, 0L, 1L, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.out.println(objectMapper.writeValueAsString(domainEvent).getBytes().length);
//        byte[] payload = objectMapper.writeValueAsString(domainEvent).getBytes();

        int numRecords = 50000;
        CountDownLatch latch = new CountDownLatch(numRecords);
        int recordSize = 507;
        Random random = new Random(0);
        List<ProducerRecord<byte[], byte[]>> producerRecordList;
//        byte[] payload = new byte[recordSize];

//        for (int i = 0; i < payload.length; i++)
//            payload[i] = (byte) (random.nextInt(26) + 65);
//        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>("covid", payload);
//        ProducerRecord<byte[], byte[]> record = new ProducerRecord<>("covid", payload);

        String BOOTSTRAP_SERVERS = this.kafkaProducerProps.getBootstrapServer();
        String CLIENT_ID_CONFIG = this.kafkaProducerProps.getClientIdConfig();
        String ACK_CONFIG = this.kafkaProducerProps.getAcksConfig();

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ProducerConfig.ACKS_CONFIG, ACK_CONFIG);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        SenderOptions<String, DomainEvent> senderOptions = SenderOptions.create(props);
        KafkaSender<String, DomainEvent> sender = KafkaSender.create(senderOptions);
        ObjectMapper objectMapper = new ObjectMapper();
//        byte[] payload;
        this.start = System.currentTimeMillis();
        Flux sourceFlux = Flux.range(1, numRecords)
                .map(i -> {

                    byte[] payload = new byte[0];
                    try {
                        payload = objectMapper.writeValueAsString(domainEvent).getBytes();
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    ProducerRecord<byte[], byte[]> record = new ProducerRecord<>("covid", payload);
                    return SenderRecord.create(record, i);

//                    ProducerRecord<Byte, Byte> producerRecord = new ProducerRecord<>(kafkaProducerProps.getTopic(), domainEvent.getUuid().toString(), domainEvent);

                }).parallel().runOn(Schedulers.boundedElastic()).sequential().publishOn(Schedulers.boundedElastic());

        Disposable disposable = sender.send(sourceFlux).map(stringSenderResult -> {
            latch.countDown();
            return stringSenderResult;
        }).subscribe();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        disposable.dispose();

        if (completionTime == 0) this.completionTime = System.currentTimeMillis();

        long elasped = completionTime - start;
        double recordsPerSec = 1000.0 * numRecords / (double) elasped;
        System.out.println("Records/s: " + recordsPerSec);

        System.out.println("DONE BYTES++");
    }


    private static Map<String, Object> getProperties(List<String> propValues) {
        Map<String, Object> props = new HashMap<String, Object>();
        if (propValues != null) {
            for (String prop : propValues) {
                String[] pieces = prop.split("=");
                if (pieces.length != 2)
                    throw new IllegalArgumentException("Invalid property: " + prop);
                props.put(pieces[0], pieces[1]);
            }
        }
        return props;
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
