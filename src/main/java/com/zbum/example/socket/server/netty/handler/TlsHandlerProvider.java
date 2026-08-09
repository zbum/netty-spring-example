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
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SniHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.DomainWildcardMappingBuilder;
import io.netty.util.Mapping;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InputStream;

@Component
public class TlsHandlerProvider {
    private final boolean enabled;
    private final boolean sniEnabled;
    private final SslContext defaultContext;
    private final Mapping<String, SslContext> sniMapping;

    public TlsHandlerProvider(NettyProperties properties, ResourceLoader resourceLoader) {
        NettyProperties.Tls tls = properties.getTls();
        enabled = tls.isEnabled();
        sniEnabled = tls.isSniEnabled();

        if (!enabled) {
            defaultContext = null;
            sniMapping = null;
            return;
        }

        defaultContext = buildContext(
                resourceLoader,
                tls.getCertificateChain(),
                tls.getPrivateKey(),
                tls.getPrivateKeyPassword(),
                "netty.tls");

        if (!sniEnabled) {
            sniMapping = null;
            return;
        }
        if (tls.getSniCertificates().isEmpty()) {
            throw new IllegalStateException(
                    "netty.tls.sni-certificates must contain at least one certificate when SNI is enabled");
        }

        DomainWildcardMappingBuilder<SslContext> mappingBuilder = new DomainWildcardMappingBuilder<>(defaultContext);
        for (NettyProperties.SniCertificate certificate : tls.getSniCertificates()) {
            if (!StringUtils.hasText(certificate.getHostname())) {
                throw new IllegalStateException("Each SNI certificate requires a hostname");
            }
            mappingBuilder.add(certificate.getHostname(), buildContext(
                    resourceLoader,
                    certificate.getCertificateChain(),
                    certificate.getPrivateKey(),
                    certificate.getPrivateKeyPassword(),
                    "SNI hostname " + certificate.getHostname()));
        }
        sniMapping = mappingBuilder.build();
    }

    public void configure(ChannelPipeline pipeline, ByteBufAllocator allocator) {
        if (!enabled) {
            return;
        }
        if (sniEnabled) {
            pipeline.addLast("sni", new SniHandler(sniMapping));
        } else {
            pipeline.addLast("tls", defaultContext.newHandler(allocator));
        }
    }

    private static SslContext buildContext(
            ResourceLoader resourceLoader,
            String certificateChain,
            String privateKey,
            String privateKeyPassword,
            String configurationName) {
        if (!StringUtils.hasText(certificateChain) || !StringUtils.hasText(privateKey)) {
            throw new IllegalStateException(
                    configurationName + " requires both certificate-chain and private-key");
        }

        Resource certificateResource = resourceLoader.getResource(certificateChain);
        Resource privateKeyResource = resourceLoader.getResource(privateKey);
        try (InputStream certificateInput = certificateResource.getInputStream();
             InputStream privateKeyInput = privateKeyResource.getInputStream()) {
            return SslContextBuilder.forServer(certificateInput, privateKeyInput, privateKeyPassword).build();
        } catch (SSLException exception) {
            throw new IllegalStateException("Invalid TLS certificate or private key for " + configurationName, exception);
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot read TLS certificate or private key for " + configurationName, exception);
        }
    }
}
