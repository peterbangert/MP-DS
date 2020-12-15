package com.mpds.simulator.domain.model.stats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class SimulationStats {

    private String writeToPath;
    //private File writeToFile;
    private PrintWriter printWriter;

    public SimulationStats(String writeToPath){
        this.writeToPath = writeToPath;
        try {
            printWriter = new PrintWriter(writeToPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        makeHeader();
    }

    public void makeHeader(){
        printWriter.println("round,time-of-day,contacts,infections,healed,duration(ms)");
    }

    public void addRoundToFile(long round, long timeOfDay, int contacts, int infection, int healed, long duration){
        String print = String.join(",", String.valueOf(round), String.valueOf(timeOfDay), String.valueOf(contacts), String.valueOf(infection), String.valueOf(healed), String.valueOf(duration));
        printWriter.println(print);
        printWriter.flush();
    }
}
