/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zbum.example.socket.server.netty.handler;

import com.zbum.example.socket.server.domain.ChannelRepository;
import com.zbum.example.socket.server.domain.User;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * event handler to login
 *
 * @author Jibeom Jung akka. Manty
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ChannelHandler.Sharable
public class LoginHandler extends ChannelInboundHandlerAdapter {
    private final ChannelRepository channelRepository;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        if (!(msg instanceof String) || !((String) msg).startsWith("login ")) {
            ctx.fireChannelRead(msg);
            return;
        }

        String stringMessage = (String) msg;
        if (log.isDebugEnabled()) {
            log.debug(stringMessage);
        }

        User user = User.of(stringMessage, ctx.channel());
        user.login(channelRepository, ctx.channel());

        ctx.writeAndFlush("Successfully logged in as " + user.getUsername() + ". \r\n");
    }
}
