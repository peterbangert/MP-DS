package com.mpds.simulator.domain.model.bins.itersecond;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.LeftBin;

public class LeftBinIterSecond extends LeftBin {

    public LeftBinIterSecond(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        if (isOverlapRight(person.getPos())){
            right.interactionWithPeople(person);
        }
    }
}
