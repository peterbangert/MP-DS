package com.mpds.simulator.domain.model.subbins;

import com.mpds.simulator.domain.model.*;

public abstract class SubBin {

    protected Coordinate ulCorner;
    protected Coordinate lrCorner;
    protected Bin parent;

    public SubBin(Coordinate ulCorner, Coordinate lrCorner, Bin parent){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        this.parent = parent;
    }

    public boolean sampleInfection(int distance){
        if(parent.getRandomGen().nextInt(parent.getInfectionDistance() + 5) > distance + 1) {
            return true;
        }
        return false;
    }

    public abstract void interactionWithPeople(Person person);

}
