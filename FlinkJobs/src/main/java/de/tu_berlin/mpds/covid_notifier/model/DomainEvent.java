package de.tu_berlin.mpds.covid_notifier.model;

import com.fasterxml.jackson.annotation.*;

import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "eventType",
        defaultImpl = InfectionReported.class
)
@JsonSubTypes({
        @JsonSubTypes.Type( value = InfectionReported.class, name = "InfectionReported" ),
        @JsonSubTypes.Type( value = PersonContact.class, name = "PersonContact" ),
        @JsonSubTypes.Type( value = PersonHealed.class, name ="PersonHealed")
})
@JsonPropertyOrder({"uuid", "personId","occuredOn"})
@Getter
@Setter
public abstract class DomainEvent  {

     UUID uuid;
     Long sequenceNumber;
     String eventType;

    //@JsonIgnore
     LocalDateTime occurredOn;

    @JsonCreator
    public DomainEvent(@JsonProperty("eventType") String eventType,
                       @JsonProperty("uuid") UUID uuid,
                       @JsonProperty("sequenceNumber") Long sequenceNumber,
                       @JsonProperty("occurredOn") LocalDateTime occurredOn) {
        this.eventType=eventType;
        this.uuid=uuid;
        this.sequenceNumber=sequenceNumber;
        this.occurredOn=occurredOn;
    }

    @JsonProperty("eventType")
    public abstract String eventType();

    public String getTime() {
        return occurredOn.toString();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    public void setOccurredOn(LocalDateTime occurredOn) {
        this.occurredOn = occurredOn;
    }
}

//@JsonIgnoreProperties("occuredOn")