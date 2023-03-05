package com.dateplanner.user.dto;

import com.dateplanner.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Slf4j(topic = "DTO")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinRequestDto {

    @Schema(description = "회원 이메일")
    @NotNull(message = "이메일이 입력되어야 합니다")
    @NotEmpty(message = "이메일이 입력되어야 합니다")
    private String email;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "닉네임")
    @NotNull(message = "닉네임이 입력되어야 합니다")
    @NotEmpty(message = "닉네임이 입력되어야 합니다")
    private String nickname;

    @Schema(description = "인증 제공자, 일반 회원가입 시에는 사용하지 않습니다")
    private String provider;

    @Schema(description = "인증 제공자로부터 받는 액세스 토큰, 일반 회원 가입 시에는 사용하지 않습니다")
    private String accessToken;

    @Builder
    public UserJoinRequestDto(String password, String email, String nickname, String provider, String accessToken) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.accessToken = accessToken;
    }

    // 일반 회원가입용
    public User toEntity(PasswordEncoder passwordEncoder) {
        User user = User.builder()
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .roleSet(Collections.singletonList("ROLE_USER"))
                .build();

        return user;
    }

    // 소셜 회원가입용 (비밀번호 필요 없음)
    public User toEntity() {
        User user = User.builder()
                .email(email)
                .nickname(nickname)
                .roleSet(Collections.singletonList("ROLE_USER"))
                .provider(provider)
                .social(true)
                .build();

        return user;
    }
}
