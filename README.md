# Stream Processing Client
## Prerequisites
Java 1.8 and Gradle 6.0.1

## Instructions
To get dependencies and build the client:
```code
$ gradle build
```
To run the client:
```code
$ gradle run
```

To run unit tests:
```code
$$ gradle test
```

Logs are written to file "logs/app.log".

## How it works
The streaming client pulls device related events from https://tweet-service.herokuapp.com/sps.
Events are aggragated and reported to stdout every second.
The client also auto reconnects when disconnected from the server.

This client is based on Netty which is a high performance non-blocking (NIO) networking framework.
Non-blocking IO engines usually scale better than blocking engines.

The reason that I chose a lower level framework like Netty is that it will give us finer control of networking IO.

Other alternative choices include Reactor-Netty and Akka etc.

## Future work
Given more time, I would work on following items to improve the client:
- Clustering
- Mechanism to dynamically handle back pressure.
- Exactly-once processing.
- Resilience and failure recovery.
- More test cases and implement a server for integration testing.
