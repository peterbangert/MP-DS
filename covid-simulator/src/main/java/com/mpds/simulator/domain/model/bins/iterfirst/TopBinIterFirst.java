package com.mpds.simulator.domain.model.bins.iterfirst;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.TopBin;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;

public class TopBinIterFirst extends TopBin {

    public TopBinIterFirst(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        super(domainEventPublisher, ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(long time, Person person) {
        Coordinate pos = person.getPos();
        if (isOverlapRight(pos)){
            right.interactionWithPeople(time, person);
        }
        if(isOverlapBelowRight(pos)){
            belowRight.interactionWithPeople(time, person);
        }
        if(isOverlapBelow(pos)){
            below.interactionWithPeople(time, person);
        }
        if(isOverlapBelowLeft(pos)){
            belowLeft.interactionWithPeople(time, person);
        }
    }
}

