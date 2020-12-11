package de.tu_berlin.mpds.covid_notifier.engine;

import de.tu_berlin.mpds.covid_notifier.model.InfectionReported;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class InfectionRedisMapper implements FlatMapFunction<InfectionReported, String> {

    @Override
    public void flatMap(InfectionReported data, Collector<String> out) throws Exception {
        try {
            //Jedis jedis = new Jedis();
            //Set<String> contacts = jedis.smembers(Long.toString(data.getPersonId()));
            
            Set<String> contacts = Redis.getContacts(Long.toString(data.getPersonId()));
            if (contacts != null) {
                contacts.stream().forEach(contact -> out.collect(contact));
            }
        } catch (Exception e) {
            System.out.println("Infection exception reading data: " + data);
            e.printStackTrace();
            System.exit(0);
        }
    }
}
