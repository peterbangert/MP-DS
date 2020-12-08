package com.mpds.simulator.domain.model.datastructures;

import com.mpds.simulator.domain.model.Person;

public class PersonNode extends LinkedListNode<Person> {

    private PersonNode next;
    public PersonNode(Person content) {
        super(content);
    }

    public PersonNode getNext(){
        return next;
    }

    public void setNext(PersonNode personNode){
        next = personNode;
    }
}
