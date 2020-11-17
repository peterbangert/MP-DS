package com.mpds.simulator;

import it.unimi.dsi.util.XorShift1024StarPhiRandom;

public class Person {

    int id;
    Coordinate pos;
    int infected;
    XorShift1024StarPhiRandom randomGen;
    Coordinate gridSize;
    boolean reported;

    public Person(int id, Coordinate position, int infected, Coordinate gridSize){
        this.id = id;
        this.gridSize = gridSize;
        pos = position;
        this.infected = infected;
        reported = false;
        randomGen = new XorShift1024StarPhiRandom();
        if(position == null) {
            pos = new Coordinate(randomGen.nextInt(gridSize.row),
                    randomGen.nextInt(gridSize.col));
        }
    }

    public void move(){
        int move = randomGen.nextInt(4);
        switch (move){
            case 0:
                if(pos.row>0){
                    pos.row -= 1;
                } else {
                    pos.row += 1;
                }
                break;
            case 1:
                if(pos.col < gridSize.col - 1){
                    pos.col += 1;
                } else {
                    pos.col -= 1;
                }
                break;
            case 2:
                if(pos.row < gridSize.row - 1){
                    pos.row += 1;
                } else {
                    pos.row -= 1;
                }
                break;
            case 3:
                if(pos.col > 0) {
                    pos.col -= 1;
                }else {
                    pos.col += 1;
                }
                break;

        }
    }
}
