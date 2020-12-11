package de.tu_berlin.mpds.covid_notifier.engine;

import de.tu_berlin.mpds.covid_notifier.config.RedisConfig;
import redis.clients.jedis.Jedis;


import java.io.Closeable;
import java.io.IOException;
import java.util.*;


public class Redis {

    public static class Connector implements Closeable {

        private static Connector self = null;
        private Jedis jedis ;
        private final int TENDAYS = 864000;

       private Connector() {

            this.jedis = new Jedis("redis", 6379, 1800);
        }


        private int writeContacts(String person1, String person2) {
            try {
                jedis.sadd(person1, person2);
                jedis.sadd(person2, person1);
                jedis.setex(person1 + ":" + person2, this.TENDAYS, "1");
                jedis.setex(person2 + ":" + person1, this.TENDAYS, "1");
                return 1;
            } catch (Exception e) {
                System.out.println("writeContacts: Jedis Issue : " + person1 + ", " + person2);
                e.printStackTrace();
                return 0;
            }

        }

        private Set<String> readContactSet(String personId) {
            try {
                Set<String> contacts = jedis.smembers(personId);
                if (contacts != null) {
                    contacts.removeIf(s -> jedis.get(personId + ":" + s) == null);
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

    public static int writeContact(String person1, String person2) {

        Connector connector = Connector.getInstance();
        return connector.writeContacts(person1, person2);
    }

    public static Set<String> getContacts(String personId) {

        Connector connector = Connector.getInstance();
        return connector.readContactSet(personId);
    }
}
