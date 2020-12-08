package com.mpds.simulator.domain.model.deprecated.datastructures;

import lombok.Data;

@Data
public class Tuple<T> {
    private T left;
    private T right;

    public Tuple(T left, T right){
        this.left = left;
        this.right = right;
    }
}
