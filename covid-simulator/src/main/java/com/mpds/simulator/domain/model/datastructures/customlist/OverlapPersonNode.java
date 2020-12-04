package com.mpds.simulator.domain.model.datastructures.customlist;

public class OverlapPersonNode extends LinkedListNode<OverlapPerson> {

    OverlapPersonNode next;

    public OverlapPersonNode(OverlapPerson content) {
        super(content);
    }

    public OverlapPersonNode getNext(){
        return next;
    }
}
