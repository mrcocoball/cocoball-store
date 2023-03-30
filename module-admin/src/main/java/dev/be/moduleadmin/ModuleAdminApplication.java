package dev.be.moduleadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = {"dev.be.moduleadmin", "dev.be.modulecore"}
)
@EntityScan("dev.be.modulecore.domain")
@EnableJpaRepositories(basePackages = {"dev.be.modulecore.repositories"})
@ConfigurationPropertiesScan
public class ModuleAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleAdminApplication.class, args);
    }

}
