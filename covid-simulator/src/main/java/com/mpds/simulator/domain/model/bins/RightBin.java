package com.mpds.simulator.domain.model.bins;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;

public abstract class RightBin extends Bin{
    protected Bin above;
    protected Bin left;
    protected Bin aboveLeft;
    protected Bin below;
    protected Bin belowLeft;

    public RightBin(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        super(domainEventPublisher, ulCorner, lrCorner);
    }

    @Override
    public boolean movePerson(Person currentNode){
        Coordinate pos = currentNode.getPos();
        switch (GridBins.randomGen.nextInt(3)){
            case 0:
                int newRow = pos.getRow() - 1;
                pos.setRow(newRow);
                if (newRow < ulCorner.getRow()){
                    above.addToBeAdded(currentNode);
                    return true;
                }
                break;
            case 1:
                newRow = pos.getRow() + 1;
                pos.setRow(newRow);
                if (newRow > lrCorner.getRow()){
                    below.addToBeAdded(currentNode);
                    return true;
                }
                break;
            case 2:
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

    public void setBelow(Bin bin){
        below = bin;
    }

    public void setBelowLeft(Bin bin){
        belowLeft = bin;
    }
}
