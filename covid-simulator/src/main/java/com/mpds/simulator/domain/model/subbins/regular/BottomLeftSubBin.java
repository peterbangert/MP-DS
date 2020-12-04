package com.mpds.simulator.domain.model.subbins.regular;

import com.mpds.simulator.domain.model.Bin;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.subbins.SubBin;

public class BottomLeftSubBin extends RegularSubBin{

    private SubBin right;

    public BottomLeftSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    public void setRight(RegularSubBin bin){
        right = bin;
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        Coordinate pos = person.getPos();
        int infectionDistance = parent.getInfectionDistance();
        if ((pos.getCol() - right.ulCorner.getCol()) + infectionDistance >= 0){
            right.interactionWithPeople(person);
        }
    }
}
