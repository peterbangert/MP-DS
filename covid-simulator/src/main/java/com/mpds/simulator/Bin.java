package com.mpds.simulator;

import java.util.ArrayList;

public class Bin {

    Coordinate ulCorner;
    Coordinate lrCorner;
    Coordinate overlapCorner;
    GridBins grid;
    ArrayList<Person> peopleInBin;
    ArrayList<Person> peopleInOverlap;
    ArrayList<Person> toMove;
    int infectionDistance;
    int infectionTime;

    ArrayList<Person[]> contacts;

    public Bin(Coordinate ulCorner, Coordinate lrCorner, Coordinate overlapSize, int infectionDistance, int infectionTime, GridBins grid){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        overlapCorner = this.lrCorner.addCoordinate(overlapSize);
        this.infectionDistance = infectionDistance;
        this.grid = grid;
        peopleInBin = new ArrayList<Person>();
        peopleInOverlap = new ArrayList<Person>();
        this.infectionTime = infectionTime;
    }

    public void calcContactsInfections(Person p1, Person p2){
        int distance = p1.pos.distanceTo(p2.pos);
        if(distance <= infectionDistance){
            //System.out.println("contact:" + String.valueOf(p1.id) + " - " + String.valueOf(p2.id));
            if(p1.infected > 0 && p2.infected == 0){
                if(p2.randomGen.nextInt(101) > distance + 1){
                    p2.infected = infectionTime + 1;
                    System.out.println("infection:" + String.valueOf(p1.id) + " - " + String.valueOf(p2.id));
                }
            } else if (p2.infected > 0 && p1.infected == 0){
                if(p1.randomGen.nextInt(101) > distance + 1){
                    p1.infected = infectionTime + 1;
                    System.out.println("infection:" + String.valueOf(p2.id) + " - " + String.valueOf(p1.id));
                }
            }
        }
    }

    public void contactsInfections(){
        Person p1, p2;
        int distance;
        for(int i=0; i<peopleInBin.size(); i++){
            p1 = peopleInBin.get(i);
            for(int j=i+1; j<peopleInBin.size(); j++){
                p2 = peopleInBin.get(j);
                calcContactsInfections(p1, p2);
            }

            for(int j=0; j<peopleInOverlap.size(); j++){
                p2 = peopleInOverlap.get(j);
                calcContactsInfections(p1, p2);
            }
        }
        toMove = peopleInBin;
        peopleInBin = new ArrayList<Person>();
        peopleInOverlap = new ArrayList<Person>();
    }

    public void movePeople(){
        Person p;
        for(int i=0; i<toMove.size(); i++){
            p = toMove.get(i);
            p.move();
            if(p.infected > 0){
                p.infected -= 1;
                if(p.infected == 0){
                    System.out.println("healed: " + String.valueOf(p.id));
                }
            }
            grid.insertPerson(p);
        }
        toMove = null;
    }
}
