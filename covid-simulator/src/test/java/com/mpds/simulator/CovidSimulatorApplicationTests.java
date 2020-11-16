package com.mpds.simulator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CovidSimulatorApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testGrid(){
        Coordinate size = new Coordinate(22, 22);
        Coordinate binSize = new Coordinate(3, 3);
        Coordinate overlap = new Coordinate(1, 1);
        GridBins grid = new GridBins(size, binSize, overlap);
    }

}
