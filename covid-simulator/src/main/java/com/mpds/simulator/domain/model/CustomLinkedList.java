package com.mpds.simulator.domain.model;

import lombok.Data;

@Data
public class CustomLinkedList<T>{

    private LinkedListNode<T> start;

    public CustomLinkedList(){
        start = null;
    }

    public void addNode(LinkedListNode<T> newNode){
        if(start != null){
            newNode.setNext(start);
        }
        start = newNode;
    }
}
