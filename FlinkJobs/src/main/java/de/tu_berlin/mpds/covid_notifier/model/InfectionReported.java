package de.tu_berlin.mpds.covid_notifier.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonPropertyOrder({"uuid", "personId", "eventType"})
@JsonIgnoreProperties("occuredOn")
public class InfectionReported extends DomainEvent {

    private  Long personId;

    @JsonCreator
    public InfectionReported(@JsonProperty("eventType") String eventType,
                             @JsonProperty("uuid") UUID uuid,
                             @JsonProperty("sequenceNumber") Long sequenceNumber,
                             @JsonProperty("personId") Long personId,
                             @JsonProperty("occurredOn") LocalDateTime occurredOn) {
        super(eventType, uuid, sequenceNumber, occurredOn);
        this.personId=personId;
    }

    public InfectionReported(String eventType, UUID uuid, Long sequenceNumber, LocalDateTime occurredOn, Long personId) {
        super(eventType, uuid, sequenceNumber, occurredOn);
        this.personId = personId;
    }

    public Long getPersonId() {
        return personId;
    }
    public void setPersonId(Long personId){
        this.personId = personId;
    }

    @Override
    public String eventType() {
        return this.getClass().getSimpleName();
    }
}
