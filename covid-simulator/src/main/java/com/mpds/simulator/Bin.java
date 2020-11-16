package com.mpds.simulator;

import java.util.ArrayList;

public class Bin {

    Coordinate ulCorner;
    Coordinate lrCorner;
    Coordinate overlap_lrCorner;
    GridBins grid;
    ArrayList<Person> peopleInBin;

    Bin(Coordinate ulCorner, Coordinate lrCorner, int[] overlapSize, GridBins grid){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        this.grid = grid;
    }
}
