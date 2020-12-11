package de.tu_berlin.mpds.covid_notifier.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tu_berlin.mpds.covid_notifier.model.DomainEvent;
import de.tu_berlin.mpds.covid_notifier.model.InfectionReported;
import de.tu_berlin.mpds.covid_notifier.model.PersonContact;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public  class DomainEventSplitter extends ProcessFunction<String, PersonContact> {

    final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") {
    };

    

    @Override
    public void processElement(String data, Context ctx, Collector<PersonContact> out) throws Exception {
        // emit Contacts to regular output
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        DomainEvent domainEvent = objectMapper.readValue(data, DomainEvent.class);
        if (domainEvent instanceof PersonContact) {
            out.collect((PersonContact) domainEvent);
        }
        // emit Infections to side output
        if (domainEvent instanceof InfectionReported) {
            ctx.output(this.outputTag, (InfectionReported) domainEvent);
        }
    }
}
