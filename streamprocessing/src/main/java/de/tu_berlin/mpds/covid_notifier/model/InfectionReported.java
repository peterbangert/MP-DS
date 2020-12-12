package de.tu_berlin.mpds.covid_notifier.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"uuid", "personId", "eventType"})
@JsonIgnoreProperties("occuredOn")
@Getter
public class InfectionReported extends DomainEvent {

    private final Long personId;

    @JsonCreator
    public InfectionReported(@JsonProperty("eventType") String eventType,
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
