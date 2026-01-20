package com.openmpy.server.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloudflare")
public record CloudflareProperties(String turnstileSecretKey) {
}
