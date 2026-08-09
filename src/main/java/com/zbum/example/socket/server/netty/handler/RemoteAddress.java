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

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public final class RemoteAddress {
    static final AttributeKey<InetSocketAddress> PROXY_ADDRESS = AttributeKey.valueOf("proxyRemoteAddress");

    private RemoteAddress() {
    }

    public static InetSocketAddress from(Channel channel) {
        InetSocketAddress proxyAddress = channel.attr(PROXY_ADDRESS).get();
        if (proxyAddress != null) {
            return proxyAddress;
        }

        SocketAddress remoteAddress = channel.remoteAddress();
        return remoteAddress instanceof InetSocketAddress ? (InetSocketAddress) remoteAddress : null;
    }

    public static String display(Channel channel) {
        InetSocketAddress address = from(channel);
        if (address == null) {
            return "unknown";
        }
        return address.getHostString() + ":" + address.getPort();
    }
}
