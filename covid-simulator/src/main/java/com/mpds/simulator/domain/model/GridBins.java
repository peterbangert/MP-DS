package com.mpds.simulator.domain.model;

import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import lombok.Data;
import lombok.Getter;

@Data
public class GridBins {

    private Coordinate size;
    private Coordinate binSize;
    private Coordinate overlapSize;
    private int binsPerRow;
    private int binsPerCol;
    private Bin[][] bins;


    public GridBins(DomainEventPublisher domainEventPublisher, Coordinate size, Coordinate binSize, Coordinate overlapSize, int infectionDistance, int infectionTime){

        this.size = size;
        this.binSize = binSize;
        this.overlapSize = overlapSize;
        int rowResidual = size.getRow() % binSize.getRow();
        int colResidual = size.getCol() % binSize.getCol();
        boolean rowResidualTooSmall = rowResidual <= overlapSize.getRow() && rowResidual != 0;
        boolean colResidualTooSmall = colResidual <= overlapSize.getCol() && colResidual != 0;

        binsPerRow = (int) Math.ceil(size.getRow() / (float) binSize.getRow());
        binsPerCol = (int) Math.ceil(size.getCol() / (float) binSize.getCol());

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
                upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * c);
                lowerRight = new Coordinate(binSize.getRow() * (r+1) - 1, binSize.getCol() * (c+1) - 1);
                bins[r][c] = new Bin(upperLeft, lowerRight, overlapSize, infectionDistance, infectionTime, this, domainEventPublisher);
            }
        }

        // Special case last column
        for(int r=0; r<binsPerRow-1; r++){
            upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * (binsPerCol-1));
            lowerRight = new Coordinate(binSize.getRow() * (r+1)-1, size.getCol()-1);
            bins[r][binsPerCol-1] = new Bin(upperLeft, lowerRight, new Coordinate(overlapSize.getRow(), 0), infectionDistance, infectionTime,this, domainEventPublisher);
        }

        // Special case last row
        for(int c=0; c<binsPerCol-1; c++){
            upperLeft = new Coordinate(binSize.getRow() * (binsPerRow-1), binSize.getCol() * c);
            lowerRight = new Coordinate(size.getRow()-1, binSize.getCol() * (c+1) -1);
            bins[binsPerRow-1][c] = new Bin(upperLeft, lowerRight, new Coordinate(0, overlapSize.getCol()), infectionDistance, infectionTime,this, domainEventPublisher);
        }

        // Special case lowest left bin
        upperLeft = new Coordinate(binSize.getRow() * (binsPerRow-1), binSize.getCol() * (binsPerCol - 1));
        lowerRight = new Coordinate(size.getRow()-1, size.getCol()-1);
        bins[binsPerRow-1][binsPerCol-1] = new Bin(upperLeft, lowerRight, new Coordinate(0, 0), infectionDistance, infectionTime, this, domainEventPublisher);
    }

    public void insertPerson(Person person){

        int row = person.getPos().getRow() / binSize.getRow();
        int col = person.getPos().getCol() / binSize.getCol();

        if(row >= binsPerRow){
            row -= 1;
        }
        if(col >= binsPerCol){
            col -= 1;
        }

        bins[row][col].getPeopleInBin().add(person);
        boolean overlapTop = person.getPos().getRow() % binSize.getRow() < overlapSize.getRow() && row > 0;
        boolean overlapLeft = person.getPos().getCol() % binSize.getCol() < overlapSize.getCol() && col > 0;

        if(overlapTop){
            bins[row-1][col].getPeopleInOverlap().add(person);
        }
        if(overlapLeft){
            bins[row][col-1].getPeopleInOverlap().add(person);
        }
        if(overlapLeft && overlapTop){
            bins[row-1][col-1].getPeopleInOverlap().add(person);
        }
    }

    public void iteration(int time){
        for(int r=0; r<binsPerRow; r++) {
            for (int c = 0; c < binsPerRow; c++) {
                bins[r][c].setTime(time);
                bins[r][c].contactsInfections();
            }
        }
        for(int r=0; r<binsPerRow; r++) {
            for (int c = 0; c < binsPerRow; c++) {
                bins[r][c].movePeople();
            }
        }
    }
}
