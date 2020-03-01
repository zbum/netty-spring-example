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
package com.zbum.example.socket.server.domain;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.NonNull;

/**
 * User Domain Object
 *
 * @author Jibeom Jung akka. Manty
 */
public class User {
    public static final AttributeKey<User> USER_ATTRIBUTE_KEY = AttributeKey.newInstance("USER");

    @Getter
    private final String username;
    private final Channel channel;

    private User(String username, Channel channel){
        this.username = username;
        this.channel = channel;
    }

    public static User of(@NonNull String loginCommand, @NonNull Channel channel) {
        if (loginCommand.startsWith("login ")) {
            return new User(loginCommand.trim().substring("login ".length()),channel );
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
        User user = channel.attr(USER_ATTRIBUTE_KEY).get();
        if ( user == null ){
            throw new UserLoggedOutException();
        }
        return user;
    }

    public void tell(Channel targetChannel, @NonNull String username, @NonNull String message) {
        if (targetChannel != null) {
            targetChannel.write(this.username);
            targetChannel.write(">");
            targetChannel.writeAndFlush(message + "\n\r");
            this.channel.writeAndFlush("The message was sent to ["+username+"] successfully.\r\n");
        }else{
            this.channel.writeAndFlush("No user named with ["+ username +"].\r\n");
        }
    }
}
