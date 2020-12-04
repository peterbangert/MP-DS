package com.mpds.simulator.domain.model.subbins.regular;

import com.mpds.simulator.domain.model.Bin;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.subbins.SubBin;

public class TopSubBin extends RegularSubBin{

    private SubBin right;
    private SubBin belowRight;
    private SubBin below;
    private SubBin belowLeft;

    public TopSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    public void setRight(RegularSubBin bin){
        right = bin;
    }

    public void setBelowRight(RegularSubBin bin){
        belowRight = bin;
    }

    public void setBelow(RegularSubBin bin){
        below = bin;
    }

    public void setBelowLeft(RegularSubBin bin){
        belowLeft = bin;
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {

        Coordinate pos = person.getPos();
        int infectionDistance = parent.getInfectionDistance();
        if ((pos.getCol() - right.ulCorner.getCol()) + infectionDistance >= 0){
            right.interactionWithPeople(person);
        }
        if(pos.distanceTo(belowRight.ulCorner) <= infectionDistance){
            belowRight.interactionWithPeople(person);
        }

        if((pos.getRow() - below.ulCorner.getRow()) + infectionDistance >= 0){
            below.interactionWithPeople(person);
        }

        if(pos.distanceToShiftedColumns(belowLeft.ulCorner, belowLeft.lrCorner.getCol() - below.ulCorner.getCol()) <= infectionDistance){
            belowLeft.interactionWithPeople(person);
        }
    }
}
