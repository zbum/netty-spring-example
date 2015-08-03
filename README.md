# TCP-Communication-Service
TCP communication server with Netty And SpringBoot

This TCP Communication Service is a simple example for developer who want to make tcp service with Spring-Boot and Netty.


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
    Your channel key is 04e9b346-50ec-4810-bd59-6daba2cc6f54
```
* Your channel key (ID) is 04e9b346-50ec-4810-bd59-6daba2cc6f54
* Connect to this server by telnet command on annother terminal.
```
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is af9b13f5-b259-4743-9a6d-d887a3a076bf
```
* From now, you can send message to 04e9b346-50ec-4810-bd59-6daba2cc6f54 channel by below
```
    04e9b346-50ec-4810-bd59-6daba2cc6f54:I Love You!!!
```
* Then, you can receive Message like below
```bash
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is 04e9b346-50ec-4810-bd59-6daba2cc6f54
    I Love You!!!
```


