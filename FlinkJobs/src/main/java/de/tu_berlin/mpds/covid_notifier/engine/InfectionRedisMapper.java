package de.tu_berlin.mpds.covid_notifier.engine;

import de.tu_berlin.mpds.covid_notifier.model.InfectionReported;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InfectionRedisMapper implements FlatMapFunction<InfectionReported, String> {

    @Override
    public void flatMap(InfectionReported data, Collector<String> out) throws Exception {
        try {
            Set<String> contacts = PipelineRedis.getContacts(Long.toString(data.getPersonId()));
            if (contacts != null) {
                contacts.forEach(contact -> out.collect(contact));
            }
        } catch (Exception e) {
            System.out.println("Infection exception reading data: " + data);
            e.printStackTrace();
            System.exit(0);
        }
    }
}
