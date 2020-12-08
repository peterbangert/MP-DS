package com.mpds.simulator.domain.model.bins.iterfirst;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.TopRight;

public class TopRightBinIterFirst extends TopRight {

    public TopRightBinIterFirst(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    public void setBelow(Bin bin){
        below = bin;
    }

    public void setBelowLeft(Bin bin){
        belowLeft = bin;
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        Coordinate pos = person.getPos();
        if(isOverlapBelow(pos)){
            below.interactionWithPeople(person);
        }
        if(isOverlapBelowLeft(pos)){
            belowLeft.interactionWithPeople(person);
        }
    }
}
