package com.mpds.simulator.domain.model.deprecated.subbins.regularDeprecated;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.deprecated.subbins.SubBin;

public class LeftNextToOverlapSubBin extends SubBin {

    private SubBin above;
    private SubBin left;
    private SubBin aboveLeft;
    private SubBin aboveRight;
    private SubBin right;
    private SubBin belowRight;
    private SubBin below;
    private SubBin belowLeft;

    public LeftNextToOverlapSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    public void setAbove(SubBin subBin){ above = subBin; }

    public void setLeft(SubBin subBin){ left = subBin; }

    public void setAboveLeft(SubBin subBin){aboveLeft = subBin;}

    public void setAboveRight(SubBin subBin){aboveRight = subBin;}

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

        if (isOverlapAboveLeft(pos)){
            aboveLeft.interactionWithPeople(person);
        }
        if (isOverlapLeft(pos)){
            left.interactionWithPeople(person);
        }
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
