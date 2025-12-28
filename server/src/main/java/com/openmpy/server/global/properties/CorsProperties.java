package com.openmpy.server.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(
        String pathPattern,
        String[] origins,
        String[] methods,
        String[] headers,
        boolean allowCredentials,
        long maxAge
) {
}
