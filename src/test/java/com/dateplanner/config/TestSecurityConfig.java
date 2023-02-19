package com.dateplanner.config;

import com.dateplanner.admin.user.entity.User;
import com.dateplanner.admin.user.repository.UserRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserRepository userRepository;

    @BeforeTestMethod
    public void securitySetup() {
        given(userRepository.getWithRoles(anyString())).willReturn(Optional.of(
                User.builder()
                        .uid("testuser")
                        .password("testpassword")
                        .email("testemail")
                        .nickname("test")
                        .deleted(false)
                        .social(false)
                        .build()
        ));
    }

}
