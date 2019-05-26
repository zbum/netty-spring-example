package com.zbum.example.socket.server.netty;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ChannelRepositoryTest {

    private ChannelRepository channelRepository = new ChannelRepository();

    @Test
    public void put() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        channelRepository.put("aaa", embeddedChannel);
        assertThat(channelRepository.get("aaa"), is(embeddedChannel));
    }

    @Test
    public void remove() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        channelRepository.put("aaa", embeddedChannel);
        channelRepository.remove("aaa");
        assertThat(channelRepository.get("aaa"), is(nullValue()));
    }

    @Test
    public void size() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel();
        channelRepository.put("aaa", embeddedChannel);
        channelRepository.remove("aaa");
        assertThat(channelRepository.size(), is(0));
    }
}