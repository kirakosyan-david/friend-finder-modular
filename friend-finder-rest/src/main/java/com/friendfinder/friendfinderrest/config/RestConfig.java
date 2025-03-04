package com.friendfinder.friendfinderrest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * The RestConfig class is a Spring configuration class that defines a RestTemplate bean.
 * RestTemplate is a class provided by Spring that simplifies making HTTP requests to external services or APIs.
 */
@Configuration
public class RestConfig {

    /**
     * Creates and returns a RestTemplate bean.
     *
     * @return The RestTemplate object.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
