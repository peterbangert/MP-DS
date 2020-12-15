package de.tu_berlin.mpds.covid_notifier.engine;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@Slf4j
public class RedisReadContacts {

    public static String redisHostname;
    public static int redisPort;
    public static int redisTimeout;

    public static class Connector implements Closeable {

        private static Connector self = null;
//        private Jedis jedis ;
//        private final int TENDAYS = 864000;

        private JedisPool jedisPool;

        private Connector() {
//            this.jedis = new Jedis("redis", 6379, 1800);
            final JedisPoolConfig poolConfig = buildPoolConfig();
            this.jedisPool = new JedisPool(poolConfig, redisHostname, redisPort, redisTimeout);
        }

        private JedisPoolConfig buildPoolConfig() {
            final JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(128);
            poolConfig.setMaxIdle(128);
            poolConfig.setMinIdle(16);
            poolConfig.setTestOnBorrow(true);
            poolConfig.setTestOnReturn(true);
            poolConfig.setTestWhileIdle(true);
            poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
            poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
            poolConfig.setNumTestsPerEvictionRun(3);
            poolConfig.setBlockWhenExhausted(true);
            return poolConfig;
        }

        private LinkedHashSet<String> readContactSet(String personId) {
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

        public static synchronized Connector getInstance() {
            if (self == null) self = new Connector();
            return self;
        }

        @Override
        public void close() throws IOException {
            this.jedisPool.close();
        }
    }


    public static LinkedHashSet<String> getContacts(String personId) {

        Connector connector = Connector.getInstance();
        return connector.readContactSet(personId);
    }
}

