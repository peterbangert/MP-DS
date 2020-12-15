package com.mpds.simulator.domain.model.deprecated.subbins.regularDeprecated;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.deprecated.subbins.SubBin;

public class BottomLeftNextToOverlapSubBin extends SubBin {

    private SubBin above;
    private SubBin left;
    private SubBin aboveLeft;
    private SubBin aboveRight;
    private SubBin right;

    public BottomLeftNextToOverlapSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    public void setAbove(SubBin subBin){ above = subBin; }

    public void setLeft(SubBin subBin){ left = subBin; }

    public void setAboveLeft(SubBin subBin){aboveLeft = subBin; }

    public void setAboveRight(SubBin subBin){aboveRight = subBin; }

    public void setRight(SubBin bin){
        right = bin;
    }


    @Override
    public void findInteractionsWithNeighbours(Person person) {
        Coordinate pos = person.getPos();

        if (isOverlapAboveLeft(pos)){
            aboveLeft.interactionWithPeople(person);
        }
        if (isOverlapLeft(pos)){
            left.interactionWithPeople(person);
        }
        if (isOverlapRight(pos)){
            right.interactionWithPeople(person);
        }
    }
}
