package com.covidnotifier.streamprocessing.model.events;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
public abstract class DomainEvent  {

    final UUID uuid;
    final Long sequenceNumber;
    final String eventType;

    //@JsonIgnore
    final LocalDateTime occurredOn;

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

}

//@JsonIgnoreProperties("occuredOn")