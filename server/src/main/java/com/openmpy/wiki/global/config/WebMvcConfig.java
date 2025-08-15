package com.openmpy.wiki.global.config;

import com.openmpy.wiki.auth.infrastructure.AdminAuthInterceptor;
import com.openmpy.wiki.global.properties.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;
    private final AdminAuthInterceptor adminAuthInterceptor;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping(corsProperties.pathPattern())
                .allowedOrigins(corsProperties.origins())
                .allowedMethods(corsProperties.methods())
                .allowedHeaders(corsProperties.headers())
                .allowCredentials(corsProperties.allowCredentials())
                .maxAge(corsProperties.maxAge());
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns("/api/v1/admin/login", "/api/v1/admin/logout");
    }
}
