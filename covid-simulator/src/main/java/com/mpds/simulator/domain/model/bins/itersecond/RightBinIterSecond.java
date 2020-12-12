package com.mpds.simulator.domain.model.bins.itersecond;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.RightBin;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;

public class RightBinIterSecond extends RightBin {

    public RightBinIterSecond(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        super(domainEventPublisher, ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(long time, Person person) {
        return;
    }
}
