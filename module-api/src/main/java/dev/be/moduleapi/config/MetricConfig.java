package dev.be.moduleapi.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MetricConfig {

    @Bean
    public InMemoryHttpTraceRepository inMemoryHttpTraceRepository() {

        InMemoryHttpTraceRepository repository = new InMemoryHttpTraceRepository();
        repository.setCapacity(1000);

        return repository;

    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry meterRegistry) {
        return new TimedAspect(meterRegistry);
    }

}
