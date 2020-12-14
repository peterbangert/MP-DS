# Flink Streaming Engine

> Requirements, useful commands, and howto's

## Requirements

1. Access to Kafka instance
   - see [kafka quickstart](https://kafka.apache.org/quickstart) 
2. Access to Redis instance
   - see [redis quickstart](https://redis.io/topics/quickstart)
3. jdk-11
   - see [open jdk install](https://openjdk.java.net/projects/jdk/11/)


## Build the Jar
- Navigate to the project directory

        mvn clean package 

## Run the Jar
- Navigate to the target directory

        java -jar streamprocessing.jar
        
## Local Kafka

- Start Zookeeper

         bin/zookeeper-server-start.sh config/zookeeper.properties

- Start Kafka

         bin/kafka-server-start.sh config/server.properties

- KafkaCat List, Produce, Consume

         kafkacat -b localhost:9092 -[P|C|L] -t topic

- Delete Topic

         bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic covid



## Local Redis

- Start Redis

         redis-server

- Check Redis

         redis-cli ping
        PONG

- Check Set

         redis-cli smembers key

