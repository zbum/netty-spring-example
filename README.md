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
* And login with login command.
```
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57220
    login A
    Successfully logged in as A.
```
* Connect to this server by telnet command on another terminal and login.
```
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57221
    login B
    Successfully logged in as B.
```
* From now, you can send message to `A` by below
```
    A::I Love You!!!
    The message was sent to [A] successfully.
```
* Then, you can receive Message like below
```bash
    $ telnet localhost 8090
    Trying ::1...
    Connected to localhost.
    Escape character is '^]'.
    Your channel key is /0:0:0:0:0:0:0:1:57220
    login A
    Successfully logged in as A. 
    B>I Love you!!
```
## TODO
* Establishing connection with TLS and SNI
 
## If you want to know about the Author
* see : [https://www.manty.co.kr](https://www.manty.co.kr)


