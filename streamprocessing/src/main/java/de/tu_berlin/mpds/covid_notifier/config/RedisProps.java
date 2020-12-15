package de.tu_berlin.mpds.covid_notifier.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
@ToString
@EnableScheduling
public class RedisProps {

    private String host;

    private int port;

    private int timeout;
}
