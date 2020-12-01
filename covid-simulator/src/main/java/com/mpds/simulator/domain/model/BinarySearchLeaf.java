package com.mpds.simulator.domain.model;

import lombok.Data;

@Data
public class BinarySearchLeaf {

    private BinarySearchTree2d parent;
    private Coordinate upperLeft;
    private Coordinate lowerRight;
    private CustomLinkedList<Person> people;
    private BinarySearchLeaf next;

    public BinarySearchLeaf(Coordinate upperLeft, Coordinate lowerRight, BinarySearchTree2d parent){
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        this.parent = parent;
        people = new CustomLinkedList<>();
        next = null;
    }

    public void insertPersonNode(LinkedListNode<Person> p){
        people.addNode(p);
    }

}
