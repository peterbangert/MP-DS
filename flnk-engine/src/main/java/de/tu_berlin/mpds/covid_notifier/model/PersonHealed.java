package de.tu_berlin.mpds.covid_notifier.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"uuid", "personId", "eventType", "occurredOn"})
public class PersonHealed extends DomainEvent{

    private final Long personId;

    @JsonCreator
    public PersonHealed(@JsonProperty("eventType") String eventType,
                        @JsonProperty("uuid") UUID uuid,
                        @JsonProperty("sequenceNumber") Long sequenceNumber,
                        @JsonProperty("personId") Long personId,
                        @JsonProperty("occurredOn") LocalDateTime occurredOn,
                        @JsonProperty("city") String city) {
        super(eventType, uuid, sequenceNumber, occurredOn,city);
        this.personId=personId;
    }

    @Override
    public String eventType() {
        return this.getClass().getSimpleName();
    }
}
