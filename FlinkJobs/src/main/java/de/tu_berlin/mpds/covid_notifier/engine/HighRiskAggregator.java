package de.tu_berlin.mpds.covid_notifier.engine;


import de.tu_berlin.mpds.covid_notifier.config.Configuration;
import de.tu_berlin.mpds.covid_notifier.model.DomainEvent;
import de.tu_berlin.mpds.covid_notifier.model.DomainEventDeserializer;
import de.tu_berlin.mpds.covid_notifier.model.InfectionReported;
import de.tu_berlin.mpds.covid_notifier.model.PersonContact;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;



public class HighRiskAggregator {


    public static void main(String[] args) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();
        //Configuration.BOOTSTRAPSERVERADDRESS
        properties.setProperty("bootstrap.servers", "localhost:9092");
        properties.setProperty("group.id", "covidAnalyser");
        final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") {
        };
        //highRiskContactProducer(env,properties,objectMapper);

        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                3, // max failures per interval
                Time.of(5, TimeUnit.MINUTES), //time interval for measuring failure rate
                Time.of(10, TimeUnit.SECONDS) // delay
        ));

        

        SimpleModule module = new SimpleModule();
        module.addDeserializer(DomainEvent.class, new DomainEventDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());
        
        FlinkKafkaConsumer<String> covidSource = new FlinkKafkaConsumer<>(
                "covid",
                new SimpleStringSchema(),
                properties);
        covidSource.setStartFromEarliest();
        DataStream<String> covidStream = env.addSource(covidSource);

        

      //  final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") };


        SingleOutputStreamOperator<PersonContact> contactStream = covidStream
                .process(new DomainEventSplitter());


        DataStream<InfectionReported> infectionsStream = contactStream
                .getSideOutput(outputTag);

        contactStream
                .map(data -> {
                    return Redis.writeContact(
                            Long.toString(data.getPerson1()),
                            Long.toString(data.getPerson2()));
                })
                .name("Contacts");

        infectionsStream
                .flatMap(new InfectionRedisMapper())
                .filter(Objects::nonNull)
                .addSink(new FlinkKafkaProducer<String>(
                        "highrisk", new SimpleStringSchema(), properties
                ))
                .name("HighRisk");

        infectionsStream.print();
        contactStream.print();

        env.execute("HighRiskAggregator");

    }

  /*  public static void highRiskContactProducer(StreamExecutionEnvironment env ,Properties properties,ObjectMapper objectMapper) throws Exception {
        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                3, // max failures per interval
                Time.of(5, TimeUnit.MINUTES), //time interval for measuring failure rate
                Time.of(10, TimeUnit.SECONDS) // delay
        ));
        FlinkKafkaConsumer<String> covidSource = new FlinkKafkaConsumer<>(
                "covid",
                new SimpleStringSchema(),
                properties);
        covidSource.setStartFromEarliest();
        DataStream<String> covidStream = env.addSource(covidSource);

        SimpleModule module = new SimpleModule();
        module.addDeserializer(DomainEvent.class, new DomainEventDeserializer());
        objectMapper.registerModule(module);
        objectMapper.registerModule(new JavaTimeModule());

        final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") {
        };


        SingleOutputStreamOperator<PersonContact> contactStream = covidStream
                .process(new DomainEventSplitter());


        DataStream<InfectionReported> infectionsStream = contactStream
                .getSideOutput(outputTag);

        contactStream
                .map(data -> {
                    return Redis.writeContact(
                            Long.toString(data.getPerson1()),
                            Long.toString(data.getPerson2()));
                })
                .name("Contacts");

        infectionsStream
                .flatMap(new InfectionRedisMapper())
                .filter(Objects::nonNull)
                .addSink(new FlinkKafkaProducer<String>(
                        "highrisk", new SimpleStringSchema(), properties
                ))
                .name("HighRisk");

        infectionsStream.print();
        contactStream.print();

        env.execute("Covid Engine");
    }


   */

}
