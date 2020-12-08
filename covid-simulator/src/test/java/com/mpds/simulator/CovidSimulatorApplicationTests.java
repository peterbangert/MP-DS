package com.mpds.simulator;

import com.mpds.simulator.domain.model.Coordinate;
import com.mpds.simulator.domain.model.GridBins;
import com.mpds.simulator.domain.model.Person;
import org.junit.jupiter.api.Test;
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
        Coordinate size = new Coordinate(50000, 50000);
        Coordinate binSize = new Coordinate(500, 500);
        Coordinate overlap = new Coordinate(10, 10);
        GridBins grid = new GridBins(null, size, binSize, overlap, 6, 30);
        grid.insertPerson(new Person(0, null, 100, size));
        // Inserting 12000 persons
        for(int i=1; i<1000000; i++){
            grid.insertPerson(new Person(i, null, 0, size));
        }
        // Run 500 rounds
        for(int i=0; i<500; i++){
            System.out.println(i);
//            grid.iteration(i);
        }
    }
}
