package com.mpds.simulator.domain.model;

import lombok.Data;

@Data
public class BinarySearchLeaf {

    private Coordinate upperLeft;
    private Coordinate lowerRight;
    private CustomLinkedList<Person> people;
    private BinarySearchLeaf next;

    public BinarySearchLeaf(Coordinate upperLeft, Coordinate lowerRight){
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        people = new CustomLinkedList<>();
    }

    public void insertPerson(PersonNode p){
        people.addNode(p);
    }

    public void removePerson(PersonNode p){
        people.addNode(p);
    }

}
