package com.mpds.simulator;

import com.mpds.simulator.domain.model.*;
import com.mpds.simulator.port.adapter.kafka.DomainEventPublisher;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

//@SpringBootTest
class CovidSimulatorApplicationTests {

    /*@Test
    void contextLoads() {
    }*/

    @Test
    public void testGrid() {

        Coordinate size = new Coordinate(22, 22);
        Coordinate binSize = new Coordinate(3, 3);
        Coordinate overlap = new Coordinate(1, 1);
        GridBins grid = new GridBins(null, size, binSize, overlap, 3, 14);
        Assert.isTrue(grid.getBins()[0][0].getUlCorner().getRow() == 0);
        Assert.isTrue(grid.getBins()[0][0].getUlCorner().getCol() == 0);
        Assert.isTrue(grid.getBins()[0][0].getLrCorner().getRow() == 2);
        Assert.isTrue(grid.getBins()[0][0].getLrCorner().getCol() == 2);
        Assert.isTrue(grid.getBins()[0][0].getOverlapCorner().getRow() == 3);
        Assert.isTrue(grid.getBins()[0][0].getOverlapCorner().getCol() == 3);

        Assert.isTrue(grid.getBins()[0][6].getUlCorner().getRow() == 0, String.valueOf(grid.getBins()[0][6].getUlCorner().getRow()));
        Assert.isTrue(grid.getBins()[0][6].getUlCorner().getCol() == 18, String.valueOf(grid.getBins()[0][6].getUlCorner().getCol()));
        Assert.isTrue(grid.getBins()[0][6].getLrCorner().getRow() == 2, String.valueOf(grid.getBins()[0][6].getLrCorner().getRow()));
        Assert.isTrue(grid.getBins()[0][6].getLrCorner().getCol() == 21, String.valueOf(grid.getBins()[0][6].getLrCorner().getCol()));
        Assert.isTrue(grid.getBins()[0][6].getOverlapCorner().getRow() == 3, String.valueOf(grid.getBins()[0][6].getOverlapCorner().getRow()));
        Assert.isTrue(grid.getBins()[0][6].getOverlapCorner().getCol() == 21, String.valueOf(grid.getBins()[0][6].getOverlapCorner().getCol()));

        Assert.isTrue(grid.getBins()[6][0].getUlCorner().getRow() == 18, String.valueOf(grid.getBins()[6][0].getUlCorner().getRow()));
        Assert.isTrue(grid.getBins()[6][0].getUlCorner().getCol() == 0, String.valueOf(grid.getBins()[6][0].getUlCorner().getCol()));
        Assert.isTrue(grid.getBins()[6][0].getLrCorner().getRow() == 21, String.valueOf(grid.getBins()[6][0].getLrCorner().getRow()));
        Assert.isTrue(grid.getBins()[6][0].getLrCorner().getCol() == 2, String.valueOf(grid.getBins()[6][0].getLrCorner().getCol()));
        Assert.isTrue(grid.getBins()[6][0].getOverlapCorner().getRow() == 21, String.valueOf(grid.getBins()[6][0].getOverlapCorner().getRow()));
        Assert.isTrue(grid.getBins()[6][0].getOverlapCorner().getCol() == 3, String.valueOf(grid.getBins()[6][0].getOverlapCorner().getCol()));

        Assert.isTrue(grid.getBins()[6][6].getUlCorner().getRow() == 18, String.valueOf(grid.getBins()[6][6].getUlCorner().getRow()));
        Assert.isTrue(grid.getBins()[6][6].getUlCorner().getCol() == 18, String.valueOf(grid.getBins()[6][6].getUlCorner().getCol()));
        Assert.isTrue(grid.getBins()[6][6].getLrCorner().getRow() == 21, String.valueOf(grid.getBins()[6][6].getLrCorner().getRow()));
        Assert.isTrue(grid.getBins()[6][6].getLrCorner().getCol() == 21, String.valueOf(grid.getBins()[6][6].getLrCorner().getCol()));
        Assert.isTrue(grid.getBins()[6][6].getOverlapCorner().getRow() == 21, String.valueOf(grid.getBins()[6][6].getOverlapCorner().getRow()));
        Assert.isTrue(grid.getBins()[6][6].getOverlapCorner().getCol() == 21, String.valueOf(grid.getBins()[6][6].getOverlapCorner().getCol()));

        Person p1 = new Person(1, new Coordinate(0, 0), 0, size);
        grid.insertPerson(p1);
        Assert.isTrue(!grid.getBins()[0][0].getPeopleInBin().isEmpty());

        Person p2 = new Person(2, new Coordinate(18, 18), 0, size);
        grid.insertPerson(p2);
        Assert.isTrue(!grid.getBins()[6][6].getPeopleInBin().isEmpty());
        Assert.isTrue(!grid.getBins()[5][6].getPeopleInOverlap().isEmpty());
        Assert.isTrue(!grid.getBins()[6][5].getPeopleInOverlap().isEmpty());
        Assert.isTrue(!grid.getBins()[5][5].getPeopleInOverlap().isEmpty());

        Person p3 = new Person(3, new Coordinate(21, 21), 0, size);
        grid.insertPerson(p3);
        Assert.isTrue(!grid.getBins()[6][6].getPeopleInBin().isEmpty());
        Assert.isTrue(grid.getBins()[6][6].getPeopleInOverlap().isEmpty());

    }

    @Test
    public void testGrid2() {
        Coordinate size = new Coordinate(100, 47);
        Coordinate binSize = new Coordinate(10, 6);
        Coordinate overlap = new Coordinate(3, 2);
        GridBins grid = new GridBins(null, size, binSize, overlap, 3, 14);

        Assert.isTrue(grid.getBins()[0][7].getUlCorner().getRow() == 0, String.valueOf(grid.getBins()[0][7].getUlCorner().getRow()));
        Assert.isTrue(grid.getBins()[0][7].getUlCorner().getCol() == 42, String.valueOf(grid.getBins()[0][7].getUlCorner().getCol()));
        Assert.isTrue(grid.getBins()[0][7].getLrCorner().getRow() == 9, String.valueOf(grid.getBins()[0][7].getLrCorner().getRow()));
        Assert.isTrue(grid.getBins()[0][7].getLrCorner().getCol() == 46, String.valueOf(grid.getBins()[0][7].getLrCorner().getCol()));
        Assert.isTrue(grid.getBins()[0][7].getOverlapCorner().getRow() == 12, String.valueOf(grid.getBins()[0][7].getOverlapCorner().getRow()));
        Assert.isTrue(grid.getBins()[0][7].getOverlapCorner().getCol() == 46, String.valueOf(grid.getBins()[0][7].getOverlapCorner().getCol()));

        Assert.isTrue(grid.getBins()[9][0].getUlCorner().getRow() == 90, String.valueOf(grid.getBins()[9][0].getUlCorner().getRow()));
        Assert.isTrue(grid.getBins()[9][0].getUlCorner().getCol() == 0, String.valueOf(grid.getBins()[9][0].getUlCorner().getCol()));
        Assert.isTrue(grid.getBins()[9][0].getLrCorner().getRow() == 99, String.valueOf(grid.getBins()[9][0].getLrCorner().getRow()));
        Assert.isTrue(grid.getBins()[9][0].getLrCorner().getCol() == 5, String.valueOf(grid.getBins()[9][0].getLrCorner().getCol()));
        Assert.isTrue(grid.getBins()[9][0].getOverlapCorner().getRow() == 99, String.valueOf(grid.getBins()[9][0].getOverlapCorner().getRow()));
        Assert.isTrue(grid.getBins()[9][0].getOverlapCorner().getCol() == 7, String.valueOf(grid.getBins()[9][0].getOverlapCorner().getCol()));

        Assert.isTrue(grid.getBins()[9][7].getUlCorner().getRow() == 90, String.valueOf(grid.getBins()[6][6].getUlCorner().getRow()));
        Assert.isTrue(grid.getBins()[9][7].getUlCorner().getCol() == 42, String.valueOf(grid.getBins()[6][6].getUlCorner().getCol()));
        Assert.isTrue(grid.getBins()[9][7].getLrCorner().getRow() == 99, String.valueOf(grid.getBins()[6][6].getLrCorner().getRow()));
        Assert.isTrue(grid.getBins()[9][7].getLrCorner().getCol() == 46, String.valueOf(grid.getBins()[6][6].getLrCorner().getCol()));
        Assert.isTrue(grid.getBins()[9][7].getOverlapCorner().getRow() == 99, String.valueOf(grid.getBins()[6][6].getOverlapCorner().getRow()));
        Assert.isTrue(grid.getBins()[9][7].getOverlapCorner().getCol() == 46, String.valueOf(grid.getBins()[6][6].getOverlapCorner().getCol()));
    }

    @Test
    public void testNextMove(){
        Coordinate size = new Coordinate(5,5);
        Person p = new Person(1, null, 0, size);
        for(int i=0; i<100; i++){
            p.move();
            //System.out.println("["+String.valueOf(p.pos.getRow())+"]["+String.valueOf(p.pos.getCol())+ "]");
        }
    }

    @Test
    public void testIteration(){
        Coordinate size = new Coordinate(100000, 100000);
        Coordinate binSize = new Coordinate(500, 500);
        Coordinate overlap = new Coordinate(10, 10);
        GridBins grid = new GridBins(null, size, binSize, overlap, 6, 30);
        grid.insertPerson(new Person(0, null, 100, size));
        // Inserting 12000 persons
        for(int i=1; i<1500000; i++){
            grid.insertPerson(new Person(i, null, 0, size));
        }
        // Run 500 rounds
        for(int i=0; i<100; i++){
            System.out.println(i);
            grid.iteration(i);
        }
    }

    @Test
    public void testBinarySearchTree(){
        Coordinate upperLeft = new Coordinate(0,0);
        Coordinate lowerRight = new Coordinate(10, 10);

        BinarySearchTree2d tree = new BinarySearchTree2d(true, upperLeft, lowerRight, 5, null);
        BinarySearchLeaf start = tree.connectLeaves().getLeft();

        LinkedListNode<Person> pn1 = new PersonNode(new Person(0, new Coordinate(0,0), 0, lowerRight));
        LinkedListNode<Person> pn2 = new PersonNode(new Person(0, new Coordinate(9,9), 0, lowerRight));
        LinkedListNode<Person> pn3 = new PersonNode(new Person(0, new Coordinate(3,7), 0, lowerRight));
        LinkedListNode<Person> pn4 = new PersonNode(new Person(0, new Coordinate(6,5), 0, lowerRight));
        LinkedListNode<Person> pn5 = new PersonNode(new Person(0, new Coordinate(6,4), 0, lowerRight));
        LinkedListNode<Person> pn6 = new PersonNode(new Person(0, new Coordinate(3,8), 0, lowerRight));
        LinkedListNode<Person> pn7 = new PersonNode(new Person(0, new Coordinate(3,8), 0, lowerRight));

        tree.addPersonNode(pn1);
        tree.addPersonNode(pn2);
        tree.addPersonNode(pn3);
        tree.addPersonNode(pn4);
        tree.addPersonNode(pn5);
        tree.addPersonNode(pn6);
        tree.addPersonNode(pn7);

        BinarySearchLeaf current = start;
        while(current != null){

            if(current.getPeople().getStart() != null){
                System.out.println();
                System.out.println("New Field");
                System.out.println(String.valueOf(current.getUpperLeft().getRow()) + ", " + String.valueOf(current.getUpperLeft().getCol()));
                System.out.println(String.valueOf(current.getLowerRight().getRow()) + ", " + String.valueOf(current.getLowerRight().getCol()));
                System.out.println();
                System.out.println("People:");
                LinkedListNode<Person> currP = current.getPeople().getStart();
                while(currP != null){
                    System.out.println(String.valueOf(currP.getContent().getPos().getRow())+ ", " + String.valueOf(currP.getContent().getPos().getCol()));
                    currP = currP.getNext();
                }
            }
            current = current.getNext();
        }
    }

    @Test
    public void printLeaves(){
        Coordinate upperLeft = new Coordinate(0,0);
        Coordinate lowerRight = new Coordinate(10, 10);

        BinarySearchTree2d tree = new BinarySearchTree2d(true, upperLeft, lowerRight, 5, null);
        BinarySearchLeaf start = tree.connectLeaves().getLeft();
        BinarySearchLeaf current = start;
        while(current != null){
            System.out.println();
            System.out.println("New Field");
            System.out.println(String.valueOf(current.getUpperLeft().getRow()) + ", " + String.valueOf(current.getUpperLeft().getCol()));
            System.out.println(String.valueOf(current.getLowerRight().getRow()) + ", " + String.valueOf(current.getLowerRight().getCol()));
            current = current.getNext();
        }
    }
}
