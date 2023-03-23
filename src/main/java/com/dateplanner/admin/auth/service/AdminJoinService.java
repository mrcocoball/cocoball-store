package com.dateplanner.admin.auth.service;

import com.dateplanner.advice.exception.PasswordMismatchException;
import com.dateplanner.advice.exception.UserNotFoundApiException;
import com.dateplanner.security.dto.TokenDto;
import com.dateplanner.security.entity.RefreshToken;
import com.dateplanner.security.jwt.JwtProvider;
import com.dateplanner.security.repository.RefreshTokenRepository;
import com.dateplanner.user.dto.UserLoginRequestDto;
import com.dateplanner.user.entity.User;
import com.dateplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
@Transactional
public class AdminJoinService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;


    // TODO : 현재 모듈이 분리되어 있지 않아 임시로 JWT 로그인 방식 진행, 모듈 분리 이후에는 폼 로그인 방식으로 변경

    public TokenDto adminLogin(UserLoginRequestDto dto) {

        // 회원 정보 조회
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(UserNotFoundApiException::new);

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new PasswordMismatchException();

        log.info("user role={}", user.getRoleSet().get(0));

        // 권한 검증
        if (!user.getRoleSet().get(0).equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("need Admin Authority");
        }

        // 액세스 토큰, 리프레시 토큰 발급
        log.info("create token start");
        TokenDto tokenDto = jwtProvider.createToken(user.getEmail(), user.getRoleSet());
        log.info("create token complete");

        // 리프레시 토큰 저장
        log.info("refresh token persist start");
        log.info("key : {}, token : {}", user.getEmail(), tokenDto.getRefreshToken());
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getEmail())
                .token(tokenDto.getRefreshToken())
                .build();

        if (refreshTokenRepository.findByKey(user.getEmail()).isPresent())
            refreshTokenRepository.deleteByKey(user.getEmail());
        refreshTokenRepository.save(refreshToken);
        log.info("refresh token persist complete");

        return tokenDto;
    }

}
