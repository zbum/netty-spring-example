# Netty Spring Boot Example
TCP communication server with Netty And SpringBoot

This TCP Communication Service is the simple backend application for developer who wants to make tcp service with Spring-Boot and Netty.

The main purpose of this codes is to explain how to build Netty based server application with Spring Boot.


## Feature
* Netcat clients can send messages to each other.
* HAProxy PROXY protocol v1/v2 can supply the original client IP address and port.
* TLS encryption and SNI certificate selection can be enabled when needed.
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

TLS and SNI are also disabled by default. To enable TLS, configure a PEM certificate chain and private key:

```properties
netty.tls.enabled=true
netty.tls.certificate-chain=file:/absolute/path/server-cert.pem
netty.tls.private-key=file:/absolute/path/server-key.pem
```

The resource locations may use `file:` or `classpath:`. For an encrypted private key, set `netty.tls.private-key-password` as well.

To select certificates by the client's SNI hostname, enable SNI and add one or more hostname mappings. The default certificate above is used when none of the mappings match.

```properties
netty.tls.sni-enabled=true
netty.tls.sni-certificates[0].hostname=chat.example.com
netty.tls.sni-certificates[0].certificate-chain=file:/absolute/path/chat-cert.pem
netty.tls.sni-certificates[0].private-key=file:/absolute/path/chat-key.pem
```

When PROXY protocol and TLS are both enabled, the load balancer must send the PROXY header before the TLS handshake.

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

### TLS login with OpenSSL

Create a self-signed certificate for local testing:

```bash
openssl req -x509 -newkey rsa:2048 -nodes -days 1 \
  -keyout server-key.pem -out server-cert.pem -subj '/CN=localhost'
```

Enable TLS with the properties shown above, restart the server, then connect and log in through TLS:

```text
$ openssl s_client -connect localhost:8090 -servername localhost -quiet
Your remote address is 127.0.0.1:57220.
login A
Successfully logged in as A.
```

Use the mapped hostname with `-servername chat.example.com` to test SNI certificate selection.

## Changelog

See [CHANGELOG.md](CHANGELOG.md).
 
## If you want to know about the Author
* see : [https://www.manty.co.kr](https://www.manty.co.kr)
