package dev.be.moduleapi.config;

import dev.be.moduleapi.security.handler.CustomAccessDeniedHandler;
import dev.be.moduleapi.security.handler.CustomAuthenticationEntryPoint;
import dev.be.moduleapi.security.handler.CustomLoginSuccessHandler;
import dev.be.moduleapi.security.jwt.JwtAuthenticationFilter;
import dev.be.moduleapi.security.jwt.JwtProvider;
import dev.be.moduleapi.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j(topic = "SECURITY")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // 주입
    private final CustomUserDetailsService customUserDetailsService;

    private final JwtProvider jwtProvider;

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

        // CORS
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

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

    // 소셜 로그인 테스트 페이지용 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // CORS 관련 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
