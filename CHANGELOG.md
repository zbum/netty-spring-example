# Changelog

## 2026-08-09 — `feature/tls-sni-support`

* Added optional TLS encryption configured with PEM certificate and private-key resources.
* Added optional SNI hostname mappings for selecting different server certificates.
* Documented local TLS login and SNI testing with OpenSSL.

## 2026-08-09 — `feature/framework-dependency-upgrades`

* Upgraded to JDK 26, Spring Boot 4.1.0, Spring Framework 7.0.8, Netty 4.2.17.Final, and Lombok 1.18.46.
* Migrated to the Netty 4.2 event loop API and delegated compatible transitive dependency versions to the Spring Boot BOM.
* Made TCP text encoding explicitly UTF-8 and verified Korean, Chinese, and Japanese message round trips.
* Replaced the telnet usage examples with netcat examples.

## 2026-08-09 — `feature/handle-non-string-messages`

* Prevented raw Netty `ByteBuf` messages from causing a `ClassCastException` in `SimpleChatServerHandler` ([#14](https://github.com/zbum/netty-spring-example/issues/14)).
* Added coverage for undecoded messages and the normal delimiter/string decoder pipeline.

## 2026-08-09 — `feature/proxy_protocol`

* Made HAProxy PROXY protocol decoding configurable with `netty.proxy-protocol-enabled`.
* Passed the original source IP and port through the Netty channel and included them in messages sent to other users.
* Added PROXY address and user-message tests; verified the full Maven test suite and Java 25 build compatibility.
