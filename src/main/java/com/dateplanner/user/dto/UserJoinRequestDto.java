package com.dateplanner.user.dto;

import com.dateplanner.user.entity.User;
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

    /**
     * 회원가입 요청용 Dto
     */

    @NotNull
    private String uid;

    @NotNull
    private String password;

    @NotNull
    @NotEmpty
    private String email;

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
