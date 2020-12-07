package com.covidnotifier.streamprocessing.model.events;



import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
@Slf4j
public class DomainEventDeserializer extends JsonDeserializer<DomainEvent> {


    @Override
    public DomainEvent deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode tree = codec.readTree(jsonParser);

        try {
            if (tree.get("eventType").asText().equals("InfectionReported")) {
                return codec.treeToValue(tree, InfectionReported.class);
            }
            if (tree.get("eventType").asText().equals("PersonContact")) {
                return codec.treeToValue(tree, PersonContact.class);
            }
            if (tree.get("eventType").asText().equals("PersonHealed")) {
                return codec.treeToValue(tree, PersonHealed.class);
            }
        }catch (Exception ex) {
           log.error("Could not deserialize the event from Kafka!",ex);
           throw ex;
        }

        throw new UnsupportedOperationException("Cannot deserialize to a known type");

    }
}
