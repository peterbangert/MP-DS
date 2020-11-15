package com.mpds.simulator.domain.model;

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
@JsonPropertyOrder({"uuid", "personContactList", "eventType", "occurredOn"})
@Getter
public class PersonContact extends DomainEvent {

    private final Long[] personContactList;

    @JsonCreator
    public PersonContact(@JsonProperty("personContactList") Long[] personContactList, @JsonProperty("occurredOn") LocalDateTime occurredOn) {
        super(UUID.randomUUID(), occurredOn);
        this.personContactList=personContactList;
    }

    @Override
    public String eventType() {
        return this.getClass().getSimpleName();
    }
}
