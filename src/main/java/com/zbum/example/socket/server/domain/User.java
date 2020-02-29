package com.zbum.example.socket.server.domain;

import com.zbum.example.socket.server.netty.ChannelRepository;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.NonNull;

public class User {
    public static final AttributeKey<User> USER_ATTRIBUTE_KEY = AttributeKey.newInstance("USER");

    @Getter
    private String username;

    private User(String username){
        this.username = username;
    }

    public static User of(@NonNull String loginCommand) {
        if (loginCommand.startsWith("login ")) {
            return new User(loginCommand.trim().substring("login ".length()));
        }

        throw new IllegalArgumentException("loginCommand ["+loginCommand+"] can not be accepted");
    }

    public void login(ChannelRepository channelRepository, Channel channel) {
        channel.attr(USER_ATTRIBUTE_KEY).set(this);
        channelRepository.put(this.username, channel);
    }

    public void logout(ChannelRepository channelRepository, Channel channel) {
        channel.attr(USER_ATTRIBUTE_KEY).getAndSet(null);
        channelRepository.remove(this.username);
    }

    public static User current(Channel channel) {
        return channel.attr(USER_ATTRIBUTE_KEY).get();
    }
}
