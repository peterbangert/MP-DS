package com.mpds.simulator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "simulator")
@Data
public class SimulatorProps {

    private long numberOfPeople;

    private int gridRows;

    private int gridColumns;

    private int binRows;

    private int binColumns;

    private int infectionDistance;

    private int daysInfected;

    private int ticksPerDay;

    private int publishInfectionAfterXDays;

    private String city;

    private int minMilliSecPerRound;

    private long initialInfectedPeople;
}
