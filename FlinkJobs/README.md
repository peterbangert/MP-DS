# Flink Streaming Engine

> Useful commands and howto's

## Build the Jar
- Navigate to the project directory

       mvn clean compile assembly:single

## Run the Jar
- Navigate to the target directory

        java -jar HighRiskAggregator-1.0-SNAPSHOT-jar-with-dependencies.jar
        
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

- Check Hashmaps

         redis-cli hgetall key

