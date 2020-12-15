package com.mpds.simulator.domain.model;

import com.mpds.simulator.domain.model.bins.*;
import com.mpds.simulator.domain.model.bins.iterfirst.*;
import com.mpds.simulator.domain.model.bins.itersecond.*;
import com.mpds.simulator.domain.model.stats.SimulationStats;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import it.unimi.dsi.util.XorShift1024StarPhiRandom;
import lombok.Data;

/**
 * <h1>Logical Grid of Simulation</h1>
 * The grid is the root of all computations.
 * It holds all constants.
 * For performance reasons it is divided into bins.
 * The bins hold a rectangular part of the grid.
 */

@Data
public class GridBins {


    private Coordinate binSize;
    private Coordinate overlapSize;
    private int binsPerRow;
    private int binsPerCol;
    private Bin[][] bins;
    private int minMilliSecPerRound;
    private boolean saveRoundStats;

    private static SimulationStats stats;

    public static int infectionDistance;
    public static int infectionTime;
    public static Coordinate size;
    public static XorShift1024StarPhiRandom randomGen;
    public static int ticksPerDay;
    public static int publishInfectionAfterXTicks;
    public static int publishInfectionAtTime;

    public static int roundContacts = 0;
    public static int roundInfections = 0;
    public static int roundHealed = 0;

    private final DomainEventPublisher domainEventPublisher;

    /**
     * @param domainEventPublisher The publisher through which all messages pass to kafka
     * @param size Grid size, first coordinate amount of rows, second columns
     * @param binSize First coordinate amount of rows in bin, second columns
     * @param infectionDistance The Manhatten distance upto which an infection between two people occurs
     * @param daysInfected The amount of days a person stays infected
     * @param ticksPerDay The amount of logical ticks until one day is considered passed
     * @param publishInfectionAfterXDays The amount of days until an infection is published after it happened
     * @param minMilliSecPerRound The minimum amount milli-seconds that have to pass during each round
     * @param saveRoundStats If round statistics are to be saved in csv
     */

    public GridBins(DomainEventPublisher domainEventPublisher, Coordinate size, Coordinate binSize, int infectionDistance, int daysInfected, int ticksPerDay, int publishInfectionAfterXDays, int minMilliSecPerRound, boolean saveRoundStats) {
        this.domainEventPublisher=domainEventPublisher;
        this.size = size;
        this.binSize = binSize;
        this.ticksPerDay = ticksPerDay;
        this.infectionDistance = infectionDistance;
        this.infectionTime = daysInfected * ticksPerDay;
        this.publishInfectionAfterXTicks = publishInfectionAfterXDays * ticksPerDay;
        this.minMilliSecPerRound = minMilliSecPerRound;
        this.saveRoundStats = saveRoundStats;
        publishInfectionAtTime = infectionTime + 1 - publishInfectionAfterXTicks;
        //System.out.println(publishInfectionAtTime);

        if(saveRoundStats){
            stats = new SimulationStats("./stats/simulation_stats.csv");
        }

        int rowResidual = size.getRow() % binSize.getRow();
        int colResidual = size.getCol() % binSize.getCol();
        boolean rowResidualTooSmall = rowResidual <= infectionDistance && rowResidual != 0;
        boolean colResidualTooSmall = colResidual <= infectionDistance && colResidual != 0;

        randomGen = new XorShift1024StarPhiRandom();
        randomGen.setSeed(0);

        binsPerRow = (int) Math.ceil(size.getRow() / (float) binSize.getRow());
        binsPerCol = (int) Math.ceil(size.getCol() / (float) binSize.getCol());

        if (rowResidualTooSmall) {
            binsPerRow -= 1;
        }
        if (colResidualTooSmall) {
            binsPerCol -= 1;
        }

        bins = new Bin[binsPerRow][binsPerCol];

        Coordinate upperLeft;
        Coordinate lowerRight;
        for (int r = 0; r < binsPerRow; r++) {
            for (int c = 0; c < binsPerCol; c++) {

                if (r == 0 && c == 0) {
                    upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * c);
                    lowerRight = new Coordinate(binSize.getRow() * (r + 1) - 1, binSize.getCol() * (c + 1) - 1);
                    bins[r][c] = new TopLeftBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                } else if (r == 0 && c == binsPerCol - 1) {
                    upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * c);
                    lowerRight = new Coordinate(binSize.getRow() * (r + 1) - 1, size.getCol() - 1);
                    bins[r][c] = new TopRightBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                } else if (r == 0) {
                    upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * c);
                    lowerRight = new Coordinate(binSize.getRow() * (r + 1) - 1, binSize.getCol() * (c + 1) - 1);
                    bins[r][c] = new TopBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                } else if (r == binsPerRow - 1 && c == 0) {
                    upperLeft = new Coordinate(binSize.getRow() * (binsPerRow - 1), binSize.getCol() * c);
                    lowerRight = new Coordinate(size.getRow() - 1, binSize.getCol() * (c + 1) - 1);
                    if (r % 2 == 0) {
                        bins[r][c] = new BottomLeftBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                    } else {
                        bins[r][c] = new BottomLeftBinIterSecond(domainEventPublisher, upperLeft, lowerRight);
                    }
                } else if (r == binsPerRow - 1 && c == binsPerCol - 1) {
                    upperLeft = new Coordinate(binSize.getRow() * (binsPerRow - 1), binSize.getCol() * c);
                    lowerRight = new Coordinate(size.getRow() - 1, binSize.getCol() * (c + 1) - 1);
                    if (r % 2 == 0) {
                        bins[r][c] = new BottomRightBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                    } else {
                        bins[r][c] = new BottomRightBinIterSecond(domainEventPublisher, upperLeft, lowerRight);
                    }
                } else if (r == binsPerRow - 1) {
                    upperLeft = new Coordinate(binSize.getRow() * (binsPerRow - 1), binSize.getCol() * c);
                    lowerRight = new Coordinate(size.getRow() - 1, binSize.getCol() * (c + 1) - 1);
                    if (r % 2 == 0) {
                        bins[r][c] = new BottomBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                    } else {
                        bins[r][c] = new BottomBinIterSecond(domainEventPublisher, upperLeft, lowerRight);
                    }
                } else if (c == 0) {
                    upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * c);
                    lowerRight = new Coordinate(binSize.getRow() * (r + 1) - 1, binSize.getCol() * (c + 1) - 1);
                    if (r % 2 == 0) {
                        bins[r][c] = new LeftBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                    } else {
                        bins[r][c] = new LeftBinIterSecond(domainEventPublisher, upperLeft, lowerRight);
                    }
                } else if (c == binsPerCol - 1) {
                    upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * (binsPerCol - 1));
                    lowerRight = new Coordinate(binSize.getRow() * (r + 1) - 1, size.getCol() - 1);
                    if (r % 2 == 0) {
                        bins[r][c] = new RightBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                    } else {
                        bins[r][c] = new RightBinIterSecond(domainEventPublisher, upperLeft, lowerRight);
                    }
                } else {
                    upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * c);
                    lowerRight = new Coordinate(binSize.getRow() * (r + 1) - 1, binSize.getCol() * (c + 1) - 1);
                    if (r % 2 == 0) {
                        bins[r][c] = new MiddleBinIterFirst(domainEventPublisher, upperLeft, lowerRight);
                    } else {
                        bins[r][c] = new MiddleBinIterSecond(domainEventPublisher, upperLeft, lowerRight);
                    }
                }
            }
        }

        for (int r = 0; r < binsPerRow; r++) {
            for (int c = 0; c < binsPerCol; c++) {
                if (r == 0 && c == 0) {
                    ((TopLeft) bins[r][c]).setRight(bins[r][c+1]);
                    ((TopLeft) bins[r][c]).setBelowRight(bins[r+1][c+1]);
                    ((TopLeft) bins[r][c]).setBelow(bins[r+1][c]);
                } else if (r == 0 && c == binsPerCol - 1) {
                    ((TopRight) bins[r][c]).setLeft(bins[r][c-1]);
                    ((TopRight) bins[r][c]).setBelow(bins[r+1][c]);
                    ((TopRight) bins[r][c]).setBelowLeft(bins[r+1][c-1]);
                } else if (r == 0) {
                    ((TopBin) bins[r][c]).setBelow(bins[r+1][c]);
                    ((TopBin) bins[r][c]).setLeft(bins[r][c-1]);
                    ((TopBin) bins[r][c]).setRight(bins[r][c+1]);
                    ((TopBin) bins[r][c]).setBelowLeft(bins[r+1][c-1]);
                    ((TopBin) bins[r][c]).setBelowRight(bins[r+1][c+1]);
                } else if (r == binsPerRow - 1 && c == 0) {
                    ((BottomLeftBin) bins[r][c]).setAbove(bins[r-1][c]);
                    ((BottomLeftBin) bins[r][c]).setAboveRight(bins[r-1][c+1]);
                    ((BottomLeftBin) bins[r][c]).setRight(bins[r][c+1]);
                } else if (r == binsPerRow - 1 && c == binsPerCol - 1) {
                    ((BottomRightBin) bins[r][c]).setAbove(bins[r-1][c]);
                    ((BottomRightBin) bins[r][c]).setAboveLeft(bins[r-1][c-1]);
                    ((BottomRightBin) bins[r][c]).setLeft(bins[r][c-1]);
                } else if (r == binsPerRow - 1) {
                    ((BottomBin) bins[r][c]).setAbove(bins[r-1][c]);
                    ((BottomBin) bins[r][c]).setAboveLeft(bins[r-1][c-1]);
                    ((BottomBin) bins[r][c]).setAboveRight(bins[r-1][c+1]);
                    ((BottomBin) bins[r][c]).setLeft(bins[r][c-1]);
                    ((BottomBin) bins[r][c]).setRight(bins[r][c+1]);
                } else if (c == 0) {
                    ((LeftBin) bins[r][c]).setAbove(bins[r-1][c]);
                    ((LeftBin) bins[r][c]).setAboveRight(bins[r-1][c+1]);
                    ((LeftBin) bins[r][c]).setRight(bins[r][c+1]);
                    ((LeftBin) bins[r][c]).setBelow(bins[r+1][c]);
                    ((LeftBin) bins[r][c]).setBelowRight(bins[r+1][c+1]);
                } else if (c == binsPerCol - 1) {
                    ((RightBin) bins[r][c]).setAbove(bins[r-1][c]);
                    ((RightBin) bins[r][c]).setAboveLeft(bins[r-1][c-1]);
                    ((RightBin) bins[r][c]).setLeft(bins[r][c-1]);
                    ((RightBin) bins[r][c]).setBelowLeft(bins[r+1][c-1]);
                    ((RightBin) bins[r][c]).setBelow(bins[r+1][c]);
                } else {
                    ((MiddleBin) bins[r][c]).setAbove(bins[r-1][c]);
                    ((MiddleBin) bins[r][c]).setAboveLeft(bins[r-1][c-1]);
                    ((MiddleBin) bins[r][c]).setAboveRight(bins[r-1][c+1]);
                    ((MiddleBin) bins[r][c]).setLeft(bins[r][c-1]);
                    ((MiddleBin) bins[r][c]).setRight(bins[r][c+1]);
                    ((MiddleBin) bins[r][c]).setBelowLeft(bins[r+1][c-1]);
                    ((MiddleBin) bins[r][c]).setBelow(bins[r+1][c]);
                    ((MiddleBin) bins[r][c]).setBelowRight(bins[r+1][c+1]);
                }
            }
        }
    }
/*
        // Special case last column
        for(int r=0; r<binsPerRow-1; r++){
            upperLeft = new Coordinate(binSize.getRow() * r, binSize.getCol() * (binsPerCol-1));
            lowerRight = new Coordinate(binSize.getRow() * (r+1)-1, size.getCol()-1);
            bins[r][binsPerCol-1] = new Bin(upperLeft, lowerRight, new Coordinate(overlapSize.getRow(), 0), infectionDistance, infectionTime,this, searchTreeBinSize, domainEventPublisher);
        }

        // Special case last row
        for(int c=0; c<binsPerCol-1; c++){
            upperLeft = new Coordinate(binSize.getRow() * (binsPerRow-1), binSize.getCol() * c);
            lowerRight = new Coordinate(size.getRow()-1, binSize.getCol() * (c+1) -1);
            bins[binsPerRow-1][c] = new Bin(upperLeft, lowerRight, new Coordinate(0, overlapSize.getCol()), infectionDistance, infectionTime,this, searchTreeBinSize, domainEventPublisher);
        }

        // Special case lowest left bin
        upperLeft = new Coordinate(binSize.getRow() * (binsPerRow-1), binSize.getCol() * (binsPerCol - 1));
        lowerRight = new Coordinate(size.getRow()-1, size.getCol()-1);
        bins[binsPerRow-1][binsPerCol-1] = new Bin(upperLeft, lowerRight, new Coordinate(0, 0), infectionDistance, infectionTime, this, searchTreeBinSize, domainEventPublisher);
    }
*/

    /**
     * Inserts a person into the right bin and thus into the grid.
     * For
     * @param person A person that holds appropriate coordinates for its position
     */

    public void insertPerson(Person person){

        int row = person.getPos().getRow() / binSize.getRow();
        int col = person.getPos().getCol() / binSize.getCol();

        //System.out.println(String.valueOf(row) + " - " + String.valueOf(col));

        if(row >= binsPerRow){
            row -= 1;
        }
        if(col >= binsPerCol){
            col -= 1;
        }

        bins[row][col].addPerson(person);

        /*
        boolean overlapTop = person.getContent().getPos().getRow() % binSize.getRow() < overlapSize.getRow() && row > 0;
        boolean overlapLeft = person.getContent().getPos().getCol() % binSize.getCol() < overlapSize.getCol() && col > 0;

        if(overlapTop){
            bins[row-1][col].getPeopleInOverlap().add(person);
        }
        if(overlapLeft){
            bins[row][col-1].getPeopleInOverlap().add(person);
        }
        if(overlapLeft && overlapTop){
            bins[row-1][col-1].getPeopleInOverlap().add(person);
        }*/
    }

    /**
     * The function to be called to simulate a round.
     * @param time The current round
     */
    public void iteration(long time){

        long startTime = System.nanoTime();

        for(int r=0; r<binsPerRow; r+=2) {
            for (int c = 0; c < binsPerCol; c++) {
                //bins[r][c].setTime(time);
                bins[r][c].iterate(time);
            }
        }

        for (int r=1; r<binsPerRow; r+=2){
            for (int c=0; c<binsPerCol; c++){
                bins[r][c].iterate(time);
            }
        }

        for (int r=0; r<binsPerRow; r++) {
            for (int c = 0; c < binsPerCol; c++) {
                bins[r][c].addNewPeople();
            }
        }

        long duration = (System.nanoTime() - startTime)/1000000;

        if(duration < minMilliSecPerRound){
            try {
                Thread.sleep((long) ((minMilliSecPerRound -duration) * 0.98));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//        System.out.println(String.format("Round %d - Time of Day %d:\ncontacts: %d\ninfections: %d\nhealed: %d\nduration (ms): %d\n", time, time%ticksPerDay, roundContacts, roundInfections, roundHealed, duration));

        if(saveRoundStats) {
            stats.addRoundToFile(time, time % ticksPerDay, roundContacts, roundInfections, roundHealed, duration);
        }
        roundContacts = 0;
        roundInfections = 0;
        roundHealed = 0;
    }
}
