package dev.be.moduleapi.config;

import dev.be.moduleapi.logging.aop.LogTraceAspect;
import dev.be.moduleapi.logging.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AopConfig {

    @Bean
    public LogTrace logTrace() { return new LogTrace(); }

    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        return new LogTraceAspect(logTrace);
    }

}
