package de.tu_berlin.mpds.covid_notifier.engine;


import de.tu_berlin.mpds.covid_notifier.config.EngineProps;
import de.tu_berlin.mpds.covid_notifier.config.KafkaProps;
import de.tu_berlin.mpds.covid_notifier.config.RedisProps;
import de.tu_berlin.mpds.covid_notifier.model.DomainEventSchema;
import de.tu_berlin.mpds.covid_notifier.model.EngineConstants;
import de.tu_berlin.mpds.covid_notifier.model.events.DomainEvent;
import de.tu_berlin.mpds.covid_notifier.model.events.InfectionReported;
import de.tu_berlin.mpds.covid_notifier.model.events.PersonContact;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.time.Time;
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

//    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private StreamExecutionEnvironment env;

    @Autowired
    private Properties properties;

    @Autowired
    private KafkaProps kafkaProps;

    @Autowired
    private EngineProps engineProps;

    @Autowired
    private RedisProps redisProps;

//    @Autowired
//    private RedisService redisService;

    final OutputTag<InfectionReported> INFECTION_OUTPUT_TAG = new OutputTag<InfectionReported>(EngineConstants.INFECTION_REPORTED_TAG_NAME) {
    };

    /**
     * StreamExecutionEnvironment
     *
     * @return StreamExecutionEnvironment
     * Initialize streaming environment config:
     * - Fail Rate policy
     * - HDFS Checkpointing
     * - Disabling Chain Operations
     */
    @Bean
    public StreamExecutionEnvironment env() {
        env = StreamExecutionEnvironment.getExecutionEnvironment();
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
     * Infection Mapper
     *
     * @param InfectionReported
     * @return String
     * <p>
     * Request contacts from Redis of set of contacts for personId from param
     */
    @Slf4j
    public static class InfectionRedisMapper implements FlatMapFunction<InfectionReported, String> {

        @Override
        public void flatMap(InfectionReported data, Collector<String> out) throws Exception {
            try {
                Set<String> contacts = RedisReadContacts.getContacts(Long.toString(data.getPersonId()));
//                Set<String> contacts = this.redisService.readContactSet(Long.toString(data.getPersonId()));
//                Set<String> contacts = RedisReadContacts.getContacts(Long.toString(data.getPersonId()));
                if (contacts != null) {
                    contacts.forEach(out::collect);
                }
            } catch (Exception e) {
                log.error("Infection exception reading data: ", e);
                System.exit(0);
            }
        }
    }

    /**
     * DomainEventSplitter
     *
     * @param DomainEvent
     * @return PersonContact
     * <p>
     * Gathers PersonContact events into outgoing collector, places InfectionReported in context with tag
     */
    public static class DomainEventSplitter extends ProcessFunction<DomainEvent, PersonContact> {

        final OutputTag<InfectionReported> outputTag = new OutputTag<InfectionReported>("InfectionReported") {
        };

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
     *
     * @throws Exception 1. Initialize Kafka Consumer to topic 'covid' from earliest start. Desierialize to DomainEvent
     *                   2. Add Kafka Consumer as environment source
     *                   3. Split the Stream into PersonContact and InfectionReported streams
     *                   4. Process Contact Stream: write to Redis
     *                   5. Process Infection Stream: Request Contact set from Redis, sink to Kafka topic 'highrisk'
     */
    @Bean
    public void highRiskContactProducer() throws Exception {

        // Read redis config
        // TODO: To be optimized
        RedisWriteContacts.redisHostname = redisProps.getHost();
        RedisWriteContacts.redisPort = redisProps.getPort();
        RedisWriteContacts.redisTimeout = redisProps.getTimeout();

        RedisReadContacts.redisHostname = redisProps.getHost();
        RedisReadContacts.redisPort = redisProps.getPort();
        RedisReadContacts.redisTimeout = redisProps.getTimeout();

        // 1. Initialize Kafka Consumer to topic 'covid' from earliest start. Desierialize to DomainEvent
        FlinkKafkaConsumer<DomainEvent> covidSource = new FlinkKafkaConsumer<>(
                kafkaProps.getCovidTopic(),
                new DomainEventSchema(),
                properties);
        covidSource.setStartFromEarliest();

        // 2. Add Kafka Consumer as environment source
        DataStream<DomainEvent> covidStream = env.addSource(covidSource)
                .name(EngineConstants.COVID_STREAM_NAME);

        // 3. Split the Stream into PersonContact and InfectionReported streams
        SingleOutputStreamOperator<PersonContact> contactStream = covidStream
                .process(new DomainEventSplitter());

        DataStream<InfectionReported> infectionsStream = contactStream
                .getSideOutput(INFECTION_OUTPUT_TAG);

        // 4. Process Contact Stream: write to Redis
        contactStream
//                .map(data -> redisService.writeContacts(Long.toString(data.getPerson1()), Long.toString(data.getPerson2())))
                .map(data -> {
                    return RedisWriteContacts.writeContact(
                            Long.toString(data.getPerson1()),
                            Long.toString(data.getPerson2()));
                })
                .name(EngineConstants.CONTACT_STREAM_NAME);

        // 5. Process Infection Stream: Request Contact set from Redis, sink to Kafka topic 'highrisk'
        infectionsStream
                .flatMap(new InfectionRedisMapper())
//                .flatMap(new InfectionRedisMapper(redisService))
                .filter(Objects::nonNull)
                .addSink(new FlinkKafkaProducer<>(
                        kafkaProps.getHighRiskTopic(), new SimpleStringSchema(), properties
                ))
                .name(kafkaProps.getHighRiskTopic());

        // Execute
        env.execute(engineProps.getJobName());
    }

}
