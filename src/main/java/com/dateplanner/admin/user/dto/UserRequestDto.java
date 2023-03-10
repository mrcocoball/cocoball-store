package com.dateplanner.admin.user.dto;

import com.dateplanner.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Slf4j(topic = "DTO")
@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {

    /**
     * 회원 생성용 Dto
     */

    @NotNull
    private String password;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String nickname;

    @Builder
    public UserRequestDto(String password, String email, String nickname) {
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .roleSet(Collections.singletonList("ROLE_USER"))
                .build();
    }

}
