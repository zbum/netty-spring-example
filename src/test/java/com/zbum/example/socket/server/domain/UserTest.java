/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */
package com.zbum.example.socket.server.domain;

import com.zbum.example.socket.server.netty.handler.HAProxyRemoteAddressHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    @Test
    void tellsRecipientTheLoggedInUsersProxyAddress() {
        EmbeddedChannel sender = new EmbeddedChannel(
                new HAProxyMessageDecoder(),
                new HAProxyRemoteAddressHandler());
        sender.writeInbound(Unpooled.copiedBuffer(
                "PROXY TCP4 203.0.113.10 192.0.2.20 45678 8090\r\n",
                StandardCharsets.US_ASCII));
        sender.readOutbound();
        EmbeddedChannel recipient = new EmbeddedChannel();

        User user = User.of("login alice", sender);
        user.tell(recipient, "bob", "hello");

        assertThat((Object) recipient.readOutbound()).isEqualTo("alice (203.0.113.10:45678)");
        assertThat((Object) recipient.readOutbound()).isEqualTo(">");
        assertThat((Object) recipient.readOutbound()).isEqualTo("hello\n\r");
        assertThat((Object) sender.readOutbound()).isEqualTo("The message was sent to [bob] successfully.\r\n");
        sender.finishAndReleaseAll();
        recipient.finishAndReleaseAll();
    }
}
