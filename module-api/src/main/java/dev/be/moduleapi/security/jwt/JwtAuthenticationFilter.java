package dev.be.moduleapi.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j(topic = "SECURITY")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    /**
     * JWT가 유효한 토큰인지 인증하기 위한 필터, UsernamePasswordAuthentication 앞에 세팅 필요
     * 로그인 폼 반환 이전에 인증 여부를 JSON으로 반환
     */

    private final JwtProvider jwtProvider;

    /**
     * request로 들어오는 JWT의 유효성 검증, JwtProvider.validationToken() 을 필터로 FilterChain에 추가
     */
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        // 토큰 쿼리값의 value를 통해 토큰을 가져옴
        String token = jwtProvider.resolveToken((HttpServletRequest) request);

        log.info("[JwtAuthenticationFilter doFilter] verifying token");
        log.info(((HttpServletRequest) request).getRequestURL().toString());

        if (token != null && jwtProvider.validationToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
