package com.mpds.simulator.domain.model;

import lombok.Data;

@Data
public class LinkedListNode<T> {

    private T content;
    private LinkedListNode<T> next;


    public LinkedListNode(T content, LinkedListNode<T> next){
        this.content = content;
        this.next = next;
    }

}
