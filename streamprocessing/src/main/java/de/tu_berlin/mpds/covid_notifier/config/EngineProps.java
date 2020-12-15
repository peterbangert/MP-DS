package de.tu_berlin.mpds.covid_notifier.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "engine")
@Data
@ToString
public class EngineProps {

    private String jobName;

}
