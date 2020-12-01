package com.mpds.simulator.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import static java.lang.Math.abs;

@Data
@AllArgsConstructor
public class Coordinate {

    private int row;
    private int col;

    public boolean isUpLeftTo(Coordinate compare){
        return this.row <= compare.row && this.col <= compare.col;
    }

    public boolean isDownRightTo(Coordinate compare){
        return this.row >= compare.row && this.col >= compare.col;
    }

    public boolean isLeftTo(Coordinate compare){
        return this.col <= compare.col;
    }

    public boolean isAboveTo(Coordinate compare){
        return this.row <= compare.row;
    }

    public int distanceTo(Coordinate point){
        return abs(this.row - point.row) + abs(this.col - point.col);
    }

    public Coordinate addCoordinate(Coordinate toAdd){
        return new Coordinate(this.row + toAdd.row, this.col + toAdd.col);
    }

    public Coordinate addInt(int toAdd){
        return new Coordinate(this.row + toAdd, this.col + toAdd);
    }
}
