package com.mpds.simulator;

import static java.lang.Math.abs;

public class Coordinate {

    int row;
    int col;

    public Coordinate(int row, int col){
        this.row = row;
        this.col = col;
    }

    public boolean isUpLeftTo(Coordinate compare){
        if(this.row < compare.row && this.col < compare.col){
            return true;
        }
        return false;
    }

    public boolean isDownRightTo(Coordinate compare){
        if(this.row > compare.row && this.col > compare.col){
            return true;
        }
        return false;
    }

    public boolean isLeftTo(Coordinate compare){
        if(this.col < compare.col){
            return true;
        }
        return false;
    }

    public boolean isAboveTo(Coordinate compare){
        if(this.row < compare.row){
            return true;
        }
        return false;
    }

    public int distanceTo(Coordinate point){
        return abs(this.row - point.row) + abs(this.row - point.row);
    }

    public Coordinate addCoordinate(Coordinate toAdd){
        return new Coordinate(this.row + toAdd.row, this.col + toAdd.col);
    }
}
