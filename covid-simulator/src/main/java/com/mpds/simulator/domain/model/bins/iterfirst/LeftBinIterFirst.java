package com.mpds.simulator.domain.model.bins.iterfirst;


import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.LeftBin;


public class LeftBinIterFirst extends LeftBin {

    public LeftBinIterFirst(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {

        Coordinate pos = person.getPos();

        if (isOverlapRight(pos)){
            right.interactionWithPeople(person);
        }
        if(isOverlapBelowRight(pos)){
            belowRight.interactionWithPeople(person);
        }
        if(isOverlapBelow(pos)){
            below.interactionWithPeople(person);
        }
        if(isOverlapAbove(pos)){
            above.interactionWithPeople(person);
        }
        if(isOverlapAboveRight(pos)){
            aboveRight.interactionWithPeople(person);
        }
    }
}

