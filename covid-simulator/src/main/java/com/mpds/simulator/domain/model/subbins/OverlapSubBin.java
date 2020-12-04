package com.mpds.simulator.domain.model.subbins;

import com.mpds.simulator.domain.model.*;
import com.mpds.simulator.domain.model.datastructures.customlist.CustomLinkedList;
import com.mpds.simulator.domain.model.datastructures.customlist.OverlapPersonNode;

public class OverlapSubBin extends SubBin{

    private CustomLinkedList<OverlapPersonNode> people;

    public OverlapSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
        people = new CustomLinkedList<>();
    }


    public void sampleInfection(Person potentiallyInfected, int distance){
        if(parent.getRandomGen().nextInt(101) > distance + 1){
            potentiallyInfected.setInfected(parent.getInfectionTime() + 1);
            parent.publishInfection(potentiallyInfected.getId());
        }
    }

    public void addPerson(OverlapPersonNode pn) {
        people.addNode(pn);
    }


    @Override
    public void interactionWithPeople(Person person) {
        OverlapPersonNode iterNode = people.getStart();
        while (iterNode != null){

            int distance = person.getPos().distanceTo(iterNode.getContent().getOriginalPos());

            if(distance <= parent.getInfectionDistance()){
                parent.publishContact(person.getId(), iterNode.getContent().getPerson().getId());
                if(person.getInfected() > 0 && person.getInfected() <= parent.getInfectionTime() && !iterNode.getContent().getOriginalInfected()){

                    if(sampleInfection(distance)) {
                        parent.publishInfection(iterNode.getContent().getPerson().getId());
                        iterNode.getContent().setNewlyInfected();
                    }

                    parent.publishInfection(iterNode.getContent().getPerson().getId());
                } else if (iterNode.getContent().getOriginalInfected() && person.getInfected() == 0){

                    if(sampleInfection(distance)) {
                        parent.publishInfection(person.getId());
                        person.setInfected(parent.getInfectionTime() + 2);
                    }
                }
            }

            iterNode = iterNode.getNext();
        }
    }
}
