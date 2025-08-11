package com.openmpy.wiki.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("meilisearch")
public record MeilisearchProperties(String host, String masterKey) {
}
