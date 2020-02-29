package com.zbum.example.socket.server.netty.handler;

import com.zbum.example.socket.server.domain.User;
import com.zbum.example.socket.server.netty.ChannelRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * event handler to login
 *
 * @author Jibeom Jung
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

        User user = User.of(stringMessage);
        user.login(channelRepository, ctx.channel());

        ctx.writeAndFlush("Successfully logged in as " + user.getUsername() + ". \r\n");
    }
}
