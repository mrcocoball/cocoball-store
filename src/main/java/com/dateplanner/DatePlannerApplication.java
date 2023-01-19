package com.dateplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DatePlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatePlannerApplication.class, args);
    }

}
