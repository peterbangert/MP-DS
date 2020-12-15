package de.tu_berlin.mpds.covid_notifier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaConfig {

    /**
     * Properties
     *
     * @return properties
     * Kafka Configuration
     */
    @Bean
    public Properties properties(Properties properties, KafkaProps kafkaProps) {
        properties.setProperty("bootstrap.servers", kafkaProps.getBootstrapServer());
        properties.setProperty("group.id", kafkaProps.getGroupId());
        return properties;
    }
}

