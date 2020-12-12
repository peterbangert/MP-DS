package com.mpds.simulator.domain.model.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"uuid", "person1", "person2", "eventType", "occurredOn"})
@Getter
public class PersonContact extends DomainEvent {

    private final Long person1;
    private final Long person2;


    @JsonCreator
    public PersonContact(@JsonProperty("sequenceNumber") Long sequenceNumber, @JsonProperty("person1") Long person1, @JsonProperty("person2") Long person2, @JsonProperty("city") String city, @JsonProperty("occurredOn") LocalDateTime occurredOn) {
        super(UUID.randomUUID(), sequenceNumber, city, occurredOn);
        this.person1=person1;
        this.person2=person2;
    }

    @Override
    public String eventType() {
        return this.getClass().getSimpleName();
    }
}
