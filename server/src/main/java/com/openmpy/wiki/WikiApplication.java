package com.openmpy.wiki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class WikiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(WikiApplication.class, args);
    }
}
