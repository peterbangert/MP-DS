package com.mpds.simulator.domain.model.stats;

import com.mpds.simulator.application.runner.CovidSimulatorRunner;
import lombok.Data;

@Data
public class Stats {

    private long count;

    public void increment(){
        this.count++;
        if(CovidSimulatorRunner.listSize==this.count) {
            System.out.println("List Size: " + CovidSimulatorRunner.listSize + ", count: "+ this.count);
            if (CovidSimulatorRunner.completionTime == 0) CovidSimulatorRunner.completionTime = System.currentTimeMillis();

            long elasped = CovidSimulatorRunner.completionTime - CovidSimulatorRunner.start;
            double recordsPerSec = 1000.0 * CovidSimulatorRunner.listSize / (double) elasped;
            System.out.println("Records/s: " + recordsPerSec);

//        disposable.dispose();
            System.out.println("DONE!!!");
        }
    };
}
