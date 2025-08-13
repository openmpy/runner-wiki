package com.openmpy.wiki.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("admin")
public record AdminProperties(String id, String password) {
}
