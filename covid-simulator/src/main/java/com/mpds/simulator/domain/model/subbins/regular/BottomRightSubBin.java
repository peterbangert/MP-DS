package com.mpds.simulator.domain.model.subbins.regular;

import com.mpds.simulator.domain.model.Bin;
import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;

public class BottomRightSubBin extends RegularSubBin {

    public BottomRightSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        return;
    }

}
