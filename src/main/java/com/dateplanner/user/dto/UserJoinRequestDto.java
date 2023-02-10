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

    @Schema(description = "회원 ID, 추후 이메일로 대체될 가능성 있음")
    @NotNull
    private String uid;

    @Schema(description = "비밀번호")
    @NotNull
    private String password;

    @Schema(description = "이메일, 회원 ID와 통합될 가능성 있음")
    @NotNull
    @NotEmpty
    private String email;

    @Schema(description = "자기소개, 닉네임으로 대체될 가능성 있음")
    @NotNull
    @NotEmpty
    private String introduce;

    @Builder
    public UserJoinRequestDto(String uid, String password, String email, String introduce) {
        this.uid = uid;
        this.password = password;
        this.email = email;
        this.introduce = introduce;
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        User user = User.builder()
                .uid(uid)
                .password(passwordEncoder.encode(password))
                .email(email)
                .introduce(introduce)
                .roleSet(Collections.singletonList("ROLE_USER"))
                .build();

        return user;
    }
}
