package com.mpds.simulator.domain.model.deprecated.datastructures;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.datastructures.CustomLinkedList;
import com.mpds.simulator.domain.model.Person;

import lombok.Data;

@Data
public class BinarySearchLeaf {

    private BinarySearchTree2d parent;
    private Coordinate upperLeft;
    private Coordinate lowerRight;
    private CustomLinkedList people;
    private BinarySearchLeaf next;

    public BinarySearchLeaf(Coordinate upperLeft, Coordinate lowerRight, BinarySearchTree2d parent){
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        this.parent = parent;
        people = new CustomLinkedList();
        next = null;
    }

    public void insertPerson(Person p){
        people.addPerson(p);
    }

}
