package dev.be.moduleapi.security.jwt;

import dev.be.moduleapi.advice.exception.CustomAuthenticationEntrypointException;
import dev.be.moduleapi.security.dto.TokenDto;
import dev.be.moduleapi.security.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j(topic = "SECURITY")
@RequiredArgsConstructor
@Component
public class JwtProvider {

    /**
     * [공부한 내용 + 기능 정리]
     * JWT 생성 및 유효성 검증을 하는 컴포넌트
     * 암호화 알고리즘 및 비밀키를 통해 토큰을 생성
     * Claim에 토근에 부가적으로 실어 보낼 정보를 담을 수 있음 (회원 구분 값 세팅 가능)
     * expire를 통해 토큰 만료 시간 지정 가능
     * resolveToken을 통해 HTTP Request Header에서 세팅된 토큰값을 가져와 유효성 검사를 진행
     */

    @Value("spring.jwt.secret")
    private String secretKey;

    private String ROLES = "roles";

    private final Long ACCESS_TOKEN_VALID_MILLISECOND = 60 * 60 * 1000L; // 1시간

    private final Long REFRESH_TOKEN_VALID_MILLISECOND = 14 * 24 * 60 * 60 * 1000L; // 14일

    private final CustomUserDetailsService userDetailsService;

    /**
     * 비밀키 암호화
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * JWT 생성
     */
    public TokenDto createToken(String email, List<String> roles) {

        // user 구분용, Claims에 email 추가
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(ROLES, roles);

        // 생성 날짜, 만료 날짜를 위한 Date
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILLISECOND)) // 발급 시간부터 1시간까지 유효
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILLISECOND)) // 발급 시간부터 14일까지 유효
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(ACCESS_TOKEN_VALID_MILLISECOND)
                .build();
    }

    /**
     * JWT로 인증 정보 조회
     */
    public Authentication getAuthentication(String token) {

        // JWT 에서 Claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보 없는지 체크
        if (claims.get(ROLES) == null) {
            throw new CustomAuthenticationEntrypointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * JWT 토큰 복호화해서 가져오기
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * HTTP Request의 Header 에서 Token을 파싱 -> "X-AUTH-TOKEN: JWT"
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    /**
     * JWT의 유효성 및 만료일자 확인
     */
    public boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
