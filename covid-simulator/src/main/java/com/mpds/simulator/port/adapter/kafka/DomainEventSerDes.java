package com.mpds.simulator.port.adapter.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpds.simulator.domain.model.events.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

@Slf4j
public class DomainEventSerDes implements Serializer<DomainEvent> {
    @Override
    public byte[] serialize(String topic, DomainEvent data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
            log.error("Could not publish the event to Kafka!",e);
        }
        return retVal;
    }
}
