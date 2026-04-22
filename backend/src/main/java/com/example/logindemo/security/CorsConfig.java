package com.example.logindemo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration cfg = new CorsConfiguration();

        // Read allowed origins from env var FRONTEND_ALLOWED_ORIGINS (comma-separated).
        // Supports wildcard patterns (e.g. https://*.vercel.app) via allowedOriginPatterns.
        String env = System.getenv("FRONTEND_ALLOWED_ORIGINS");
        List<String> patterns;
        if (env != null && !env.isBlank()) {
            patterns = Arrays.stream(env.split(",")).map(String::trim).toList();
        } else {
            patterns = List.of("http://localhost:3000", "https://*.vercel.app");
        }

        cfg.setAllowedOriginPatterns(patterns);
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", cfg);
        return new CorsFilter(source);
    }
}
