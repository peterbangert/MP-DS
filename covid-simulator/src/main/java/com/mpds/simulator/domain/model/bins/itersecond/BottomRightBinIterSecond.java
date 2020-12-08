package com.mpds.simulator.domain.model.bins.itersecond;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.bins.BottomRightBin;

public class BottomRightBinIterSecond extends BottomRightBin {

    public BottomRightBinIterSecond(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    @Override
    public void findInteractionsWithNeighbours(Person person) {
        return;
    }
}
