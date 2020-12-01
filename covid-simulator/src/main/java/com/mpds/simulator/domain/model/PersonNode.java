package com.mpds.simulator.domain.model;

public class PersonNode extends LinkedListNode<Person>{

    public <T> PersonNode(Person content, PersonNode next) {
        super(content, next);
    }
}
