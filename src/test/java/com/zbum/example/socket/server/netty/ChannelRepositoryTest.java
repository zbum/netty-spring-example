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
package com.zbum.example.socket.server.netty;

import com.zbum.example.socket.server.domain.ChannelRepository;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Test for  ChannelRepository.java
 *
 * @author Jibeom Jung akka. Manty
 */
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