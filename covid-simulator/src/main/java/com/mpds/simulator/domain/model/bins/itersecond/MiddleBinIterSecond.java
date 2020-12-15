package com.mpds.simulator.domain.model.bins.itersecond;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.MiddleBin;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;

public class MiddleBinIterSecond extends MiddleBin {

    public MiddleBinIterSecond(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        super(domainEventPublisher, ulCorner, lrCorner);
    }

    public void setAbove(Bin bin){ above = bin; }

    public void setLeft(Bin bin){ left = bin; }

    public void setAboveLeft(Bin bin){aboveLeft = bin;}

    public void setAboveRight(Bin bin){aboveRight = bin;}

    public void setRight(Bin bin){
        right = bin;
    }

    public void setBelowRight(Bin bin){
        belowRight = bin;
    }

    public void setBelow(Bin bin){
        below = bin;
    }

    public void setBelowLeft(Bin bin){
        belowLeft = bin;
    }

    @Override
    public void findInteractionsWithNeighbours(long time, Person person) {
        if (isOverlapRight(person.getPos())){
            right.interactionWithPeople(time, person);
        }
    }
}
