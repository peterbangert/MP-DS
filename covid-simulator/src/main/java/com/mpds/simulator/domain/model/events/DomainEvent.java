package com.mpds.simulator.domain.model.events;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public abstract class DomainEvent {

    private final UUID uuid;

    private final Long sequenceNumber;

    private final String city;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private final LocalDateTime occurredOn;

    @JsonProperty("eventType")
    public abstract String eventType();
}
