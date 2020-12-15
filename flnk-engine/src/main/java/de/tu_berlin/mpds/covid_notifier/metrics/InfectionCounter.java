package de.tu_berlin.mpds.covid_notifier.metrics;

import de.tu_berlin.mpds.covid_notifier.model.InfectionReported;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;

public class InfectionCounter extends RichMapFunction<InfectionReported, InfectionReported> {
    private transient Counter counter;

    @Override
    public void open(Configuration config) {
        this.counter = getRuntimeContext()
                .getMetricGroup()
                .counter("infectionCounter");
    }

    @Override
    public InfectionReported map(InfectionReported value) throws Exception {
        this.counter.inc();
        return value;
    }
}