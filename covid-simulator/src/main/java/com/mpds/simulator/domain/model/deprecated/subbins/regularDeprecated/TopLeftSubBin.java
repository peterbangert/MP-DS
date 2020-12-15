package com.mpds.simulator.domain.model.deprecated.subbins.regularDeprecated;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.deprecated.subbins.SubBin;

public class TopLeftSubBin extends SubBin {

    private SubBin above;
    private SubBin aboveRight;
    private SubBin right;
    private SubBin belowRight;
    private SubBin below;

    public TopLeftSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
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
    }
}
