# Netty Spring Boot Example
TCP communication server with Netty And SpringBoot

This TCP Communication Service is the simple backend application for developer who wants to make tcp service with Spring-Boot and Netty.

The main purpose of this codes is to explain how to build Netty based server application with Spring Boot.


## Feature
* Netcat clients can send messages to each other.
* HAProxy PROXY protocol v1/v2 can supply the original client IP address and port.
* TCP messages use UTF-8 explicitly, including Korean, Chinese, and Japanese text.

## Requirements

* JDK 26
* Maven 3.6.3 or later

The project uses Spring Boot 4.1.0 (Spring Framework 7.0.8), Netty 4.2.17.Final, and Lombok 1.18.46.

## Configuration

Set `netty.proxy-protocol-enabled=true` only when every connection is made through a load balancer that sends a PROXY protocol header. The default is `false`, which keeps direct TCP and netcat connections working normally.

When enabled, the server uses the source address from the PROXY header for the login session. A direct message received from that user includes the address, for example:

```text
alice (203.0.113.10:45678)>hello
```

## How to use

Run `com.zbum.example.socket.server.MantyChatApplication` from an IDE or start it with Maven:

```bash
mvn spring-boot:run
```

Open the first terminal, connect with netcat, and log in as `A`:

```text
$ nc localhost 8090
Your remote address is 127.0.0.1:57220.
login A
Successfully logged in as A.
```

Open another terminal and log in as `B`:

```text
$ nc localhost 8090
Your remote address is 127.0.0.1:57221.
login B
Successfully logged in as B.
```

From `B`, send a UTF-8 message to `A` using `<username>::<message>`:

```text
A::안녕하세요 你好 こんにちは
The message was sent to [A] successfully.
```

The first terminal receives the sender's name, address, and message:

```text
B (127.0.0.1:57221)>안녕하세요 你好 こんにちは
```

Press `Ctrl-C` to close an `nc` session.

## TODO
* Establishing connection with TLS and SNI

## Changelog

See [CHANGELOG.md](CHANGELOG.md).
 
## If you want to know about the Author
* see : [https://www.manty.co.kr](https://www.manty.co.kr)
