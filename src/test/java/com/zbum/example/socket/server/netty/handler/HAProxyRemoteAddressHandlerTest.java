/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 */
package com.zbum.example.socket.server.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HAProxyRemoteAddressHandlerTest {
    @Test
    void exposesProxySourceAddressToFollowingHandlers() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new HAProxyMessageDecoder(),
                new HAProxyRemoteAddressHandler());

        channel.writeInbound(Unpooled.copiedBuffer(
                "PROXY TCP4 203.0.113.10 192.0.2.20 45678 8090\r\n",
                StandardCharsets.US_ASCII));

        assertThat(RemoteAddress.display(channel)).isEqualTo("203.0.113.10:45678");
        assertThat((Object) channel.readOutbound())
                .isEqualTo("Your remote address is 203.0.113.10:45678.\r\n");
        assertThat(channel.pipeline().get(HAProxyMessageDecoder.class)).isNull();
        assertThat(channel.pipeline().get(HAProxyRemoteAddressHandler.class)).isNull();
        channel.finishAndReleaseAll();
    }
}
