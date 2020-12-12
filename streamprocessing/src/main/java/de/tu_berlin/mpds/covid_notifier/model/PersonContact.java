package de.tu_berlin.mpds.covid_notifier.model;

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
    public PersonContact(@JsonProperty("eventType") String eventType,
                        @JsonProperty("uuid") UUID uuid,
                         @JsonProperty("sequenceNumber") Long sequenceNumber,
                         @JsonProperty("person1") Long person1,
                         @JsonProperty("person2") Long person2,
                         @JsonProperty("occurredOn") LocalDateTime occurredOn,
                         @JsonProperty("city") String city) {
        super(eventType, uuid, sequenceNumber, occurredOn,city);
        this.person1=person1;
        this.person2=person2;
    }

    @Override
    public String eventType() {
        return this.getClass().getSimpleName();
    }
}
