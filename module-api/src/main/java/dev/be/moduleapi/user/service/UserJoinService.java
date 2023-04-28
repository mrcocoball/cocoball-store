package dev.be.moduleapi.user.service;

import dev.be.moduleapi.advice.exception.*;
import dev.be.moduleapi.security.dto.AccessTokenDto;
import dev.be.moduleapi.security.dto.TokenDto;
import dev.be.moduleapi.security.jwt.JwtProvider;
import dev.be.moduleapi.security.oauth.dto.OAuthAccessTokenDto;
import dev.be.moduleapi.security.oauth.dto.ProfileDto;
import dev.be.moduleapi.security.oauth.service.OAuthProviderService;
import dev.be.moduleapi.user.dto.UserJoinRequestDto;
import dev.be.moduleapi.user.dto.UserLoginRequestDto;
import dev.be.moduleapi.user.dto.UserSocialLoginRequestDto;
import dev.be.modulecore.domain.security.RefreshToken;
import dev.be.modulecore.domain.user.User;
import dev.be.modulecore.repositories.security.RefreshTokenRepository;
import dev.be.modulecore.repositories.user.UserRepository;
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
    private final OAuthProviderService oAuthProviderService;


    public String join(@Valid UserJoinRequestDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailDuplicateException();
        }

        if (userRepository.findByNickname(dto.getNickname()).isPresent()) {
            throw new UserNicknameDuplicateException();
        }

        return userRepository.save(dto.toEntity(passwordEncoder)).getEmail();
    }

    public Boolean emailDuplicateCheck(String email) {
        return userRepository.existsByEmail(email);
    }

    public Boolean nicknameDuplicateCheck(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public TokenDto login(UserLoginRequestDto dto) {

        // 회원 정보 조회
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(UserNotFoundApiException::new);

        // 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
            throw new PasswordMismatchException();

        // 액세스 토큰, 리프레시 토큰 발급
        log.info("create token start");
        TokenDto tokenDto = jwtProvider.createToken(user.getEmail(), user.getUid(), user.getRoleSet());
        log.info("create token complete");

        // 리프레시 토큰 저장
        log.info("refresh token persist start");
        log.info("key : {}, token : {}", user.getEmail(), tokenDto.getRefreshToken());
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getEmail())
                .token(tokenDto.getRefreshToken())
                .build();

        if (refreshTokenRepository.findByKey(user.getEmail()).isPresent()) refreshTokenRepository.deleteByKey(user.getEmail());
        refreshTokenRepository.save(refreshToken);
        log.info("refresh token persist complete");

        return tokenDto;
    }

    public void logout(String refreshToken) {

        // 만료된 리프레시 토큰 확인
        if (!jwtProvider.validationToken(refreshToken)) {
            throw new CustomRefreshTokenException();
        }

        // 리프레시 토큰에서 username, 권한 조회
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);

        // username (email) 로 유저 검색, 리프레시 토큰 여부 확인
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundApiException::new);
        RefreshToken validRefreshToken = refreshTokenRepository.findByKey(user.getEmail()).orElseThrow(CustomRefreshTokenException::new);

        // 리프레시 토큰 불일치 여부 확인
        if (!validRefreshToken.getToken().equals(refreshToken))
            throw new CustomRefreshTokenException();

        // 리프레시 토큰 삭제
        refreshTokenRepository.deleteByKey(user.getEmail());

    }


    public String joinBySocial(UserJoinRequestDto dto) {

        /**
         * 이메일, 닉네임 중복 시 가입을 막아놨으나 카카오로부터 받은 액세스 토큰을 재활용할 수 없으므로 (다시 발급받아야 하므로)
         * 프론트엔드에서 소셜 회원가입 화면에서 닉네임 중복 체크를 꼭 하게끔 하고 넘길 수 있도록 해야 함
         * 중복 이메일, 이미 소셜 회원가입 되어 있는 이메일은 재가입 불가 처리
         */

        log.info("[UserJoinService joinBySocial] email duplicate checking");

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            if (dto.getProvider().equals("kakao")) oAuthProviderService.kakaoUnlink(dto.getAccessToken());
            throw new EmailDuplicateException();
        }

        log.info("[UserJoinService joinBySocial] email and provider duplicate checking");

        if (userRepository.findByEmailAndProvider(dto.getEmail(), dto.getProvider()).isPresent()) {
            throw new UserExistException();
        }

        log.info("[UserJoinService joinBySocial] nickname duplicate checking");

        if (userRepository.findByNickname(dto.getNickname()).isPresent()) {
            if (dto.getProvider().equals("kakao")) oAuthProviderService.kakaoUnlink(dto.getAccessToken());
            throw new UserNicknameDuplicateException();
        }

        return userRepository.save(dto.toEntity()).getEmail();
    }


    public TokenDto loginBySocial(UserSocialLoginRequestDto dto, String provider) {

        /**
         * 프론트엔드에서 소셜 로그인 화면에서 한번 더 카카오 측에 인가 코드를 요청해서 받아야 함
         */

        // 액세스 토큰으로부터 인증 프로필 전달 받음
        ProfileDto profile = oAuthProviderService.getProfile(dto.getAccess_token(), provider);

        if (profile == null) throw new UserNotFoundApiException();

        User user = userRepository.findByEmailAndProvider(profile.getEmail(), provider).orElseThrow(UserNotFoundApiException::new);

        TokenDto tokenDto = jwtProvider.createToken(user.getEmail(), user.getUid(), user.getRoleSet());

        // 리프레시 토큰 저장
        log.info("refresh token persist start");
        log.info("key : {}, token : {}", user.getEmail(), tokenDto.getRefreshToken());
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getEmail())
                .token(tokenDto.getRefreshToken())
                .build();

        if (refreshTokenRepository.findByKey(user.getEmail()).isPresent()) refreshTokenRepository.deleteByKey(user.getEmail());
        refreshTokenRepository.save(refreshToken);
        log.info("refresh token persist complete");

        return tokenDto;
    }

    public AccessTokenDto refreshAccessToken(String refreshToken) {

        // 만료된 리프레시 토큰 확인
        if (!jwtProvider.validationToken(refreshToken)) {
            throw new CustomRefreshTokenException();
        }

        // 리프레시 토큰에서 username, 권한 조회
        Authentication authentication = jwtProvider.getAuthentication(refreshToken);

        // username (email) 로 유저 검색, 리프레시 토큰 여부 확인
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(UserNotFoundApiException::new);
        RefreshToken validRefreshToken = refreshTokenRepository.findByKey(user.getEmail()).orElseThrow(CustomRefreshTokenException::new);

        // 리프레시 토큰 불일치 여부 확인
        if (!validRefreshToken.getToken().equals(refreshToken))
            throw new CustomRefreshTokenException();

        // 액세스 토큰 재발급
        return jwtProvider.refreshAccessToken(user.getEmail(), user.getUid(), user.getRoleSet());

    }

    public boolean isRegister(String accessToken) {
        // 액세스 토큰으로부터 인증 프로필 전달 받음
        ProfileDto profile = oAuthProviderService.getProfile(accessToken, "kakao");
        User user = userRepository.findByEmailAndProvider(profile.getEmail(), "kakao").orElseThrow(UserNotFoundApiException::new);
        return true;
    }

}
