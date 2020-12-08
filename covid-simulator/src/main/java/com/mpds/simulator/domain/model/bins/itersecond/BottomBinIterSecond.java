package com.mpds.simulator.domain.model.bins.itersecond;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.BottomBin;

public class BottomBinIterSecond extends BottomBin {

    public BottomBinIterSecond(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        if (isOverlapRight(person.getPos())){
            right.interactionWithPeople(person);
        }
    }
}
