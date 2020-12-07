package com.mpds.simulator.domain.model;

import com.mpds.simulator.domain.model.events.DomainEvent;
import com.mpds.simulator.domain.model.events.InfectionReported;
import com.mpds.simulator.domain.model.events.PersonContact;
import com.mpds.simulator.domain.model.events.PersonHealed;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

@Data
@Slf4j
public class Bin {

    private Coordinate ulCorner;
    private Coordinate lrCorner;
    private Coordinate overlapCorner;
    private GridBins grid;
    private ArrayList<Person> peopleInBin;
    private ArrayList<Person> peopleInOverlap;
    private ArrayList<Person> toMove;
    private int infectionDistance;
    private int infectionTime;
    private int time;
    private DomainEventPublisher publisher;

    private ArrayList<Person[]> contacts;

    private ArrayList<DomainEvent> domainEventsList;

    public Bin(Coordinate ulCorner, Coordinate lrCorner, Coordinate overlapSize, int infectionDistance, int infectionTime, GridBins grid, DomainEventPublisher publisher){
        this.domainEventsList = new ArrayList<>();
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        overlapCorner = this.lrCorner.addCoordinate(overlapSize);
        this.infectionDistance = infectionDistance;
        this.grid = grid;
        peopleInBin = new ArrayList<>();
        peopleInOverlap = new ArrayList<>();
        this.infectionTime = infectionTime;
        this.publisher = publisher;
    }

    public void calcContactsInfections(Person p1, Person p2){
        int distance = p1.getPos().distanceTo(p2.getPos());
        if(distance <= infectionDistance){
//            System.out.println("contact:" + String.valueOf(p1.id) + " - " + String.valueOf(p2.id));
            DomainEvent personContactEvent = new PersonContact((long) time, (long) p1.getId(), (long) p2.getId(), LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            this.domainEventsList.add(personContactEvent);
//            this.publisher.sendEvent(personContactEvent).subscribe();
            if(p1.getInfected() > 0 && p2.getInfected() == 0){
                checkInfection(p1, p2, distance);
            } else if (p2.getInfected() > 0 && p1.getInfected() == 0){
                checkInfection(p2, p1, distance);
            }
        }
    }

    // Check if the infected person is within distance
    private void checkInfection(Person infectedPerson, Person healthyPerson, int distance) {
        if(healthyPerson.getRandomGen().nextInt(101) > distance + 1){
            healthyPerson.setInfected(infectionTime+1);
//            log.info("infection:" + infectedPerson.getId() + " - " + healthyPerson.getId());
            DomainEvent domainEvent = new InfectionReported((long) time, (long) healthyPerson.getId(), LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            this.domainEventsList.add(domainEvent);
//            this.publisher.sendEvent(domainEvent).subscribe();
        }
    }

    public void contactsInfections(){
        Person p1, p2;
        int distance;
        for(int i=0; i<peopleInBin.size(); i++){
            p1 = peopleInBin.get(i);
            for(int j=i+1; j<peopleInBin.size(); j++){
                p2 = peopleInBin.get(j);
                calcContactsInfections(p1, p2);
            }

            for (Person person : peopleInOverlap) {
                p2 = person;
                calcContactsInfections(p1, p2);
            }
        }
        toMove = peopleInBin;
        peopleInBin = new ArrayList<>();
        peopleInOverlap = new ArrayList<>();
    }

    public void movePeople(){
        Person p;
        for (Person person : toMove) {
            p = person;
            p.move();
            if (p.getInfected() > 0) {
                p.decrementInfection();
                if (p.getInfected() == 0) {
//                    log.info("Person healed: " + p.getId());
//                    System.out.println("healed: " + p.getId());
                    DomainEvent domainEvent = new PersonHealed((long) time, (long) p.getId(), LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
                    this.domainEventsList.add(domainEvent);
//                    this.publisher.sendEvent(domainEvent).subscribe();
                }
            }
            grid.insertPerson(p);
        }
        toMove = null;
    }
}
