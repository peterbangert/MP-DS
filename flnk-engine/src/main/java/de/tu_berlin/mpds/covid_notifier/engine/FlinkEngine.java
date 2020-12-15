package de.tu_berlin.mpds.covid_notifier.engine;


import de.tu_berlin.mpds.covid_notifier.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
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

    /**
     * StreamExecutionEnvironment
     * @return StreamExecutionEnvironment
     * Initialize streaming environment config:
     *      - Fail Rate policy
     *      - HDFS Checkpointing
     *      - Disabling Chain Operations
     */
    @Bean
    public StreamExecutionEnvironment env(){
        env=  StreamExecutionEnvironment.getExecutionEnvironment();
        env.disableOperatorChaining();
        env.enableCheckpointing(1000000000L);
        CheckpointConfig config = env.getCheckpointConfig();
        config.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        env.setRestartStrategy(RestartStrategies.failureRateRestart(
                3, // max failures per interval
                Time.of(5, TimeUnit.MINUTES), //time interval for measuring failure rate
                Time.of(10, TimeUnit.SECONDS) // delay
        ));
        return env;
    }

    /**
     * Properties
     * @return properties
     * Kafka Configuration
     */
    @Bean
    public Properties Properties() {
        properties.setProperty("bootstrap.servers", "kafka:9092");
        properties.setProperty("group.id", "covidAnalyser");
        return properties;
    }


    public static class InfectionRedisMapper extends RichFlatMapFunction<InfectionReported, String> {

        private transient Counter infectionCounter;
        private transient Counter highRiskCounter;

        @Override
        public void open(Configuration config) {
            this.highRiskCounter = getRuntimeContext()
                    .getMetricGroup()
                    .counter("highRiskCounter");
            this.infectionCounter = getRuntimeContext()
                    .getMetricGroup()
                    .counter("infectionCounter");
        }

        /**
         * Infection Mapper
         * @param  data 
         * @return String
         *
         * Request contacts from Redis of set of contacts for personId from param
         */
        @Override
        public void flatMap(InfectionReported data, Collector<String> out) throws Exception {
            try {
                infectionCounter.inc();
                Set<String> contacts = RedisReadContacts.getContacts(Long.toString(data.getPersonId()));
                if (contacts != null) {
                    contacts.stream().forEach(contact -> {
                        highRiskCounter.inc();
                        out.collect(contact);
                    });
                }
            } catch (Exception e) {
                System.out.println("Infection exception reading data: " + data);
                e.printStackTrace();
                System.exit(0);
            }
        }
    }


    public static class DomainEventSplitter extends ProcessFunction<DomainEvent, PersonContact> {

        final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") {
        };

        /**
         * DomainEventSplitter
         * @param data
         * @return PersonContact
         *
         * Gathers PersonContact events into outgoing collector, places InfectionReported in context with tag
         */

        @Override
        public void processElement(DomainEvent data, Context ctx, Collector<PersonContact> out) throws Exception {

            // emit Contacts to regular output
            if (data instanceof PersonContact) {
                out.collect((PersonContact) data);
            }
            // emit Infections to side output
            if (data instanceof InfectionReported) {
                ctx.output(this.outputTag, (InfectionReported) data);
            }
        }
    }

    /**
     * highRiskContactProducer
     * @throws Exception
     *
     * 1. Initialize Kafka Consumer to topic 'covid' from earliest start. Desierialize to DomainEvent
     * 2. Add Kafka Consumer as environment source
     * 3. Split the Stream into PersonContact and InfectionReported streams
     * 4. Process Contact Stream: write to Redis
     * 5. Process Infection Stream: Request Contact set from Redis, sink to Kafka topic 'highrisk'
     */
    @Bean
    public void highRiskContactProducer() throws Exception {

        // 1. Initialize Kafka Consumer to topic 'covid' from earliest start. Desierialize to DomainEvent
        FlinkKafkaConsumer<DomainEvent> covidSource = new FlinkKafkaConsumer<>(
                "covid",
                new DomainEventSchema(),
                properties);
       // covidSource.setStartFromEarliest();

        // 2. Add Kafka Consumer as environment source
        DataStream<DomainEvent> covidStream = env.addSource(covidSource)
                .name("Covid Data");

        // 3. Split the Stream into PersonContact and InfectionReported streams
        SingleOutputStreamOperator<PersonContact> contactStream = covidStream
                .process(new DomainEventSplitter());

        DataStream<InfectionReported> infectionsStream = contactStream
                .getSideOutput(outputTag);

        // 4. Process Contact Stream: write to Redis
        contactStream
                .map(data -> {
                    return RedisWriteContacts.writeContact(
                            Long.toString(data.getPerson1()),
                            Long.toString(data.getPerson2()));
                })
                .name("Contacts");

        // 5. Process Infection Stream: Request Contact set from Redis, sink to Kafka topic 'highrisk'
        infectionsStream
                .flatMap(new InfectionRedisMapper())
                .filter(Objects::nonNull)
                .addSink(new FlinkKafkaProducer<String>(
                        "highrisk", new SimpleStringSchema(), properties
                ))
                .name("HighRisk");

        // Execute
        env.execute("Covid Engine");
    }

}
