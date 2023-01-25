package com.dateplanner.config;

import com.dateplanner.security.handler.CustomAccessDeniedHandler;
import com.dateplanner.security.handler.CustomAuthenticationEntryPoint;
import com.dateplanner.security.handler.CustomLoginSuccessHandler;
import com.dateplanner.security.jwt.JwtAuthenticationFilter;
import com.dateplanner.security.jwt.JwtProvider;
import com.dateplanner.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "SECURITY")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // 주입
    private final CustomUserDetailsService customUserDetailsService;

    private final JwtProvider jwtProvider;

    // TODO: 인증 기능 추후 구현
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        // GET AuthenticationManager
        AuthenticationManager authenticationManager =
                authenticationManagerBuilder.build();

        // 반드시 필요
        http.authenticationManager(authenticationManager);

        // CustomLoginSuccessHandler
        CustomLoginSuccessHandler successHandler = new CustomLoginSuccessHandler();

        http.httpBasic().disable();

        http.csrf().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http
                // 어노테이션 형식으로 변경하였음
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                        .antMatchers("/", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html",
//                                "/webjars/**", "/swagger/**", "/swagger-ui/**").permitAll()
//                        .antMatchers("/api/*/login", "/api/*/join").permitAll()
//                        .antMatchers(HttpMethod.GET, "/exception/**").permitAll()
//                        .anyRequest().authenticated())
                // 사용자 정의 EntryPoint 추가
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                // 사용자 정의 AccessDeniedHandler 추가
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 넣음
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                //.formLogin().and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
