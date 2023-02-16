package com.dateplanner.admin.user.service;

import com.dateplanner.admin.user.dto.UserLoginRequestDto;
import com.dateplanner.advice.exception.*;
import com.dateplanner.security.dto.TokenDto;
import com.dateplanner.security.dto.TokenRequestDto;
import com.dateplanner.security.entity.RefreshToken;
import com.dateplanner.security.jwt.JwtProvider;
import com.dateplanner.security.repository.RefreshTokenRepository;
import com.dateplanner.admin.user.dto.UserJoinRequestDto;
import com.dateplanner.admin.user.entity.User;
import com.dateplanner.admin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Slf4j(topic = "SERVICE")
@RequiredArgsConstructor
@Service
@Transactional
public class UserJoinService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;


    public TokenDto login(UserLoginRequestDto dto) {

        // 회원 정보 조회
        User user = userRepository.findByUid(dto.getUid()).orElseThrow(UserNotFoundApiException::new);

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new PasswordMismatchException();

        // 액세스 토큰, 리프레시 토큰 발급
        log.info("create token start");
        TokenDto tokenDto = jwtProvider.createToken(user.getUid(), user.getRoleSet());
        log.info("create token complete");

        // 리프레시 토큰 저장
        log.info("refresh token persist start");
        log.info("key : {}, token : {}", user.getUid(), tokenDto.getRefreshToken());
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getUid())
                .token(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        log.info("refresh token persist complete");

        return tokenDto;
    }


    public String join(@Valid UserJoinRequestDto dto) {
        if (userRepository.findByUid(dto.getUid()).isPresent()) {
            throw new UserIdDuplicateException();
        }

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailDuplicateException();
        }

        return userRepository.save(dto.toEntity(passwordEncoder)).getUid();
    }


    public TokenDto refresh(TokenRequestDto dto) {

        // 만료된 리프레시 토큰 확인
        if (!jwtProvider.validationToken(dto.getRefreshToken())) {
            throw new CustomRefreshTokenException();
        }

        // 액세스 토큰에서 username (uid) 가져오기
        String accessToken = dto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // username (uid) 로 유저 검색, 리프레시 토큰 여부 확인
        User user = userRepository.findByUid(authentication.getName()).orElseThrow(UserNotFoundApiException::new);
        RefreshToken refreshToken = refreshTokenRepository.findByKey(user.getUid()).orElseThrow(CustomRefreshTokenException::new);

        // 리프레시 토큰 불일치 여부 확인
        if (!refreshToken.getToken().equals(dto.getRefreshToken()))
            throw new CustomRefreshTokenException();

        // 액세스 토큰, 리프레시 토큰 재발급 및 리프레시 토큰 저장
        TokenDto newCreatedToken = jwtProvider.createToken(user.getUid(), user.getRoleSet());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());

        refreshTokenRepository.save(updateRefreshToken);

        return newCreatedToken;

    }

}
