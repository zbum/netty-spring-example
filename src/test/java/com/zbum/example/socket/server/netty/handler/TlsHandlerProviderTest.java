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

import com.zbum.example.socket.server.config.NettyProperties;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.ssl.SniHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TlsHandlerProviderTest {
    @Test
    void leavesPipelineUnchangedWhenTlsIsDisabled() {
        TlsHandlerProvider provider = new TlsHandlerProvider(new NettyProperties(), new DefaultResourceLoader());
        EmbeddedChannel channel = new EmbeddedChannel();

        provider.configure(channel.pipeline(), channel.alloc());

        assertThat(channel.pipeline().get(SslHandler.class)).isNull();
        assertThat(channel.pipeline().get(SniHandler.class)).isNull();
        channel.finishAndReleaseAll();
    }

    @Test
    void requiresDefaultCertificateWhenTlsIsEnabled() {
        NettyProperties properties = new NettyProperties();
        properties.getTls().setEnabled(true);

        assertThatThrownBy(() -> new TlsHandlerProvider(properties, new DefaultResourceLoader()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("netty.tls requires both certificate-chain and private-key");
    }

    @Test
    void addsTlsHandlerWhenTlsIsEnabled() throws Exception {
        SelfSignedCertificate certificate = new SelfSignedCertificate("localhost");
        try {
            NettyProperties properties = tlsProperties(certificate);
            TlsHandlerProvider provider = new TlsHandlerProvider(properties, new DefaultResourceLoader());
            EmbeddedChannel channel = new EmbeddedChannel();

            provider.configure(channel.pipeline(), channel.alloc());

            assertThat(channel.pipeline().get("tls")).isInstanceOf(SslHandler.class);
            channel.finishAndReleaseAll();
        } finally {
            certificate.delete();
        }
    }

    @Test
    void addsSniHandlerWhenSniIsEnabled() throws Exception {
        SelfSignedCertificate defaultCertificate = new SelfSignedCertificate("localhost");
        SelfSignedCertificate chatCertificate = new SelfSignedCertificate("chat.example.com");
        try {
            NettyProperties properties = tlsProperties(defaultCertificate);
            properties.getTls().setSniEnabled(true);
            NettyProperties.SniCertificate sniCertificate = new NettyProperties.SniCertificate();
            sniCertificate.setHostname("chat.example.com");
            sniCertificate.setCertificateChain(chatCertificate.certificate().toURI().toString());
            sniCertificate.setPrivateKey(chatCertificate.privateKey().toURI().toString());
            properties.getTls().getSniCertificates().add(sniCertificate);

            TlsHandlerProvider provider = new TlsHandlerProvider(properties, new DefaultResourceLoader());
            EmbeddedChannel channel = new EmbeddedChannel();
            provider.configure(channel.pipeline(), channel.alloc());

            assertThat(channel.pipeline().get("sni")).isInstanceOf(SniHandler.class);
            channel.finishAndReleaseAll();
        } finally {
            defaultCertificate.delete();
            chatCertificate.delete();
        }
    }

    private static NettyProperties tlsProperties(SelfSignedCertificate certificate) {
        NettyProperties properties = new NettyProperties();
        properties.getTls().setEnabled(true);
        properties.getTls().setCertificateChain(certificate.certificate().toURI().toString());
        properties.getTls().setPrivateKey(certificate.privateKey().toURI().toString());
        return properties;
    }
}
