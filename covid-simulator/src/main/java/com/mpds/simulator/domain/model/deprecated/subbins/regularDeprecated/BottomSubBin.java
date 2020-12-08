package com.mpds.simulator.domain.model.deprecated.subbins.regularDeprecated;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.deprecated.subbins.SubBin;

public class BottomSubBin extends SubBin {

    private SubBin right;
    private SubBin left;
    private SubBin above;
    private SubBin aboveLeft;
    private SubBin aboveRight;

    public BottomSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    public void setRight(SubBin bin){
        right = bin;
    }


    @Override
    public void findInteractionsWithNeighbours(Person person) {
        if (isOverlapRight(person.getPos())){
            right.interactionWithPeople(person);
        }
    }

}
