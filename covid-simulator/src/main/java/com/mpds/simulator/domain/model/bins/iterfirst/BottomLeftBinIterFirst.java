package com.mpds.simulator.domain.model.bins.iterfirst;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.BottomLeftBin;

public class BottomLeftBinIterFirst extends BottomLeftBin {

    public BottomLeftBinIterFirst(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        Coordinate pos = person.getPos();

        if (isOverlapAbove(pos)){
            above.interactionWithPeople(person);
        }
        if (isOverlapAboveRight(pos)){
            aboveRight.interactionWithPeople(person);
        }
        if (isOverlapRight(pos)){
            right.interactionWithPeople(person);
        }
    }
}
