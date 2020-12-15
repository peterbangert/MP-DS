package de.tu_berlin.mpds.covid_notifier.metrics;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;


public class HighRiskCounter extends RichMapFunction<String, String> {
    private transient Counter counter;

    @Override
    public void open(Configuration config) {
        this.counter = getRuntimeContext()
                .getMetricGroup()
                .counter("highRiskCounter");
    }

    @Override
    public String map(String value) throws Exception {
        this.counter.inc();
        return value;
    }
}