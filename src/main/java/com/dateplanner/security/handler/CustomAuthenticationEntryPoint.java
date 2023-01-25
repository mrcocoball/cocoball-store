package com.dateplanner.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Spring Security 관련 예외는 DispatcherServlet 앞단의 Filter에서 잡기 때문에 @RestControllerAdvice로 핸들링할 수 없음
     * (RestControllerAdvice는 DispatcherServlet 내부의 예외를 처리하기 때문)
     * 따라서 Spring Security 관련 예외는, exception/entryPoint로 리다이렉트해서 처리
     */

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.sendRedirect("/exception/entrypoint"); // 해당 URL로 리다이렉션 한 후 예외 처리
    }
}
