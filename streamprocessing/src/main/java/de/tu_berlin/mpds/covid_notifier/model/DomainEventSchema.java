package de.tu_berlin.mpds.covid_notifier.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.tu_berlin.mpds.covid_notifier.model.events.DomainEvent;
import de.tu_berlin.mpds.covid_notifier.model.events.InfectionReported;
import de.tu_berlin.mpds.covid_notifier.model.events.PersonContact;
import de.tu_berlin.mpds.covid_notifier.model.events.PersonHealed;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

@Slf4j
public class DomainEventSchema implements DeserializationSchema<DomainEvent> {

    static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private static final long serialVersionUID = 1L;

    private static DomainEvent fromString(String input) {
        try {
            if (input.contains("InfectionReported")) {
                return objectMapper.readValue(input, InfectionReported.class);
            }
            if (input.contains("PersonContact")) {
                return objectMapper.readValue(input, PersonContact.class);
            }
            if (input.contains("PersonHealed")) {
                return objectMapper.readValue(input, PersonHealed.class);
            }
        }catch (Exception ex) {
            log.error("Could not deserialize the event from Kafka!", ex);

        }
        return null;
    }

    @Override
    public DomainEvent deserialize(byte[] bytes) throws IOException {
        return fromString(new String(bytes));
    }

    @Override
    public boolean isEndOfStream(DomainEvent domainEvent) {
        return false;
    }

    @Override
    public TypeInformation<DomainEvent> getProducedType() {
        return TypeInformation.of(DomainEvent.class);
    }
}
