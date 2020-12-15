package de.tu_berlin.mpds.covid_notifier.engine;


import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;

@Service
public class RedisWriteContacts {

    public static String redisHostname="redis";
    public static int redisPort=6379;
    public static int redisTimeout=1800;


    public static class Connector implements Closeable {

        private static Connector self = null;
        private final JedisPool jedisPool;
        private final int TENDAYS = 864000;

        private Connector() {

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


        private int writeContacts(String person1, String person2) {
            try (Jedis jedis = jedisPool.getResource()) {
                try {
                    Pipeline contactPipeline = jedis.pipelined();
                    contactPipeline.sadd(person1, person2);
                    contactPipeline.sadd(person2, person1);
                    contactPipeline.setex(person1 + ":" + person2, TENDAYS, "1");
                    contactPipeline.setex(person2 + ":" + person1, TENDAYS, "1");
                    contactPipeline.sync();
                    return 1;
                } catch (Exception e) {
                    System.out.println("writeContacts: Jedis Issue : " + person1 + ", " + person2);
                    e.printStackTrace();
                    return 0;
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

    public static int writeContact(String person1, String person2) {

        Connector connector = Connector.getInstance();
        return connector.writeContacts(person1, person2);
    }


}


