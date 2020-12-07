package com.covidnotifier.streamprocessing.kafkareader;


import com.covidnotifier.streamprocessing.aggregator.CovidPositiveAggregator;
import com.covidnotifier.streamprocessing.model.AppUser;
import com.covidnotifier.streamprocessing.model.events.DomainEvent;
import com.covidnotifier.streamprocessing.model.events.DomainEventDeserializer;
import com.covidnotifier.streamprocessing.model.events.InfectionReported;
import com.covidnotifier.streamprocessing.model.events.PersonContact;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Objects;
import java.util.Properties;



@Service
public class ReadFromKafka {

    private static final ObjectMapper objectMapper = new ObjectMapper();

   private final StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

       private Properties properties = new Properties();

       @Bean
       public void setProperties(){
           properties.setProperty("bootstrap.servers", "localhost:9092");
           properties.setProperty("group.id", "covidAnalyser");
       }


   @Bean
   public void aggregatingPositiveUser() throws Exception {
       FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>("covid", new SimpleStringSchema(), properties);
       kafkaSource.setStartFromEarliest();
       //FlinkKafkaProducer<String> kafkaSink = new FlinkKafkaProducer<>("highrisk", new SimpleStringSchema(), properties);
       DataStream<String> stream = streamExecutionEnvironment.addSource(kafkaSource);

       SimpleModule module = new SimpleModule();
       module.addDeserializer(DomainEvent.class, new DomainEventDeserializer());
       objectMapper.registerModule(module);
       objectMapper.registerModule(new JavaTimeModule());

       KeyedStream<DomainEvent, String> covidEvents = stream.map(data -> {
           try {
               return objectMapper.readValue(data, DomainEvent.class);
           } catch (Exception e) {
               System.out.println("exception reading data: " + data);
              e.printStackTrace();
               return null;
           }
       }).filter(Objects::nonNull).keyBy(DomainEvent::eventType);

       //covidEvents.print();

       //covidEvents.keyBy();

       DataStream<Tuple2<String, Long>> result = covidEvents.timeWindow(Time.seconds(5))
               .aggregate(new CovidPositiveAggregator());
       result.print();

       streamExecutionEnvironment.execute();

   }



}
