package com.mpds.simulator.domain.model;

import lombok.Data;

/**
 * <h1>Status of a Person and it's Reference to the Next</h1>
 * Every person holds a unique ID.
 * A coordinate can be specified or if null chosen randomly.
 * The infection status as well sleep phase are specified here.
 * Moreover, each person can be part of a single linked list and thus it possesses a possible reference to a next person.
 */
@Data
public class Person {

    private int id;
    private Coordinate pos;
    private int infected;
    private Person next;
    private short asleep;
    private short awake;
    //private boolean reported;

    /**
     * @param id Unique ID
     * @param position Coordinate position on the grid
     * @param infected How many ticks a person is to be considered infected
     * @param asleep When a person goes to sleep in the day cycle
     * @param awake When a person wakes up in the day cycle
     */
    public Person(int id, Coordinate position, short infected, short asleep, short awake){
        this.id = id;
        pos = position;
        this.infected = infected;
        //reported = false;
        if(position == null) {
            pos = new Coordinate(GridBins.randomGen.nextInt(GridBins.size.getRow()),
                    GridBins.randomGen.nextInt(GridBins.size.getCol()));
        }

        if(asleep == -1 && awake == -1){
            short first = (short) GridBins.randomGen.nextInt((GridBins.ticksPerDay*3) / 5);
            short second = (short) GridBins.randomGen.nextInt((GridBins.ticksPerDay*3) / 5);

            if (first <= second){
                this.asleep = first;
                this.awake = second;
            } else {
                this.asleep = second;
                this.awake = first;
            }
        }

        //System.out.println(asleep);
        //System.out.println(awake);
        next = null;
    }

    public void decrementInfection(){
        if (infected > 0) {//System.out.println(String.format("before %d - after %d\n", infected, infected-1));
        infected--;}
    }

    public boolean isAwake(int timeOfDay){
        return timeOfDay >= awake || timeOfDay < asleep;
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
