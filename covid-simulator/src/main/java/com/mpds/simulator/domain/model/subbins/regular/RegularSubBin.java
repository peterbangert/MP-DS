package com.mpds.simulator.domain.model.subbins.regular;

import com.mpds.simulator.domain.model.*;
import com.mpds.simulator.domain.model.datastructures.customlist.CustomLinkedList;
import com.mpds.simulator.domain.model.datastructures.customlist.PersonNode;
import com.mpds.simulator.domain.model.subbins.SubBin;
import lombok.Data;

@Data
public abstract class RegularSubBin extends SubBin {

    protected CustomLinkedList<PersonNode> people;

    public RegularSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
        people = new CustomLinkedList<>();
    }

    public void addPerson(PersonNode pn){
        people.addNode(pn);
    }

    public void possibleInfection(Person potentiallyInfected, int distance){
        if(sampleInfection(distance)){
            potentiallyInfected.setInfected(parent.getInfectionTime() + 1);
            parent.publishInfection(potentiallyInfected.getId());
        }
    }

    public void calcInteractions(Person person1, Person person2){

        int distance = person1.getPos().distanceTo(person2.getPos());

        if (distance <= parent.getInfectionDistance()){
            parent.publishContact(person1.getId(), person2.getId());
            if(person1.getInfected() > 0 && person2.getInfected() <= 0){
                possibleInfection(person2, distance);
            } else if (person2.getInfected() > 0 && person1.getInfected() <= 0){
                possibleInfection(person1, distance);
            }
        }
    }

    public void interactionWithPeople(Person person){
        PersonNode iterNode = people.getStart();
        while (iterNode != null){
            calcInteractions(person, iterNode.getContent());
            iterNode = iterNode.getNext();
        }
    }


    public abstract void findInteractionsWithNeighbours(Person person);

    public void findInteractions(PersonNode currentNode){
        PersonNode iterNode = currentNode.getNext();
        Person currentPerson = currentNode.getContent();
        while (iterNode != null){
            calcInteractions(currentPerson, iterNode.getContent());
            iterNode = iterNode.getNext();
        }

        findInteractionsWithNeighbours(currentNode.getContent());
    }

    //public abstract void movePerson;

    /*
    public void calcInfections(PersonNode currentNode){
        PersonNode iterNode = currentNode.getNext();
        while(iterNode != null){

        }
    }*/

    public void iterate(){
        PersonNode currentNode = people.getStart();

        while(currentNode != null){
            Person currentPerson = currentNode.getContent();
            findInteractions(currentNode);
        }
    }

}
