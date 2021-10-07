# Kafka Client

This project demonstrates producing and consuming kafka messages using Apache Avro.

## Requirements
- Java 11
- IntelliJ/Eclipse/Visual Studio Code (optional)
- Docker

## Docker compose files

This application depends on a Kafka cluster configured in the docker compose file.

```shell
docker-compose -f common.yml -f kafka_cluster.yml up
```

## Running the Demo Application

You can import all the projects in IntelliJ and create a Run Configuration for 2 projects:
- random-words-to-kafka-service

Or you can build and run the projects in the terminal using the maven command.