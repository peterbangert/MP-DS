package de.tu_berlin.mpds.covid_notifier.engine;


import de.tu_berlin.mpds.covid_notifier.config.RedisConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;


import java.io.Closeable;
import java.io.IOException;
import java.util.*;

@Service
public class RedisReadContacts {

    public static class Connector implements Closeable {

        private static Connector self = null;
        private Jedis jedis ;
        private final int TENDAYS = 864000;

        private Connector() {

            this.jedis = new Jedis("redis", 6379, 1800);
        }



        private LinkedHashSet<String> readContactSet(String personId) {
            try {
                LinkedHashSet<String> contacts = new LinkedHashSet<>( jedis.smembers(personId));
                Pipeline expirationPipeline = jedis.pipelined();
                contacts.stream().forEach(s -> expirationPipeline.get(personId + ":" + s));
                List<Object> responses = expirationPipeline.syncAndReturnAll();

                for (int i = 0; i < contacts.size(); i++) {
                    if (responses.get(i) == null) {
                        contacts.remove(i);
                    }
                }
                return contacts;

            } catch (Exception e) {
                System.out.println("readContactSets: Jedis Issue : " + personId);
                e.printStackTrace();
                return null;
            }
        }

        public static synchronized Connector getInstance() {
            if (self == null) self = new Connector();
            return self;
        }

        @Override
        public void close() throws IOException {

            this.jedis.close();
        }
    }


    public static LinkedHashSet<String> getContacts(String personId) {

        Connector connector = Connector.getInstance();
        return connector.readContactSet(personId);
    }
}

