package com.mpds.simulator.domain.model.bins;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.datastructures.PersonNode;
import com.mpds.simulator.domain.model.Person;

public abstract class BottomRightBin extends Bin {

    protected Bin above;
    protected Bin left;
    protected Bin aboveLeft;

    public BottomRightBin(Coordinate ulCorner, Coordinate lrCorner){
        super(ulCorner, lrCorner);
    }

    @Override
    public boolean movePerson(Person currentNode){
        Coordinate pos = currentNode.getPos();
        switch (GridBins.randomGen.nextInt(2)){
            case 0:
                int newRow = pos.getRow() - 1;
                pos.setRow(newRow);
                if (newRow < ulCorner.getRow()){
                    above.addToBeAdded(currentNode);
                    return true;
                }
                break;
            case 1:
                int newCol = pos.getCol() - 1;
                pos.setCol(newCol);
                if (newCol < ulCorner.getCol()){
                    left.addToBeAdded(currentNode);
                    return true;
                }
                break;
        }
        return false;
    }

    public void setAbove(Bin bin){ above = bin; }

    public void setLeft(Bin bin){ left = bin; }

    public void setAboveLeft(Bin bin){aboveLeft = bin;}
}