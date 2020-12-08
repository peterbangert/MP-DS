package com.mpds.simulator.domain.model.bins;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.datastructures.CustomLinkedList;
import com.mpds.simulator.domain.model.datastructures.PersonNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class Bin {

    protected Coordinate ulCorner;
    protected Coordinate lrCorner;
    protected CustomLinkedList people;
    protected CustomLinkedList toBeAdded;

    public Bin(Coordinate ulCorner, Coordinate lrCorner){
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
        return GridBins.randomGen.nextInt(GridBins.infectionDistance + 5) > distance + 1;
    }

    public void addPerson(Person pn){
        people.addPerson(pn);
    }

    public void addToBeAdded(Person pn) {toBeAdded.addPerson(pn); }

    public void possibleInfection(Person potentiallyInfected, int distance){
        if(sampleInfection(distance)){
            potentiallyInfected.setInfected(GridBins.infectionTime + 2);
            publishInfection(potentiallyInfected.getId());
        }
    }


    public static void publishContact(int id1, int id2){
        //System.out.println("contact: " + String.valueOf(id1) + " - " + String.valueOf(id2));
        //DomainEvent personContactEvent = new PersonContact(time, (long) id1, (long) id2, LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        //this.grid.getDomainEventPublisher().sendMessages(personContactEvent).subscribe();
    }

    public static void publishInfection(int id){
        //log.info("infection:" + infectedPerson.getId() + " - " + healthyPerson.getId());
        System.out.println("infection: " + String.valueOf(id));
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

    public void interactionWithPeople(Person person){
        Person iterNode = people.getStart();
        while (iterNode != null){
            calcInteractions(person, iterNode);
            iterNode = iterNode.getNext();
        }
    }


    public abstract void findInteractionsWithNeighbours(Person person);

    public void findInteractions(Person currentPerson){
        //System.out.println("find interactions");
        Person iterNode = currentPerson.getNext();
        while (iterNode != null){
            if(iterNode.getId() == 98){
                //System.out.println();;
            };
            //System.out.println("iternode " + String.valueOf(iterNode.getId()));
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
            //System.out.println("Start changed " + String.valueOf(startPerson.getId()));
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
            //System.out.println("new iteration");
            findInteractions(middlePerson);
            int id_before = middlePerson.getNext().getId();
            if (movePerson(middlePerson)){
                beforePerson.setNext(nextPerson);
                int id_after = -1;
                if (middlePerson.getNext() != null){
                    id_after = middlePerson.getNext().getId();
                }
                //System.out.println("Person " + String.valueOf(middlePerson.getId()) + " next from " + String.valueOf(id_before) + " to " + String.valueOf(id_after) + " before neu " + String.valueOf(beforePerson.getNext().getId()));
            } else {
                beforePerson = middlePerson;
            }
            middlePerson = nextPerson;
            //System.out.println("New middle " + String.valueOf(middlePerson.getId()));
            nextPerson = nextPerson.getNext();
        }
        findInteractions(middlePerson);
        if(movePerson(middlePerson)){
            beforePerson.setNext(null);
            people.setEnd(null);
        }
    }

    public void iterate(){
        System.out.println("iterate");
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
        iterateRest(currentPerson, beforePerson);
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
