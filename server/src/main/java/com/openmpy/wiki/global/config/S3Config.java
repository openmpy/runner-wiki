package com.openmpy.wiki.global.config;

import com.openmpy.wiki.global.properties.S3Properties;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@RequiredArgsConstructor
@Configuration
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public S3Client s3Client() {
        final StaticCredentialsProvider provider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(s3Properties.accessKey(), s3Properties.secretKey())
        );

        final S3Configuration s3Configuration = S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build();

        return S3Client.builder()
                .region(Region.of(s3Properties.region()))
                .credentialsProvider(provider)
                .endpointOverride(URI.create(s3Properties.endpoint()))
                .serviceConfiguration(s3Configuration)
                .build();
    }
}
