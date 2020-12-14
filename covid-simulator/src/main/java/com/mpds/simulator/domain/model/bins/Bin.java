package com.mpds.simulator.domain.model.bins;

import com.mpds.simulator.application.runner.CovidSimulatorRunner;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.datastructures.CustomLinkedList;
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

/**
 * <h1>Logical Subunit of Grid</h1>
 * Bins and their neighbouring bins can be considered independent logical units.
 * They iterate through each person in the bin and calculate if it has any interactions with other people in the bin or the neighbours.
 * Interactions include contact and infection events.
 * The person is subsequently moved and possibly moved to a neighbouring bin.
 */

@Data
@Slf4j
public abstract class Bin {

    protected Coordinate ulCorner;
    protected Coordinate lrCorner;
    protected CustomLinkedList people;
    protected CustomLinkedList toBeAdded;

    private final DomainEventPublisher domainEventPublisher;

    /**
     * @param domainEventPublisher The publisher object that holds the connection to kafka
     * @param ulCorner The upper left corner coordinates of the bin
     * @param lrCorner The lower right corner coordinates of the bin
     */
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
        return true;
    }

    public void addPerson(Person pn){
        people.addPerson(pn);
    }

    public void addToBeAdded(Person pn) {//System.out.println("moved bin: " + String.valueOf(pn.getId()));
    toBeAdded.addPerson(pn); }

    public void possibleInfection(long time, Person potentiallyInfected, int distance){
        if(sampleInfection(distance)){
            potentiallyInfected.setInfected(GridBins.infectionTime + 1);
            GridBins.roundInfections++;
            //publishInfection(time, potentiallyInfected.getId());
        }
    }


    /**
     * Publish a PersonContact domain event to Kafka
     * @param time The time within the simulator
     * @param id1 The id of the first person
     * @param id2 The id of the second person
     */
    private void publishContact(long time, int id1, int id2){
        DomainEvent personContactEvent = new PersonContact(time, (long) id1, (long) id2, CovidSimulatorRunner.city, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        this.domainEventPublisher.publishEvent(personContactEvent);
        GridBins.roundContacts++;
    }

    /**
     * Publish a PersonInfected domain event to Kafka
     * @param time The time within the simulator
     * @param id The id of the infected person
     */
    private void publishInfection(long time, int id){
        log.debug("Infected person:" + id);
        DomainEvent domainEvent = new InfectionReported(time, (long) id, CovidSimulatorRunner.city,LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        this.domainEventPublisher.publishEvent(domainEvent);
    }

    /**
     * Publish a PersonHealed domain event to Kafka
     * @param time The time within the simulator
     * @param id The id of the healed person
     */
    private void publishHealed(long time, int id){
        log.debug("Person healed: " + id);
        DomainEvent domainEvent = new PersonHealed(time, (long) id, CovidSimulatorRunner.city, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        this.domainEventPublisher.publishEvent(domainEvent);
        GridBins.roundHealed++;
    }


    public void calcInteractions(long time, Person person1, Person person2){

        int timeOfDay = (int) (time % GridBins.ticksPerDay);
        if (person2.isAwake(timeOfDay)){

        int distance = person1.getPos().distanceTo(person2.getPos());

        if (distance <= GridBins.infectionDistance){
            publishContact(time, person1.getId(), person2.getId());

            if (person1.getInfected() > 0){
                if(person2.getInfected() <= 0 && person1.getInfected() <= GridBins.infectionTime){
                    possibleInfection(time, person2, distance);
                }
            } else {
                if (person2.getInfected() > 0 && person2.getInfected() <= GridBins.infectionTime){
                    possibleInfection(time, person1, distance);
                }
            }
        }
    }}

    public void interactionWithPeople(long time, Person person){
        Person iterNode = people.getStart();
        while (iterNode != null){
            calcInteractions(time, person, iterNode);
            iterNode = iterNode.getNext();
        }
    }


    public abstract void findInteractionsWithNeighbours(long time, Person person);

    public void findInteractions(long time, Person currentPerson){

        Person iterNode = currentPerson.getNext();
        while (iterNode != null){
            calcInteractions(time, currentPerson, iterNode);
            iterNode = iterNode.getNext();
        }

        findInteractionsWithNeighbours(time, currentPerson);
    }

    public abstract boolean movePerson(Person currentNode);

    public void checkHealthStatus(long time, Person person){

        if (person.getInfected() == 1) {
            publishHealed(time, person.getId());
        } else if (person.getInfected() == GridBins.publishInfectionAtTime) {
            publishInfection(time, person.getId());
        }
        person.decrementInfection();
    }


    public Person iterateStart(long time, Person startPerson){

        checkHealthStatus(time, startPerson);


        if (!startPerson.isAwake((int) (time % GridBins.ticksPerDay))){
            return startPerson;
        }


        Person nextPerson = startPerson.getNext();

        findInteractions(time, startPerson);


        while (movePerson(startPerson)){
            people.setStart(nextPerson);
            if (nextPerson == null){
                people.setEnd(null);
                return null;
            }
            startPerson = nextPerson;
            checkHealthStatus(time, startPerson);

            if (!startPerson.isAwake((int) (time % GridBins.ticksPerDay))){
                return startPerson;
            }

            nextPerson = nextPerson.getNext();
            findInteractions(time, startPerson);
        }
        return startPerson;
    }

    public void iterateRest(long time, Person beforePerson, Person middlePerson){

        Person nextPerson = middlePerson.getNext();

        while (nextPerson != null){
            checkHealthStatus(time, middlePerson);

            if (!middlePerson.isAwake((int) (time % GridBins.ticksPerDay))){
                beforePerson = middlePerson;
                middlePerson = nextPerson;
                nextPerson = nextPerson.getNext();
                continue;
            }

            findInteractions(time, middlePerson);

            if (movePerson(middlePerson)){
                beforePerson.setNext(nextPerson);
            } else {
                beforePerson = middlePerson;
            }

            middlePerson = nextPerson;
            nextPerson = nextPerson.getNext();
        }

        checkHealthStatus(time, middlePerson);

        if (!middlePerson.isAwake((int) (time % GridBins.ticksPerDay))){
            return;
        }

        findInteractions(time, middlePerson);
        if(movePerson(middlePerson)){
            beforePerson.setNext(null);
            people.setEnd(null);
        }
    }


    public void iterate(long time){


        Person currentPerson = people.getStart();

        if(currentPerson == null){
            return;
        }

        Person beforePerson = iterateStart(time, currentPerson);

        if (beforePerson == null){
            return;
        }

        currentPerson = beforePerson.getNext();

        if(currentPerson == null){
            return;
        }
        iterateRest(time, beforePerson, currentPerson);
    }

    public void addNewPeople(){
        if(toBeAdded.getStart() == null){
            return;
        }
        people.addPeople(toBeAdded);
    }
}
