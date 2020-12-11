package com.mpds.simulator.port.adapter.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpds.simulator.config.KafkaProducerProps;
import com.mpds.simulator.domain.model.events.DomainEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class DomainEventPublisher {

    private final ObjectMapper objectMapper;

    private final KafkaProducerProps kafkaProducerProps;
    private final KafkaSender<String, byte[]> sender;

    private final SimpleDateFormat dateFormat;

    public DomainEventPublisher(ObjectMapper objectMapper, KafkaProducerProps kafkaProducerProps) {
        this.objectMapper=objectMapper;
        this.kafkaProducerProps = kafkaProducerProps;

        String BOOTSTRAP_SERVERS = this.kafkaProducerProps.getBootstrapServer();
        String CLIENT_ID_CONFIG = this.kafkaProducerProps.getClientIdConfig();
        String ACK_CONFIG = this.kafkaProducerProps.getAcksConfig();

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID_CONFIG);
        props.put(ProducerConfig.ACKS_CONFIG, ACK_CONFIG);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        SenderOptions<String, byte[]> senderOptions = SenderOptions.create(props);

        sender = KafkaSender.create(senderOptions);
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");
    }

//    public Mono<Void> sendMessages(DomainEvent domainEvent) {
//        ProducerRecord<String, DomainEvent> producerRecord = new ProducerRecord<>(kafkaProducerProps.getTopic(), domainEvent.getUuid().toString(), domainEvent);
//
//        return sender.send(Mono.just(SenderRecord.create(producerRecord, domainEvent.getUuid().toString())))
//                .doOnNext(r -> {
//                    RecordMetadata metadata = r.recordMetadata();
//                    System.out.printf("Message %s sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
//                            r.correlationMetadata(),
//                            metadata.topic(),
//                            metadata.partition(),
//                            metadata.offset(),
//                            dateFormat.format(new Date(metadata.timestamp())));
//                }).next()
//                .then()
//                .doOnError(e -> log.error("Sending to Kafka failed:"+  e.getMessage()));
//    }

    public Mono<Void> publishEvent(DomainEvent domainEvent, CountDownLatch latch) {
        ProducerRecord<String, byte[]> producerRecord = new ProducerRecord<>(kafkaProducerProps.getTopic(), domainEvent.getUuid().toString(), toBytes(domainEvent));
        return sender.send(Mono.just(SenderRecord.create(producerRecord, domainEvent.getUuid().toString())))
                .doOnNext(r -> {
                    latch.countDown();
//                    RecordMetadata metadata = r.recordMetadata();
//                    System.out.printf("Message %s sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
//                            r.correlationMetadata(),
//                            metadata.topic(),
//                            metadata.partition(),
//                            metadata.offset(),
//                            dateFormat.format(new Date(metadata.timestamp())));
                })
                .then()
                .doOnError(e -> log.error("Sending to Kafka failed:"+  e.getMessage()));
    }

    private byte[] toBytes(DomainEvent domainEvent){
        byte[] payload = new byte[domainEvent.toString().getBytes().length];
        try {
            payload = objectMapper.writeValueAsString(domainEvent).getBytes();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return payload;
    }
}
