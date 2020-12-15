package com.mpds.simulator.domain.model.datastructures;

import lombok.Data;

@Data
public abstract class LinkedListNode<T> {

    private T content;
    private LinkedListNode<T> next;


    public LinkedListNode(T content){
        this.content = content;
        next = null;
    }

}
