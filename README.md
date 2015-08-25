# # netty-spring-example
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
    Your channel key is /0:0:0:0:0:0:0:1:57220
```
* Your channel key (ID) is /0:0:0:0:0:0:0:1:57220.
* Connect to this server by telnet command on another terminal.
```
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57221
```
* From now, you can send message to /0:0:0:0:0:0:0:1:57220 channel by below command.
```
    /0:0:0:0:0:0:0:1:57220::I Love You!!!
```
* Then, you can receive Message like below on other terminal(/0:0:0:0:0:0:0:1:57220).
```bash
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57220
    I Love You!!!
```


