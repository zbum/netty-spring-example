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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

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

    @Test
    void forwardsRawByteBufMessagesWithoutCastingThemToString() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new SimpleChatServerHandler(new ChannelRepository()));
        channel.readOutbound();
        ByteBuf message = PooledByteBufAllocator.DEFAULT.directBuffer().writeByte(1);

        assertThat(channel.writeInbound(message)).isTrue();
        assertThat((Object) channel.readInbound()).isSameAs(message);
        message.release();
        channel.finishAndReleaseAll();
    }

    @Test
    void decodesRawTcpDataBeforeItReachesTheChatHandler() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()),
                new StringDecoder(),
                new SimpleChatServerHandler(new ChannelRepository()));
        channel.readOutbound();
        ByteBuf message = PooledByteBufAllocator.DEFAULT.directBuffer();
        message.writeCharSequence("hello\r\n", StandardCharsets.UTF_8);

        channel.writeInbound(message);

        assertThat((Object) channel.readOutbound()).isEqualTo("hello\n\r");
        channel.finishAndReleaseAll();
    }
}
