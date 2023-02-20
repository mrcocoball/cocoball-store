package com.dateplanner.admin.user.dto;

import com.dateplanner.admin.user.entity.User;
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
    @NotNull
    @NotEmpty
    private String email;

    @Schema(description = "비밀번호")
    @NotNull
    private String password;

    @Schema(description = "닉네임")
    @NotNull
    @NotEmpty
    private String nickname;

    @Builder
    public UserJoinRequestDto(String password, String email, String nickname) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        User user = User.builder()
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .roleSet(Collections.singletonList("ROLE_USER"))
                .build();

        return user;
    }
}
