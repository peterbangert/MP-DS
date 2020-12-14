package de.tu_berlin.mpds.covid_notifier.config;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;


@Data
@ToString
@Getter
public class RedisConfig {

    public static final String host="redis";

    public static final Integer port=6379;
}