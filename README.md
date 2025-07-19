# **<p align="center">Synthetic Human — Demo Starter + Bishop Service</p>**

## **About the project**

Repository consists of two Maven modules.

Module **synthetic-human-core-starter** - Spring Boot Starter: command queue, audit (Kafka / Console), Micrometer metrics

Module **bishop-prototype** - REST application that uses a starter and demonstrates the developed Starter

The minimum JDK is 17; the build and launch were tested on Java 17 Temurin.

## **<p align="center">Launching Application</p>**

1. Launch [Dockerfile](Dockerfile)
``` bash
docker compose build
```
2. Launch [docker-compose.yml](docker-compose.yml)
``` bash
docker compose up -d
```

## **<p align="center">Configuration</p>**

You can change the configuration in the [application.properties](bishop-prototype/src/main/resources/application.properties) file.

1. **synth.queue.capacity** - default(5) - COMMON queue size

2. **synth.queue.critical-threads** - default(1) - CRITICAL queue size

3. **synth.audit.mode** - default(KAFKA) - Output of audit messages

4. **synth.monitor.interval** - default(10s) - Interval for displaying metric messages

## **<p align="center">HTTP-interface</p>**

POST -> http://localhost:8080/v1/commands -> JSON={description, author, priority} -> 202, 429, 400, 500

## **<p align="center">Monitoring</p>**

You can see all the metrics in the logs of the bishop container.

**OR**

http://localhost:9090 - UI Prometheus: command_queue_size (Graph), command_completed_total{author="Name"} (Graph)

## **<p align="center">Audit</p>**

When configuring synth.audit.mode=CONSOLE, messages will be output in the logs of the "bishop" container, and when configuring synth.audit.mode=KAFKA, messages will be output in the "audit-tail" container


## **<p align="center">Command processing time</p>**

The command processing time can be specified in the file [CommandDispatcher](synthetic-human-core-starter/src/main/java/com/danila/synthetichumancorestarter/application/CommandDispatcher.java).

You can set Thread.sleep(N ms) at your discretion;

## **<p align="center">As an example</p>**

POST http://localhost:8080/v1/commands
Body: JSON
Text: {
"description": "Inspect left thruster",
"author": "Ripley",
"priority": "COMMON"
}

GET http://localhost:8080/actuator/metrics/command_queue_size - current number of messages in the queue

GET http://localhost:8080/actuator/metrics/command_completed_total?tag=author:Ripley - Number of completed assignments for author