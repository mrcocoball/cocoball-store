package dev.be.moduleapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.profiles.active:local")
class ModuleApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
