package com.covidnotifier.streamprocessing.kafkareader;


import com.covidnotifier.streamprocessing.aggregator.CovidPositiveAggregator;
import com.covidnotifier.streamprocessing.model.AppUser;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

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
       DataStream<String> stream = streamExecutionEnvironment.addSource(kafkaSource);


       KeyedStream<AppUser, String> appUserCovidPositive = stream.map(data -> {
           try {
               return objectMapper.readValue(data, AppUser.class);
           } catch (Exception e) {
               System.out.println("exception reading data: " + data);
              e.printStackTrace();
               return null;
           }
       }).filter(Objects::nonNull).keyBy(AppUser::getCovidPositive);

       DataStream<Tuple2<String, Long>> result = appUserCovidPositive.timeWindow(Time.seconds(5))
               .aggregate(new CovidPositiveAggregator());

       result.print();
       streamExecutionEnvironment.execute();

   }



}
