package com.mpds.simulator;

import com.mpds.simulator.domain.model.*;
import com.mpds.simulator.domain.model.bins.Bin;
import com.mpds.simulator.domain.model.datastructures.LinkedListNode;
import com.mpds.simulator.domain.model.datastructures.PersonNode;
import com.mpds.simulator.domain.model.deprecated.datastructures.BinarySearchLeaf;
import com.mpds.simulator.domain.model.deprecated.datastructures.BinarySearchTree2d;
import org.junit.jupiter.api.Test;

//@SpringBootTest
class CovidSimulatorApplicationTests {

    /*@Test
    void contextLoads() {
    }*/

    /*
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
     */

    /*
    @Test
    public void testBinarySearchTree(){
        Coordinate upperLeft = new Coordinate(0,0);
        Coordinate lowerRight = new Coordinate(10, 10);

        BinarySearchTree2d tree = new BinarySearchTree2d(true, upperLeft, lowerRight, 5, null);
        BinarySearchLeaf start = tree.connectLeaves().getLeft();

        PersonNode pn1 = new PersonNode(new Person(0, new Coordinate(0,0), 0));
        PersonNode pn2 = new PersonNode(new Person(1, new Coordinate(9,9), 0));
        PersonNode pn3 = new PersonNode(new Person(2, new Coordinate(3,7), 0));
        PersonNode pn4 = new PersonNode(new Person(3, new Coordinate(6,5), 0));
        PersonNode pn5 = new PersonNode(new Person(4, new Coordinate(6,4), 0));
        PersonNode pn6 = new PersonNode(new Person(5, new Coordinate(3,8), 0));
        PersonNode pn7 = new PersonNode(new Person(6, new Coordinate(3,8), 0));

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
    public void testContactInfections(){


        Coordinate upperLeft = new Coordinate(0,0);
        Coordinate lowerRight = new Coordinate(10, 10);

        Coordinate size = new Coordinate(30, 30);
        Coordinate binSize = new Coordinate(10, 10);
        Coordinate overlap = new Coordinate(0, 0);
        GridBins grid = new GridBins(null, size, binSize, overlap, 4, 14, 5);

        Bin bin = grid.getBins()[0][0];
        System.out.println(bin.getUlCorner());
        System.out.println(bin.getLrCorner());
        System.out.println();

        PersonNode pn1 = new PersonNode(new Person(0, new Coordinate(0,0), 0));
        PersonNode pn2 = new PersonNode(new Person(1, new Coordinate(1,0), 0));
        PersonNode pn3 = new PersonNode(new Person(2, new Coordinate(1,1), 0));
        PersonNode pn4 = new PersonNode(new Person(3, new Coordinate(3, 3), 0));
        PersonNode pn5 = new PersonNode(new Person(4, new Coordinate(7, 5), 0));

        bin.getSearchTree().addPersonNode(pn1);
        bin.getSearchTree().addPersonNode(pn2);
        bin.getSearchTree().addPersonNode(pn3);
        bin.getSearchTree().addPersonNode(pn4);
        bin.getSearchTree().addPersonNode(pn5);
        /*
        System.out.println(bin.getFirstLeaf() == bin.getFirstLeaf().getNext());
        System.out.println(bin.getFirstLeaf().getPeople().getStart());
        System.out.println(bin.getFirstLeaf().getUpperLeft());



        bin.iteration();

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
    */

    @Test
    public void smallTest(){
        Coordinate binSize = new Coordinate(4,4);
        Coordinate size = new Coordinate(30, 30);
        GridBins grid = new GridBins(null, size, binSize, 4, 3, 10, 1, 500, false);
        grid.insertPerson(new Person(0, new Coordinate(0,0), (short) 30, (short) -1, (short) -1));
        grid.insertPerson(new Person(1, new Coordinate(1,0), (short) 0, (short) -1, (short) -1));
        grid.insertPerson(new Person(2, new Coordinate(1,1), (short) 0, (short) -1, (short) -1));
        grid.insertPerson(new Person(3, new Coordinate(3, 3), (short) 0, (short) -1, (short) -1));
        grid.insertPerson(new Person(4, new Coordinate(7, 5), (short) 0, (short) -1, (short) -1));

        for(int i =5; i<1000; i++){
            grid.insertPerson(new Person(i, null, (short) 0, (short) -1, (short) -1));
        }


        for(int i=0; i<500; i++){
            //System.out.println(i);
            grid.iteration(i);
            for(int r=0; r<grid.getBinsPerRow(); r++){
                for(int c=0; c<grid.getBinsPerCol(); c++){
                    if(grid.getBins()[r][c].getPeople().getStart() != null){
                        //System.out.println(grid.getBins()[r][c].getPeople());
                    }
                }
            }
        }
    }

    @Test
    public void watchBabyPurr(){
        Coordinate binSize = new Coordinate(20,20);
        Coordinate size = new Coordinate(4000, 4000);
        GridBins grid = new GridBins(null, size, binSize, 6, 2, 30, 1, 0, true);

        grid.insertPerson(new Person(0, null, (short) GridBins.publishInfectionAtTime, (short) -1, (short) -1));

        /*
        int j = 1;
        for(int r=0; r<size.getRow(); r+=10){
            for(int c=0; c<size.getCol(); c+=10) {
                grid.insertPerson(new Person(j, new Coordinate(r, c), 0));
                j++;
            }
        }*/

        for(int i = 1; i < 1000000; i++){
            grid.insertPerson(new Person(i, null, (short) 0, (short) -1, (short) -1));
        }
        /*
        Person iter = grid.getBins()[0][0].getPeople().getStart();
        while (iter != null){
            System.out.println(iter.getId());
            iter = iter.getNext();
        }*/


        // Run 500 rounds
        for(int i=0; i<150; i++){
            //System.out.println("Iteration: " + String.valueOf(i));
            grid.iteration(i);
        }
    }
}
