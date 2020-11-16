package com.mpds.simulator;

import it.unimi.dsi.util.XorShift1024StarPhiRandom;

public class Person {

    int id;
    Coordinate pos;
    boolean infected;
    XorShift1024StarPhiRandom randomGen;

    public Person(int id, Coordinate gridSize){
        this.id = id;
        infected = false;
        randomGen = new XorShift1024StarPhiRandom();

        pos = new Coordinate(randomGen.nextInt(gridSize.row),
                randomGen.nextInt(gridSize.col));
    }



}
