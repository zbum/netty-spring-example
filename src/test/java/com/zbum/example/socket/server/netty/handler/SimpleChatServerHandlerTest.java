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

import com.zbum.example.socket.server.domain.ChannelRepository;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleChatServerHandlerTest {
    @Test
    void announcesTheRemoteAddressWhenAChannelBecomesActive() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new SimpleChatServerHandler(new ChannelRepository()));

        assertThat((Object) channel.readOutbound())
                .isEqualTo("Your remote address is unknown.\r\n");
        channel.finishAndReleaseAll();
    }

    @Test
    void echoesMessagesThatAreNotDirectMessages() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new SimpleChatServerHandler(new ChannelRepository()));
        channel.readOutbound();

        channel.writeInbound("hello");

        assertThat((Object) channel.readOutbound()).isEqualTo("hello\n\r");
        channel.finishAndReleaseAll();
    }
}
