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

    ArrayList<Person[]> contacts;

    public Bin(Coordinate ulCorner, Coordinate lrCorner, Coordinate overlapSize, int infectionDistance, GridBins grid){
        this.ulCorner = ulCorner;
        this.lrCorner = lrCorner;
        overlapCorner = this.lrCorner.addCoordinate(overlapSize);
        this.infectionDistance = infectionDistance;
        this.grid = grid;
        peopleInBin = new ArrayList<Person>();
        peopleInOverlap = new ArrayList<Person>();
    }

    public void calcContactsInfections(Person p1, Person p2){
        int distance = p1.pos.distanceTo(p2.pos);
        if(distance <= infectionDistance){
            System.out.println("contact:" + String.valueOf(p1.id) + " - " + String.valueOf(p2.id));
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
            grid.insertPerson(p);
            toMove = null;
        }
    }
}
