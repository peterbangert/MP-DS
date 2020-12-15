package de.tu_berlin.mpds.covid_notifier.service;

import de.tu_berlin.mpds.covid_notifier.model.EngineConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService implements Serializable {

    private final JedisPool jedisPool;

    public LinkedHashSet<String> readContactSet(String personId) {
        try (Jedis jedis = jedisPool.getResource()) {
            try {
                LinkedHashSet<String> contacts = new LinkedHashSet<>(jedis.smembers(personId));
                Pipeline expirationPipeline = jedis.pipelined();
                contacts.forEach(s -> expirationPipeline.get(personId + ":" + s));
                List<Object> responses = expirationPipeline.syncAndReturnAll();

                for (int i = 0; i < contacts.size(); i++) {
                    if (responses.get(i) == null) {
                        contacts.remove(i);
                    }
                }
                return contacts;

            } catch (Exception e) {
                log.error("readContactSets: Jedis Issue : " + personId, e);
                return null;
            }
        }
    }

    public int writeContacts(String person1, String person2) {
        try (Jedis jedis = jedisPool.getResource()) {
            try {
                Pipeline contactPipeline = jedis.pipelined();
                contactPipeline.sadd(person1, person2);
                contactPipeline.sadd(person2, person1);
                contactPipeline.setex(person1 + ":" + person2, EngineConstants.TENDAYS, "1");
                contactPipeline.setex(person2 + ":" + person1, EngineConstants.TENDAYS, "1");
                contactPipeline.sync();
                return 1;
            } catch (Exception e) {
                log.error("writeContacts: Jedis Issue : " + person1 + ", " + person2, e);
                return 0;
            }
        }
    }

}
