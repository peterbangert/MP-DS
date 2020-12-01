package com.mpds.simulator.domain.model;


import lombok.Data;

@Data

public class BinarySearchTree2d {

    private boolean splitOnRow;
    private Coordinate upperLeft;
    private Coordinate lowerRight;

    private BinarySearchTree2d parent;
    private BinarySearchTree2d leftTree;
    private BinarySearchTree2d rightTree;

    private BinarySearchLeaf leftLeaf;
    private BinarySearchLeaf rightLeaf;

    private Coordinate leftLowerRight;
    private Coordinate rightUpperLeft;

    public BinarySearchTree2d(boolean splitOnRow, Coordinate upperLeft, Coordinate lowerRight, int untilCornerDistLowerThan, BinarySearchTree2d parent){

        this.splitOnRow = splitOnRow;
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
        this.parent = parent;

        if(splitOnRow) {
            leftLowerRight = new Coordinate(upperLeft.getRow() + (lowerRight.getRow() - upperLeft.getRow()) / 2, lowerRight.getCol());
            rightUpperLeft = new Coordinate(upperLeft.getRow() + (lowerRight.getRow() - upperLeft.getRow()) / 2 + 1, upperLeft.getCol());
        } else {
            leftLowerRight = new Coordinate(lowerRight.getRow(), upperLeft.getCol() + (lowerRight.getCol() - upperLeft.getCol()) / 2);
            rightUpperLeft = new Coordinate(upperLeft.getRow(), upperLeft.getCol() + (lowerRight.getCol() - upperLeft.getCol()) / 2 + 1);
            }
        /*
        System.out.println(splitOnRow);
        System.out.println(upperLeft.getRow());
        System.out.println(upperLeft.getCol());
        System.out.println(leftLowerRight.getRow());
        System.out.println(leftLowerRight.getCol());
        System.out.println();
        System.out.println(rightUpperLeft.getRow());
        System.out.println(rightUpperLeft.getCol());
        System.out.println(lowerRight.getRow());
        System.out.println(lowerRight.getCol());
        System.out.println();
        System.out.println("distance to: " + String.valueOf(upperLeft.distanceTo(lowerRight)));
        */
        if(upperLeft.distanceTo(lowerRight) >= untilCornerDistLowerThan){

            leftTree = new BinarySearchTree2d(!splitOnRow, upperLeft, leftLowerRight, untilCornerDistLowerThan, this);

            rightTree = new BinarySearchTree2d(!splitOnRow, rightUpperLeft, lowerRight, untilCornerDistLowerThan, this);

            leftLeaf = null;
            rightLeaf = null;

        } else {
            leftLeaf = new BinarySearchLeaf(upperLeft, leftLowerRight, this);
            rightLeaf = new BinarySearchLeaf(rightUpperLeft, lowerRight, this);

            leftTree = null;
            rightTree = null;
        }

    }


    public Tuple<BinarySearchLeaf> connectLeaves(){

        if(leftTree != null){
            Tuple<BinarySearchLeaf> leftTuple = leftTree.connectLeaves();
            Tuple<BinarySearchLeaf> rightTuple = rightTree.connectLeaves();

            leftTuple.getRight().setNext(rightTuple.getLeft());

            return new Tuple<>(leftTuple.getLeft(), rightTuple.getRight());

        } else {
            leftLeaf.setNext(rightLeaf);
            return new Tuple<>(leftLeaf, rightLeaf);
        }
    }

    public void addPersonNode(LinkedListNode<Person> pn){

        if(pn.getContent().getPos().isUpLeftTo(leftLowerRight)){
            if(leftTree != null){
                leftTree.addPersonNode(pn);
            } else {
                leftLeaf.insertPersonNode(pn);
            }
        } else {
            if(rightTree != null){
                rightTree.addPersonNode(pn);
            } else {
                rightLeaf.insertPersonNode(pn);
            }
        }
    }
}