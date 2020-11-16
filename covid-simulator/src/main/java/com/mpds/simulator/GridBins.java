package com.mpds.simulator;

public class GridBins {

    Coordinate size;
    int binsPerRow;
    int binsPerCol;
    Bin[][] bins;

    public GridBins(Coordinate size, Coordinate binSize, Coordinate overlapSize){
        this.size = size;

        int rowResidual = size.row % binSize.row;
        int colResidual = size.col % binSize.col;
        boolean rowResidualTooSmall = rowResidual <= overlapSize.row && rowResidual != 0;
        boolean colResidualTooSmall = colResidual <= overlapSize.col && colResidual != 0;

        binsPerRow = (int) Math.ceil(size.row / (float) binSize.row);
        binsPerCol = (int) Math.ceil(size.col / (float) binSize.col);

        if(rowResidualTooSmall) {
            binsPerRow -= 1;
        }
        if(colResidualTooSmall){
            binsPerCol -= 1;
        }

        bins = new Bin[binsPerRow][binsPerCol];

        Coordinate upperLeft;
        Coordinate lowerRight;
        for(int r=0; r<binsPerRow-1; r++){
            for(int c=0; c<binsPerCol-1; c++){
                upperLeft = new Coordinate(binSize.row * r, binSize.col * c);
                lowerRight = new Coordinate(binSize.row * (r+1) - 1, binSize.col * (c+1) - 1);
                bins[r][c] = new Bin(upperLeft, lowerRight, overlapSize, this);
            }
        }

        // Special case last column
        for(int r=0; r<binsPerRow-1; r++){
            upperLeft = new Coordinate(binSize.row * r, binSize.col * (binsPerCol-1));
            lowerRight = new Coordinate(binSize.row * (r+1)-1, size.col-1);
            bins[r][binsPerCol-1] = new Bin(upperLeft, lowerRight, new Coordinate(overlapSize.row, 0), this);
        }

        // Special case last row
        for(int c=0; c<binsPerCol-1; c++){
            upperLeft = new Coordinate(binSize.row * (binsPerRow-1), binSize.col * c);
            lowerRight = new Coordinate(size.row-1, binSize.col * (c+1) -1);
            bins[binsPerRow-1][c] = new Bin(upperLeft, lowerRight, new Coordinate(0, overlapSize.col), this);
        }

        // Special case lowest left bin
        upperLeft = new Coordinate(binSize.row * (binsPerRow-1), binSize.col * (binsPerCol - 1));
        lowerRight = new Coordinate(size.row-1, size.col-1);
        bins[binsPerRow-1][binsPerCol-1] = new Bin(upperLeft, lowerRight, new Coordinate(0, 0), this);
    }
}
