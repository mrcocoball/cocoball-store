package com.dateplanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class RetryConfig {

    /* RetryTemplate 사용 시

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplate();
    }
     */
}
