package com.mpds.simulator.domain.model.datastructures.customlist;

import lombok.Data;

@Data
public class CustomLinkedList<T extends LinkedListNode>{

    private T start;

    public CustomLinkedList(){
        start = null;
    }

    public void addNode(T newNode){
        if(start != null){
            newNode.setNext(start);
        }
        start = newNode;
    }
}
