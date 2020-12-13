package com.mpds.simulator.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaProducerProps {

    private String bootstrapServer;

    private String topic;

    private String clientIdConfig;

    private String acksConfig;

    private int maxRequestSize;

    private long bufferMemory;

    private int batchSize;

    private long lingerMs;
}
