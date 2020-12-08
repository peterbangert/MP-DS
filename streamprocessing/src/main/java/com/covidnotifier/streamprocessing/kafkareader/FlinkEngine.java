package com.covidnotifier.streamprocessing.kafkareader;


import com.covidnotifier.streamprocessing.aggregator.CovidPositiveAggregator;
import com.covidnotifier.streamprocessing.model.events.DomainEvent;
import com.covidnotifier.streamprocessing.model.events.DomainEventDeserializer;
import com.covidnotifier.streamprocessing.model.events.InfectionReported;
import com.covidnotifier.streamprocessing.model.events.PersonContact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducerBase;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.streaming.connectors.redis.RedisSink;
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommand;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisCommandDescription;
import org.apache.flink.streaming.connectors.redis.common.mapper.RedisMapper;
import org.apache.flink.util.Collector;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;


@Service
public class FlinkEngine {

    private static final ObjectMapper objectMapper = new ObjectMapper();

   private final StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

       private Properties properties = new Properties();

       @Bean
       public void setProperties(){
           properties.setProperty("bootstrap.servers", "localhost:9092");
           properties.setProperty("group.id", "covidAnalyser");
       }
/**
   @Bean
   public void eventAggregator() throws Exception {
       FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>(
               "covid",
               new SimpleStringSchema(),
               properties);
       kafkaSource.setStartFromEarliest();
       DataStream<String> stream = streamExecutionEnvironment.addSource(kafkaSource);

       SimpleModule module = new SimpleModule();
       module.addDeserializer(DomainEvent.class, new DomainEventDeserializer());
       objectMapper.registerModule(module);
       objectMapper.registerModule(new JavaTimeModule());

       stream
               .map(data -> {
                    try {
                       return objectMapper.readValue(data, DomainEvent.class);
                    } catch (Exception e) {
                       System.out.println("exception reading data: " + data);
                       e.printStackTrace();
                       return null;
                    }
                })
               .filter(Objects::nonNull)
               .keyBy(DomainEvent::eventType)
               .timeWindow(Time.seconds(5))
               .aggregate(new CovidPositiveAggregator())
               .map(data -> {
                   return data.f0 + ":" + data.f1 ;
               })
               .addSink(new FlinkKafkaProducer<String>(
                       "aggregate", new SimpleStringSchema(), properties
                       ))
               .name("Aggregator");

       streamExecutionEnvironment.execute();

   }
**/

/**
    @Bean
    public void infectionProducer() throws Exception {
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>(
                "covid",
                new SimpleStringSchema(),
                properties);
        kafkaSource.setStartFromEarliest();
        DataStream<String> stream = streamExecutionEnvironment.addSource(kafkaSource);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(DomainEvent.class, new DomainEventDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());

        stream
                .map(data -> {
                    try {
                        DomainEvent de = objectMapper.readValue(data, DomainEvent.class);
                        if (de instanceof InfectionReported) {
                            return Long.toString(((InfectionReported) de).getPersonId());
                        }
                        return null;
                    } catch (Exception e) {
                        System.out.println("exception reading data: " + data);
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .addSink(new FlinkKafkaProducer<String>(
                        "infections", new SimpleStringSchema(), properties
                ))
                .name("Infections");;

        streamExecutionEnvironment.execute();
    }
 **/

/**
    public static class RedisExampleMapper implements RedisMapper<Tuple2<String, String>> {

        @Override
        public RedisCommandDescription getCommandDescription() {
            return new RedisCommandDescription(RedisCommand.SADD);

        }

        @Override
        public String getKeyFromData(Tuple2<String, String> data) {
            return data.f0;
        }

        @Override
        public String getValueFromData(Tuple2<String, String> data) {
            return data.f1;
        }
    }

    @Bean
    public void contactProducer() throws Exception {
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>(
                "covid",
                new SimpleStringSchema(),
                properties);
        kafkaSource.setStartFromEarliest();
        DataStream<String> stream = streamExecutionEnvironment.addSource(kafkaSource);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(DomainEvent.class, new DomainEventDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());

        FlinkJedisPoolConfig conf = new FlinkJedisPoolConfig.Builder().setHost("127.0.0.1").build();

        DataStream<Tuple2<String,String>> p1p2Stream = stream
                .map(new MapFunction<String, Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> map(String data) throws Exception {
                        try {
                            DomainEvent de = objectMapper.readValue(data, DomainEvent.class);
                            if (de instanceof PersonContact) {
                                return new Tuple2<>(
                                        Long.toString(((PersonContact) de).getPerson1()),
                                        Long.toString(((PersonContact) de).getPerson2())
                                );
                            }
                            return null;
                        } catch (Exception e) {
                            System.out.println("exception reading data: " + data);
                            e.printStackTrace();
                            return null;
                        }
                    }
                })
                .filter(Objects::nonNull);

        DataStream<Tuple2<String,String>> p2p1Stream = p1p2Stream.project(1,0);

        DataStream<Tuple2<String,String>> aggregateContactStream = p1p2Stream.union(p2p1Stream);

        aggregateContactStream
                .addSink(new RedisSink<Tuple2<String, String>>(conf, new RedisExampleMapper()))
                .name("Contacts");
                //.print();

        streamExecutionEnvironment.execute();
    }
 **/
    /**
    @Bean
    public void highRiskAggregator() throws Exception {
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>(
                "infections",
                new SimpleStringSchema(),
                properties);
        kafkaSource.setStartFromEarliest();
        DataStream<String> stream = streamExecutionEnvironment.addSource(kafkaSource);


        stream
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String data, Collector<String> out) throws Exception {

                        try {
                            Jedis jedis = new Jedis();
                            Set<String> contacts = jedis.smembers(data);
                            contacts.stream().forEach(contact -> out.collect(contact));
                        } catch (Exception e) {
                            System.out.println("exception reading data: " + data);
                            e.printStackTrace();
                        }
                    }

                })
                .filter(Objects::nonNull)
                .addSink(new FlinkKafkaProducer<String>(
                        "highrisk", new SimpleStringSchema(), properties
                ))
                .name("HighRisk");;

        streamExecutionEnvironment.execute();
    }
**/
    @Bean
    public void lowRiskAggregator() throws Exception {
        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>(
                "highrisk",
                new SimpleStringSchema(),
                properties);
        kafkaSource.setStartFromEarliest();
        DataStream<String> stream = streamExecutionEnvironment.addSource(kafkaSource);


        stream
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String data, Collector<String> out) throws Exception {

                        try {
                            Jedis jedis = new Jedis();
                            Set<String> contacts = jedis.smembers(data);
                            contacts.stream().forEach(contact -> out.collect(contact));
                        } catch (Exception e) {
                            System.out.println("exception reading data: " + data);
                            e.printStackTrace();
                        }
                    }

                })
                .filter(Objects::nonNull)
                .addSink(new FlinkKafkaProducer<String>(
                        "lowrisk", new SimpleStringSchema(), properties
                ))
                .name("HighRisk");;

        streamExecutionEnvironment.execute();
    }

}
