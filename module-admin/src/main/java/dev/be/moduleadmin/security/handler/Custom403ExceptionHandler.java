package dev.be.moduleadmin.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Custom403ExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // ACCESS DENIED됨

        response.setStatus(HttpStatus.FORBIDDEN.value());

        // JSON 요청이었는가? 헤더로 확인
        String contentType = request.getHeader("Content-Type");

        boolean jsonRequest = contentType.startsWith("application/json"); // application/json으로 시작?

        // 일반적인 request였다면
        if (!jsonRequest) {

            response.sendRedirect("/user/login?error=ACCESS_DENIED");
        }

    }
}
