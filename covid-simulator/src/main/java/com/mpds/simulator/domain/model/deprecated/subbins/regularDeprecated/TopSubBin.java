package com.mpds.simulator.domain.model.deprecated.subbins.regularDeprecated;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.deprecated.subbins.SubBin;

public class TopSubBin extends SubBin {

    private SubBin left;
    private SubBin right;
    private SubBin belowRight;
    private SubBin below;
    private SubBin belowLeft;

    public TopSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    public void setRight(SubBin bin){
        right = bin;
    }

    public void setBelowRight(SubBin bin){
        belowRight = bin;
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

        if (isOverlapRight(pos)){
            right.interactionWithPeople(person);
        }
        if(isOverlapBelowRight(pos)){
            belowRight.interactionWithPeople(person);
        }
        if(isOverlapBelow(pos)){
            below.interactionWithPeople(person);
        }
        if(isOverlapBelowLeft(pos)){
            belowLeft.interactionWithPeople(person);
        }
    }
}
