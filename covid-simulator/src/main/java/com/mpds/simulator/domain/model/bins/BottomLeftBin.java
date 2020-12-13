package com.mpds.simulator.domain.model.bins;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;

public abstract class BottomLeftBin extends Bin {
    protected Bin above;
    protected Bin aboveRight;
    protected Bin right;


    public BottomLeftBin(DomainEventPublisher domainEventPublisher, Coordinate ulCorner, Coordinate lrCorner){
        super(domainEventPublisher, ulCorner, lrCorner);
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
                int newCol = pos.getCol() + 1;
                pos.setCol(newCol);
                if (newCol > lrCorner.getCol()){
                    right.addToBeAdded(currentNode);
                    return true;
                }
                break;
        }
        return false;
    }

    public void setAbove(Bin bin){ above = bin; }

    public void setAboveRight(Bin bin){aboveRight = bin;}

    public void setRight(Bin bin){
        right = bin;
    }
}
