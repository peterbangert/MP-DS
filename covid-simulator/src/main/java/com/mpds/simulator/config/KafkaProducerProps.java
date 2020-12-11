package com.mpds.simulator.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
@ToString
@EnableScheduling
public class KafkaProducerProps {

    private String bootstrapServer;

    private String topic;

    private String clientIdConfig;

    private String acksConfig;

    private int maxRequestSize;

    private long bufferMemory;

    private int batchSize;
}
