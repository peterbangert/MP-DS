package com.mpds.simulator.domain.model.bins;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.datastructures.CustomLinkedList;
import com.mpds.simulator.domain.model.datastructures.PersonNode;
import com.mpds.simulator.domain.model.events.DomainEvent;
import com.mpds.simulator.domain.model.events.InfectionReported;
import com.mpds.simulator.domain.model.events.PersonContact;
import com.mpds.simulator.domain.model.events.PersonHealed;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.metrics.stats.Count;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;

@Data
@Slf4j
public abstract class Bin {

    private final DomainEventPublisher domainEventPublisher;

    protected Coordinate ulCorner;
    protected Coordinate lrCorner;
    protected CustomLinkedList people;
    protected CustomLinkedList toBeAdded;

    public Bin(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        this.domainEventPublisher=domainEventPublisher;
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;

        people = new CustomLinkedList();
        toBeAdded = new CustomLinkedList();
    }

    public boolean isOverlapAboveLeft(Coordinate pos){
        return pos.distanceTo(ulCorner) < GridBins.infectionDistance - 1;
    }

    public boolean isOverlapAbove(Coordinate pos){
        return (ulCorner.getRow() - pos.getRow()) + GridBins.infectionDistance > 0;
    }

    public boolean isOverlapAboveRight(Coordinate pos){
        return pos.distanceToShiftedColumns(ulCorner, lrCorner.getCol() - ulCorner.getCol()) < GridBins.infectionDistance - 1;
    }

    public boolean isOverlapRight(Coordinate pos){
        return (pos.getCol() - lrCorner.getCol()) + GridBins.infectionDistance > 0;
    }

    public boolean isOverlapBelowRight(Coordinate pos){
        return pos.distanceTo(lrCorner) < GridBins.infectionDistance - 1;
    }

    public boolean isOverlapBelow(Coordinate pos){
        return (pos.getRow() - lrCorner.getRow()) + GridBins.infectionDistance > 0;
    }

    public boolean isOverlapBelowLeft(Coordinate pos){
        return pos.distanceToShiftedColumns(lrCorner, ulCorner.getCol() - lrCorner.getCol()) < GridBins.infectionDistance - 1;
    }

    public boolean isOverlapLeft(Coordinate pos){
        return (ulCorner.getCol() - pos.getCol()) + GridBins.infectionDistance > 0;
    }

    public boolean sampleInfection(int distance){
        return GridBins.randomGen.nextInt(GridBins.infectionDistance*2) > distance + 1;
    }

    public void addPerson(Person pn){
        people.addPerson(pn);
    }

    public void addToBeAdded(Person pn) {//System.out.println("moved bin: " + String.valueOf(pn.getId()));
    toBeAdded.addPerson(pn); }

    public void possibleInfection(Person potentiallyInfected, int distance){
        if(sampleInfection(distance)){
            potentiallyInfected.setInfected(GridBins.infectionTime + 2);
            publishInfection(potentiallyInfected.getId());
        }
    }

    public Mono<Void> possibleInfectionReactive(long time, Person potentiallyInfected, int distance, CountDownLatch latch){
        if(sampleInfection(distance)){
            potentiallyInfected.setInfected(GridBins.infectionTime + 2);
//            publishInfection(potentiallyInfected.getId());
            return publishInfectionReactive(time, potentiallyInfected.getId(), latch);

        }
        return Mono.empty();
    }

    public static void publishContact(int id1, int id2){
        //System.out.println("contact: " + String.valueOf(id1) + " - " + String.valueOf(id2));
        //DomainEvent personContactEvent = new PersonContact(time, (long) id1, (long) id2, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(personContactEvent).subscribe();
    }


    public Mono<Void> publishContactReactive(long time, int id1, int id2, CountDownLatch latch){
        DomainEvent personContactEvent = new PersonContact(time, (long) id1, (long) id2, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        return this.domainEventPublisher.publishEvent(personContactEvent, latch);

        //System.out.println("contact: " + String.valueOf(id1) + " - " + String.valueOf(id2));
        //DomainEvent personContactEvent = new PersonContact(time, (long) id1, (long) id2, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(personContactEvent).subscribe();
    }

    public Mono<Void> publishInfectionReactive(long time, int id, CountDownLatch latch){
        //log.info("infection:" + infectedPerson.getId() + " - " + healthyPerson.getId());
        //System.out.println("infection: " + String.valueOf(id));
        DomainEvent domainEvent = new InfectionReported(time, (long) id, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        return this.domainEventPublisher.publishEvent(domainEvent, latch);
    }

    public Mono<Void> publishHealed(long time, int id, CountDownLatch latch){
        //log.info("Person healed: " + id);
        //System.out.println("healed: " + id);
        DomainEvent domainEvent = new PersonHealed(time, (long) id, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        return this.domainEventPublisher.publishEvent(domainEvent, latch);
        //this.grid.getDomainEventPublisher().sendMessages(domainEvent).subscribe();
    }

    public static void publishInfection(int id){
        //log.info("infection:" + infectedPerson.getId() + " - " + healthyPerson.getId());
        //System.out.println("infection: " + String.valueOf(id));
        //DomainEvent domainEvent = new InfectionReported(time, (long) id, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(domainEvent).subscribe();
    }

    public static void publishHealed(int id){
        //log.info("Person healed: " + id);
        //System.out.println("healed: " + id);
        //DomainEvent domainEvent = new PersonHealed(time, (long) id, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(domainEvent).subscribe();
    }


    public void calcInteractions(Person person1, Person person2){

        int distance = person1.getPos().distanceTo(person2.getPos());

        if (distance <= GridBins.infectionDistance){
            publishContact(person1.getId(), person2.getId());

            if (person1.getInfected() > 0){
                if(person2.getInfected() <= 0 && person1.getInfected() <= GridBins.infectionTime){
                    possibleInfection(person2, distance);
                }
            } else {
                if (person2.getInfected() > 0 && person2.getInfected() <= GridBins.infectionTime){
                    possibleInfection(person1, distance);
                }
            }
        }
    }

    public Mono<Void> calcInteractionsReactive(long time, Person person1, Person person2, CountDownLatch latch){

        int distance = person1.getPos().distanceTo(person2.getPos());

        if (distance <= GridBins.infectionDistance){

            return publishContactReactive(time, person1.getId(), person2.getId(), latch)
                    .flatMap(unused -> {
                        if (person1.getInfected() > 0){
                            if(person2.getInfected() <= 0 && person1.getInfected() <= GridBins.infectionTime){
//                                possibleInfection(person2, distance);
                                return possibleInfectionReactive(time, person2, distance, latch);
                            }
                        } else {
                            if (person2.getInfected() > 0 && person2.getInfected() <= GridBins.infectionTime){
//                                possibleInfection(person1, distance);
                                return possibleInfectionReactive(time, person1, distance, latch);
                            }
                        }
                        return Mono.empty();
                    });

        }
        return Mono.empty();
    }

//    public Mono<Void> calcInteractionsReactive(Person person1, Person person2){
//
//        int distance = person1.getPos().distanceTo(person2.getPos());
//
//        if (distance <= GridBins.infectionDistance){
//            publishContact(person1.getId(), person2.getId());
//
//            if (person1.getInfected() > 0){
//                if(person2.getInfected() <= 0 && person1.getInfected() <= GridBins.infectionTime){
//                    possibleInfection(person2, distance);
//                }
//            } else {
//                if (person2.getInfected() > 0 && person2.getInfected() <= GridBins.infectionTime){
//                    possibleInfection(person1, distance);
//                }
//            }
//        }
//    }

    public void interactionWithPeople(Person person){
        Person iterNode = people.getStart();
        while (iterNode != null){
            calcInteractions(person, iterNode);
            iterNode = iterNode.getNext();
        }
    }

    public Mono<Void> interactionWithPeopleReactive(long time, Person person, CountDownLatch latch){
        Person iterNode = people.getStart();

        Flux.
        return Flux.create(fluxSink -> {
            while (iterNode != null){
                calcInteractionsReactive(time, person, iterNode, latch)
                .flatMap(unused -> {

                });

                iterNode = iterNode.getNext();
            }
//            while(true) {
//                fluxSink.next(System.currentTimeMillis());
//            }
        });
//        while (iterNode != null){
//            calcInteractionsReactive(time, person, iterNode, latch);
//
//            iterNode = iterNode.getNext();
//        }
    }


    public abstract void findInteractionsWithNeighbours(Person person);

    public void findInteractions(Person currentPerson){
        Person iterNode = currentPerson.getNext();
        while (iterNode != null){
            calcInteractions(currentPerson, iterNode);
            iterNode = iterNode.getNext();
        }

        findInteractionsWithNeighbours(currentPerson);
    }

    public abstract boolean movePerson(Person currentNode);


    public Person iterateStart(Person startPerson){
        Person nextPerson = startPerson.getNext();

        findInteractions(startPerson);

        while (movePerson(startPerson)){
            people.setStart(nextPerson);
            if (nextPerson == null ){
                people.setEnd(null);
                return null;
            }
            startPerson = nextPerson;
            nextPerson = nextPerson.getNext();
            findInteractions(startPerson);
        }
        return startPerson;
    }

    public void iterateRest(Person beforePerson, Person middlePerson){

        Person nextPerson = middlePerson.getNext();

        while (nextPerson != null){
            findInteractions(middlePerson);
            int id_before = middlePerson.getNext().getId();
            if (movePerson(middlePerson)){
                beforePerson.setNext(nextPerson);
                int id_after = -1;
                if (middlePerson.getNext() != null){
                    id_after = middlePerson.getNext().getId();
                }
            } else {
                beforePerson = middlePerson;
            }
            middlePerson = nextPerson;
            nextPerson = nextPerson.getNext();
        }
        findInteractions(middlePerson);
        if(movePerson(middlePerson)){
            beforePerson.setNext(null);
            people.setEnd(null);
        }
    }

    public void iterate(){


        Person currentPerson = people.getStart();

        if(currentPerson == null){
            return;
        }

        Person beforePerson = iterateStart(currentPerson);

        if (beforePerson == null){
            return;
        }

        currentPerson = beforePerson.getNext();

        if(currentPerson == null){
            return;
        }
        iterateRest(beforePerson, currentPerson);
    }

    public Flux<Void> iterateReactive(long time){


        Person currentPerson = people.getStart();

        if(currentPerson == null){

            return Flux.empty();
//            return;
        }

        Person beforePerson = iterateStart(currentPerson);

        if (beforePerson == null){
            return;
        }

        currentPerson = beforePerson.getNext();

        if(currentPerson == null){
            return;
        }
        iterateRest(beforePerson, currentPerson);
    }

    /*
    public void iterate(){
        Person currentPerson = people.getStart();
        if(currentPerson != null){

            Person nextNode = currentPerson.getNext();
            findInteractions(currentPerson);

            while (movePerson(currentPerson)){
                people.setStart(nextNode);
                if(nextNode == null){
                    return;
                }
                currentPerson = nextNode;
                nextNode = nextNode.getNext();
            }
            if (nextNode == null){
                return;
            }
        } else {
            return;
        }

        Person beforePerson = currentPerson;
        currentPerson = currentPerson.getNext();
        Person nextPerson = currentPerson.getNext();

        while(nextPerson != null){
            findInteractions(currentPerson);
            if(movePerson(currentPerson)){
                beforePerson.setNext(nextPerson);
            }
            beforePerson = currentPerson;
            currentPerson = nextPerson;
            nextPerson = currentPerson.getNext();
        }
        findInteractions(currentPerson);
        if(movePerson(currentPerson)){
            people.setEnd(beforePerson);
            beforePerson.setNext(null);
        }
    }

     */

    public void addNewPeople(){
        if(toBeAdded.getStart() == null){
            return;
        }
        people.addPeople(toBeAdded);
    }
}
