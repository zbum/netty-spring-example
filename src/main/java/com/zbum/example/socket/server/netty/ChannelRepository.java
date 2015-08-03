package com.zbum.example.socket.server.netty;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * Created by 1000892 on 15. 8. 2..
 */
public class ChannelRepository {
    private HashMap<String, Channel> channelCache = new HashMap<String, Channel>();

    public ChannelRepository put(String key, Channel value) {
        channelCache.put(key, value);
        return this;
    }

    public Channel get(String key) {
        return channelCache.get(key);
    }
}
