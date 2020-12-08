package com.mpds.simulator.domain.model.bins.iterfirst;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.BottomBin;

public class BottomBinIterFirst extends BottomBin {

    public BottomBinIterFirst(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        Coordinate pos = person.getPos();

        if (isOverlapAboveLeft(pos)){
            aboveLeft.interactionWithPeople(person);
        }
        if (isOverlapAbove(pos)){
            above.interactionWithPeople(person);
        }
        if (isOverlapAboveRight(pos)){
            aboveRight.interactionWithPeople(person);
        }
        if (isOverlapRight(pos)) {
            right.interactionWithPeople(person);
        }
    }
}
