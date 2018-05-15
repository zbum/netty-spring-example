# Netty Spring Boot Example
TCP communication server with Netty And SpringBoot

This TCP Communication Service is the simple backend application for developer who wants to make tcp service with Spring-Boot and Netty.

The main purpose of this codes is to explain how to build Netty based server application with Spring Boot.


## Feature
* Telnet Client can send message to other telnet client.

## How to use
* Run com.zbum.example.socket.server.netty.Application with IDE or Maven
```
    $ mvn spring-boot:run
```
* Connect to this server by telnet command.
```
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57220
```
* Your channel key (ID) is /0:0:0:0:0:0:0:1:57220
* Connect to this server by telnet command on annother terminal.
```
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57221
```
* From now, you can send message to /0:0:0:0:0:0:0:1:57220 channel by below
```
    /0:0:0:0:0:0:0:1:57220::I Love You!!!
```
* Then, you can receive Message like below
```bash
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57220
    I Love You!!!
```


