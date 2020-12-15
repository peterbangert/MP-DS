package com.mpds.simulator.domain.model.deprecated.subbins;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.datastructures.CustomLinkedList;
import com.mpds.simulator.domain.model.datastructures.PersonNode;
import lombok.Data;

@Data
public abstract class SubBin {

    protected Coordinate ulCorner;
    protected Coordinate lrCorner;
    protected CustomLinkedList people;
    Bin parent;

    public SubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        this.parent = parent;

        people = new CustomLinkedList();
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
        return parent.getRandomGen().nextInt(GridBins.infectionDistance + 5) > distance + 1;
    }

    public void addPerson(Person pn){
        people.addPerson(pn);
    }

    public void possibleInfection(Person potentiallyInfected, int distance){
        if(sampleInfection(distance)){
            potentiallyInfected.setInfected(GridBins.infectionTime + 2);
            parent.publishInfection(potentiallyInfected.getId());
        }
    }

    public void calcInteractions(Person person1, Person person2){

        int distance = person1.getPos().distanceTo(person2.getPos());

        if (distance <= GridBins.infectionDistance){
            parent.publishContact(person1.getId(), person2.getId());

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

    public void findInteractions(Person currentNode){
        Person iterNode = currentNode.getNext();
        Person currentPerson = currentNode;
        while (iterNode != null){
            calcInteractions(currentPerson, iterNode);
            iterNode = iterNode.getNext();
        }

        findInteractionsWithNeighbours(currentNode);
    }

    //public abstract void movePerson;


    public void iterate(){
        Person currentNode = people.getStart();

        while(currentNode != null){
            Person currentPerson = currentNode;
            findInteractions(currentNode);
        }
    }
}
