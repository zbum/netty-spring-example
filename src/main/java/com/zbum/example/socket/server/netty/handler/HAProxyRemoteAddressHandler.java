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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.haproxy.HAProxyMessage;

import java.net.InetSocketAddress;

public class HAProxyRemoteAddressHandler extends SimpleChannelInboundHandler<HAProxyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HAProxyMessage message) {
        if (message.sourceAddress() != null && message.sourcePort() > 0) {
            ctx.channel().attr(RemoteAddress.PROXY_ADDRESS)
                    .set(InetSocketAddress.createUnresolved(message.sourceAddress(), message.sourcePort()));
        }

        ctx.writeAndFlush("Your remote address is " + RemoteAddress.display(ctx.channel()) + ".\r\n");
        ctx.pipeline().remove(this);
    }
}
