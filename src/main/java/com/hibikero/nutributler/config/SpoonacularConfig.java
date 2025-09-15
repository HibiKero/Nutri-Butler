package com.hibikero.nutributler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spoonacular API配置类
 */
@Configuration
public class SpoonacularConfig {
    
    @Value("${spoonacular.api.key:}")
    private String apiKey;
    
    @Value("${spoonacular.api.base-url:https://api.spoonacular.com}")
    private String baseUrl;
    
    @Bean
    public WebClient spoonacularWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("X-API-Key", apiKey)
                .build();
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getBaseUrl() {
        return baseUrl;
    }
}
