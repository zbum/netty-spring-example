package com.zbum.example.socket.server.netty.handler;

import com.zbum.example.socket.server.netty.ChannelRepository;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class LoginHandlerTest {

    ChannelRepository channelRepository;

    @BeforeEach
    void setup() {
        channelRepository = mock(ChannelRepository.class);
    }

    @Test
    @DisplayName("login handler test")
    void testLogin() {
        // given
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new LoginHandler(this.channelRepository));

        // when
        embeddedChannel.writeInbound("login aaa\r\n");

        // then
        Queue<Object> outboundMessages = embeddedChannel.outboundMessages();
        assertThat(outboundMessages.poll()).isEqualTo("Successfully logged in as aaa. \r\n");
    }
}