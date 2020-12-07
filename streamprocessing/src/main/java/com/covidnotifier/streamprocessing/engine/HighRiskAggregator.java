package com.covidnotifier.streamprocessing.engine;


import com.covidnotifier.streamprocessing.model.events.DomainEvent;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

public final class HighRiskAggregator
        extends RichCoFlatMapFunction<DomainEvent, DomainEvent, DomainEvent> {

    private ValueState<Boolean> seen = null;

    @Override
    public void open(Configuration config) {
        ValueStateDescriptor<Boolean> descriptor = new ValueStateDescriptor<>(
                // state name
                "have-seen-key",
                // type information of state
                TypeInformation.of(new TypeHint<Boolean>() {
                }));
        seen = getRuntimeContext().getState(descriptor);
    }

    @Override
    public void flatMap1(DomainEvent control, Collector<DomainEvent> out) throws Exception {
        seen.update(Boolean.TRUE);
    }

    @Override
    public void flatMap2(DomainEvent data, Collector<DomainEvent> out) throws Exception {
        if (seen.value() == Boolean.TRUE) {
            out.collect(data);
        }
    }
}
