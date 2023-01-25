package com.dateplanner.user.dto;

import com.dateplanner.user.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "DTO")
@Getter
public class UserResponseDto {

    private final String uid;
    private final String email;
    private final String introduce;

    public UserResponseDto(User user) {
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
    }

}
