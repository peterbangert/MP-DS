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

    public int distanceToShiftedColumns(Coordinate point, int shift) {
        return abs(this.row - point.row) + abs(this.col - (point.col +  shift));
    }

    public Coordinate addCoordinate(Coordinate toAdd){
        return new Coordinate(this.row + toAdd.row, this.col + toAdd.col);
    }

    public Coordinate addInt(int toAdd){
        return new Coordinate(this.row + toAdd, this.col + toAdd);
    }

    public boolean isContainedBy(Coordinate upperLeft, Coordinate lowerRight){
        return this.isDownRightTo(upperLeft) && this.isUpLeftTo(lowerRight);
    }

    public boolean infectionRangeContainedMiddle(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) - infectionDistance >= 0
                && (this.col - upperLeft.col) - infectionDistance >= 0
                && (lowerRight.row - this.row) - infectionDistance >= 0
                && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedLeft(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) - infectionDistance >= 0
                && (this.col - upperLeft.col) >= 0
                && (lowerRight.row - this.row) - infectionDistance >= 0
                && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedRight(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) - infectionDistance >= 0
                && (this.col - upperLeft.col) - infectionDistance >= 0
                && (lowerRight.row - this.row) - infectionDistance >= 0
                && (lowerRight.col - this.col) >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedTop(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) >= 0
                && (this.col - upperLeft.col) - infectionDistance >= 0
                && (lowerRight.row - this.row) - infectionDistance >= 0
                && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedBottom(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) - infectionDistance >= 0
                && (this.col - upperLeft.col) - infectionDistance >= 0
                && (lowerRight.row - this.row) >= 0
                && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedTopLeft(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) >= 0
                && (this.col - upperLeft.col) >= 0
                && (lowerRight.row - this.row) - infectionDistance >= 0
                && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedTopRight(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) >= 0
                && (this.col - upperLeft.col) - infectionDistance >= 0
                && (lowerRight.row - this.row) - infectionDistance >= 0
                && (lowerRight.col - this.col)  >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedBottomLeft(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) - infectionDistance >= 0
                && (this.col - upperLeft.col)  >= 0
                && (lowerRight.row - this.row)  >= 0
                && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; }
        return false;
    }

    public boolean infectionRangeContainedBottomRight(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){
        if((this.row - upperLeft.row) - infectionDistance >= 0
                && (this.col - upperLeft.col) - infectionDistance >= 0
                && (lowerRight.row - this.row) >= 0
                && (lowerRight.col - this.col) >= 0){ return  true; }
        return false;
    }




    public boolean infectionRangeContained(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight, int borderCase){

        switch (borderCase) {
            case 0: if((this.row - upperLeft.row) - infectionDistance >= 0
                    && (this.col - upperLeft.col) - infectionDistance >= 0
                    && (lowerRight.row - this.row) - infectionDistance >= 0
                    && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; } break;

            case 1: if((this.row - upperLeft.row) - infectionDistance >= 0
                    && (this.col - upperLeft.col) - infectionDistance >= 0
                    && (lowerRight.row - this.row) - infectionDistance >= 0
                    && (lowerRight.col - this.col) >= 0){ return  true; } break;

            case 2: if((this.row - upperLeft.row) - infectionDistance >= 0
                    && (this.col - upperLeft.col) - infectionDistance >= 0
                    && (lowerRight.row - this.row) >= 0
                    && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; } break;

            case 3: if((this.row - upperLeft.row) - infectionDistance >= 0
                    && (this.col - upperLeft.col) - infectionDistance >= 0
                    && (lowerRight.row - this.row) >= 0
                    && (lowerRight.col - this.col) >= 0){ return  true; } break;

            case 4: if((this.row - upperLeft.row) - infectionDistance >= 0
                    && (this.col - upperLeft.col) >= 0
                    && (lowerRight.row - this.row) - infectionDistance >= 0
                    && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; } break;

            case 5: if((this.row - upperLeft.row) - infectionDistance >= 0
                    && (this.col - upperLeft.col)  >= 0
                    && (lowerRight.row - this.row)  >= 0
                    && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; } break;

            case 6: if((this.row - upperLeft.row) >= 0
                    && (this.col - upperLeft.col) - infectionDistance >= 0
                    && (lowerRight.row - this.row) - infectionDistance >= 0
                    && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; } break;

            case 7: if((this.row - upperLeft.row) >= 0
                    && (this.col - upperLeft.col) - infectionDistance >= 0
                    && (lowerRight.row - this.row) - infectionDistance >= 0
                    && (lowerRight.col - this.col)  >= 0){ return  true; } break;

            case 8: if((this.row - upperLeft.row) >= 0
                    && (this.col - upperLeft.col) >= 0
                    && (lowerRight.row - this.row) - infectionDistance >= 0
                    && (lowerRight.col - this.col) - infectionDistance >= 0){ return  true; } break;
        }
        return false;
    }

    public boolean infectionRangeOverlaps(int infectionDistance, Coordinate upperLeft, Coordinate lowerRight){

        /*
        boolean toTop = this.row - upperLeft.row >= 0;
        boolean toTopInfection = this.row - upperLeft.row + infectionDistance >= 0;

        boolean toLeft = this.col - upperLeft.col >= 0;
        boolean toLeftInfection = this.col - upperLeft.col + infectionDistance >= 0;

        boolean toBottom = lowerRight.row - this.row >= 0;
        boolean toBottomInfection = lowerRight.row - this.row + infectionDistance >= 0;

        boolean toRight = lowerRight.col - this.col >= 0;
        boolean toRightInfection = lowerRight.col - this.col + infectionDistance >= 0;

        if ((toTop &&
                ((toLeft &&
                        ((toBottom && toRightInfection) ||
                                (toBottomInfection && toRight))) ||
                        (toLeftInfection && toBottom && toRight))) ||
                (toTopInfection && toLeft && toBottom && toRight)){
            return true;
        }
         */


        int toTop = this.row - upperLeft.row >= 0 ? 1 : 0;
        int toTopInfection = (this.row - upperLeft.row) + infectionDistance >= 0 ? 1 : 0;

        int toLeft = this.col - upperLeft.col >= 0 ? 1 : 0;
        int toLeftInfection = (this.col - upperLeft.col) + infectionDistance >= 0 ? 1 : 0;

        int toBottom = lowerRight.row - this.row >= 0 ? 1 : 0;
        int toBottomInfection = (lowerRight.row - this.row) + infectionDistance >= 0 ? 1 : 0;

        int toRight = lowerRight.col - this.col >= 0 ? 1 : 0;
        int toRightInfection = (lowerRight.col - this.col) + infectionDistance >= 0 ? 1 : 0;

        if (toTop + toTopInfection + toLeft + toLeftInfection + toBottom + toBottomInfection + toRight + toRightInfection >= 7){
            return true;
        }

        if (this.distanceTo(upperLeft) <= infectionDistance
                || this.distanceTo(lowerRight) <= infectionDistance
                || this.distanceToShiftedColumns(upperLeft, lowerRight.col - upperLeft.col) <= infectionDistance
                || this.distanceToShiftedColumns(lowerRight, upperLeft.col - lowerRight.col) <= infectionDistance){
            return true;
        }

        return false;
    }
}
