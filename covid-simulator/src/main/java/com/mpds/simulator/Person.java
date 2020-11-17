package com.mpds.simulator;

import it.unimi.dsi.util.XorShift1024StarPhiRandom;

public class Person {

    int id;
    Coordinate pos;
    boolean infected;
    XorShift1024StarPhiRandom randomGen;
    Coordinate gridSize;

    public Person(int id, Coordinate position, Coordinate gridSize){
        this.id = id;
        this.gridSize = gridSize;
        pos = position;
        infected = false;
        randomGen = new XorShift1024StarPhiRandom();
        if(position == null) {
            pos = new Coordinate(randomGen.nextInt(gridSize.row),
                    randomGen.nextInt(gridSize.col));
        }
    }



}
