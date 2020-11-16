package com.mpds.simulator;

import java.util.ArrayList;

public class Bin {

    Coordinate ulCorner;
    Coordinate lrCorner;
    Coordinate overlap_lrCorner;
    GridBins grid;
    ArrayList<Person> peopleInBin;
    ArrayList<Person> peopleInOverlap;

    public Bin(Coordinate ulCorner, Coordinate lrCorner, Coordinate overlapSize, GridBins grid){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        this.grid = grid;
    }
}
