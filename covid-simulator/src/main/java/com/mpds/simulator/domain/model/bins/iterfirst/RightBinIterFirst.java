package com.mpds.simulator.domain.model.bins.iterfirst;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.RightBin;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;

public class RightBinIterFirst extends RightBin {

    public RightBinIterFirst(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        super(domainEventPublisher, ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(long time, Person person) {
        Coordinate pos = person.getPos();

        if (isOverlapAboveLeft(pos)){
            aboveLeft.interactionWithPeople(time, person);
        }
        if (isOverlapAbove(pos)){
            above.interactionWithPeople(time, person);
        }
        if(isOverlapBelow(pos)){
            below.interactionWithPeople(time, person);
        }
        if(isOverlapBelowLeft(pos)){
            belowLeft.interactionWithPeople(time, person);
        }
    }
}
