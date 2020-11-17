package com.mpds.simulator;

import java.util.ArrayList;

public class Bin {

    Coordinate ulCorner;
    Coordinate lrCorner;
    Coordinate overlapCorner;
    GridBins grid;
    ArrayList<Person> peopleInBin;
    ArrayList<Person> peopleInOverlap;

    public Bin(Coordinate ulCorner, Coordinate lrCorner, Coordinate overlapSize, GridBins grid){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        overlapCorner = this.lrCorner.addCoordinate(overlapSize);
        this.grid = grid;
        peopleInBin = new ArrayList<Person>();
        peopleInOverlap = new ArrayList<Person>();
    }
}
