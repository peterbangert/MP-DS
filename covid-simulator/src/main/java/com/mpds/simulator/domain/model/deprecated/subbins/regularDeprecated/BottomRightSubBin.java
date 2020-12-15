package com.mpds.simulator.domain.model.deprecated.subbins.regularDeprecated;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.deprecated.Bin;
import com.mpds.simulator.domain.model.deprecated.subbins.SubBin;

public class BottomRightSubBin extends SubBin {

    private SubBin above;
    private SubBin left;
    private SubBin aboveLeft;

    public BottomRightSubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        super(ulCorner, lrCorner, parent);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        return;
    }

}
