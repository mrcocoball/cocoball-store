package dev.be.moduleadmin;

import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Slf4j(topic = "SERVICE")
@Component
@RequiredArgsConstructor
public class InitUser {

    private final InitUserService initUserService;

    @PostConstruct
    public void init() {
        initUserService.init();
    }

    @Component
    static class InitUserService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Value("${com.dateplanner.admin.username}")
        private String username;

        @Value("${com.dateplanner.admin.password}")
        private String password;

        @Transactional
        public void init() {

            log.info("[InitUserService init] admin user checking...");
            if (userRepository.findByEmail(username).isEmpty()) {

                log.info("[InitUserService init] admin user is not exists");

                User admin = User.builder()
                        .password(passwordEncoder.encode(password))
                        .email(username)
                        .nickname("admin")
                        .roleSet(Collections.singletonList("ROLE_ADMIN"))
                        .build();

                userRepository.save(admin);
            }
            log.info("[InitUserService init] admin user checking complete");

        }
    }

}
