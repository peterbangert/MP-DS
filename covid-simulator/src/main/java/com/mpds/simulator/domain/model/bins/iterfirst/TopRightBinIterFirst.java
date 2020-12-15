package com.mpds.simulator.domain.model.bins.iterfirst;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.TopRight;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;

public class TopRightBinIterFirst extends TopRight {

    public TopRightBinIterFirst(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        super(domainEventPublisher, ulCorner, lrCorner);
    }

    public void setBelow(Bin bin){
        below = bin;
    }

    public void setBelowLeft(Bin bin){
        belowLeft = bin;
    }

    @Override
    public void findInteractionsWithNeighbours(long time, Person person) {
        Coordinate pos = person.getPos();
        if(isOverlapBelow(pos)){
            below.interactionWithPeople(time, person);
        }
        if(isOverlapBelowLeft(pos)){
            belowLeft.interactionWithPeople(time, person);
        }
    }
}
