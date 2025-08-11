package com.openmpy.wiki.global.config;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.openmpy.wiki.global.properties.MeilisearchProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class MeilisearchConfig {

    private final MeilisearchProperties meilisearchProperties;

    @Bean
    public Client meilisearchClient() {
        final Config config = new Config(meilisearchProperties.host(), meilisearchProperties.masterKey());
        return new Client(config);
    }
}
