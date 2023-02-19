package com.dateplanner.admin.user.dto;

import com.dateplanner.admin.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
public class UserResponseDto {

    private final String uid;
    private final String email;
    private final String nickname;

    public UserResponseDto(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }

}
