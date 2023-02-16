package com.dateplanner.security.service;

import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.admin.user.repository.UserRepository;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("[CustomUserDetailsService loadUserByUsername] principal checking");
        return userRepository.getWithRoles(username).orElseThrow(UserNotFoundApiException::new);

    }
}
