package com.mpds.simulator.domain.model.deprecated.datastructures.customlist;

import com.mpds.simulator.domain.model.datastructures.LinkedListNode;

public class OverlapPersonNode extends LinkedListNode<OverlapPerson> {

    OverlapPersonNode next;

    public OverlapPersonNode(OverlapPerson content) {
        super(content);
    }

    public OverlapPersonNode getNext(){
        return next;
    }
}
