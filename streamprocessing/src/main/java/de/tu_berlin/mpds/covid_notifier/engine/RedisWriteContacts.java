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
public class RedisWriteContacts {

    public static class Connector implements Closeable {

        private static Connector self = null;
        private Jedis jedis ;
        private final int TENDAYS = 864000;

        private Connector() {

            this.jedis = new Jedis("redis", 6379, 1800);
        }


        private int writeContacts(String person1, String person2) {
            try {
                Pipeline contactPipeline = jedis.pipelined();
                contactPipeline.sadd(person1, person2);
                contactPipeline.sadd(person2, person1);
                contactPipeline.setex(person1 + ":" + person2, this.TENDAYS, "1");
                contactPipeline.setex(person2 + ":" + person1, this.TENDAYS, "1");
                contactPipeline.sync();
                return 1;
            } catch (Exception e) {
                System.out.println("writeContacts: Jedis Issue : " + person1 + ", " + person2);
                e.printStackTrace();
                return 0;
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

    public static int writeContact(String person1, String person2) {

        Connector connector = Connector.getInstance();
        return connector.writeContacts(person1, person2);
    }


}

