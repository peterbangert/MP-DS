package com.mpds.simulator.domain.model;

import it.unimi.dsi.util.XorShift1024StarPhiRandom;
import lombok.Data;

@Data
public class Person {

    private int id;
    private Coordinate pos;
    private int infected;
    private Person next;
    //private boolean reported;

    public Person(int id, Coordinate position, int infected){
        this.id = id;
        pos = position;
        this.infected = infected;
        //reported = false;
        if(position == null) {
            pos = new Coordinate(GridBins.randomGen.nextInt(GridBins.size.getRow()),
                    GridBins.randomGen.nextInt(GridBins.size.getCol()));
        }
        next = null;
    }

    public void decrementInfection(){
        infected--;
    }

    /*
    public void move(){
        int move = randomGen.nextInt(4);
        switch (move){
            case 0:
                if(pos.getRow()>0){
                    pos.setRow(pos.getRow()- 1);
//                    pos.row -= 1;
                } else {
                    pos.setRow(pos.getRow()+ 1);
//                    pos.row += 1;
                }
                break;
            case 1:
                if(pos.getCol() < gridSize.getCol() - 1){
                    pos.setCol(pos.getCol()+1);
//                    pos.col += 1;
                } else {
                    pos.setCol(pos.getCol()-11);
//                    pos.col -= 1;
                }
                break;
            case 2:
                if(pos.getRow() < gridSize.getRow() - 1){
                    pos.setRow(pos.getRow()+1);
//                    pos.row += 1;
                } else {
                    pos.setRow(pos.getRow()-1);
//                    pos.row -= 1;
                }
                break;
            case 3:
                if(pos.getCol() > 0) {
                    pos.setCol(pos.getCol()-1);
//                    pos.col -= 1;
                }else {
                    pos.setCol(pos.getCol()+1);
//                    pos.col += 1;
                }
                break;

        }
    }*/
}
