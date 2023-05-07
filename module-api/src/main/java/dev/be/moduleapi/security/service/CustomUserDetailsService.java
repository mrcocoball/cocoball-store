package dev.be.moduleapi.security.service;

import dev.be.moduleapi.advice.exception.UserNotFoundApiException;
import dev.be.modulecore.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j(topic = "SECURITY")
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // 주입
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.getWithRolesByEmail(email).orElseThrow(UserNotFoundApiException::new);

    }
}
