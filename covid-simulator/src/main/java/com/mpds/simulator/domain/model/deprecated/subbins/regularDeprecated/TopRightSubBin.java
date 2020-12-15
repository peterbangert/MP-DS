package com.mpds.simulator.domain.model.deprecated.subbins.regularDeprecated;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.deprecated.subbins.SubBin;

public class TopRightSubBin extends SubBin {

    private SubBin left;
    private SubBin below;
    private SubBin belowLeft;

    public TopRightSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    public void setBelow(SubBin bin){
        below = bin;
    }

    public void setBelowLeft(SubBin bin){
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
