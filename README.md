# Netty Spring Boot Example
TCP communication server with Netty And SpringBoot

This TCP Communication Service is the simple backend application for developer who wants to make tcp service with Spring-Boot and Netty.

The main purpose of this codes is to explain how to build Netty based server application with Spring Boot.


## Feature
* Telnet Client can send message to other telnet client.
* HAProxy PROXY protocol v1/v2 can supply the original client IP address and port.

## Configuration

Set `netty.proxy-protocol-enabled=true` only when every connection is made through a load balancer that sends a PROXY protocol header. The default is `false`, which keeps direct TCP and telnet connections working normally.

When enabled, the server uses the source address from the PROXY header for the login session. A direct message received from that user includes the address, for example:

```text
alice (203.0.113.10:45678)>hello
```

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

## Changelog

### 2026-08-09 — `feature/handle-non-string-messages`

* Prevented raw Netty `ByteBuf` messages from causing a `ClassCastException` in `SimpleChatServerHandler` ([#14](https://github.com/zbum/netty-spring-example/issues/14)).
* Added coverage for undecoded messages and the normal delimiter/string decoder pipeline.

### 2026-08-09 — `feature/proxy_protocol`

* Made HAProxy PROXY protocol decoding configurable with `netty.proxy-protocol-enabled`.
* Passed the original source IP and port through the Netty channel and included them in messages sent to other users.
* Added PROXY address and user-message tests; verified the full Maven test suite and Java 25 build compatibility.
 
## If you want to know about the Author
* see : [https://www.manty.co.kr](https://www.manty.co.kr)
