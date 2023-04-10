package dev.be;

import dev.be.moduleadmin.ModuleAdminApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = ModuleAdminApplication.class) // 테스트 클래스를 찾지 못해서 설정
class ModuleAdminApplicationTests {

    @Test
    void contextLoads() {
    }

}
