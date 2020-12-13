package de.tu_berlin.mpds.covid_notifier.engine;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class FlinkEngine {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private  StreamExecutionEnvironment env;

    @Autowired
    private Properties properties;

    final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") {
    };


    @Bean
    public StreamExecutionEnvironment env(){
       env=  StreamExecutionEnvironment.getExecutionEnvironment();
        env.disableOperatorChaining();
       return env;
    }
    @Bean
    public Properties Properties() {
        properties.setProperty("bootstrap.servers", "kafka:9092");
        properties.setProperty("group.id", "covidAnalyser");
        return properties;
    }


    public static class InfectionRedisMapper implements FlatMapFunction<InfectionReported, String> {

        @Override
        public void flatMap(InfectionReported data, Collector<String> out) throws Exception {
            try {
                //Jedis jedis = new Jedis();
                //Set<String> contacts = jedis.smembers(Long.toString(data.getPersonId()));
                Set<String> contacts = PipelineRedis.getContacts(Long.toString(data.getPersonId()));
                if (contacts != null) {
                    contacts.stream().forEach(contact -> out.collect(contact));
                }
            } catch (Exception e) {
                System.out.println("Infection exception reading data: " + data);
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public static class DomainEventSplitter extends ProcessFunction<String, PersonContact> {

        final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") {
        };


        @Override
        public void processElement(String data, Context ctx, Collector<PersonContact> out) throws Exception {
            // emit Contacts to regular output
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            DomainEvent domainEvent = objectMapper.readValue(data, DomainEvent.class);
            if (domainEvent instanceof PersonContact) {
                out.collect((PersonContact) domainEvent);
            }
            // emit Infections to side output
            if (domainEvent instanceof InfectionReported) {
                ctx.output(this.outputTag, (InfectionReported) domainEvent);
            }
        }
    }


    @Bean
    public void highRiskContactProducer() throws Exception {
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
                    return PipelineRedis.writeContact(
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

        env.execute("Covid Engine");
    }

}
