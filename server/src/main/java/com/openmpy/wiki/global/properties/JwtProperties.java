package com.openmpy.wiki.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("jwt")
public record JwtProperties(String secretKey, long expiration) {
}
